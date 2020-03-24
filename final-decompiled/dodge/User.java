// 
// Decompiled by Procyon v0.5.36
// 

package dodge;

import java.io.Serializable;

class User implements Serializable
{
    public String name;
    public int time;
    public int score;
    public int result;
    
    public User(final String name, final int time, final int score, final int result) {
        this.name = name;
        this.time = time;
        this.score = score;
        this.result = result;
    }
}
