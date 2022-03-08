
package fiap;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Set;

@Path("/apichat")
public class GreetingResource {
    
    // REST API
    static protected Set<String> msgs = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));
    
    @Inject
    StartWebSocket socket;

    @GET
    public Set<String> list() {
        return msgs;
    }

    @POST
    public Set<String> add(String message) {
        String username = "APICHAT";
        msgs.add(message);
        socket.broadcast(">> " + username + ": " + message);
        return msgs;
    }
    
}
