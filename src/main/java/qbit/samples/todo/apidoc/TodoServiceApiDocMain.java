package qbit.samples.todo.apidoc;

import io.advantageous.qbit.admin.ManagedServiceBuilder;
import io.advantageous.qbit.meta.builder.ContextMetaBuilder;

/**
 * @author mammatustech/advantageous
 */
public class TodoServiceApiDocMain {
	
	public static void main(final String... args) throws Exception {

		/* create the ManagedServiceBuilder which manages a clean shutdown, health, stats, etc. */
		final ManagedServiceBuilder managedServiceBuilder =
				ManagedServiceBuilder.managedServiceBuilder()
						.setRootURI("/v1") //Defaults to services
						.setPort(8888); //Defaults to 8080 or environment variable PORT

        /* Context meta builder to document this endpoint.  */
		ContextMetaBuilder contextMetaBuilder = managedServiceBuilder.getContextMetaBuilder();
		contextMetaBuilder.setContactEmail("lunati-not-real-email@gmail.com");
		contextMetaBuilder.setDescription("A great service to show building a todo list");
		contextMetaBuilder.setContactURL("http://www.bwbl.lunati/master/of/rodeo");
		contextMetaBuilder.setContactName("Buffalo Wild Bill Lunati");
		contextMetaBuilder.setLicenseName("Commercial");
		contextMetaBuilder.setLicenseURL("http://www.canttouchthis.com");
		contextMetaBuilder.setTitle("Todo Title");
		contextMetaBuilder.setVersion("47.0");
		
		
		// not used for this showcase: how to start from the doc
		// and use Swagger to generate the client
		// managedServiceBuilder.getStatsDReplicatorBuilder().setHost("192.168.59.103");
		// managedServiceBuilder.setEnableStatsD(true);


		/* Start the service. */
		managedServiceBuilder.addEndpointService(new TodoService()) // register TodoService
				.getEndpointServerBuilder()
				.build().startServer();
		
		System.out.println("Todo Service ApiDoc started.");
		
		/* start the Admin Server for exposing health endpoints and Swagger meta data. */
		managedServiceBuilder.getAdminBuilder().build().startServer();
		
		System.out.println("Todo Service Admin Server started");
		
	}
	
}
