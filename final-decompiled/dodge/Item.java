// 
// Decompiled by Procyon v0.5.36
// 

package dodge;

import java.awt.image.ImageObserver;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

public class Item extends Animation
{
    public static int baseValue;
    private int value;
    private static final Rectangle boundary;
    private static int baseMaxD;
    private static int baseMinD;
    private int dx;
    private int dy;
    
    static {
        Item.baseValue = 100;
        boundary = new Rectangle(-100, -100, 1000, 800);
        Item.baseMaxD = 30;
        Item.baseMinD = 10;
    }
    
    public Item(final Image image, final int x, final int y, final int w, final int h, final int rowFrameNum, final int allFrameNum, final int frameW, final int frameH, final boolean isLooping) {
        super(image, x, y, w, h, rowFrameNum, allFrameNum, frameW, frameH, isLooping);
        this.value = Item.baseValue;
        this.setNewRandomDelta();
    }
    
    public Item(final Image image, final int x, final int y, final int rowFrameNum, final int allFrameNum, final int frameW, final int frameH, final boolean isLooping) {
        super(image, x, y, rowFrameNum, allFrameNum, frameW, frameH, isLooping);
        this.value = Item.baseValue;
        this.setNewRandomDelta();
    }
    
    public void setNewRandomDelta() {
        this.dx = (int)(Math.cos(Math.random() * 360.0) * (Item.baseMaxD - Item.baseMinD));
        this.dy = (int)(Math.cos(Math.random() * 360.0) * (Item.baseMaxD - Item.baseMinD));
        if (this.dx == 0) {
            this.dx = (int)(Math.random() * (Item.baseMaxD - Item.baseMinD) + Item.baseMinD);
        }
        if (this.dy == 0) {
            this.dy = (int)(Math.random() * (Item.baseMaxD - Item.baseMinD) + Item.baseMinD);
        }
    }
    
    @Override
    public void draw(final Graphics2D g2d, final ImageObserver observer) {
        this.move();
        super.draw(g2d, observer);
    }
    
    public void move() {
        if (this.isVisible()) {
            this.setX(this.getX() + this.dx);
            this.setY(this.getY() + this.dy);
            if (this.getX() < Item.boundary.x || this.getX() > Item.boundary.width) {
                this.dx *= -1;
                this.setX(this.getX() + 2 * this.dx);
            }
            if (this.getY() < Item.boundary.y || this.getY() > Item.boundary.height) {
                this.dy *= -1;
                this.setY(this.getY() + 2 * this.dy);
            }
        }
    }
    
    public void returnInitXY() {
        int initX = (int)(Math.random() * 1000.0);
        int initY = (int)(Math.random() * 800.0);
        switch ((int)(4.0 * Math.random())) {
            case 0: {
                initX = (int)(Math.random() * -10.0);
                break;
            }
            case 1: {
                initX = 1000 + (int)(Math.random() * 10.0);
                break;
            }
            case 2: {
                initY = (int)(Math.random() * -10.0);
                break;
            }
            case 3: {
                initY = 800 + (int)(Math.random() * 10.0);
                break;
            }
        }
        this.setX(initX);
        this.setY(initY);
    }
    
    public int getValue() {
        return this.value;
    }
    
    public void setValue(final int value) {
        this.value = value;
    }
    
    public int getDx() {
        return this.dx;
    }
    
    public int getDy() {
        return this.dy;
    }
    
    public void setDx(final int dx) {
        this.dx = dx;
    }
    
    public void setDy(final int dy) {
        this.dy = dy;
    }
}
