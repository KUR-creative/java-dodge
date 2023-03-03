/*
 * 게임에 사용될 모든 리소스를 로드하고 저장한다.
 * 현재는 이미지 리소스만 저장한다.
 * 시스템에 단 하나만 존재해야하기에 싱글톤 패턴을 적용하였다. 
 * 
 * 개선점: 메타데이터(따로 파일 경로나 이름이 있는  텍스트 파일)를 
 * 이용한다면 반복되는 코드를  줄일 수 있으며 생산성과 범용성을 높일 여지가 있다.
 */
package dodge;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ResLib {
    private static ResLib _instance;
    private Image bulletRes;
    private Image bulletBumpRes;
    private Image shipRes;
    private Image repairRes;
    private Image goldRes;
    private Image hpBarRes;
    private Image explosionRes;
    
    public static void initResLib(){
        _instance = new ResLib();
    }
    
    private ResLib(){
        loadAllResources();
    }
    
    private void loadAllResources(){
        bulletRes     = loadImg(new File("img" + File.separator + "bullet.png"));
        bulletBumpRes = loadImg(new File("img" + File.separator + "bulletBump.png"));
        shipRes       = loadImg(new File("img" + File.separator + "ship.png"));
        repairRes     = loadImg(new File("img" + File.separator + "repair.png"));
        goldRes       = loadImg(new File("img" + File.separator + "gold.png"));
        hpBarRes      = loadImg(new File("img" + File.separator + "hpBar.png"));
        explosionRes  = loadImg(new File("img" + File.separator + "explosion.png"));
    }
    
    private Image loadImg(File path){
        try {
            return ImageIO.read(path);
        }
        catch (IOException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public Image getBulletRes()     { return bulletRes; }
    public Image getBulletBumpRes() { return bulletBumpRes; }
    public Image getShipRes()       { return shipRes; }
    public Image getRepairRes()     { return repairRes; }
    public Image getGoldRes()       { return goldRes; }
    public Image getHpBarRes()      { return hpBarRes; }    
    public Image getExplosionRes()  { return explosionRes; }

    public static ResLib instance() { return _instance; }        
}
