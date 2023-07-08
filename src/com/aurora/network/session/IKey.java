// 
// Decompiled by Procyon v0.5.36
// 

package com.aurora.network.session;

import com.aurora.network.io.Message;

public interface IKey
{
    void sendKey() throws Exception;
    
    void setKey(final Message p0) throws Exception;
    
    void setKey(final byte[] p0);
    
    byte[] getKey();
    
    boolean sentKey();
    
    void setSentKey(final boolean p0);
}
