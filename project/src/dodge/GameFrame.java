/*
 * 게임의 최상위 컨테이너인 창을 구현한다.
 * 종료시에 유저 데이터를 파일로 출력한다.
 */
package dodge;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class GameFrame extends JFrame {
    public static final int windowW = 1000;
    public static final int windowH = 800;
    private final String title = "java dodge!"; 
    
    public GameFrame(){
        ScoreManager scoreManager = new ScoreManager();
        scoreManager.loadUserList();
        
        ResLib.initResLib();
        ObjLib.initObjLib();
        SceneManager.initSceneManager( ObjLib.instance(),scoreManager );
        add(new GamePanel());
        
        setSize(windowW,windowH);
        setResizable(false);
        
        setTitle(title);
        setLocationRelativeTo(null);
        
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                JFrame frame = (JFrame)e.getWindow();
                scoreManager.saveUserList();
                System.exit(EXIT_ON_CLOSE);
            }
        });
    }
    
    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable(){
            @Override
            public void run(){
                GameFrame ms = new GameFrame();
                ms.setVisible(true);
            }
        });
    }
    
}
