package qbit.samples.todo.workers;

import io.advantageous.qbit.admin.ManagedServiceBuilder;
import io.advantageous.qbit.reactive.Reactor;
import io.advantageous.qbit.reactive.ReactorBuilder;
import io.advantageous.qbit.service.ServiceBuilder;
import io.advantageous.qbit.service.ServiceBundle;
import io.advantageous.qbit.service.ServiceQueue;
import io.advantageous.qbit.service.dispatchers.ServiceWorkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TodoServiceWorkersMain {
	
	private static final String SERVICE_NAME = "todo-service";
	private static final String SERVICE_URI = "/v1";
	private static final String SERVICE_HOST = "localhost";
	private static final int SERVICE_PORT = 8080;
	
	private static final int EXEC_WORKERS = 4;
	
	private static final Logger logger = LoggerFactory.getLogger(TodoServiceWorkersMain.class);
	
	public static void main(String... args) {
		
		final ManagedServiceBuilder managedServiceBuilder = ManagedServiceBuilder.managedServiceBuilder()
				.setRootURI(SERVICE_URI)
				.setPort(SERVICE_PORT);

		final Reactor reactor = ReactorBuilder.reactorBuilder()
				.setDefaultTimeOut(1)
				.setTimeUnit(TimeUnit.SECONDS)
				.build();
		
		// creating a Worker Pool (work is being dispatched in a round robin way)
		final ServiceWorkers execOpDispatcher = ServiceWorkers.workers();
		
		for (int i = 0; i < EXEC_WORKERS; i++) {
			ServiceQueue execOpServiceQueue = ServiceBuilder.serviceBuilder()
					.setServiceObject(new ExecuteOp()).build();
			execOpDispatcher.addService(execOpServiceQueue);
		}
		
		execOpDispatcher.start();
		
		final ServiceBundle bundle = managedServiceBuilder
				.createServiceBundleBuilder().setAddress("/").build();
		
		bundle.addServiceConsumer("execWorkers", execOpDispatcher);
		bundle.start();
		
		final ExecuteOpAsync executeOpAsync = bundle.
				createLocalProxy(ExecuteOpAsync.class, "execWorkers");

//		final ServiceQueue execOpServiceQueue = managedServiceBuilder
//				.createServiceBuilderForServiceObject(new ExecuteOp()).build();
//
//		execOpServiceQueue.startServiceQueue().startCallBackHandler();
//
//		final qbit.qbit.samples.todo.callback.ExecuteOpAsync executeOpAsync = execOpServiceQueue.createProxy(ExecuteOpAsync.class);
		
		managedServiceBuilder
				.addEndpointService(new TodoService(reactor, executeOpAsync))
				.getEndpointServerBuilder()
				.setEndpointName(SERVICE_NAME)
				.build()
				.startServer();
		
		logger.info("Todo Service started listening on {}:{}{}/{}\n\n",
				SERVICE_HOST, SERVICE_PORT, SERVICE_URI, SERVICE_NAME);
	}
	
}
