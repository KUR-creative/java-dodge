// 
// Decompiled by Procyon v0.5.36
// 

package dodge;

import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;

public class SceneManager
{
    private static SceneManager _instance;
    private ObjLib objectLib;
    private GameDirector director;
    private ScoreManager scoreManager;
    private SCENE nowScene;
    
    public static SceneManager instance() {
        return SceneManager._instance;
    }
    
    public static void initSceneManager(final ObjLib objLibRef, final ScoreManager scoreManagerRef) {
        SceneManager._instance = new SceneManager(objLibRef, scoreManagerRef);
    }
    
    private SceneManager(final ObjLib objLibRef, final ScoreManager scoreManagerRef) {
        this.nowScene = SCENE.READY;
        this.objectLib = objLibRef;
        this.director = new GameDirector(objLibRef);
        this.scoreManager = scoreManagerRef;
    }
    
    public void change(final SCENE scene) {
        this.nowScene = scene;
        this.enterScene();
    }
    
    private void enterScene() {
        this.objectLib.deactivateAll();
        final JLabel titleLabel = (JLabel)this.objectLib.getJComponent("title");
        final JLabel enterInfoLabel = (JLabel)this.objectLib.getJComponent("enterInfo");
        final JButton shiftKeyInfo = (JButton)this.objectLib.getJComponent("shiftInfo");
        final JButton upKeyInfo = (JButton)this.objectLib.getJComponent("upInfo");
        final JButton downKeyInfo = (JButton)this.objectLib.getJComponent("downInfo");
        final JButton leftKeyInfo = (JButton)this.objectLib.getJComponent("leftInfo");
        final JButton rightKeyInfo = (JButton)this.objectLib.getJComponent("rightInfo");
        final JButton highScoreBtn = (JButton)this.objectLib.getJComponent("highScores");
        final JButton resetBtn = (JButton)this.objectLib.getJComponent("resetBtn");
        final JLabel hpLabel = (JLabel)this.objectLib.getJComponent("hp");
        final JLabel scoreLabel = (JLabel)this.objectLib.getJComponent("scoreLabel");
        final JLabel timeLabel = (JLabel)this.objectLib.getJComponent("timeLabel");
        final JLabel scoreBox = (JLabel)this.objectLib.getJComponent("scoreBox");
        final JLabel timeBox = (JLabel)this.objectLib.getJComponent("timeBox");
        final JLabel gameOverLabel = (JLabel)this.objectLib.getJComponent("gameOver");
        final JLabel gradeLabel = (JLabel)this.objectLib.getJComponent("gradeLabel");
        final JLabel result = (JLabel)this.objectLib.getJComponent("result");
        final JLabel nameLabel = (JLabel)this.objectLib.getJComponent("nameLabel");
        final JTextField nameBox = (JTextField)this.objectLib.getJComponent("nameBox");
        final JLabel nameInfo1 = (JLabel)this.objectLib.getJComponent("nameInfo1");
        final JLabel nameInfo2 = (JLabel)this.objectLib.getJComponent("nameInfo2");
        final JLabel nameInfo3 = (JLabel)this.objectLib.getJComponent("nameInfo3");
        final JLabel thankLabel = (JLabel)this.objectLib.getJComponent("thankLabel");
        final JLabel creditLabel = (JLabel)this.objectLib.getJComponent("creditLabel");
        final JTextPane scorePane = (JTextPane)this.objectLib.getJComponent("scorePane");
        final JTextPane scoreInfo = (JTextPane)this.objectLib.getJComponent("scoreInfo");
        final GameObject hpBar = this.objectLib.get("hpBar");
        final Ship ship = (Ship)this.objectLib.get("ship");
        final Animation shipExplosion = (Animation)this.objectLib.get("shipExplosion");
        final Item repair = (Item)this.objectLib.get("repair");
        highScoreBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (SceneManager.this.nowScene == SCENE.GAME_OVER) {
                    final String resultNumStr = result.getText().substring(8);
                    final int time = Integer.parseInt(timeBox.getText());
                    final int score = Integer.parseInt(scoreBox.getText());
                    final int resultNum = Integer.parseInt(resultNumStr);
                    SceneManager.this.scoreManager.saveResult(nameBox.getText(), time, score, resultNum);
                    SceneManager.this.scoreManager.saveUserList();
                }
                SceneManager.this.change(SCENE.SCORES);
            }
        });
        switch (this.nowScene) {
            case READY: {
                titleLabel.setVisible(true);
                enterInfoLabel.setVisible(true);
                shiftKeyInfo.setVisible(true);
                upKeyInfo.setVisible(true);
                downKeyInfo.setVisible(true);
                leftKeyInfo.setVisible(true);
                rightKeyInfo.setVisible(true);
                highScoreBtn.setVisible(true);
                resetBtn.setVisible(true);
                titleLabel.setFont(new Font("Consolas", 1, 120));
                titleLabel.setForeground(Color.WHITE);
                titleLabel.setLocation(titleLabel.getX(), 200);
                titleLabel.setBounds(60, 50, 900, 200);
                enterInfoLabel.setFont(new Font("Consolas", 1, 50));
                enterInfoLabel.setForeground(Color.WHITE);
                enterInfoLabel.setBounds(165, 480, 900, 100);
                shiftKeyInfo.setFont(new Font("Arial", 1, 35));
                shiftKeyInfo.setBounds(155, 370, 150, 70);
                upKeyInfo.setFont(new Font("Arial", 1, 35));
                downKeyInfo.setFont(new Font("Arial", 1, 35));
                leftKeyInfo.setFont(new Font("Arial", 1, 35));
                rightKeyInfo.setFont(new Font("Arial", 1, 35));
                upKeyInfo.setBounds(710, 290, 70, 70);
                downKeyInfo.setBounds(710, 370, 70, 70);
                leftKeyInfo.setBounds(630, 370, 70, 70);
                rightKeyInfo.setBounds(790, 370, 70, 70);
                highScoreBtn.setFont(new Font("Consolas", 1, 20));
                highScoreBtn.setBounds(377, 600, 200, 40);
                resetBtn.setFont(new Font("Consolas", 1, 20));
                resetBtn.setBounds(377, 670, 200, 40);
                resetBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        SceneManager.this.scoreManager.clearUserList();
                    }
                });
                ship.setVisible(true);
                break;
            }
            case PLAYING: {
                this.director.gameStart();
                hpLabel.setVisible(true);
                final Item[] itemArr = this.objectLib.getAllgolds();
                Item[] array;
                for (int length = (array = itemArr).length, i = 0; i < length; ++i) {
                    final Item item = array[i];
                    item.setVisible(true);
                    item.setDy((int)(7.0 * Math.random() + 3.0));
                    item.setDx((int)(7.0 * Math.random() + 3.0));
                }
                hpBar.setVisible(true);
                ship.setVisible(true);
                ship.setXY(450, 300);
                repair.setVisible(true);
                this.objectLib.setAllBulletBumpsVisibility(true);
                hpLabel.setFont(new Font("Arial", 1, 32));
                hpLabel.setForeground(Color.GREEN);
                hpLabel.setBounds(131, 10, 100, 30);
                break;
            }
            case GAME_OVER: {
                this.director.gameEnd();
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
                gameOverLabel.setFont(new Font("Consolas", 1, 120));
                gameOverLabel.setForeground(Color.WHITE);
                gameOverLabel.setBounds(110, 90, 900, 100);
                final String resultNumStr = result.getText().substring(8);
                final int resultNum = Integer.parseInt(resultNumStr);
                final String gradeStr = this.scoreManager.getExpectedGradeStr(resultNum);
                gradeLabel.setText(gradeStr);
                gradeLabel.setFont(new Font("Modern No. 20", 1, 70));
                final int grade = this.scoreManager.getExpectedGrade(resultNum);
                if (grade == 1) {
                    gradeLabel.setForeground(Color.YELLOW);
                }
                else if (grade == 2) {
                    gradeLabel.setForeground(Color.white);
                }
                else if (grade == 3) {
                    gradeLabel.setForeground(Color.GRAY);
                }
                else {
                    gradeLabel.setForeground(Color.darkGray);
                }
                gradeLabel.setBounds(350, 230, 900, 100);
                nameLabel.setFont(new Font("Consolas", 1, 32));
                nameLabel.setForeground(Color.WHITE);
                nameLabel.setBounds(200, 370, 200, 35);
                nameBox.setFont(new Font("Serif", 1, 50));
                nameBox.setForeground(Color.BLACK);
                nameBox.setBounds(200, 405, 200, 50);
                nameBox.setFocusable(true);
                nameBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        nameBox.getParent().requestFocus();
                        if (SceneManager.this.nowScene == SCENE.GAME_OVER) {
                            final String resultNumStr = result.getText().substring(8);
                            final int time = Integer.parseInt(timeBox.getText());
                            final int score = Integer.parseInt(scoreBox.getText());
                            final int resultNum = Integer.parseInt(resultNumStr);
                            SceneManager.this.scoreManager.saveResult(nameBox.getText(), time, score, resultNum);
                            SceneManager.this.scoreManager.saveUserList();
                        }
                        SceneManager.this.change(SCENE.PLAYING);
                    }
                });
                nameInfo1.setFont(new Font("Serif", 1, 15));
                nameInfo1.setForeground(Color.WHITE);
                nameInfo1.setBounds(150, 505, 400, 35);
                nameInfo2.setFont(new Font("Serif", 1, 15));
                nameInfo2.setForeground(Color.WHITE);
                nameInfo2.setBounds(165, 525, 400, 35);
                nameInfo3.setFont(new Font("Serif", 1, 15));
                nameInfo3.setForeground(Color.WHITE);
                nameInfo3.setBounds(150, 545, 400, 35);
                highScoreBtn.setFont(new Font("Consolas", 1, 20));
                highScoreBtn.setBounds(240, 580, 160, 25);
                timeLabel.setFont(new Font("Arial", 1, 50));
                timeLabel.setForeground(Color.CYAN);
                timeLabel.setBounds(530, 367, 300, 50);
                timeBox.setFont(new Font("Courier New", 1, 50));
                timeBox.setForeground(Color.CYAN);
                timeBox.setBounds(680, 370, 300, 50);
                scoreLabel.setFont(new Font("Arial", 1, 50));
                scoreLabel.setForeground(Color.YELLOW);
                scoreLabel.setBounds(530, 455, 300, 50);
                scoreBox.setFont(new Font("Courier New", 1, 50));
                scoreBox.setForeground(Color.YELLOW);
                scoreBox.setBounds(680, 460, 3100, 50);
                result.setFont(new Font("Consolas", 1, 50));
                result.setForeground(Color.MAGENTA);
                result.setBounds(530, 565, 600, 50);
                enterInfoLabel.setFont(new Font("Consolas", 1, 45));
                enterInfoLabel.setForeground(Color.WHITE);
                enterInfoLabel.setBounds(185, 670, 900, 100);
                shipExplosion.setXY(ship.getX(), ship.getY());
                shipExplosion.rePlay();
                break;
            }
            case SCORES: {
                thankLabel.setVisible(true);
                scorePane.setVisible(true);
                scoreInfo.setVisible(true);
                creditLabel.setVisible(true);
                enterInfoLabel.setVisible(true);
                thankLabel.setFont(new Font("Consolas", 1, 70));
                thankLabel.setForeground(Color.WHITE);
                thankLabel.setBounds(90, 20, 900, 100);
                scorePane.setEditable(false);
                scorePane.setText(this.scoreManager.printUserList());
                scorePane.setFont(new Font("Serif", 1, 20));
                scorePane.setBackground(Color.BLACK);
                scorePane.setForeground(Color.WHITE);
                scorePane.setBounds(110, 100, 800, 500);
                scoreInfo.setEditable(false);
                scoreInfo.setText("\ub4f1\uc218\uac00 \uc9e4\ub824\uc11c \ub098\uc624\uc9c0 \uc54a\ub294\n\ud558\uc704\uad8c \uc5ec\ub7ec\ubd84\ub4e4\uc740 \uc2e4\ud589 \ud30c\uc77c\n\uc606\uc5d0 \uc788\ub294 Scores.txt \ud30c\uc77c\uc744 \n\ucc38\uace0\ud558\uc2dc\uae30 \ubc14\ub78d\ub2c8\ub2e4. \n\n\n\uc660\ub9cc\ud558\uba74 \uc774\ub984 \uae38\uac8c \uc4f0\uc9c0 \ub9c8\uc138\uc694\n\uc601\uc5b4 \ud55c\uae00 \uc11e\uc5ec \uc788\ub294 String \n\uae38\uc774 \uc81c\ud55c \ud558\ub294 \uac70 \uadc0\ucc2e\uc544\uc11c\n\uc548 \ub9cc\ub4e4\uc5c8\uc2b5\ub2c8\ub2e4\n \n\n1\ub144 \ubc18 \ub3d9\uc548 \uc815\ub9d0 \ub9ce\uc740 \uac83\uc744 \ubc30\uc6e0\uc2b5\ub2c8\ub2e4. \n\uadf8 \uac83\ub4e4\uc774 \ubaa8\uc5ec \uc774\ub807\uac8c \uacb0\uc2e4\uc744 \ub9fa\uc5c8\uc2b5\ub2c8\ub2e4. \n\ub2e4\uc2dc \ud55c\ubc88, \ud50c\ub808\uc774\ud574\uc8fc\uc154\uc11c \uac10\uc0ac\ud569\ub2c8\ub2e4!  \n");
                scoreInfo.setFont(new Font("Serif", 1, 18));
                scoreInfo.setBackground(Color.BLACK);
                scoreInfo.setForeground(Color.WHITE);
                scoreInfo.setBounds(580, 160, 800, 500);
                creditLabel.setFont(new Font("Consolas", 1, 60));
                creditLabel.setForeground(Color.CYAN);
                creditLabel.setBounds(163, 600, 900, 100);
                enterInfoLabel.setFont(new Font("Consolas", 1, 45));
                enterInfoLabel.setForeground(Color.WHITE);
                enterInfoLabel.setBounds(175, 670, 900, 100);
                break;
            }
        }
    }
    
    public void enterFrame() {
        switch (this.nowScene) {
            case PLAYING: {
                this.director.enterFrame();
                final GameObject hpBar = this.objectLib.get("hpBar");
                final Ship ship = (Ship)this.objectLib.get("ship");
                final Bullet[] bulletArr = this.objectLib.getAllBullets();
                Bullet[] array;
                for (int length = (array = bulletArr).length, i = 0; i < length; ++i) {
                    final Bullet bullet = array[i];
                    if (ship.hitTest(bullet)) {
                        ship.setHp(ship.getHp() - bullet.getDamage());
                        final Animation bulletBump = this.objectLib.getNextBulletBump();
                        bulletBump.setXY(bullet.getX(), bullet.getY());
                        bulletBump.rePlay();
                        bullet.returnInitXY();
                    }
                }
                final Item repair = (Item)this.objectLib.get("repair");
                if (ship.hitTest(repair)) {
                    ship.setHp(ship.getHp() + repair.getValue());
                    repair.returnInitXY();
                }
                final JLabel scoreBox = (JLabel)this.objectLib.getJComponent("scoreBox");
                final Item[] goldArr = this.objectLib.getAllgolds();
                Item[] array2;
                for (int length2 = (array2 = goldArr).length, j = 0; j < length2; ++j) {
                    final Item gold = array2[j];
                    if (ship.hitTest(gold)) {
                        ship.setScore(ship.getScore() + gold.getValue());
                        gold.returnInitXY();
                    }
                }
                hpBar.setWidthByRatio(ship.getHp() / (double)ship.getBaseHp());
                if (ship.getHp() <= 0) {
                    this.change(SCENE.GAME_OVER);
                    break;
                }
                break;
            }
        }
    }
    
    public void keyPressed(final KeyEvent e) {
        final Ship ship = (Ship)ObjLib.instance().get("ship");
        switch (this.nowScene) {
            case READY: {
                if (e.getKeyCode() == 10) {
                    this.change(SCENE.PLAYING);
                }
                ship.keyPressed(e);
                break;
            }
            case PLAYING: {
                ship.keyPressed(e);
                break;
            }
            case GAME_OVER: {
                final JTextField nameBox = (JTextField)this.objectLib.getJComponent("nameBox");
                final JLabel timeBox = (JLabel)this.objectLib.getJComponent("timeBox");
                final JLabel scoreBox = (JLabel)this.objectLib.getJComponent("scoreBox");
                final JLabel resultBox = (JLabel)this.objectLib.getJComponent("result");
                if (e.getKeyCode() == 10) {
                    final String resultNum = resultBox.getText().substring(8);
                    final int time = Integer.parseInt(timeBox.getText());
                    final int score = Integer.parseInt(scoreBox.getText());
                    final int result = Integer.parseInt(resultNum);
                    this.scoreManager.saveResult(nameBox.getText(), time, score, result);
                    this.scoreManager.saveUserList();
                    this.change(SCENE.PLAYING);
                    break;
                }
                break;
            }
            case SCORES: {
                if (e.getKeyCode() == 10) {
                    this.change(SCENE.PLAYING);
                    break;
                }
                break;
            }
        }
    }
    
    public void keyReleased(final KeyEvent e) {
        final Ship ship = (Ship)ObjLib.instance().get("ship");
        switch (this.nowScene) {
            case READY: {
                ship.keyReleased(e);
                break;
            }
            case PLAYING: {
                ship.keyReleased(e);
            }
        }
    }
    
    public SCENE getNowScene() {
        return this.nowScene;
    }
    
    public enum SCENE
    {
        READY("READY", 0), 
        PLAYING("PLAYING", 1), 
        GAME_OVER("GAME_OVER", 2), 
        SCORES("SCORES", 3);
        
        private SCENE(final String name, final int ordinal) {
        }
    }
}
