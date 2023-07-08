package com.aurora.network.io;

import com.aurora.network.server.GirlkunServer;
import com.aurora.network.session.TypeSession;
import com.aurora.network.CommandMessage;
import java.net.Socket;
import com.aurora.network.handler.IMessageHandler;
import com.aurora.network.handler.IMessageSendCollect;
import java.io.DataInputStream;
import com.aurora.network.session.ISession;

public class Collector implements Runnable {

    private ISession session;
    private DataInputStream dis;
    private IMessageSendCollect collect;
    private IMessageHandler messageHandler;

    public Collector(final ISession session, final Socket socket) {
        this.session = session;
        this.setSocket(socket);
    }

    public Collector setSocket(final Socket socket) {
        try {
            this.dis = new DataInputStream(socket.getInputStream());
        } catch (Exception ex) {
        }
        return this;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (this.session.isConnected()) {
                    final Message msg = this.collect.readMessage(this.session, this.dis);
                    if (msg.command == CommandMessage.REQUEST_KEY) {
                        if (this.session.getTypeSession() == TypeSession.SERVER) {
                            this.session.sendKey();
                        } else {
                            this.session.setKey(msg);
                        }
                    } else {
                        this.messageHandler.onMessage(this.session, msg);
                    }
                    msg.cleanup();
                }
                Thread.sleep(1L);
            }
        } catch (Exception ex) {
            try {
                GirlkunServer.gI().getAcceptHandler().sessionDisconnect(this.session);
            } catch (Exception ex2) {
            }
            if (this.session != null) {
                System.out.println("Mất kết nối đến session " + this.session.getIP() + "...");
                this.session.disconnect();
            }
        }
    }

    public void setCollect(final IMessageSendCollect collect) {
        this.collect = collect;
    }

    public void setMessageHandler(final IMessageHandler handler) {
        this.messageHandler = handler;
    }

    public void close() {
        if (this.dis != null) {
            try {
                this.dis.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void dispose() {
        this.session = null;
        this.dis = null;
        this.collect = null;
    }
}
