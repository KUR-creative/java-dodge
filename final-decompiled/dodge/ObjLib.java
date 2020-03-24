// 
// Decompiled by Procyon v0.5.36
// 

package dodge;

import java.awt.image.ImageObserver;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.Collection;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.util.ArrayList;
import javax.swing.JComponent;
import java.util.TreeMap;

public class ObjLib
{
    private static ObjLib _instance;
    public static final int initOut = -100;
    private TreeMap<String, JComponent> jcomponentMap;
    private TreeMap<String, GameObject> namedObjMap;
    public static final int maxBulletNum = 300;
    private int bulletNum;
    private ArrayList<Bullet> bulletArr;
    private final int maxBumpNum = 20;
    private ArrayList<Animation> bulletBumpArr;
    private int bumpIndex;
    private final int maxGoldNum = 20;
    private ArrayList<Item> goldArr;
    private int goldIndex;
    
    public static ObjLib instance() {
        return ObjLib._instance;
    }
    
    public static void initObjLib() {
        ObjLib._instance = new ObjLib();
    }
    
    private ObjLib() {
        this.jcomponentMap = new TreeMap<String, JComponent>();
        this.namedObjMap = new TreeMap<String, GameObject>();
        this.bulletNum = 300;
        this.bulletArr = new ArrayList<Bullet>(this.bulletNum);
        this.bulletBumpArr = new ArrayList<Animation>(20);
        this.bumpIndex = 0;
        this.goldArr = new ArrayList<Item>();
        this.goldIndex = 0;
        final JLabel titleLabel = new JLabel(" java dodge!!");
        final JLabel enterInfoLabel = new JLabel("press   [ENTER]  to start");
        final JButton shiftKeyInfo = new JButton("Shift");
        final JButton upKeyInfo = new JButton("\u2191");
        final JButton downKeyInfo = new JButton("\u2193");
        final JButton leftKeyInfo = new JButton("\u2190");
        final JButton rightKeyInfo = new JButton("\u2192");
        final JButton highScoreBtn = new JButton("high scores");
        final JButton resetBtn = new JButton("reset records");
        this.jcomponentMap.put("title", titleLabel);
        this.jcomponentMap.put("enterInfo", enterInfoLabel);
        this.jcomponentMap.put("shiftInfo", shiftKeyInfo);
        this.jcomponentMap.put("upInfo", upKeyInfo);
        this.jcomponentMap.put("downInfo", downKeyInfo);
        this.jcomponentMap.put("leftInfo", leftKeyInfo);
        this.jcomponentMap.put("rightInfo", rightKeyInfo);
        this.jcomponentMap.put("highScores", highScoreBtn);
        this.jcomponentMap.put("resetBtn", resetBtn);
        final JLabel hpLabel = new JLabel("Hull");
        final JLabel scoreLabel = new JLabel("score");
        final JLabel timeLabel = new JLabel("time  ");
        final JLabel scoreBox = new JLabel("not yet");
        final JLabel timeBox = new JLabel("56789");
        this.jcomponentMap.put("hp", hpLabel);
        this.jcomponentMap.put("scoreLabel", scoreLabel);
        this.jcomponentMap.put("timeLabel", timeLabel);
        this.jcomponentMap.put("scoreBox", scoreBox);
        this.jcomponentMap.put("timeBox", timeBox);
        final JLabel gameOverLabel = new JLabel(" Game\n Over!!");
        final JLabel gradeLabel = new JLabel("1st Place");
        final JLabel result = new JLabel("aaaa");
        this.jcomponentMap.put("gameOver", gameOverLabel);
        this.jcomponentMap.put("result", result);
        this.jcomponentMap.put("gradeLabel", gradeLabel);
        final JLabel nameLabel = new JLabel("user name: ");
        final JTextField nameBox = new JTextField("noname");
        final JLabel nameInfo1 = new JLabel("\uc774\ub984\uc744 \uc785\ub825\ud558\uace0 [ENTER]\ud0a4\ub97c \ub204\ub974\uba74 ");
        final JLabel nameInfo2 = new JLabel("\uc785\ub825\ud55c \uc774\ub984\uc73c\ub85c \uc810\uc218\uac00 \uc800\uc7a5\ub429\ub2c8\ub2e4.");
        final JLabel nameInfo3 = new JLabel("(\uc774\ub984\uc740 6\uc790 \uc774\ud558\uc758 \uc601\uc5b4/\uc22b\uc790\ub9cc \uc4f0\uc138\uc694)");
        this.jcomponentMap.put("nameLabel", nameLabel);
        this.jcomponentMap.put("nameBox", nameBox);
        this.jcomponentMap.put("nameInfo1", nameInfo1);
        this.jcomponentMap.put("nameInfo2", nameInfo2);
        this.jcomponentMap.put("nameInfo3", nameInfo3);
        final JLabel thankLabel = new JLabel("Thank You For Playing!");
        final JLabel creditLabel = new JLabel("made by KUR CREATIVE");
        final JTextPane scorePane = new JTextPane();
        final JTextPane scoreInfo = new JTextPane();
        this.jcomponentMap.put("thankLabel", thankLabel);
        this.jcomponentMap.put("scorePane", scorePane);
        this.jcomponentMap.put("creditLabel", creditLabel);
        this.jcomponentMap.put("scoreInfo", scoreInfo);
        this.setAllJcomponentsUnfocusable();
        final GameObject hpBar = new GameObject(ResLib.instance().getHpBarRes(), 10, 10, 300, 30);
        final Ship ship = new Ship(ResLib.instance().getShipRes(), 450, 300, 50, 65, 5, 5, 51, 86, true);
        final Animation shipExplosion = new Animation(ResLib.instance().getExplosionRes(), -100, -100, 80, 80, 4, 16, 64, 64, false);
        final Item repair = new Item(ResLib.instance().getRepairRes(), -100, -100, 4, 16, 69, 69, true);
        this.namedObjMap.put("hpBar", hpBar);
        this.namedObjMap.put("ship", ship);
        this.namedObjMap.put("shipExplosion", shipExplosion);
        this.namedObjMap.put("repair", repair);
        for (int i = 0; i < 300; ++i) {
            this.bulletArr.add(new Bullet(i, ResLib.instance().getBulletRes()));
        }
        for (int i = 0; i < 20; ++i) {
            this.bulletBumpArr.add(new Animation(ResLib.instance().getBulletBumpRes(), -100, -100, 4, 16, 31, 28, false));
        }
        for (int i = 0; i < 20; ++i) {
            this.goldArr.add(new Item(ResLib.instance().getGoldRes(), -100, -100, 4, 16, 36, 36, false));
        }
    }
    
    public JComponent getJComponent(final String key) {
        return this.jcomponentMap.get(key);
    }
    
    public GameObject get(final String key) {
        return this.namedObjMap.get(key);
    }
    
    public JComponent[] getAllJComponents() {
        final JComponent[] retArr = new JComponent[this.jcomponentMap.size()];
        final Collection c = this.jcomponentMap.values();
        final Iterator itr = c.iterator();
        int i = 0;
        while (itr.hasNext()) {
            retArr[i] = itr.next();
            ++i;
        }
        return retArr;
    }
    
    private void setAllJcomponentsUnfocusable() {
        final Collection c = this.jcomponentMap.values();
        final Iterator itr = c.iterator();
        while (itr.hasNext()) {
            itr.next().setFocusable(false);
        }
    }
    
    public Bullet[] getAllBullets() {
        final Bullet[] retArr = new Bullet[this.bulletArr.size()];
        return this.bulletArr.toArray(retArr);
    }
    
    public Bullet getBulletByID(final int id) {
        return this.bulletArr.get(id);
    }
    
    public Animation getNextBulletBump() {
        if (this.bumpIndex == 19) {
            this.bumpIndex = 0;
        }
        ++this.bumpIndex;
        return this.bulletBumpArr.get(this.bumpIndex);
    }
    
    public Item[] getAllgolds() {
        final Item[] retArr = new Item[this.goldArr.size()];
        return this.goldArr.toArray(retArr);
    }
    
    public void deactivateAll() {
        final Collection jc = this.jcomponentMap.values();
        final Iterator jIter = jc.iterator();
        while (jIter.hasNext()) {
            jIter.next().setVisible(false);
        }
        final Collection nc = this.namedObjMap.values();
        final Iterator nIter = nc.iterator();
        while (nIter.hasNext()) {
            nIter.next().setVisible(false);
        }
        final Iterator bIter = this.bulletArr.iterator();
        while (bIter.hasNext()) {
            bIter.next().setVisible(false);
        }
        final Iterator aIter = this.bulletBumpArr.iterator();
        while (aIter.hasNext()) {
            aIter.next().setVisible(false);
        }
        final Iterator gIter = this.goldArr.iterator();
        while (gIter.hasNext()) {
            gIter.next().setVisible(false);
        }
    }
    
    public void setBulletsVisibleFromZero(int toIndex, final boolean visibility) {
        if (toIndex > 300) {
            toIndex = 300;
        }
        for (int i = 0; i < toIndex; ++i) {
            this.bulletArr.get(i).setVisible(visibility);
        }
    }
    
    public void setAllBulletsVisibility(final boolean visibility) {
        final Iterator iter = this.bulletArr.iterator();
        while (iter.hasNext()) {
            iter.next().setVisible(visibility);
        }
    }
    
    public void setAllBulletBumpsVisibility(final boolean visibility) {
        final Iterator iter = this.bulletBumpArr.iterator();
        while (iter.hasNext()) {
            iter.next().setVisible(visibility);
        }
    }
    
    public void drawAllGameObj(final Graphics2D g2d, final ImageObserver observer) {
        final Collection c = this.namedObjMap.values();
        final Iterator gIter = c.iterator();
        while (gIter.hasNext()) {
            gIter.next().draw(g2d, observer);
        }
        final Iterator aIter = this.bulletBumpArr.iterator();
        while (aIter.hasNext()) {
            aIter.next().draw(g2d, observer);
        }
        final Iterator gIter2 = this.goldArr.iterator();
        while (gIter2.hasNext()) {
            gIter2.next().draw(g2d, observer);
        }
        final Iterator bIter = this.bulletArr.iterator();
        while (bIter.hasNext()) {
            bIter.next().draw(g2d, observer);
        }
    }
}
