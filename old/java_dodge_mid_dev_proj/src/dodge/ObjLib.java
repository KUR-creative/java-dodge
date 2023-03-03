package dodge;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class ObjLib {
	private static ObjLib _instance;
	
	public static final int initOut = -100;
	
	/*총알들 같은건 어떡할건데? 섣불리 하지 말자.
	public static enum NAME{
		title, 
		enter_info, shift_info, up_info, 
	}
	*/
	
	//TODO: 일단 문자열로 해뒀다가 나중에 ENUM으로 가능하면 바꿔라. 그 땐 아예 아무것도 할당 안 하기!
	//TODO: 나중에 이넘으로 해결되면 get(다양한 이넘클래스 오버로드)로 해결 가능하겠다. 
	private TreeMap<String, JComponent> jcomponentMap = new TreeMap<>();
	
	//네임드 오브젝트: 단 하나만 존재하는 게임 오브젝트.
	private TreeMap<String, GameObject> namedObjMap = new TreeMap<>();
	
	//총알 모음
	public static final int maxBulletNum = 300; //적당히 해
	private int bulletNum = maxBulletNum;
	private ArrayList<Bullet> bulletArr = new ArrayList<>(bulletNum);
	
	//총알 파괴 효과 모음
	private final int maxBumpNum = 20;
	private ArrayList<Animation> bulletBumpArr = new ArrayList<>(maxBumpNum);
	//총알 파괴시 옮겨지는 bulletBump의 인덱스다. 
	private int bumpIndex = 0;
	
	private final int maxGoldNum = 20;
	private ArrayList<Item> goldArr = new ArrayList<>();
	private int goldIndex = 0;
	
	public static ObjLib instance(){
		return _instance;
	}
	
	public static void initObjLib() {
		_instance = new ObjLib();		
	}
	
//여기서는 생성만 하고, 관리는 sceneManager가 한다!	
	private ObjLib(){
		//---------------------READY------------------------
		JLabel titleLabel = new JLabel(" java dodge!!");
		JLabel enterInfoLabel = new JLabel("press   [ENTER]  to start");
		JButton shiftKeyInfo	= new JButton("Shift");		
		JButton upKeyInfo			= new JButton("↑");
		JButton downKeyInfo		= new JButton("↓");
		JButton leftKeyInfo		= new JButton("←");
		JButton rightKeyInfo	= new JButton("→");
		JButton highScoreBtn = new JButton("high scores");
		JButton creditBtn = new JButton("who made this?");
		jcomponentMap.put("title", titleLabel);
		jcomponentMap.put("enterInfo", enterInfoLabel);
		jcomponentMap.put("shiftInfo", shiftKeyInfo);
		jcomponentMap.put("upInfo", upKeyInfo);
		jcomponentMap.put("downInfo", downKeyInfo);
		jcomponentMap.put("leftInfo", leftKeyInfo);
		jcomponentMap.put("rightInfo", rightKeyInfo);
		jcomponentMap.put("highScores", highScoreBtn);
		jcomponentMap.put("creditBtn", creditBtn);
		
		//---------------------PLAYING------------------------
		JLabel hpLabel		= new JLabel("Hull");
		JLabel scoreLabel	= new JLabel("score");
		JLabel timeLabel	= new JLabel("  time");
		JLabel scoreBox	= new JLabel("not yet");
		JLabel timeBox	= new JLabel("56789");
		jcomponentMap.put("hp", hpLabel);
		jcomponentMap.put("scoreLabel", scoreLabel);
		jcomponentMap.put("timeLabel", timeLabel);
		jcomponentMap.put("scoreBox", scoreBox);
		jcomponentMap.put("timeBox", timeBox);
		
		
		//---------------------GAME_OVER------------------------
		JLabel gameOverLabel = new JLabel(" Game\n Over!!");
		JLabel result = new JLabel("aaaa");
		jcomponentMap.put("gameOver", gameOverLabel);
		jcomponentMap.put("result", result);
		
		//---------------------SCORES------------------------
		JTextArea scoreArea 			= new JTextArea();        
		JScrollPane scrollPane	= new JScrollPane(scoreBox);
		jcomponentMap.put("scoreArea", scoreArea);
		jcomponentMap.put("scrollBar", scrollPane);
		
		//기본적으로 모든 JComponent들은 포커스를 빼놓는다.
		setAllJcomponentsUnfocusable();
		
		
		//====================GameObject=====================
		
		//네임드 오브젝트
		GameObject hpBar = new GameObject(ResLib.instance().getHpBarRes(), 10, 10, 300, 30);
		Ship ship = new Ship(ResLib.instance().getShipRes(), 
				450, 300, 50, 65, 5, 5, 51, 86, true);
		Animation shipExplosion = new Animation(ResLib.instance().getExplosionRes(),
				initOut, initOut, 80, 80, 4, 16, 64, 64, false); 
		
		Item repair = new Item(ResLib.instance().getRepairRes(), 
				initOut, initOut, 4, 16, 69, 69, true);
		//---------------------네임드 맵에 넣기--------------
		namedObjMap.put("hpBar", hpBar);
		namedObjMap.put("ship", ship);
		namedObjMap.put("shipExplosion", shipExplosion);
		namedObjMap.put("repair", repair);
		
		//bullet의 id는 인덱스와 동일하다.
		for(int i = 0; i < maxBulletNum; i++){
			bulletArr.add( new Bullet(i,ResLib.instance().getBulletRes()) );
		}
		
		for(int i = 0; i < maxBumpNum; i++){
			bulletBumpArr.add(new Animation(ResLib.instance().getBulletBumpRes(),
					initOut,initOut, 4,16, 31,28, false));
		}
		
		for(int i = 0; i < maxGoldNum; i++){
			goldArr.add(new Item(ResLib.instance().getGoldRes(),
					initOut,initOut, 4,16, 36,36, false));
		}
	}
	
	
	//--------------------getObjects--------------------------
	public JComponent getJComponent(String key){
		return jcomponentMap.get(key);
	}
	
	public GameObject get(String key){
		return namedObjMap.get(key);
	}
	
	public JComponent[] getAllJComponents(){
		JComponent[] retArr = new JComponent[jcomponentMap.size()];
		Collection c = jcomponentMap.values();
    Iterator itr = c.iterator();
    
    int i = 0;
    while(itr.hasNext()){
    	retArr[i] = (JComponent)itr.next();
    	i++;
    }    
		return retArr;
	}
	
	//기본적으로 모든 jcomponent들은 포커스 빼놓는다.
	private void setAllJcomponentsUnfocusable(){
		Collection c = jcomponentMap.values();
    Iterator itr = c.iterator();
    
    while(itr.hasNext()){
    	((JComponent) itr.next()).setFocusable(false);
    }
	}
	
	public Bullet[] getAllBullets(){
		Bullet[] retArr = new Bullet[bulletArr.size()];
		return bulletArr.toArray(retArr);
	}
	
	public Bullet getBulletByID(int id){
		return bulletArr.get(id);
	}
	
	public Animation getNextBulletBump(){
		if(bumpIndex == maxBumpNum - 1){
			bumpIndex = 0;
		}
		bumpIndex++;
		return bulletBumpArr.get(bumpIndex);
	}
	
	public Item[] getAllgolds(){
		Item[] retArr = new Item[goldArr.size()];
		return goldArr.toArray(retArr);
	}
	
	public void deactivateAll(){
		Collection jc = jcomponentMap.values();
    Iterator jIter = jc.iterator();
    while(jIter.hasNext()){
    	((JComponent) jIter.next()).setVisible(false);    	
    }
    
    Collection nc = namedObjMap.values();
    Iterator nIter = nc.iterator();
    while(nIter.hasNext()){
    	((GameObject) nIter.next()).setVisible(false);    	
    }
    
    Iterator bIter = bulletArr.iterator();
		while(bIter.hasNext()){
			((Bullet)bIter.next()).setVisible(false);
		}
		
		Iterator aIter = bulletBumpArr.iterator();
		while(aIter.hasNext()){
			((Animation)aIter.next()).setVisible(false);
		}
		
		Iterator gIter = goldArr.iterator();
		while(gIter.hasNext()){
			((Item)gIter.next()).setVisible(false);
		}
	}
	
	//*******이거 아마 쓸모없어질 듯.
	public void setBulletsVisibleFromZero(int toIndex, boolean visibility){
		if(toIndex > maxBulletNum){
			toIndex = maxBulletNum;
		}
		for(int i = 0; i < toIndex; i++){
			bulletArr.get(i).setVisible(visibility);
		}		
	}
	
	public void setAllBulletsVisibility(boolean visibility){
		Iterator iter = bulletArr.iterator();
		while(iter.hasNext()){
			((Bullet)iter.next()).setVisible(visibility);
		}
	}
	
	public void setAllBulletBumpsVisibility(boolean visibility){
		Iterator iter = bulletBumpArr.iterator();
		while(iter.hasNext()){
			((Animation)iter.next()).setVisible(visibility);
		}
	}
	
	
	public void drawAllGameObj(Graphics2D g2d, ImageObserver observer){
		Collection c = namedObjMap.values();
    Iterator gIter = c.iterator();
    while(gIter.hasNext()){
    	((GameObject) gIter.next()).draw(g2d, observer);   	
    }
    
    Iterator bIter = bulletArr.iterator();
		while(bIter.hasNext()){
			((Bullet)bIter.next()).draw(g2d, observer);
		}
		
		Iterator aIter = bulletBumpArr.iterator();
		while(aIter.hasNext()){
			((Animation)aIter.next()).draw(g2d, observer);
		}
		
		Iterator gIter1 = goldArr.iterator();
		while(gIter1.hasNext()){
			((Item)gIter1.next()).draw(g2d, observer);
		}
	}
	
}
