/*
 * 게임의 적인 총알을 구현한다.
 * 
 * 개선점: 
 * 1. 현재 총알의 위치를 수정하는 함수는 렌더링 함수인 draw를 통해 호출된다.
 *    즉 렌더링과 게임 상태 변화가 묶여 있어 성능 저하의 원인이 된다.
 *    이 것을 분리해야 한다.
 * 2. Item 클래스와 많은 부분이 겹친다. 공통의 부모 클래스를 만들거나 
 *    같이 준수하는 인터페이스를 만들어야 한다.
 * 3. move메서드는 정적이다. 런타임에 변경될 수 없는 구조다. strategy 패턴을 이용한다면
 *    동적으로 움직임 알고리즘이 교체될 수 있을 것이다.
 */
package dodge;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

public class Bullet extends GameObject {
    //balance 데이터
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
        if(dx == 0){    dx = (int)(Math.random() * ( baseMaxD - baseMinD ) + baseMinD);    }
        if(dy == 0){    dy = (int)(Math.random() * ( baseMaxD - baseMinD ) + baseMinD);    }
    
    }
        
    //getters
    public static int getBaseMaxD() {    return baseMaxD;    }
    public static int getBaseMinD() {    return baseMinD;    }
        
    public int getId() {    return id;    }
    
    public int getDx() {    return dx;    }
    public int getDy() {    return dy;    }
    
    public int getDamage() {    return damage;    }

    //setters
    public static void setBaseMaxD(int baseMaxD) {    Bullet.baseMaxD = baseMaxD;    }
    public static void setBaseMinD(int baseMinD) {    Bullet.baseMinD = baseMinD;    }
    
    public void setId(int id) {    this.id = id;    }
    public void setDx(int dx) {    this.dx = dx;    }
    public void setDy(int dy) {    this.dy = dy;    }
    
    public void setDamage(int damage) {    this.damage = damage;    }

}
