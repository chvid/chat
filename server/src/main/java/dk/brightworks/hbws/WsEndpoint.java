package dk.brightworks.hbws;

import dk.brightworks.autowirer.Autowirer;
import dk.brightworks.hbws.api.WsSessions;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint("/ws")
@WebListener
public class WsEndpoint extends Endpoint implements ServletContextListener {
    private static Logger logger = Logger.getLogger(WsEndpoint.class.getName());

    private static Autowirer autowirer;

    public void onOpen(Session session, EndpointConfig endpointConfig) {
        logger.info("onOpen(" + session + ", " + endpointConfig + ")");
        autowirer.lookupInstance(WsSessions.class).add(session);
        session.addMessageHandler(new MessageHandlerPartialString(session.getBasicRemote()));
    }

    public void onClose(Session session, CloseReason closeReason) {
        autowirer.lookupInstance(WsSessions.class).remove(session);
    }

    public void contextInitialized(ServletContextEvent sce) {
        if (autowirer != null) {
            throw new RuntimeException("Servlet already initialized");
        }

        logger.info("Initializing services ...");
        long start = System.currentTimeMillis();

        String packageToScan = sce.getServletContext().getInitParameter("packageToScan");

        if (packageToScan == null) {
            logger.warning("Init parameter packageToScan has not been set. This causes slow startup.");
            packageToScan = "";
        }

        autowirer = new Autowirer();

        autowirer.add(new WsMap(), new WsSessions());

        autowirer.addPackage(packageToScan);

        logger.info("Component scan took " + (System.currentTimeMillis() - start) + " msec.");

        autowirer.init();

        logger.info("All service has been initialized. (" + (System.currentTimeMillis() - start) + " msec.)");
    }

    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Calling shutdown on all services ...");
        long start = System.currentTimeMillis();
        autowirer.shutdown();
        autowirer = null;
        logger.info("All services have been shut down. (" + (System.currentTimeMillis() - start) + " msec.)");
    }

    private static class MessageHandlerPartialString implements MessageHandler.Partial<String> {
        private final RemoteEndpoint.Basic remoteEndpointBasic;

        private MessageHandlerPartialString(RemoteEndpoint.Basic remoteEndpointBasic) {
            this.remoteEndpointBasic = remoteEndpointBasic;
        }

        public void onMessage(String message, boolean last) {
            logger.info("onMessage(" + message + ", " + last + ")");
            try {
                String header = EnvelopeUtils.findHeader(message);
                WsMap.Reference invocation = autowirer.lookupInstance(WsMap.class).findInvocation(header);
                if (invocation == null) {
                    throw new RuntimeException("Unable to find ws method for: " + header);
                }
                Envelope<?> envelope = EnvelopeUtils.deserializeEnvelope(message, invocation.getMethod().getParameterTypes()[0]);
                invocation.getMethod().invoke(invocation.getObject(), envelope.getBody());
                // remoteEndpointBasic.sendText(message, last);
            } catch (Throwable e) {
                logger.log(Level.WARNING, "Unhandled error: " + e, e);
            }
        }
    }
}