/*
 * 어쩌면 Hp 같은 거는 따로 User 클래스를 만드는게 좋을지도 모르지만
 * 일단은 ship과 같이 간다...
 */

package dodge;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;

public class Ship extends Animation {	
	//valence 데이터
	public static final int baseHp = 600;
	private int hp = baseHp;
	private int score = 0;
	
	private Rectangle boundary = null;	
	
	private final int baseDelta = 15;
	private final int baseLowDelta = 7;
	private int delta = baseDelta;
	
	private boolean isMoving = false;
	
	private boolean isShiftKeyDown = false;
	private boolean isUpKeyDown = false;
	private boolean isDownKeyDown = false;
	private boolean isLeftKeyDown = false;
	private boolean isRightKeyDown = false;
	
	//ship은 특별한 충돌범위를 가진다. 날개와 불꽃은 충돌범위가 아니다.
	// "동체" 만 충돌범위이다.
	Rectangle hitRect = getHitRect();
	private int wingWidth = hitRect.width / 3;
	
	
	public Ship(Image image, int x, int y, int w, int h,
			int rowFrameNum, int frameNum, int frameW, 
			int frameH, boolean isLooping) {
		super(image, x, y, w, h, rowFrameNum, frameNum, frameW, frameH, isLooping);
		boundary = new Rectangle(0,0,
			GameFrame.windowW - w, GameFrame.windowH - (int)(1.5 * h));
			//h * 1.5.. 왜지?
				
		//날개와 불꽃을 충돌범위에서 제외한다.
		System.out.println("전" + hitRect.x);
		hitRect.x += (int)(wingWidth);	System.out.println("후" + hitRect.x);
		hitRect.width /= 3;
		hitRect.height = (hitRect.height * 4) / 5;
	}
	
	@Override
	public void setVisible(boolean visible) {
		if(visible == false){
			isMoving = false;      
      
			isShiftKeyDown = false;
			isUpKeyDown = false;   
			isDownKeyDown = false; 
			isLeftKeyDown = false; 
			isRightKeyDown = false;
		}
		super.setVisible(visible);
	};
	
	//ship은 특별한 충돌대상을 가진다.
	//x가 바뀌면 날개 길이만큼 더해야 한다. y는 괜찮음.
	@Override
	public void setX(int x) {	
		super.setX(x);
		hitRect.x = x + wingWidth;
	}
	//크기를 바꾸면 다시 계산해야 한다. TODO:테스트 하지 않음
	@Override
	public void setW(int w) {	
		super.setW(w);
		hitRect.width = w / 3;
	}
	@Override
	public void setH(int h) {	
		super.setH(h);
		hitRect.height = (h * 4) / 5;
	}
	
	@Override
	public void draw(Graphics2D g2d, ImageObserver observer) {
		
			//shift
			if(isShiftKeyDown){
				delta = baseLowDelta;
			}else{
				delta = baseDelta;
			}
			//y
			if(isUpKeyDown){
				setY(getY() - delta);
			}else if(isDownKeyDown){
				setY(getY() + delta);
			}
			//x
			if(isLeftKeyDown){
				setX(getX() - delta);
			}else if(isRightKeyDown){
				setX(getX() + delta);
			}
			
			//moving boundary
			if(getX() < boundary.x){
				setX(boundary.x + 1);
			}else if(getX() > boundary.width){
				setX(boundary.width - 1);
			}		
			if(getY() < boundary.y){
				setY(boundary.y + 1);
			}else if(getY() > boundary.height){
				setY(boundary.height - 1);
			}
		
		
		super.draw(g2d, observer);				
	}
	
	public void keyPressed(KeyEvent e) {
		//shift
		if(e.getKeyCode() == KeyEvent.VK_SHIFT){
			isShiftKeyDown = true;
		}
		//y
		if(e.getKeyCode() == KeyEvent.VK_UP){
			isUpKeyDown = true;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN){
			isDownKeyDown = true;
		}
		//x
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			isLeftKeyDown = true;
		}else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			isRightKeyDown = true;
		}
	}    

	public void keyReleased(KeyEvent e) {
		//shift
		if(e.getKeyCode() == KeyEvent.VK_SHIFT){
			isShiftKeyDown = false;
		}
		//y
		if(e.getKeyCode() == KeyEvent.VK_UP){
			isUpKeyDown = false;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN){
			isDownKeyDown = false;
		}
		//x
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			isLeftKeyDown = false;
		}else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			isRightKeyDown = false;
		}
	}

	public int getBaseHp() {	return baseHp;	}	
	public int getHp() {	return hp;	}
	public void setHp(int hp) {	this.hp = hp;	}

	public int getScore() {	return score;	}
	public void setScore(int score) {	this.score = score;	}
	
}
