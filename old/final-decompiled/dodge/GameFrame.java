// 
// Decompiled by Procyon v0.5.36
// 

package dodge;

import java.awt.EventQueue;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.Component;
import javax.swing.JFrame;

public class GameFrame extends JFrame
{
    public static final int windowW = 1000;
    public static final int windowH = 800;
    private final String title = "java dodge!";
    
    public GameFrame() {
        final ScoreManager scoreManager = new ScoreManager();
        scoreManager.loadUserList();
        ResLib.initResLib();
        ObjLib.initObjLib();
        SceneManager.initSceneManager(ObjLib.instance(), scoreManager);
        this.add(new GamePanel());
        this.setSize(1000, 800);
        this.setResizable(false);
        this.setTitle("java dodge!");
        this.setLocationRelativeTo(null);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                final JFrame frame = (JFrame)e.getWindow();
                scoreManager.saveUserList();
                System.exit(3);
            }
        });
    }
    
    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                final GameFrame ms = new GameFrame();
                ms.setVisible(true);
            }
        });
    }
}
