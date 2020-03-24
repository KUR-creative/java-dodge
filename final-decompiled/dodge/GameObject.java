// 
// Decompiled by Procyon v0.5.36
// 

package dodge;

import java.awt.image.ImageObserver;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Image;

public class GameObject
{
    private boolean visible;
    private int x;
    private int y;
    private int w;
    private int h;
    private int initW;
    private int initH;
    private Image image;
    private Rectangle hitRect;
    
    public GameObject(final Image image, final int x, final int y, final int w, final int h) {
        this.hitRect = null;
        this.x = x;
        this.y = y;
        this.initW = w;
        this.w = w;
        this.initH = h;
        this.h = h;
        this.image = image;
        this.hitRect = new Rectangle(x, y, w, h);
    }
    
    public void draw(final Graphics2D g2d, final ImageObserver observer) {
        if (this.visible) {
            g2d.drawImage(this.image, this.x, this.y, this.w, this.h, observer);
        }
    }
    
    public void setWidthByRatio(final double ratio) {
        this.setW((int)(ratio * this.initW));
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getW() {
        return this.w;
    }
    
    public int getH() {
        return this.h;
    }
    
    public void setX(final int x) {
        this.x = x;
        this.hitRect.x = x;
    }
    
    public void setY(final int y) {
        this.y = y;
        this.hitRect.y = y;
    }
    
    public void setW(final int w) {
        this.w = w;
        this.hitRect.width = w;
    }
    
    public void setH(final int h) {
        this.h = h;
        this.hitRect.height = h;
    }
    
    public void setXY(final int x, final int y) {
        this.x = x;
        this.y = y;
        this.hitRect.x = x;
        this.hitRect.y = y;
    }
    
    public Image getImage() {
        return this.image;
    }
    
    public Rectangle getHitRect() {
        return this.hitRect;
    }
    
    public boolean hitTest(final GameObject obj) {
        return this.visible && obj.visible && this.hitRect.x < obj.hitRect.x + obj.hitRect.width && this.hitRect.x + this.hitRect.width > obj.hitRect.x && this.hitRect.y < obj.hitRect.y + obj.hitRect.height && this.hitRect.height + this.hitRect.y > obj.hitRect.y;
    }
}
