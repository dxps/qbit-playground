package qbit.samples.todo.callback;

import io.advantageous.qbit.admin.ManagedServiceBuilder;
import io.advantageous.qbit.reactive.Reactor;
import io.advantageous.qbit.reactive.ReactorBuilder;
import io.advantageous.qbit.service.ServiceQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

//import io.advantageous.qbit.server.EndpointServerBuilder;
//import io.advantageous.qbit.server.ServiceEndpointServer;

public class TodoServiceMain {
	
	static final String SERVICE_NAME = "todo-service";
	static final String SERVICE_URI = "/v1";
	static final String SERVICE_HOST = "localhost";
	static final int SERVICE_PORT = 8080;
	
	static final Logger logger = LoggerFactory.getLogger(TodoServiceMain.class);
	
	public static void main(String... args) {
		
		/* ManagedServiceBuilder provides manages a clean shutdown, health, stats, etc. */
		final ManagedServiceBuilder managedServiceBuilder = ManagedServiceBuilder.managedServiceBuilder()
				.setRootURI(SERVICE_URI)   // defaults to services
				.setPort(SERVICE_PORT);      // defaults to 8080 or environment variable PORT
		
//		ServiceEndpointServer server = new EndpointServerBuilder()
//				.setEndpointName("todo-service")
//				.setHost("localhost")
//				.setPort(8080)
//				.build();
		
		final Reactor reactor = ReactorBuilder.reactorBuilder()
				.setDefaultTimeOut(1)
				.setTimeUnit(TimeUnit.SECONDS)
				.build();
		
//		server.initServices(new TodoService(reactor));
//		server.start();
		
		final ServiceQueue execOpServiceQueue = managedServiceBuilder
				.createServiceBuilderForServiceObject(new ExecuteOp()).build();
		
		execOpServiceQueue.startServiceQueue().startCallBackHandler();
		
		final ExecuteOpAsync executeOpAsync = execOpServiceQueue.createProxy(ExecuteOpAsync.class);
		
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
