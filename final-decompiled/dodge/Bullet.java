// 
// Decompiled by Procyon v0.5.36
// 

package dodge;

import java.awt.image.ImageObserver;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

public class Bullet extends GameObject
{
    public static int baseDamage;
    private int damage;
    private int id;
    private static final Rectangle boundary;
    private static final int initialW = 11;
    private static final int initialH = 11;
    private int initX;
    private int initY;
    private static int baseMaxD;
    private static int baseMinD;
    private int dx;
    private int dy;
    
    static {
        Bullet.baseDamage = 50;
        boundary = new Rectangle(-100, -100, 1200, 1000);
        Bullet.baseMaxD = 7;
        Bullet.baseMinD = 1;
    }
    
    public Bullet(final int id, final Image image, final int x, final int y, final int w, final int h) {
        super(image, x, y, w, h);
        this.damage = Bullet.baseDamage;
        this.id = id;
        this.setNewRandomDelta();
    }
    
    public Bullet(final int id, final Image image, final int x, final int y) {
        this(id, image, x, y, 11, 11);
    }
    
    public Bullet(final int id, final Image image) {
        this(id, image, 450, 450, 11, 11);
        this.setInitialXY();
        this.setX(this.initX);
        this.setY(this.initY);
    }
    
    private void setInitialXY() {
        this.initX = (int)(Math.random() * 1000.0);
        this.initY = (int)(Math.random() * 800.0);
        switch ((int)(4.0 * Math.random())) {
            case 0: {
                this.initX = (int)(Math.random() * -100.0);
                break;
            }
            case 1: {
                this.initX = 1000 + (int)(Math.random() * 100.0);
                break;
            }
            case 2: {
                this.initY = (int)(Math.random() * -100.0);
                break;
            }
            case 3: {
                this.initY = 800 + (int)(Math.random() * 100.0);
                break;
            }
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
            if (this.getX() < Bullet.boundary.x || this.getX() > Bullet.boundary.width) {
                this.dx *= -1;
                this.setX(this.getX() + 2 * this.dx);
            }
            if (this.getY() < Bullet.boundary.y || this.getY() > Bullet.boundary.height) {
                this.dy *= -1;
                this.setY(this.getY() + 2 * this.dy);
            }
        }
    }
    
    public void returnInitXY() {
        this.setX(this.initX);
        this.setY(this.initY);
    }
    
    public void setNewRandomDelta() {
        this.dx = (int)(Math.cos(Math.random() * 360.0) * (Bullet.baseMaxD - Bullet.baseMinD));
        this.dy = (int)(Math.cos(Math.random() * 360.0) * (Bullet.baseMaxD - Bullet.baseMinD));
        if (this.dx == 0) {
            this.dx = (int)(Math.random() * (Bullet.baseMaxD - Bullet.baseMinD) + Bullet.baseMinD);
        }
        if (this.dy == 0) {
            this.dy = (int)(Math.random() * (Bullet.baseMaxD - Bullet.baseMinD) + Bullet.baseMinD);
        }
    }
    
    public static int getBaseMaxD() {
        return Bullet.baseMaxD;
    }
    
    public static int getBaseMinD() {
        return Bullet.baseMinD;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getDx() {
        return this.dx;
    }
    
    public int getDy() {
        return this.dy;
    }
    
    public int getDamage() {
        return this.damage;
    }
    
    public static void setBaseMaxD(final int baseMaxD) {
        Bullet.baseMaxD = baseMaxD;
    }
    
    public static void setBaseMinD(final int baseMinD) {
        Bullet.baseMinD = baseMinD;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setDx(final int dx) {
        this.dx = dx;
    }
    
    public void setDy(final int dy) {
        this.dy = dy;
    }
    
    public void setDamage(final int damage) {
        this.damage = damage;
    }
}
