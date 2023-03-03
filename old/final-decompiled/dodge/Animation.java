// 
// Decompiled by Procyon v0.5.36
// 

package dodge;

import java.awt.image.ImageObserver;
import java.awt.Graphics2D;
import java.awt.Image;

public class Animation extends GameObject
{
    private final int maxFrame;
    private final int rowFrameNum;
    private final int frameW;
    private final int frameH;
    private final int sheetH;
    private int frame;
    private int frameX;
    private int frameY;
    private boolean isLooping;
    private int delay;
    private int frameRate;
    
    public Animation(final Image image, final int x, final int y, final int w, final int h, final int rowFrameNum, final int allFrameNum, final int frameW, final int frameH, final boolean isLooping) {
        super(image, x, y, w, h);
        this.frame = 0;
        this.delay = 0;
        this.frameRate = 3;
        this.maxFrame = allFrameNum - 1;
        this.rowFrameNum = rowFrameNum;
        this.frameW = frameW;
        this.frameH = frameH;
        this.isLooping = isLooping;
        this.sheetH = allFrameNum / rowFrameNum * frameH;
    }
    
    public Animation(final Image image, final int x, final int y, final int rowFrameNum, final int frameNum, final int frameW, final int frameH, final boolean isLooping) {
        this(image, x, y, frameW, frameH, rowFrameNum, frameNum, frameW, frameH, isLooping);
    }
    
    @Override
    public void draw(final Graphics2D g2d, final ImageObserver observer) {
        if (this.isVisible()) {
            this.enterFrame();
            g2d.drawImage(this.getImage(), this.getX(), this.getY(), this.getX() + this.getW(), this.getY() + this.getH(), this.frameX, this.frameY, this.frameX + this.frameW, this.frameY + this.frameH, observer);
        }
    }
    
    private void enterFrame() {
        if (!this.isLooping && this.frame > this.maxFrame) {
            return;
        }
        this.frameX = this.frame % this.rowFrameNum * this.frameW;
        this.frameY = this.frame / this.rowFrameNum * this.frameH % this.sheetH;
        ++this.frame;
    }
    
    public void rePlay() {
        this.frame = 0;
    }
}
