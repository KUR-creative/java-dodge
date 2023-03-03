/*
 * 게임의 장면(scene)을 구현하고 관리한다.
 *  
 * 개선점:
 * 일단은 돌아가게 하지만, TODO: scene Node 구조로 refactoring이 필요하다.
 * -> 장면을 기술하는 클래스 Scene를 만들고 이걸 상속해서 ReadyScene, PlayingScene...
 * 등을 만든다. 그리고 SceneManager 클래스는 이것들을 적절한 자료구조에 보관한다.
 * 그리고 보관된 Scene들을 적절히 움직여주면 되겠지만... 나중에 한다.
 * 
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
import javax.swing.JTextField;
import javax.swing.JTextPane;

import dodge.SceneManager.SCENE;

public class SceneManager {
    private static SceneManager _instance;
    private ObjLib objectLib;
    private GameDirector director;
    private ScoreManager scoreManager;
    
    public static enum SCENE{
        READY, PLAYING, GAME_OVER, SCORES;
    }
    private SCENE nowScene = SCENE.READY;
    
    //싱글톤
    public static SceneManager instance() {
        return _instance;
    }
    public static void initSceneManager(ObjLib objLibRef, ScoreManager scoreManagerRef){
        _instance = new SceneManager(objLibRef, scoreManagerRef);
    }
    private SceneManager(ObjLib objLibRef, ScoreManager scoreManagerRef){
        this.objectLib = objLibRef;
        director = new GameDirector(objLibRef);
        scoreManager = scoreManagerRef;
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
        JLabel titleLabel       = (JLabel)objectLib.getJComponent("title");
        JLabel enterInfoLabel   = (JLabel)objectLib.getJComponent("enterInfo");
        JButton shiftKeyInfo    = (JButton)objectLib.getJComponent("shiftInfo");
        JButton upKeyInfo       = (JButton)objectLib.getJComponent("upInfo");   
        JButton downKeyInfo     = (JButton)objectLib.getJComponent("downInfo"); 
        JButton leftKeyInfo     = (JButton)objectLib.getJComponent("leftInfo"); 
        JButton rightKeyInfo    = (JButton)objectLib.getJComponent("rightInfo");
        JButton highScoreBtn    = (JButton)objectLib.getJComponent("highScores");
        JButton resetBtn        = (JButton)objectLib.getJComponent("resetBtn");
        
        //---------------------PLAYING------------------------
        JLabel hpLabel      = (JLabel)objectLib.getJComponent("hp");
        JLabel scoreLabel   = (JLabel)objectLib.getJComponent("scoreLabel");
        JLabel timeLabel    = (JLabel)objectLib.getJComponent("timeLabel");
        JLabel scoreBox = (JLabel)objectLib.getJComponent("scoreBox");
        JLabel timeBox  = (JLabel)objectLib.getJComponent("timeBox");
        
        //---------------------GAME_OVER-----------------------
        JLabel gameOverLabel = (JLabel)objectLib.getJComponent("gameOver");
        JLabel gradeLabel = (JLabel)objectLib.getJComponent("gradeLabel");
        JLabel result = (JLabel)objectLib.getJComponent("result");
        
        JLabel nameLabel = (JLabel)objectLib.getJComponent("nameLabel");
        JTextField nameBox = (JTextField)objectLib.getJComponent("nameBox");
        JLabel nameInfo1 = (JLabel)objectLib.getJComponent("nameInfo1");
        JLabel nameInfo2 = (JLabel)objectLib.getJComponent("nameInfo2");
        JLabel nameInfo3 = (JLabel)objectLib.getJComponent("nameInfo3");
        
        //---------------------SCORES------------------------
        JLabel thankLabel = (JLabel)objectLib.getJComponent("thankLabel");
        JLabel creditLabel = (JLabel)objectLib.getJComponent("creditLabel");
        JTextPane scorePane = (JTextPane)objectLib.getJComponent("scorePane");
        JTextPane scoreInfo = (JTextPane)objectLib.getJComponent("scoreInfo");
        
        GameObject hpBar = objectLib.get("hpBar");
        Ship ship = (Ship)objectLib.get("ship");
        Animation shipExplosion = (Animation)objectLib.get("shipExplosion");
        Item repair = (Item)objectLib.get("repair");
        
        
        //하이스코어 버튼: 리스너 추가
        //scene에 구애받지 않는 버튼이라서 이렇게 한다..
        highScoreBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(nowScene == SCENE.GAME_OVER){
                    String resultNumStr = result.getText().substring(8);
                  int time      = Integer.parseInt(timeBox.getText());
                  int score     = Integer.parseInt(scoreBox.getText());
                  int resultNum = Integer.parseInt(resultNumStr); 
                scoreManager.saveResult(nameBox.getText(), time, score, resultNum);
                scoreManager.saveUserList();
                }
                change(SCENE.SCORES);
            }
        });
        
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
            resetBtn.setVisible(true);
            
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
            
            highScoreBtn.setFont(new Font("Consolas",Font.BOLD,20));
            highScoreBtn.setBounds(377,600,200,40);
            
            resetBtn.setFont(new Font("Consolas",Font.BOLD,20));
            resetBtn.setBounds(377,670,200,40);
            
            
            
            //resetBtn: 리스너 추가...
            resetBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    scoreManager.clearUserList();
                }
            });
            
            //시연용 함선
            ship.setVisible(true);
            
            break;
            
        case PLAYING:
            //게임 밸런서 시작. bullet의 출현빈도를 조절한다.
            director.gameStart();
            //활성화
            hpLabel.setVisible(true);
            //TODO: 렌더링과 게임 상태 변경을 분리하는 리팩토링 후에 주석을 풀어라.
            //scoreLabel.setVisible(true);
            //scoreBox.setVisible(true);
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
            ship.setXY(450, 300);
            repair.setVisible(true);
            
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
            
            TODO: 렌더링과 게임 상태 변경을 분리하는 리팩토링 후에 주석을 풀어라.
            //score
            scoreLabel.setFont(new Font("Arial",Font.BOLD,15));
            scoreLabel.setForeground(Color.YELLOW);
            scoreLabel.setBounds(GameFrame.windowW - 130,10,100,30);
            //score box
            scoreBox.setFont(new Font("Courier New",Font.BOLD,15));
            scoreBox.setForeground(Color.YELLOW);
            scoreBox.setBounds(GameFrame.windowW - 80,10,100,30);
            */
                        
            break;
            
        case GAME_OVER:
            //게임 끝
            director.gameEnd();
            //활성화
            gameOverLabel.setVisible(true);
            gradeLabel.setVisible(true);
            
            nameLabel.setVisible(true);
            nameBox.setVisible(true);
            nameInfo1.setVisible(true);
            nameInfo2.setVisible(true);
            nameInfo3.setVisible(true);
            highScoreBtn.setVisible(true);
            
            timeLabel.setVisible(true);
            timeBox.setVisible(true);
            scoreLabel.setVisible(true);
            scoreBox.setVisible(true);
            result.setVisible(true);
                                    
            enterInfoLabel.setVisible(true);
            
            shipExplosion.setVisible(true);
            
            //game over!
            gameOverLabel.setFont(new Font("Consolas",Font.BOLD,120));
            gameOverLabel.setForeground(Color.WHITE);
            gameOverLabel.setBounds(110,90,900,100);
            //gradeLabel
            String resultNumStr = result.getText().substring(8);
            int resultNum   = Integer.parseInt(resultNumStr);
            String gradeStr = scoreManager.getExpectedGradeStr(resultNum);
            gradeLabel.setText(gradeStr);
            gradeLabel.setFont(new Font("Modern No. 20",Font.BOLD,70));
            int grade = scoreManager.getExpectedGrade(resultNum);
            if(grade == 1){
                gradeLabel.setForeground(Color.YELLOW); //금장
            }else if(grade == 2){
                gradeLabel.setForeground(Color.white); //은장
            }else if(grade == 3){
                gradeLabel.setForeground(Color.GRAY); //동장
            }else{
                gradeLabel.setForeground(Color.darkGray); //똥장
            }
            
            gradeLabel.setBounds(350,230,900,100);
            
            //nameLabel
            nameLabel.setFont(new Font("Consolas",Font.BOLD,32));
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setBounds(200,370, 200,35);
            //nameBox
            nameBox.setFont(new Font("Serif",Font.BOLD,50));
            nameBox.setForeground(Color.BLACK);
            nameBox.setBounds(200,405, 200,50);
            nameBox.setFocusable(true);
            nameBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    nameBox.getParent().requestFocus();
                    if(nowScene == SCENE.GAME_OVER){
                        String resultNumStr = result.getText().substring(8);
                      int time      = Integer.parseInt(timeBox.getText());
                      int score     = Integer.parseInt(scoreBox.getText());
                      int resultNum = Integer.parseInt(resultNumStr); 
                    scoreManager.saveResult(nameBox.getText(), time, score, resultNum);
                    scoreManager.saveUserList();
                    }
                    change(SCENE.PLAYING);
                }
            });
            //nameInfo
            nameInfo1.setFont(new Font("Serif",Font.BOLD,15));
            nameInfo1.setForeground(Color.WHITE);
            nameInfo1.setBounds(150,505, 400,35);
            nameInfo2.setFont(new Font("Serif",Font.BOLD,15));
            nameInfo2.setForeground(Color.WHITE);
            nameInfo2.setBounds(165,525, 400,35);
            nameInfo3.setFont(new Font("Serif",Font.BOLD,15));
            nameInfo3.setForeground(Color.WHITE);
            nameInfo3.setBounds(150,545, 400,35);
            //highScoreBtn
            highScoreBtn.setFont(new Font("Consolas",Font.BOLD,20));
            highScoreBtn.setBounds(240,580,160,25);
            
            //time
            timeLabel.setFont(new Font("Arial",Font.BOLD,50));
            timeLabel.setForeground(Color.CYAN);
            timeLabel.setBounds(530,367, 300,50);
            //time box
            timeBox.setFont(new Font("Courier New",Font.BOLD,50));
            timeBox.setForeground(Color.CYAN);
            timeBox.setBounds(680,370, 300,50);
            
            //score
            scoreLabel.setFont(new Font("Arial",Font.BOLD,50));
            scoreLabel.setForeground(Color.YELLOW);
            scoreLabel.setBounds(530,455, 300,50);
            //score box
            scoreBox.setFont(new Font("Courier New",Font.BOLD,50));
            scoreBox.setForeground(Color.YELLOW);
            scoreBox.setBounds(680,460, 3100,50);
            
            //result
            result.setFont(new Font("Consolas",Font.BOLD,50));
            result.setForeground(Color.MAGENTA);
            result.setBounds(530,565, 600,50);
            
            //press [ENTER] to start
            enterInfoLabel.setFont(new Font("Consolas",Font.BOLD,45));
            enterInfoLabel.setForeground(Color.WHITE);
            enterInfoLabel.setBounds(185,670,900,100);
            
            shipExplosion.setXY( ship.getX(), ship.getY() );
            shipExplosion.rePlay();
            break;
            
        case SCORES:
            thankLabel.setVisible(true);
            scorePane.setVisible(true);
            scoreInfo.setVisible(true);
            creditLabel.setVisible(true);
            
            enterInfoLabel.setVisible(true);
            
            //thankLabel
            thankLabel.setFont(new Font("Consolas",Font.BOLD,70));
            thankLabel.setForeground(Color.WHITE);
            thankLabel.setBounds(90,20,900,100);
            
            //scorePane
            scorePane.setEditable(false);
            scorePane.setText(scoreManager.printUserList());
            scorePane.setFont(new Font("Serif",Font.BOLD,20));
            scorePane.setBackground(Color.BLACK);
            scorePane.setForeground(Color.WHITE);
            scorePane.setBounds(110, 100, GameFrame.windowW - 200, GameFrame.windowH - 300);
            
            //scoreInfo
            scoreInfo.setEditable(false);
            scoreInfo.setText("등수가 짤려서 나오지 않는\n"
                                                + "하위권 여러분들은 실행 파일\n"
                                                + "옆에 있는 Scores.txt 파일을 \n"
                                                + "참고하시기 바랍니다. \n"
                                                + "\n"
                                                + "\n"
                                                + "왠만하면 이름 길게 쓰지 마세요\n"
                                                + "영어 한글 섞여 있는 String \n"
                                                + "길이 제한 하는 거 귀찮아서\n"
                                                + "안 만들었습니다\n "
                                                + "\n"
                                                + "\n"
                                                + "1년 반 동안 정말 많은 것을 배웠습니다. \n"
                                                + "그 것들이 모여 이렇게 결실을 맺었습니다. \n"
                                                + "다시 한번, 플레이해주셔서 감사합니다!  \n");
            scoreInfo.setFont(new Font("Serif",Font.BOLD,18));
            scoreInfo.setBackground(Color.BLACK);
            scoreInfo.setForeground(Color.WHITE);
            scoreInfo.setBounds(580, 160, GameFrame.windowW - 200, GameFrame.windowH - 300);
            
            //creditLabel
            creditLabel.setFont(new Font("Consolas",Font.BOLD,60));
            creditLabel.setForeground(Color.CYAN);
            creditLabel.setBounds(163,600,900,100);
            
            //press [ENTER] to start
            enterInfoLabel.setFont(new Font("Consolas",Font.BOLD,45));
            enterInfoLabel.setForeground(Color.WHITE);
            enterInfoLabel.setBounds(175,670,900,100);
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
          JLabel scoreBox = (JLabel)objectLib.getJComponent("scoreBox");
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
          JTextField nameBox= (JTextField)objectLib.getJComponent("nameBox");
          JLabel timeBox    = (JLabel)objectLib.getJComponent("timeBox");
          JLabel scoreBox   = (JLabel)objectLib.getJComponent("scoreBox");
          JLabel resultBox  = (JLabel)objectLib.getJComponent("result");
          
          // press ENTER to start
          if(e.getKeyCode() == KeyEvent.VK_ENTER){ 
              String resultNum = resultBox.getText().substring(8);
              int time      = Integer.parseInt(timeBox.getText());
              int score     = Integer.parseInt(scoreBox.getText());
              int result    = Integer.parseInt(resultNum); //8개를 끊어내라..
            scoreManager.saveResult(nameBox.getText(), time, score, result);
            scoreManager.saveUserList();
            
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
