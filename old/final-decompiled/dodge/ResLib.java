// 
// Decompiled by Procyon v0.5.36
// 

package dodge;

import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Image;

public class ResLib
{
    private static ResLib _instance;
    private Image bulletRes;
    private Image bulletBumpRes;
    private Image shipRes;
    private Image repairRes;
    private Image goldRes;
    private Image hpBarRes;
    private Image explosionRes;
    
    public static void initResLib() {
        ResLib._instance = new ResLib();
    }
    
    private ResLib() {
        this.loadAllResources();
    }
    
    private void loadAllResources() {
        this.bulletRes = this.loadImg(new File("img" + File.separator + "bullet.png"));
        this.bulletBumpRes = this.loadImg(new File("img" + File.separator + "bulletBump.png"));
        this.shipRes = this.loadImg(new File("img" + File.separator + "ship.png"));
        this.repairRes = this.loadImg(new File("img" + File.separator + "repair.png"));
        this.goldRes = this.loadImg(new File("img" + File.separator + "gold.png"));
        this.hpBarRes = this.loadImg(new File("img" + File.separator + "hpBar.png"));
        this.explosionRes = this.loadImg(new File("img" + File.separator + "explosion.png"));
    }
    
    private Image loadImg(final File path) {
        try {
            return ImageIO.read(path);
        }
        catch (IOException e) {
            System.out.println(e.toString());
            return null;
        }
    }
    
    public Image getBulletRes() {
        return this.bulletRes;
    }
    
    public Image getBulletBumpRes() {
        return this.bulletBumpRes;
    }
    
    public Image getShipRes() {
        return this.shipRes;
    }
    
    public Image getRepairRes() {
        return this.repairRes;
    }
    
    public Image getGoldRes() {
        return this.goldRes;
    }
    
    public Image getHpBarRes() {
        return this.hpBarRes;
    }
    
    public Image getExplosionRes() {
        return this.explosionRes;
    }
    
    public static ResLib instance() {
        return ResLib._instance;
    }
}
