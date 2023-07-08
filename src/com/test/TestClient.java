package com.test;

import com.aurora.network.session.ISession;
import com.aurora.network.io.Message;
import com.aurora.network.example.MessageHandler;
import com.aurora.network.example.MessageSendCollect;
import com.aurora.network.session.Session;

public class TestClient {

    public static void main(final String[] args) throws Exception {
        final ISession session = new Session("localhost", 5155).setSendCollect(new MessageSendCollect()).setMessageHandler(new MessageHandler()).setReconnect(false).start();
        final Message msg = new Message(4);
        msg.writeUTF("Arriety");
        session.sendMessage(msg);
        msg.cleanup();
        Thread.sleep(1000L);
        session.disconnect();
    }
}
