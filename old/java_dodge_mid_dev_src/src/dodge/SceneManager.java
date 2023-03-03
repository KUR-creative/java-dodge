/*
 * 일단은 돌아가게 하지만, TODO: scene Node 구조로 refactoring이 필요하다.
 * -> 장면을 기술하는 클래스 Scene를 만들고 이걸 상속해서 ReacyScene, PlayingScene...
 * 등을 만든다. 그리고 SceneManager 클래스는 이것들을 적절한 자료구조에 보관한다.
 * 그리고 보관된 Scene들을 적절히 움직여주면 되겠지만... 나중에 한다.
 * 
 * 일단 scene과 관련된 모든 일들을 여기서 캡슐화한다.
 */
package dodge;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import dodge.SceneManager.SCENE;

public class SceneManager {
	private static SceneManager _instance;
	private ObjLib objectLib;
	private GameDirector director;
	
	public static enum SCENE{
		READY, PLAYING, GAME_OVER, SCORES;
	}
	private SCENE nowScene = SCENE.READY;
	
	//싱글톤
	public static SceneManager instance() {
		return _instance;
	}
	public static void initSceneManager(ObjLib objLibRef){
		_instance = new SceneManager(objLibRef);
	}	
	private SceneManager(ObjLib objLibRef){
		this.objectLib = objLibRef;
		director = new GameDirector(objLibRef);
	}
		
	public void change(SCENE scene){
		nowScene = scene;
		enterScene();
	}	
	
	//TODO: 나중에 예외처리할 것.
	//장면 들어가기 전 초기화에 가깝다.
	private void enterScene(){
		objectLib.deactivateAll();
		
		//---------------------READY------------------------
		JLabel titleLabel			= (JLabel)objectLib.getJComponent("title");				
		JLabel enterInfoLabel	= (JLabel)objectLib.getJComponent("enterInfo");		
		JButton shiftKeyInfo	= (JButton)objectLib.getJComponent("shiftInfo");	
		JButton upKeyInfo			= (JButton)objectLib.getJComponent("upInfo");   	
		JButton downKeyInfo		= (JButton)objectLib.getJComponent("downInfo"); 	
		JButton leftKeyInfo		= (JButton)objectLib.getJComponent("leftInfo"); 	
		JButton rightKeyInfo	= (JButton)objectLib.getJComponent("rightInfo");	
		JButton highScoreBtn	= (JButton)objectLib.getJComponent("highScores");	
		JButton creditBtn			= (JButton)objectLib.getJComponent("creditBtn");
		
		//---------------------PLAYING------------------------
		JLabel hpLabel		= (JLabel)objectLib.getJComponent("hp");
		JLabel scoreLabel	= (JLabel)objectLib.getJComponent("scoreLabel");
		JLabel timeLabel	= (JLabel)objectLib.getJComponent("timeLabel");
		JLabel scoreBox	= (JLabel)objectLib.getJComponent("scoreBox");
		JLabel timeBox	= (JLabel)objectLib.getJComponent("timeBox");
		
		//---------------------GAME_OVER-----------------------
		JLabel gameOverLabel = (JLabel)objectLib.getJComponent("gameOver");
		
		
		//---------------------SCORES------------------------
		JTextArea scoreArea 		= (JTextArea)objectLib.getJComponent("scoreArea");
		JScrollPane scrollPane	= (JScrollPane)objectLib.getJComponent("scrollBar");
		
		GameObject hpBar = objectLib.get("hpBar");
		Ship ship = (Ship)objectLib.get("ship");
		Animation shipExplosion = (Animation)objectLib.get("shipExplosion");
		Item repair = (Item)objectLib.get("repair");
		
		
		switch(nowScene){
		case READY:
			//활성화
			titleLabel.setVisible(true);
			enterInfoLabel.setVisible(true);
			shiftKeyInfo.setVisible(true);
			upKeyInfo.setVisible(true);
			downKeyInfo.setVisible(true);
			leftKeyInfo.setVisible(true);
			rightKeyInfo.setVisible(true);
			highScoreBtn.setVisible(true);
			creditBtn.setVisible(true);
			
			//java dodge!!
			titleLabel.setFont(new Font("Consolas",Font.BOLD,120));
			titleLabel.setForeground(Color.WHITE);
			titleLabel.setLocation(titleLabel.getX(), 200);
			titleLabel.setBounds(60,50,900,200);
			
			//[ENTER]키 안내
			enterInfoLabel.setFont(new Font("Consolas",Font.BOLD,50));
			enterInfoLabel.setForeground(Color.WHITE);
			enterInfoLabel.setBounds(165,480,900,100);
			
			//쉬프트 키 안내
			shiftKeyInfo.setFont(new Font("Arial",Font.BOLD,35));
			shiftKeyInfo.setBounds(155,370,150,70);
			//방향키 안내
			upKeyInfo.setFont(new Font("Arial",Font.BOLD,35));
			downKeyInfo.setFont(new Font("Arial",Font.BOLD,35));
			leftKeyInfo.setFont(new Font("Arial",Font.BOLD,35));
			rightKeyInfo.setFont(new Font("Arial",Font.BOLD,35));
			upKeyInfo.setBounds(710,290,70,70);
			downKeyInfo.setBounds(710,370,70,70);
			leftKeyInfo.setBounds(630,370,70,70);
			rightKeyInfo.setBounds(790,370,70,70);
			
			highScoreBtn.setFont(new Font("Consolas",Font.BOLD,15));
			highScoreBtn.setBounds(377,600,200,40);
			
			creditBtn.setFont(new Font("Consolas",Font.BOLD,15));
			creditBtn.setBounds(377,670,200,40);
			
			//리스너 추가
			highScoreBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					change(SCENE.SCORES);
				}
			});
			
			//시연용 함선
			ship.setVisible(true);
			
			break;
			
		case PLAYING:
			//게임 밸런서 시작.
			director.gameStart();
			//활성화
			hpLabel.setVisible(true);
			scoreLabel.setVisible(true);
			scoreBox.setVisible(true);
			//timeLabel.setVisible(true);
			//timeBox.setVisible(true);
			Item[] itemArr = objectLib.getAllgolds();
			for(Item item : itemArr){
				item.setVisible(true);
				item.setDy((int)(7 * Math.random() + 3));
				item.setDx((int)(7 * Math.random() + 3));
			}

			hpBar.setVisible(true);
			ship.setVisible(true);
			repair.setVisible(true);
			
			//이거는 이제 밸런서가 조절한다. -> 시간에 따라 달라져야 한다.
			//objectLib.setAllBulletsVisibility(true);
			objectLib.setAllBulletBumpsVisibility(true);
			
			//Hull 
			hpLabel.setFont(new Font("Arial",Font.BOLD,32));
			hpLabel.setForeground(Color.GREEN);
			hpLabel.setBounds(131,10,100,30);
			
			/*time
			timeLabel.setFont(new Font("Arial",Font.BOLD,15));
			timeLabel.setForeground(Color.CYAN);
			timeLabel.setBounds(GameFrame.windowW - 260,10,100,30);
			//time box
			timeBox.setFont(new Font("Courier New",Font.BOLD,15));
			timeBox.setForeground(Color.CYAN);
			timeBox.setBounds(GameFrame.windowW - 210,10,100,30);
			*/
			
			//score
			scoreLabel.setFont(new Font("Arial",Font.BOLD,15));
			scoreLabel.setForeground(Color.YELLOW);
			scoreLabel.setBounds(GameFrame.windowW - 130,10,100,30);
			//score box
			scoreBox.setFont(new Font("Courier New",Font.BOLD,15));
			scoreBox.setForeground(Color.YELLOW);
			scoreBox.setBounds(GameFrame.windowW - 80,10,100,30);
						
			break;
			
		case GAME_OVER:
			//게임 끝
			director.gameEnd();
			//활성화
			gameOverLabel.setVisible(true);
			timeLabel.setVisible(true);
			timeBox.setVisible(true);
			scoreLabel.setVisible(true);
			scoreBox.setVisible(true);
			
			enterInfoLabel.setVisible(true);
			
			shipExplosion.setVisible(true);
			
			//game over!			
			gameOverLabel.setFont(new Font("Consolas",Font.BOLD,100));
			gameOverLabel.setForeground(Color.WHITE);
			gameOverLabel.setLocation(titleLabel.getX(), 200);
			gameOverLabel.setBounds(150,100,900,100);
			
			//time
			timeLabel.setFont(new Font("Arial",Font.BOLD,50));
			timeLabel.setForeground(Color.CYAN);
			timeLabel.setBounds(450,307, 300,50);
			//time box
			timeBox.setFont(new Font("Courier New",Font.BOLD,50));
			timeBox.setForeground(Color.CYAN);
			timeBox.setBounds(600,310, 300,50);
			
			//score
			scoreLabel.setFont(new Font("Arial",Font.BOLD,50));
			scoreLabel.setForeground(Color.YELLOW);
			scoreLabel.setBounds(450,395, 300,50);
			//score box
			scoreBox.setFont(new Font("Courier New",Font.BOLD,50));
			scoreBox.setForeground(Color.YELLOW);
			scoreBox.setBounds(600,400, 3100,50);
			
			//press [ENTER] to start
			enterInfoLabel.setFont(new Font("Consolas",Font.BOLD,45));
			enterInfoLabel.setForeground(Color.WHITE);
			enterInfoLabel.setBounds(165,670,900,100);
			
			shipExplosion.setXY( ship.getX(), ship.getY() );
			shipExplosion.rePlay();
			break;
			
		case SCORES:
			scrollPane.setVisible(true);
			scoreArea.setVisible(true);
			scrollPane.setVisible(true);
			
			enterInfoLabel.setVisible(true);
			
			//scoreBox.setEditable(false);
			scoreArea.setFont(new Font("Serif",Font.BOLD,50));
			scoreArea.setBounds(100, 100, GameFrame.windowW - 200, GameFrame.windowH - 300);
			scrollPane.setBounds(100, 100, GameFrame.windowW - 200, GameFrame.windowH - 300);
			
			//press [ENTER] to start
			enterInfoLabel.setFont(new Font("Consolas",Font.BOLD,45));
			enterInfoLabel.setForeground(Color.WHITE);
			enterInfoLabel.setBounds(165,670,900,100);
			break;
		}
	}
	
	//====================동일한 이벤트가 와도 장면에 따라 달라지는 반응====================
	public void enterFrame(){
		switch(nowScene){
  	case READY:  		
  		break;
  		
  	case PLAYING:
  		//밸런서 프레임마다 업데이트.
  		director.enterFrame();
  		
  		GameObject hpBar = objectLib.get("hpBar");
  		Ship ship = (Ship)objectLib.get("ship");
  		
  		//ship과 bullet들 충돌 체크
  		Bullet[] bulletArr = objectLib.getAllBullets();
  		for(Bullet bullet : bulletArr){
  			if(ship.hitTest(bullet)){
  				ship.setHp(ship.getHp() - bullet.getDamage());
  				//총알은 파괴된다. bump가 파괴 위치에 와서 재생된다.
  				Animation bulletBump = objectLib.getNextBulletBump();
  				bulletBump.setXY(bullet.getX(), bullet.getY());
  				bulletBump.rePlay();
  				bullet.returnInitXY();
  			}
  		}
  		
  		//repair와 ship 충돌 체크
  		Item repair = (Item)objectLib.get("repair");
  		if(ship.hitTest(repair)){
  			ship.setHp(ship.getHp() + repair.getValue());
  			repair.returnInitXY();
  		}
  		
  		//ship과 gold 충돌 체크
  		JLabel scoreBox	= (JLabel)objectLib.getJComponent("scoreBox");
  		Item[] goldArr = objectLib.getAllgolds();
			for(Item gold : goldArr){
				if(ship.hitTest(gold)){
	  			ship.setScore( ship.getScore() + gold.getValue() );
	  			gold.returnInitXY();	  			
	  		}
			}
			
  		//hpBar 업데이트
  		hpBar.setWidthByRatio((double)ship.getHp() / (double)ship.getBaseHp());

  		//ship 사망
  		if(ship.getHp() <= 0){
  			change(SCENE.GAME_OVER);
  		}
  		
  		break;
  		
  	case GAME_OVER:
  		
  		break;
  		
  	case SCORES:
  		break;
  	}
	}

	public void keyPressed(KeyEvent e){
		Ship ship = (Ship)ObjLib.instance().get("ship");
		
		switch(nowScene){
  	case READY:
  		// press ENTER to start
  		if(e.getKeyCode() == KeyEvent.VK_ENTER){
  			change(SCENE.PLAYING);
  		}
  		//시연용 ship
  		ship.keyPressed(e);
  		break;
  		
  	case PLAYING:
  		ship.keyPressed(e);
  		break;
  		
  	case GAME_OVER:
  		// press ENTER to start
  		if(e.getKeyCode() == KeyEvent.VK_ENTER){
  			change(SCENE.PLAYING);
  		}
  		break;
  		
  	case SCORES:
  		// press ENTER to start
  		if(e.getKeyCode() == KeyEvent.VK_ENTER){
  			change(SCENE.PLAYING);
  		}
  		break;
  	}
	}
	
	public void keyReleased(KeyEvent e){
		Ship ship = (Ship)ObjLib.instance().get("ship");
		switch(nowScene){
  	case READY:
  		ship.keyReleased(e);
  		break;
  		
  	case PLAYING:
  		ship.keyReleased(e);
  		break;
  		
  	case GAME_OVER:
  		break;
  		
  	case SCORES:
  		break;
  	}
	}
		
	public SCENE getNowScene(){
		return nowScene;
	}	
}
