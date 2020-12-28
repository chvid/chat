package dk.brightworks.chat;

import dk.brightworks.autowirer.wire.Autowired;
import dk.brightworks.hbws.api.WsSessions;
import dk.brightworks.hbws.api.WsMethod;
import dk.brightworks.hbws.api.WsServer;

import java.util.logging.Logger;

@WsServer
public class ChatServer {
    private static Logger logger = Logger.getLogger(ChatServer.class.getName());

    @Autowired
    WsSessions sessions;

    @WsMethod
    public void post(ChatLine line) {
        logger.info("post(" + line + ")");
        sessions.all(ChatClient.class).newChatLine(line);
    }
}
