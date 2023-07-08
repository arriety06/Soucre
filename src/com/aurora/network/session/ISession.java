// 
// Decompiled by Procyon v0.5.36
// 

package com.aurora.network.session;

import com.aurora.network.io.Message;
import com.aurora.network.handler.IKeySessionHandler;
import com.aurora.network.handler.IMessageHandler;
import com.aurora.network.handler.IMessageSendCollect;

public interface ISession extends IKey
{
    TypeSession getTypeSession();
    
    ISession setSendCollect(final IMessageSendCollect p0);
    
    ISession setMessageHandler(final IMessageHandler p0);
    
    ISession setKeyHandler(final IKeySessionHandler p0);
    
    ISession startSend();
    
    ISession startCollect();
    
    ISession start();
    
    ISession setReconnect(final boolean p0);
    
    void initThreadSession();
    
    void reconnect();
    
    String getIP();
    
    boolean isConnected();
    
    long getID();
    
    void sendMessage(final Message p0);
    
    void doSendMessage(final Message p0) throws Exception;
    
    void disconnect();
    
    void dispose();
    
    int getNumMessages();
}
