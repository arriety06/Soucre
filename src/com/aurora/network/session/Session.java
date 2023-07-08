// 
// Decompiled by Procyon v0.5.36
// 

package com.aurora.network.session;

import com.aurora.util.StringUtil;
import com.aurora.network.server.GirlkunServer;
import com.aurora.network.server.GirlkunSessionManager;
import com.aurora.network.handler.IMessageHandler;
import com.aurora.network.handler.IMessageSendCollect;
import com.aurora.network.io.Message;
import java.net.InetSocketAddress;
import java.io.IOException;
import com.aurora.network.handler.IKeySessionHandler;
import com.aurora.network.io.Collector;
import com.aurora.network.io.Sender;
import java.net.Socket;

public class Session implements ISession
{
    private static ISession I;
    private static int ID_INIT;
    public TypeSession typeSession;
    private byte[] KEYS;
    private boolean sentKey;
    public int id;
    private Socket socket;
    private boolean connected;
    private boolean reconnect;
    private Sender sender;
    private Collector collector;
    private Thread tSender;
    private Thread tCollector;
    private IKeySessionHandler keyHandler;
    private String ip;
    private String host;
    private int port;
    
    public static ISession gI() throws Exception {
        if (Session.I == null) {
            throw new Exception("Instance ch\u01b0a \u0111\u01b0\u1ee3c kh\u1edfi t\u1ea1o!");
        }
        return Session.I;
    }
    
    public static ISession initInstance(final String host, final int port) throws Exception {
        if (Session.I != null) {
            throw new Exception("Instance \u0111\u00e3 \u0111\u01b0\u1ee3c kh\u1edfi t\u1ea1o!");
        }
        return Session.I = new Session(host, port);
    }
    
    public Session(final String host, final int port) throws IOException {
        this.KEYS = "Girlkun75".getBytes();
        this.id = 752002;
        (this.socket = new Socket(host, port)).setSendBufferSize(1048576);
        this.socket.setReceiveBufferSize(1048576);
        this.typeSession = TypeSession.CLIENT;
        this.connected = true;
        this.host = host;
        this.port = port;
        this.initThreadSession();
    }
    
    public Session(final Socket socket) {
        this.KEYS = "Girlkun75".getBytes();
        this.id = Session.ID_INIT++;
        this.typeSession = TypeSession.SERVER;
        this.socket = socket;
        try {
            this.socket.setSendBufferSize(1048576);
            this.socket.setReceiveBufferSize(1048576);
        }
        catch (Exception ex) {}
        this.connected = true;
        this.ip = ((InetSocketAddress)socket.getRemoteSocketAddress()).getAddress().toString().replace("/", "");
        this.initThreadSession();
    }
    
    @Override
    public void sendMessage(final Message msg) {
        if (this.isConnected() && this.sender.getNumMessages() < 200) {
            this.sender.sendMessage(msg);
        }
    }
    
    @Override
    public ISession setSendCollect(final IMessageSendCollect collect) {
        this.sender.setSend(collect);
        this.collector.setCollect(collect);
        return this;
    }
    
    @Override
    public ISession setMessageHandler(final IMessageHandler handler) {
        this.collector.setMessageHandler(handler);
        return this;
    }
    
    @Override
    public ISession setKeyHandler(final IKeySessionHandler handler) {
        this.keyHandler = handler;
        return this;
    }
    
    @Override
    public ISession startSend() {
        this.tSender.start();
        return this;
    }
    
    @Override
    public ISession startCollect() {
        this.tCollector.start();
        return this;
    }
    
    @Override
    public String getIP() {
        return this.ip;
    }
    
    @Override
    public long getID() {
        return this.id;
    }
    
    @Override
    public void disconnect() {
        this.connected = false;
        this.sentKey = false;
        if (this.sender != null) {
            this.sender.close();
        }
        if (this.collector != null) {
            this.collector.close();
        }
        if (this.socket != null) {
            try {
                this.socket.close();
            }
            catch (IOException ex) {}
        }
        if (this.reconnect) {
            this.reconnect();
            return;
        }
        this.dispose();
    }
    
    @Override
    public void dispose() {
        if (this.sender != null) {
            this.sender.dispose();
        }
        if (this.collector != null) {
            this.collector.dispose();
        }
        this.socket = null;
        this.sender = null;
        this.collector = null;
        this.tSender = null;
        this.tCollector = null;
        this.ip = null;
        GirlkunSessionManager.gI().removeSession(this);
    }
    
    @Override
    public void sendKey() throws Exception {
        if (this.keyHandler == null) {
            throw new Exception("Key handler ch\u01b0a \u0111\u01b0\u1ee3c kh\u1edfi t\u1ea1o!");
        }
        if (GirlkunServer.gI().isRandomKey()) {
            this.KEYS = StringUtil.randomText(7).getBytes();
        }
        this.keyHandler.sendKey(this);
    }
    
    @Override
    public void setKey(final Message message) throws Exception {
        if (this.keyHandler == null) {
            throw new Exception("Key handler ch\u01b0a \u0111\u01b0\u1ee3c kh\u1edfi t\u1ea1o!");
        }
        this.keyHandler.setKey(this, message);
    }
    
    @Override
    public void setKey(final byte[] key) {
        this.KEYS = key;
    }
    
    @Override
    public boolean sentKey() {
        return this.sentKey;
    }
    
    @Override
    public void setSentKey(final boolean sent) {
        this.sentKey = sent;
    }
    
    @Override
    public void doSendMessage(final Message msg) throws Exception {
        this.sender.doSendMessage(msg);
    }
    
    @Override
    public ISession start() {
        this.tSender.start();
        this.tCollector.start();
        return this;
    }
    
    @Override
    public boolean isConnected() {
        return this.connected;
    }
    
    @Override
    public byte[] getKey() {
        return this.KEYS;
    }
    
    @Override
    public TypeSession getTypeSession() {
        return this.typeSession;
    }
    
    @Override
    public ISession setReconnect(final boolean b) {
        this.reconnect = b;
        return this;
    }
    
    @Override
    public int getNumMessages() {
        if (this.isConnected()) {
            return this.sender.getNumMessages();
        }
        return -1;
    }
    
    @Override
    public void reconnect() {
        if (this.typeSession == TypeSession.CLIENT && !this.isConnected()) {
            try {
                this.socket = new Socket(this.host, this.port);
                this.connected = true;
                this.initThreadSession();
                this.start();
            }
            catch (Exception e) {
                try {
                    Thread.sleep(1000L);
                    this.reconnect();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public void initThreadSession() {
        this.tSender = new Thread((this.sender != null) ? this.sender.setSocket(this.socket) : (this.sender = new Sender(this, this.socket)));
        this.tCollector = new Thread((this.collector != null) ? this.collector.setSocket(this.socket) : (this.collector = new Collector(this, this.socket)));
    }
}
