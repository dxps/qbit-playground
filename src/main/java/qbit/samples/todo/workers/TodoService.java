package qbit.samples.todo.workers;


import io.advantageous.qbit.annotation.*;
import io.advantageous.qbit.reactive.Callback;
import io.advantageous.qbit.reactive.Reactor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by rhightower on 1/19/15.
 * 2017 | vision8 | Update to illustrate async exercise.
 */

@RequestMapping("/todo-service")
public class TodoService {
	
	private List<TodoItem> items = new ArrayList<>();
	
	private final Reactor reactor;
	
	private final ExecuteOpAsync executeOpAsync;
	
	private Logger logger = LoggerFactory.getLogger(TodoService.class);
	
	/** Create a new instance of this service. */
	public TodoService(final Reactor reactor, final ExecuteOpAsync executeOpAsync) {
		this.reactor = reactor;
		this.executeOpAsync = executeOpAsync;
		// register collaborating services
		this.reactor.addServiceToFlush(executeOpAsync);
	}
	
	
	@RequestMapping("/todo/count")
	public int size() {
		return items.size();
	}
	
	
	@RequestMapping(value = "/todo")
	public List<TodoItem> list() {
		logger.debug(">>> list > items={}", items);
		return items;
	}
	
	
	@RequestMapping(value = "/todo", method = RequestMethod.POST)
	public void add(TodoItem todoItem) {
		items.add(todoItem);
	}
	
	
	@RequestMapping(value = "/todo/exec-op-sync", description = "Execute a slow operation in a blocking (synchronous) way.")
	public int execOpSync(final @RequestParam("execTime") int execTime) {
		
		System.out.printf("%s | TodoService > execOpSync |-> execTime=%d\n", LocalTime.now(), execTime);
		Op op = new Op(execTime);
		new ExecuteOp().execute(op);
		System.out.printf("%s | TodoService > execOpSync |<- execTime=%d\n", LocalTime.now(), execTime);
		return op.resultExecTime();
	}
	
	
	@RequestMapping(value = "/todo/exec-op-async", description = "Execute a slow operation in a non-blocking (asynchronous) way.")
	public void execOpAsync(final Callback<Boolean> callback,
	                        final @RequestParam("execTime") int execTime) {
		
		// System.out.printf("%s | TodoService > execOpAsync |-> execTime=%d\n", LocalTime.now(), execTime);
		logger.debug(">>> execOpAsync > execTime={}", execTime);
		
		final Callback<Boolean> responseCallback = reactor.callbackBuilder()
				.setCallback(Boolean.class, succeeded -> {
					callback.accept(succeeded);
					logger.info("<<< execOpAsync > execTime={}", execTime);
				})
				.setOnTimeout(() -> {
					// callback.accept(false); One way.
					// callback.onTimeout(); Another way
					callback.onError(new TimeoutException(
							"Timeout while async executing an op for execTime=" + execTime));
				})
				.setOnError(callback::onError)
				.setTimeoutDuration(8).setTimeoutTimeUnit(TimeUnit.SECONDS)
				.build();
		
		executeOpAsync.execute(responseCallback, new Op(execTime));
	}
	
	
	/**
	 * Register to be notified when the service queue is idle, empty,
	 * or has hit its batch limit.
	 */
	@QueueCallback({QueueCallbackType.EMPTY, QueueCallbackType.IDLE, QueueCallbackType.LIMIT})
	private void process() {
		
		reactor.process();
	}
	
}
