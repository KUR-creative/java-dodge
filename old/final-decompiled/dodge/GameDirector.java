// 
// Decompiled by Procyon v0.5.36
// 

package dodge;

import javax.swing.JLabel;

public class GameDirector
{
    ObjLib objectLib;
    private long start;
    private int elapseSecond;
    private Bullet[] bulletArr;
    private Ship ship;
    JLabel timeBox;
    JLabel scoreBox;
    
    public GameDirector(final ObjLib objLibRef) {
        this.objectLib = objLibRef;
        this.timeBox = (JLabel)this.objectLib.getJComponent("timeBox");
        this.scoreBox = (JLabel)this.objectLib.getJComponent("scoreBox");
        this.bulletArr = this.objectLib.getAllBullets();
        this.ship = (Ship)this.objectLib.get("ship");
    }
    
    public void gameStart() {
        this.start = System.currentTimeMillis();
        ((Ship)this.objectLib.get("ship")).setHp(600);
        Bullet[] bulletArr;
        for (int length = (bulletArr = this.bulletArr).length, i = 0; i < length; ++i) {
            final Bullet bullet = bulletArr[i];
            bullet.returnInitXY();
            bullet.setNewRandomDelta();
        }
        this.bulletsDmgInit();
        this.repairInit();
        this.goldInit();
        this.ship.setScore(0);
    }
    
    public void gameEnd() {
        this.timeBox.setText(Integer.toString(this.elapseSecond));
        this.scoreBox.setText(Integer.toString(this.ship.getScore()));
        final JLabel result = (JLabel)this.objectLib.getJComponent("result");
        result.setText("result: " + this.elapseSecond * this.ship.getScore() / 100);
    }
    
    public void enterFrame() {
        this.computeElapseTime();
        this.setBulletsVisible();
        this.bulletsDmgUp();
        this.repairUp();
        this.goldUp();
    }
    
    private void computeElapseTime() {
        this.elapseSecond = (int)(System.currentTimeMillis() - this.start) / 100;
    }
    
    private void setBulletsVisible() {
        final int bulletNum = this.elapseSecond / 2 + 140;
        this.objectLib.setBulletsVisibleFromZero(bulletNum, true);
    }
    
    private void bulletsDmgUp() {
        Bullet[] bulletArr;
        for (int length = (bulletArr = this.bulletArr).length, i = 0; i < length; ++i) {
            final Bullet bullet = bulletArr[i];
            bullet.setDamage(bullet.getDamage() + this.elapseSecond / 150);
        }
    }
    
    private void bulletsDmgInit() {
        Bullet[] bulletArr;
        for (int length = (bulletArr = this.bulletArr).length, i = 0; i < length; ++i) {
            final Bullet bullet = bulletArr[i];
            bullet.setDamage(Bullet.baseDamage);
        }
    }
    
    private void repairUp() {
        final Item repair = (Item)this.objectLib.get("repair");
        repair.setValue(repair.getValue() + this.elapseSecond / 100);
        if (this.elapseSecond % 30 == 0) {
            repair.setNewRandomDelta();
        }
    }
    
    private void repairInit() {
        final Item repair = (Item)this.objectLib.get("repair");
        repair.setValue(Item.baseValue);
    }
    
    private void goldUp() {
        final Item[] goldArr = this.objectLib.getAllgolds();
        Item[] array;
        for (int length = (array = goldArr).length, i = 0; i < length; ++i) {
            final Item gold = array[i];
            gold.setValue(gold.getValue() + this.elapseSecond / 40);
        }
    }
    
    private void goldInit() {
        final Item[] goldArr = this.objectLib.getAllgolds();
        Item[] array;
        for (int length = (array = goldArr).length, i = 0; i < length; ++i) {
            final Item gold = array[i];
            gold.setValue(Item.baseValue);
        }
    }
}
