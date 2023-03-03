/*
 * 게임 상의 아이템인 gold와 repair를 구현한다.
 * 
 * 개선점: 
 * move메서드를 동적으로 변경 가능하게 하여 gold와 repair의 움직임이 다를 수 있어야 한다.
 * 
 */
package dodge;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

public class Item extends Animation {
    //value는 회복량이나 점수량일 수 있다.
    public static int baseValue = 100;
    private int value = baseValue;
    
    private static final Rectangle boundary 
    = new Rectangle(-100, -100, GameFrame.windowW, GameFrame.windowH);

    private static int baseMaxD = 30;
    private static int baseMinD = 10;  
    private int dx;
    private int dy;    

    public Item(Image image, int x, int y, int w, int h, int rowFrameNum, int allFrameNum, int frameW, int frameH,
            boolean isLooping) {
        super(image, x, y, w, h, rowFrameNum, allFrameNum, frameW, frameH, isLooping);
        
        setNewRandomDelta();
    }
    
    public Item(Image image, int x, int y, int rowFrameNum, int allFrameNum, int frameW, int frameH,
            boolean isLooping) {
        super(image, x, y, rowFrameNum, allFrameNum, frameW, frameH, isLooping);
        setNewRandomDelta();
    }
    
    public void setNewRandomDelta(){
        dx = (int)(Math.cos(Math.random()*360) * (baseMaxD - baseMinD));
        dy = (int)(Math.cos(Math.random()*360) * (baseMaxD - baseMinD));
        if(dx == 0){    dx = (int)(Math.random() * ( baseMaxD - baseMinD ) + baseMinD);    }
        if(dy == 0){    dy = (int)(Math.random() * ( baseMaxD - baseMinD ) + baseMinD);    }
    }
    
    @Override
    public void draw(Graphics2D g2d, ImageObserver observer) {
        move();
        super.draw(g2d, observer);        
    };
    
    public void move(){
        //System.out.println(getX() + "," + getY());
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
    
    //화면에서 보이지 않는 곳으로 옮겨진다.
    public void returnInitXY(){
        int initX = (int)(Math.random() * GameFrame.windowW);
        int initY = ((int)(Math.random() * GameFrame.windowH));
        
        switch( (int)(4 * Math.random()) ){
        case 0:
            initX = (int)(Math.random() * -10);
            break;
            
        case 1:
            initX = GameFrame.windowW + (int)(Math.random() * 10);
            break;
            
        case 2:
            initY = (int)(Math.random() * -10);
            break;
            
        case 3:
            initY = GameFrame.windowH + (int)(Math.random() * 10);
            break;
        }
        
        setX(initX);
        setY(initY);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }
    
}
