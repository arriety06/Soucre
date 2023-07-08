// 
// Decompiled by Procyon v0.5.36
// 
package com.aurora.network.handler;

import com.aurora.network.io.Message;
import com.aurora.network.session.ISession;

public interface IKeySessionHandler {

    void sendKey(final ISession p0);

    void setKey(final ISession p0, final Message p1) throws Exception;
}
