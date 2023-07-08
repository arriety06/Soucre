// 
// Decompiled by Procyon v0.5.36
// 

package com.aurora.network.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public interface IMessage extends IIOMessage
{
    DataOutputStream writer();
    
    DataInputStream reader();
    
    byte[] getData();
    
    void cleanup();
    
    void dispose();
}
