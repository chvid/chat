package dk.brightworks.hbws.api;

import dk.brightworks.hbws.EnvelopeUtils;

import javax.websocket.Session;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class WsSessions {
    private Set<Session> sessions = new HashSet<>();

    public void add(Session session) {
        sessions.add(session);
    }

    public void remove(Session session) {
        sessions.remove(session);
    }

    public Set<String> ids() {
        return sessions.stream().map(Session::getId).collect(Collectors.toSet());
    }

    public <T> T all(Class<T> klass) {
        return (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{klass}, (proxy, method, args) -> {
            for (Session s : sessions) {
                s.getBasicRemote().sendText(
                        EnvelopeUtils.serializeInEnvelope(method.getName(), args[0])
                );
            }
            return null;
        });
    }

    public <T> T specific(String id, Class<T> klass) {
        return (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{klass}, (proxy, method, args) -> {
            for (Session s : sessions) {
                if (s.getId().equals(id)) {
                    s.getBasicRemote().sendText(
                            EnvelopeUtils.serializeInEnvelope(method.getName(), args[0])
                    );
                }
            }
            return null;
        });
    }
}
