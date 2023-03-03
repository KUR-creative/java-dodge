/*
 * GameObject와 JComponent들이 올려져서 렌더링되는 JPanel이다.
 * 사용자 입력을 받으며 일정 시간마다 모든 GameObject와 JComponent를 
 * 다시 렌더링한다.
 */
package dodge;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

import dodge.SceneManager.SCENE;

public class GamePanel extends JPanel implements ActionListener {
    //화면 갱신용
    private Timer timer;
    private final int fps = 40;
    private final int delay = 1000 / 30;
    
    public GamePanel() {
        setLayout(null);//레이아웃을 없애야 컴포넌트를 변형할 수 있다.
        setOpaque(true);
        
        setBackground(Color.BLACK); 
              
        addKeyListener(new KeyInputListener());
        setFocusable(true);//이 클래스에 포커스!
        
        //이름 입력 박스에서 포커스를 옮길 때 필요.
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocus();
            };
        });
        
                
        //어차피 한 번에 다 집어넣어야 되고, 관리는 sceneManager가 한다!
        JComponent[] jcomponentArr = ObjLib.instance().getAllJComponents();
        for(JComponent item : jcomponentArr){
            add(item);
        }
        SceneManager.instance().change(SCENE.READY);
        
        //일정 시간마다 화면을 새로 그리게 한다.
        timer = new Timer(delay,this);
        timer.start();
        
        //ResourceLibrary resLib = new ResourceLibrary();
        //여기서 Board(이 클래스 자신)에 linstener들을 붙인다.
        //addKeyListener(new TAdapter());
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();        
    }
    
    @Override    //이 함수가 바로 렌더링함수
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    doEnterFrameThings(g);

    Toolkit.getDefaultToolkit().sync();
  }
    
    private void doEnterFrameThings(Graphics g) {
        //delay(ms)마다 렌더링
        Graphics2D g2d = (Graphics2D) g;
        ObjLib.instance().drawAllGameObj(g2d, this);
        //delay마다 게임 상태 변화
        SceneManager.instance().enterFrame();
    }    
    
    private class KeyInputListener extends KeyAdapter {        
    @Override
    public void keyPressed(KeyEvent e) {
        SceneManager.instance().keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        SceneManager.instance().keyReleased(e);
    }
  }    
}
