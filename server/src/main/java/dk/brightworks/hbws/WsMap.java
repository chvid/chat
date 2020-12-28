package dk.brightworks.hbws;

import dk.brightworks.autowirer.Autowirer;
import dk.brightworks.autowirer.wire.Autowired;
import dk.brightworks.autowirer.wire.Init;
import dk.brightworks.hbws.api.WsMethod;
import dk.brightworks.hbws.api.WsServer;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class WsMap {
    public static class Reference {
        private Method method;
        private Object object;

        public Reference(Object object, Method method) {
            this.object = object;
            this.method = method;
        }

        public Method getMethod() {
            return method;
        }

        public Object getObject() {
            return object;
        }

        public String toString() {
            return object.getClass().getName() + "::" + method.getName();
        }

    }

    private Map<String, Reference> map = new HashMap<>();

    @Autowired
    Autowirer autowirer;

    @Init
    public void init() {
        for (Object object : autowirer.findServicesWithAnnotation(WsServer.class)) {
            String prefix = object.getClass().getAnnotation(WsServer.class).value();
            if (!prefix.equals("")) prefix = prefix + ".";
            for (Method method : object.getClass().getMethods()) {
                if (method.isAnnotationPresent(WsMethod.class)) {
                    map.put(prefix + method.getName(), new Reference(object, method));
                }
            }
        }
    }

    public Reference findInvocation(String name) {
        return map.get(name);
    }

    public Map<String, Reference> getMap() {
        return map;
    }
}
