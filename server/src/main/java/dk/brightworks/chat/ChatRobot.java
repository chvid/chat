package dk.brightworks.chat;

import dk.brightworks.autowirer.wire.Autowired;
import dk.brightworks.autowirer.wire.Init;
import dk.brightworks.autowirer.wire.Service;
import dk.brightworks.autowirer.wire.Shutdown;
import dk.brightworks.hbws.api.WsSessions;

import java.util.logging.Logger;

@Service
public class ChatRobot {
    private static Logger logger = Logger.getLogger(ChatRobot.class.getName());

    @Autowired
    WsSessions sessions;

    private Thread thread;

    @Init
    public void init() {
        thread = new Thread(() -> {
            logger.info("Daemon thread stared ...");
            while (true) {
                try {
                    sessions.all(ChatClient.class).ping("People online: " + sessions.ids());
                    Thread.sleep(10000L);
                } catch (Exception e) {
                    if (e instanceof InterruptedException) {
                        return;
                    }
                    logger.info("Ignoring exception: " + e);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    @Shutdown
    public void shutdown() {
        thread.interrupt();
    }
}
