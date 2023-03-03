/*
 * 장면 PLAYING 에서 
 * 1. 게임 밸런스와 
 * 2. 초기화 등을 담당한다. 
 * 
 * 게임에 매우 의존하며 수정이 잦은 클래스이다.
 */
package dodge;

import javax.swing.JLabel;

public class GameDirector {
    ObjLib objectLib;
    private long start;
    private int elapseSecond;
    private Bullet[] bulletArr;
    private Ship ship;
    
    JLabel timeBox;
    JLabel scoreBox;
    
    public GameDirector(ObjLib objLibRef){
        objectLib = objLibRef;
        timeBox = (JLabel)objectLib.getJComponent("timeBox");
        scoreBox = (JLabel)objectLib.getJComponent("scoreBox");
        bulletArr = objectLib.getAllBullets();
        ship = (Ship)objectLib.get("ship"); 
    }
    
    
    //이걸 해야 시간을 재고 총알과 아이템들을 적절히 생성한다.
    public void gameStart(){
        start = System.currentTimeMillis();
        ((Ship) objectLib.get("ship")).setHp(Ship.baseHp);
                
        for(Bullet bullet : bulletArr){
            bullet.returnInitXY();
            bullet.setNewRandomDelta();
        }    
        
        //초기화
        bulletsDmgInit();
        repairInit();
        goldInit();    
        
        ship.setScore(0);
    }    
    
    public void gameEnd(){
        timeBox.setText(Integer.toString(elapseSecond));
        scoreBox.setText(Integer.toString(ship.getScore()));
        JLabel result = (JLabel)objectLib.getJComponent("result");
        result.setText( "result: " + (elapseSecond * ship.getScore())/100 );
            
    }
    
    //프레임마다 호출된다.
    public void enterFrame(){
        computeElapseTime();
        setBulletsVisible();
        //생존 시간에 따른 보상 증가
        bulletsDmgUp();
        repairUp();
        goldUp();
    }
    
    private void computeElapseTime(){        
        elapseSecond = (int)(System.currentTimeMillis() - start)/100;
    }
            
    private void setBulletsVisible(){
        //y = ax + 100 일차식
        int bulletNum = elapseSecond/2 + 140;
        objectLib.setBulletsVisibleFromZero(bulletNum, true);
    }
    
    private void bulletsDmgUp(){        
        for(Bullet bullet : bulletArr){
            bullet.setDamage(bullet.getDamage() + elapseSecond/150);// TODO/20 하기. start = System.currentTimeMillis();
        }
    }
    
    private void bulletsDmgInit(){
        for(Bullet bullet : bulletArr){
            bullet.setDamage(bullet.baseDamage);                            
        }            
    }
    
    //시간이 갈 수록 효과가 좋아진다.
    private void repairUp(){
        Item repair = (Item)objectLib.get("repair");
        repair.setValue(repair.getValue() + elapseSecond/100);
        if(elapseSecond % 30 == 0){
            repair.setNewRandomDelta();
        }        
    }
    
    private void repairInit(){
        Item repair = (Item)objectLib.get("repair");
        repair.setValue(repair.baseValue);
    }
    
    //시간이 갈 수록 효과가 좋아진다.
    private void goldUp(){
        Item[] goldArr = objectLib.getAllgolds();
        for(Item gold : goldArr){
            gold.setValue(gold.getValue() + elapseSecond/40);                
        }
    }
    
    private void goldInit(){
        Item[] goldArr = objectLib.getAllgolds();
        for(Item gold : goldArr){
            gold.setValue(gold.baseValue);    
        }
    }
    
}
