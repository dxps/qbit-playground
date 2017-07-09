package qbit.samples.todo.workers;

import io.advantageous.qbit.admin.ManagedServiceBuilder;
import io.advantageous.qbit.reactive.Reactor;
import io.advantageous.qbit.reactive.ReactorBuilder;
import io.advantageous.qbit.service.ServiceBundle;
import io.advantageous.qbit.service.dispatchers.RoundRobinServiceWorkerBuilder;
import io.advantageous.qbit.service.dispatchers.ServiceMethodDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TodoServiceWorkersCopyMain {
	
	static final String SERVICE_NAME = "todo-service";
	static final String SERVICE_URI = "/v1";
	static final String SERVICE_HOST = "localhost";
	static final int SERVICE_PORT = 8080;
	
	static final Logger logger = LoggerFactory.getLogger(TodoServiceWorkersCopyMain.class);
	
	public static void main(String... args) {
		
		final ManagedServiceBuilder managedServiceBuilder = ManagedServiceBuilder.managedServiceBuilder()
				.setRootURI(SERVICE_URI)
				.setPort(SERVICE_PORT);
		
		final Reactor reactor = ReactorBuilder.reactorBuilder()
				.setDefaultTimeOut(1)
				.setTimeUnit(TimeUnit.SECONDS)
				.build();
		
		final ServiceMethodDispatcher executeOpMethodDispatcher = RoundRobinServiceWorkerBuilder
				.roundRobinServiceWorkerBuilder()
				.setWorkerCount(4)
				.setServiceObjectSupplier(ExecuteOp::new)
				.build();
		executeOpMethodDispatcher.start();
		
		final ServiceBundle bundle = managedServiceBuilder
				.createServiceBundleBuilder()
				.setAddress("/")
				.build();
		bundle.addServiceConsumer("/execWorkers", executeOpMethodDispatcher);
		final ExecuteOpAsync executeOpAsync = bundle.createLocalProxy(ExecuteOpAsync.class, "/execWorkers");
		
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
