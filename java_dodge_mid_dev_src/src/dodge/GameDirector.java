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
	
	
	int[] speedUpTimeArr = { 10, 30, 60, 100, 150};
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
		
		bulletsDmgInit();
		repairInit();
		ship.setScore(0);
	}	
	
	public void gameEnd(){
		timeBox.setText(Integer.toString(elapseSecond));
		
		scoreBox.setText(Integer.toString(ship.getScore()));
	}
	
	//프레임마다 호출된다.
	public void enterFrame(){
		computeElapseTime();
		setBulletsVisible();
		bulletsDmgUp();
		repairUp();
	}
	
	private void computeElapseTime(){		
		elapseSecond = (int)(System.currentTimeMillis() - start)/100;
	}
			
	private void setBulletsVisible(){
		//y = ax + 100 일차식
		int bulletNum = elapseSecond + 140;
		objectLib.setBulletsVisibleFromZero(bulletNum, true);
	}
	
	private void bulletsDmgUp(){
		for(int time : speedUpTimeArr){
			if(elapseSecond == time){
				for(Bullet bullet : bulletArr){
				bullet.setDamage(bullet.getDamage() + elapseSecond/50);
				}	
			}
		}		
	}
	
	private void bulletsDmgInit(){
		for(int time : speedUpTimeArr){
			if(elapseSecond == time){
				for(Bullet bullet : bulletArr){
				bullet.setDamage(bullet.baseDamage);
				}	
			}
		}		
	}
	
	//시간이 갈 수록 효과가 좋아진다.
	private void repairUp(){
		Item repair = (Item)objectLib.get("repair");
		repair.setValue(repair.getValue() + elapseSecond/50);
		if(elapseSecond % 10 == 0){
			repair.setNewRandomDelta();
		}
	}
	
	private void repairInit(){
		Item repair = (Item)objectLib.get("repair");
		repair.setValue(repair.baseValue);
	}
	
	
}
