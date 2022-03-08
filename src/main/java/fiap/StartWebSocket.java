package fiap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.Session;

@ServerEndpoint("/chat/{username}")         
@ApplicationScoped
public class StartWebSocket {

    Map<String, Session> sessions = new ConcurrentHashMap<>(); 
    
    //@Inject
    //GreetingResource apichat;

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        sessions.put(username, session);
        broadcast("Usuário \'" + username + "\' entrou");
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        sessions.remove(username);
        broadcast("Usuário :" + username + ": saiu");
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
        sessions.remove(username);
        broadcast("Usuário :" + username + ": saiu com erro: " + throwable);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username) {
        broadcast(">> " + username + ": " + message);
        GreetingResource.msgs.add(message);
    }

    protected void broadcast(String message) {
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result ->  {
                if (result.getException() != null) {
                    System.out.println("Falha ao enviar a mensagem: " + result.getException());
                }
            });
        });
    }
}
