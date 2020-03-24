package dodge;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

public class Bullet extends GameObject {
	//valence 데이터
	public static int baseDamage = 50;
	private int damage = baseDamage;
	
	private int id;
	
	private static final Rectangle boundary 
		= new Rectangle(-100, -100, GameFrame.windowW + 200, GameFrame.windowH + 200);
	
	private static final int initialW = 11;
	private static final int initialH = 11;
	private int initX;
	private int initY;
	
	private static int baseMaxD = 7;
	private static int baseMinD = 1;  
	private int dx;
	private int dy;	
	

	public Bullet(int id, Image image, int x, int y, int w, int h) {
		super(image, x, y, w, h);
		this.id = id;
		
		setNewRandomDelta();
	}

	public Bullet(int id, Image image, int x, int y) {
		this(id, image, x, y, initialW, initialH);
	}
	
	//
	public Bullet(int id, Image image){
		this(id, image, 450,450, initialW,initialH);
		
		setInitialXY();
			
		setX(initX);
		setY(initY);		
	}
	
	private void setInitialXY(){
		initX = (int)(Math.random() * GameFrame.windowW);
		initY = (int)(Math.random() * GameFrame.windowH);
		
		//x나  y 중 하나를 안 보이는 곳으로 밀어버린다.
		switch( (int)(4 * Math.random()) ){
		case 0:
			initX = (int)(Math.random() * -100);
			break;
			
		case 1:
			initX = GameFrame.windowW + (int)(Math.random() * 100);
			break;
			
		case 2:
			initY = (int)(Math.random() * -100);
			break;
			
		case 3:
			initY = GameFrame.windowH + (int)(Math.random() * 100);
			break;
		}
	}
	
	@Override
	public void draw(Graphics2D g2d, ImageObserver observer) {
		move();
		super.draw(g2d, observer);		
	};
	
	public void move(){
		if(isVisible()){
			setX(getX() + dx);
			setY(getY() + dy);
			
			if(getX() < boundary.x || getX() > boundary.width){
				dx *= -1;				
				setX(getX() + 2 * dx);
			}		
			if(getY() < boundary.y || getY() > boundary.height){
				dy *= -1;							
				setY(getY() + 2 * dy);
			}
		}		
	}	
	
	public void returnInitXY(){
		setX(initX);
		setY(initY);
	}
	
	public void setNewRandomDelta(){
		dx = (int)(Math.cos(Math.random()*360) * (baseMaxD - baseMinD));
		dy = (int)(Math.cos(Math.random()*360) * (baseMaxD - baseMinD));
		if(dx == 0){	dx = (int)(Math.random() * ( baseMaxD - baseMinD ) + baseMinD);	}
		if(dy == 0){	dy = (int)(Math.random() * ( baseMaxD - baseMinD ) + baseMinD);	}
	
	}
		
	//getters
	public static int getBaseMaxD() {	return baseMaxD;	}
	public static int getBaseMinD() {	return baseMinD;	}
		
	public int getId() {	return id;	}
	
	public int getDx() {	return dx;	}
	public int getDy() {	return dy;	}
	
	public int getDamage() {	return damage;	}

	//setters
	public static void setBaseMaxD(int baseMaxD) {	Bullet.baseMaxD = baseMaxD;	}
	public static void setBaseMinD(int baseMinD) {	Bullet.baseMinD = baseMinD;	}
	
	public void setId(int id) {	this.id = id;	}
	public void setDx(int dx) {	this.dx = dx;	}
	public void setDy(int dy) {	this.dy = dy;	}
	
	public void setDamage(int damage) {	this.damage = damage;	}

	

	

}
