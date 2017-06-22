import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import project.structures.CustomHeaders;
import project.structures.DateParamConverterProvider;

/**
 * Created by student on 26.02.2017.
 */
public class Main {

    public static void main(String[] args){
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(8080).build();
        ResourceConfig config = new ResourceConfig(Students.class, Courses.class);
        config.register(DateParamConverterProvider.class);
        config.register(CustomHeaders.class);
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config);
    }
}
