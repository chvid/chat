package dk.brightworks.chat;

import dk.brightworks.hbws.api.WsClient;

@WsClient
interface ChatClient {
    void newChatLine(ChatLine line);
    void ping(String message);
}
