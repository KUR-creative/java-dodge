// 
// Decompiled by Procyon v0.5.36
// 

package dodge;

import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

public class Ship extends Animation
{
    public static final int baseHp = 600;
    private int hp;
    private int score;
    private Rectangle boundary;
    private final int baseDelta = 15;
    private final int baseOtherDelta = 30;
    private int delta;
    private boolean isMoving;
    private boolean isShiftKeyDown;
    private boolean isUpKeyDown;
    private boolean isDownKeyDown;
    private boolean isLeftKeyDown;
    private boolean isRightKeyDown;
    Rectangle hitRect;
    private int wingWidth;
    
    public Ship(final Image image, final int x, final int y, final int w, final int h, final int rowFrameNum, final int frameNum, final int frameW, final int frameH, final boolean isLooping) {
        super(image, x, y, w, h, rowFrameNum, frameNum, frameW, frameH, isLooping);
        this.hp = 600;
        this.score = 0;
        this.boundary = null;
        this.delta = 15;
        this.isMoving = false;
        this.isShiftKeyDown = false;
        this.isUpKeyDown = false;
        this.isDownKeyDown = false;
        this.isLeftKeyDown = false;
        this.isRightKeyDown = false;
        this.hitRect = this.getHitRect();
        this.wingWidth = this.hitRect.width / 3;
        this.boundary = new Rectangle(0, 0, 1000 - w, 800 - (int)(1.5 * h));
        System.out.println("\uc804" + this.hitRect.x);
        final Rectangle hitRect = this.hitRect;
        hitRect.x += this.wingWidth;
        System.out.println("\ud6c4" + this.hitRect.x);
        final Rectangle hitRect2 = this.hitRect;
        hitRect2.width /= 3;
        this.hitRect.height = this.hitRect.height * 4 / 5;
    }
    
    @Override
    public void setVisible(final boolean visible) {
        if (!visible) {
            this.isMoving = false;
            this.isShiftKeyDown = false;
            this.isUpKeyDown = false;
            this.isDownKeyDown = false;
            this.isLeftKeyDown = false;
            this.isRightKeyDown = false;
        }
        super.setVisible(visible);
    }
    
    @Override
    public void setX(final int x) {
        super.setX(x);
        this.hitRect.x = x + this.wingWidth;
    }
    
    @Override
    public void setW(final int w) {
        super.setW(w);
        this.hitRect.width = w / 3;
    }
    
    @Override
    public void setH(final int h) {
        super.setH(h);
        this.hitRect.height = h * 4 / 5;
    }
    
    @Override
    public void draw(final Graphics2D g2d, final ImageObserver observer) {
        if (this.isShiftKeyDown) {
            this.delta = 30;
        }
        else {
            this.delta = 15;
        }
        if (this.isUpKeyDown) {
            this.setY(this.getY() - this.delta);
        }
        else if (this.isDownKeyDown) {
            this.setY(this.getY() + this.delta);
        }
        if (this.isLeftKeyDown) {
            this.setX(this.getX() - this.delta);
        }
        else if (this.isRightKeyDown) {
            this.setX(this.getX() + this.delta);
        }
        if (this.getX() < this.boundary.x) {
            this.setX(this.boundary.x + 1);
        }
        else if (this.getX() > this.boundary.width) {
            this.setX(this.boundary.width - 1);
        }
        if (this.getY() < this.boundary.y) {
            this.setY(this.boundary.y + 1);
        }
        else if (this.getY() > this.boundary.height) {
            this.setY(this.boundary.height - 1);
        }
        super.draw(g2d, observer);
    }
    
    public void keyPressed(final KeyEvent e) {
        if (e.getKeyCode() == 16) {
            this.isShiftKeyDown = true;
        }
        if (e.getKeyCode() == 38) {
            this.isUpKeyDown = true;
        }
        else if (e.getKeyCode() == 40) {
            this.isDownKeyDown = true;
        }
        if (e.getKeyCode() == 37) {
            this.isLeftKeyDown = true;
        }
        else if (e.getKeyCode() == 39) {
            this.isRightKeyDown = true;
        }
    }
    
    public void keyReleased(final KeyEvent e) {
        if (e.getKeyCode() == 16) {
            this.isShiftKeyDown = false;
        }
        if (e.getKeyCode() == 38) {
            this.isUpKeyDown = false;
        }
        else if (e.getKeyCode() == 40) {
            this.isDownKeyDown = false;
        }
        if (e.getKeyCode() == 37) {
            this.isLeftKeyDown = false;
        }
        else if (e.getKeyCode() == 39) {
            this.isRightKeyDown = false;
        }
    }
    
    public int getBaseHp() {
        return 600;
    }
    
    public int getHp() {
        return this.hp;
    }
    
    public void setHp(final int hp) {
        this.hp = hp;
    }
    
    public int getScore() {
        return this.score;
    }
    
    public void setScore(final int score) {
        this.score = score;
    }
}
