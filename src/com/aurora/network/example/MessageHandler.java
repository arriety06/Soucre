// 
// Decompiled by Procyon v0.5.36
// 
package com.aurora.network.example;

import com.aurora.network.io.Message;
import com.aurora.network.session.ISession;
import com.aurora.network.handler.IMessageHandler;

public class MessageHandler implements IMessageHandler {

    @Override
    public void onMessage(final ISession session, final Message msg) throws Exception {
        System.out.println(msg.reader().readUTF());
        msg.cleanup();
    }
}
