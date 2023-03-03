// 
// Decompiled by Procyon v0.5.36
// 

package dodge;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.image.ImageObserver;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import java.awt.Component;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.KeyListener;
import java.awt.Color;
import java.awt.LayoutManager;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener
{
    private Timer timer;
    private final int fps = 40;
    private final int delay = 33;
    
    public GamePanel() {
        this.setLayout(null);
        this.setOpaque(true);
        this.setBackground(Color.BLACK);
        this.addKeyListener(new KeyInputListener((KeyInputListener)null));
        this.setFocusable(true);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                GamePanel.this.requestFocus();
            }
        });
        final JComponent[] jcomponentArr = ObjLib.instance().getAllJComponents();
        JComponent[] array;
        for (int length = (array = jcomponentArr).length, i = 0; i < length; ++i) {
            final JComponent item = array[i];
            this.add(item);
        }
        SceneManager.instance().change(SceneManager.SCENE.READY);
        (this.timer = new Timer(33, this)).start();
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        this.repaint();
    }
    
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        this.doEnterFrameThings(g);
        Toolkit.getDefaultToolkit().sync();
    }
    
    private void doEnterFrameThings(final Graphics g) {
        final Graphics2D g2d = (Graphics2D)g;
        ObjLib.instance().drawAllGameObj(g2d, this);
        SceneManager.instance().enterFrame();
    }
    
    private class KeyInputListener extends KeyAdapter
    {
        @Override
        public void keyPressed(final KeyEvent e) {
            SceneManager.instance().keyPressed(e);
        }
        
        @Override
        public void keyReleased(final KeyEvent e) {
            SceneManager.instance().keyReleased(e);
        }
    }
}
