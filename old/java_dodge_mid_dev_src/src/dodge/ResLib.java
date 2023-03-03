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
		bulletRes			= loadImg(new File("img" + File.separator + "bullet.png"));
		bulletBumpRes	= loadImg(new File("img" + File.separator + "bulletBump.png"));
		shipRes				= loadImg(new File("img" + File.separator + "ship.png"));
		repairRes			= loadImg(new File("img" + File.separator + "repair.png"));
		goldRes				= loadImg(new File("img" + File.separator + "gold.png"));
		hpBarRes			= loadImg(new File("img" + File.separator + "hpBar.png"));
		explosionRes	= loadImg(new File("img" + File.separator + "explosion.png"));
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

	public Image getBulletRes() 		{	return bulletRes;	}
	public Image getBulletBumpRes() {	return bulletBumpRes;	}
	public Image getShipRes() 			{	return shipRes;	}
	public Image getRepairRes() 		{	return repairRes;	}
	public Image getGoldRes() 			{	return goldRes;	}
	public Image getHpBarRes() 			{	return hpBarRes;	}	
	public Image getExplosionRes()	{	return explosionRes;	}

	public static ResLib instance() {	return _instance;	}		
}
