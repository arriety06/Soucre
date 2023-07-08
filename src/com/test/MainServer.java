// 
// Decompiled by Procyon v0.5.36
// 
package com.test;

import com.aurora.network.session.Session;
import com.aurora.network.example.MessageSendCollect;
import com.aurora.network.session.ISession;
import com.aurora.network.server.ISessionAcceptHandler;
import com.aurora.network.server.GirlkunServer;

public class MainServer {

    public static void main(final String[] args) throws Exception {
        GirlkunServer.gI().init().setAcceptHandler(new ISessionAcceptHandler() {
            @Override
            public void sessionInit(final ISession session) {
                session.setSendCollect(new MessageSendCollect()).setSendCollect(new MessageSendCollect()).setMessageHandler((s, msg) -> System.out.println(msg.readUTF())).start();
            }

            @Override
            public void sessionDisconnect(final ISession session) {
            }
        }).setTypeSessioClone(Session.class).start(5155);
    }
}
