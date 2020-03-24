/*
package test;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import dodge.GameFrame;
import dodge.GamePanel;

public class ResourceLoadTest extends JFrame {
	private final int windowW = 1800;
	private final int windowH = 1000;
	private final String title = "ResourceLoadTest"; 
	
	public ResourceLoadTest(){
		JPanel testPane = new JPanel();
		//테스트: 패널에 모든 리소스를 올려보라.
		File path = new File("img" + File.separator + "bullet.png");
		try {
			image = ImageIO.read(path);
		}
		catch (IOException e) {
			System.out.println(e.toString());
		}
		ResourceLibrary
		
		//----------------------------
		add(testPane);
		
		setSize(windowW,windowH);
		setResizable(false);
		
		setTitle(title);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	/*
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable(){
			@Override
			public void run(){
				ResourceLoadTest ms = new ResourceLoadTest();
				ms.setVisible(true);
			}
		});
	}
	
	
	
}
*/