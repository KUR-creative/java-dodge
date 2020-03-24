package dodge;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class GameFrame extends JFrame {
	public static final int windowW = 1000;
	public static final int windowH = 800;
	private final String title = "java dodge!"; 
	
	public GameFrame(){
		ResLib.initResLib();
		ObjLib.initObjLib();
		SceneManager.initSceneManager( ObjLib.instance() );
		add(new GamePanel());
		
		setSize(windowW,windowH);
		setResizable(false);
		
		setTitle(title);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
