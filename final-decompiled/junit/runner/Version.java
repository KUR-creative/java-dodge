// 
// Decompiled by Procyon v0.5.36
// 

package junit.runner;

public class Version
{
    private Version() {
    }
    
    public static String id() {
        return "4.12";
    }
    
    public static void main(final String[] args) {
        System.out.println(id());
    }
}
