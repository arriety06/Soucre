// 
// Decompiled by Procyon v0.5.36
// 
package com.aurora.network.server;

import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.aurora.network.session.SessionFactory;
import java.io.IOException;
import com.aurora.network.session.ISession;
import com.aurora.network.session.Session;
import java.net.ServerSocket;

public class GirlkunServer implements IGirlkunServer {

    private static GirlkunServer I;
    private int port;
    private ServerSocket serverListen;
    private Class sessionClone;
    private boolean start;
    private boolean randomKey;
    private IServerClose serverClose;
    private ISessionAcceptHandler acceptHandler;
    private Thread loopServer;

    public static GirlkunServer gI() {
        if (GirlkunServer.I == null) {
            GirlkunServer.I = new GirlkunServer();
        }
        return GirlkunServer.I;
    }

    private GirlkunServer() {
        this.port = -1;
        this.sessionClone = Session.class;
    }

    @Override
    public IGirlkunServer init() {
        this.loopServer = new Thread(this);
        return this;
    }

    @Override
    public IGirlkunServer start(final int port) throws Exception {
        if (port < 0) {
            throw new Exception("Vui lòng khởi tạo port server!");
        }
        if (this.acceptHandler == null) {
            throw new Exception("AcceptHandler chưa được khởi tạo!");
        }
        if (!ISession.class.isAssignableFrom(this.sessionClone)) {
            throw new Exception("Type session clone không hợp lệ!");
        }
        try {
            this.port = port;
            this.serverListen = new ServerSocket(port);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Lỗi khởi tạo server tại port " + port);
            System.exit(0);
        }
        this.start = true;
        this.loopServer.start();
        System.out.println("Server Aurora đang chạy tại port " + this.port);
        return this;
    }

    @Override
    public IGirlkunServer close() {
        this.start = false;
        if (this.serverListen != null) {
            try {
                this.serverListen.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (this.serverClose != null) {
            this.serverClose.serverClose();
        }
        System.out.println("Server Aurora đã đóng!");
        return this;
    }

    @Override
    public IGirlkunServer dispose() {
        this.acceptHandler = null;
        this.loopServer = null;
        this.serverListen = null;
        return this;
    }

    @Override
    public IGirlkunServer setAcceptHandler(final ISessionAcceptHandler handler) {
        this.acceptHandler = handler;
        return this;
    }

    @Override
    public void run() {
        while (this.start) {
            try {
                final Socket socket = this.serverListen.accept();
                final ISession session = SessionFactory.gI().cloneSession(this.sessionClone, socket);
                this.acceptHandler.sessionInit(session);
                GirlkunSessionManager.gI().putSession(session);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception ex2) {
                Logger.getLogger(GirlkunServer.class.getName()).log(Level.SEVERE, null, ex2);
            }
        }
    }

    @Override
    public IGirlkunServer setDoSomeThingWhenClose(final IServerClose serverClose) {
        this.serverClose = serverClose;
        return this;
    }

    @Override
    public IGirlkunServer randomKey(final boolean isRandom) {
        this.randomKey = isRandom;
        return this;
    }

    @Override
    public boolean isRandomKey() {
        return this.randomKey;
    }

    @Override
    public IGirlkunServer setTypeSessioClone(final Class clazz) throws Exception {
        this.sessionClone = clazz;
        return this;
    }

    @Override
    public ISessionAcceptHandler getAcceptHandler() throws Exception {
        if (this.acceptHandler == null) {
            throw new Exception("AcceptHandler ch\u01b0a \u0111\u01b0\u1ee3c kh\u1edfi t\u1ea1o!");
        }
        return this.acceptHandler;
    }

    @Override
    public void stopConnect() {
        this.start = false;
    }
}
