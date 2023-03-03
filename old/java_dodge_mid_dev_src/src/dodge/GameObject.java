package dodge;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

public class GameObject {
	private boolean visible;
	
	private int x;
	private int y;
	private int w;
	private int h;	
	
	private int initW;
	private int initH;
	
	private Image image;
	
	private Rectangle hitRect = null; //충돌 체크도 한다.
	
	public GameObject(Image image, int x, int y, int w, int h){
		this.x = x;
		this.y = y;
		this.w = initW = w;
		this.h = initH = h;
		this.image = image;
		hitRect = new Rectangle(x, y, w, h);
	}
	
	public void draw(Graphics2D g2d, ImageObserver observer){	
		if(visible){
			g2d.drawImage(image, x, y, w, h, observer);	
		}		
	}
	
	
	public void setWidthByRatio(double ratio){
		setW( (int)(ratio * initW) );
	}
	
	public boolean isVisible() {	return visible;	}
	public void setVisible(boolean visible) {	this.visible = visible;	}
	
	public int getX() {	return x;	}
	public int getY() {	return y;	}
	public int getW() {	return w;	}
	public int getH() {	return h;	}

	public void setX(int x) {	this.x = x;	hitRect.x = x;}
	public void setY(int y) {	this.y = y;	hitRect.y = y;}
	public void setW(int w) {	this.w = w;	hitRect.width = w;}
	public void setH(int h) {	this.h = h;	hitRect.height = h;}
	public void setXY(int x, int y){	
		this.x = x;	
		this.y = y;
		hitRect.x = x;	
		hitRect.y = y;
	}
	
	public Image getImage() {	return image;	}

	public Rectangle getHitRect() {	return hitRect;	}

	public boolean hitTest(GameObject obj) {
		if(visible && obj.visible){
			if (hitRect.x < obj.hitRect.x + obj.hitRect.width		&&	// a.좌 < b.우
					hitRect.x + hitRect.width > obj.hitRect.x				&&	// a.우 > b.좌
					hitRect.y < obj.hitRect.y + obj.hitRect.height	&&	// a.하 < b.상
					hitRect.height + hitRect.y > obj.hitRect.y) 				// a.상 > b.하
			{
				return true;
			}
		}		
		return false;
	}

	
	
}
