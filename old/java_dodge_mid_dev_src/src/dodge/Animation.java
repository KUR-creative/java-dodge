package dodge;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;

public class Animation extends GameObject {
	//프레임의 수. 주의할 것은 프레임이 0부터 시작한다는 것.
	private final int maxFrame; 
	//스프라이트 시트에서 하나의 행에 있는 프레임 수
	private final int rowFrameNum;
	//프레임 하나의 크기
	private final int frameW;
	private final int frameH;
	//스프라이트 시트 자체 높이. 루핑시 y값의 나머지를 구해 frameY를 얻기 위함.
	private final int sheetH;
	
	//현재 프레임 
	private int frame = 0;
	//실제 클리핑 위치
	private int frameX;
	private int frameY;
	//반복?
	private boolean isLooping;
	//애니메이션 속도 조절
	private int delay = 0;		//TODO:이거 어떻게할지... 지워질듯.
	private int frameRate = 3;

	public Animation(Image image, int x, int y, int w, int h,
			int rowFrameNum, int allFrameNum, 
			int frameW, int frameH, boolean isLooping) 
	{
		super(image, x, y, w, h);

		this.maxFrame = allFrameNum - 1;
		this.rowFrameNum = rowFrameNum;
		this.frameW = frameW;
		this.frameH = frameH;
		this.isLooping = isLooping;
		
		sheetH = (allFrameNum / rowFrameNum) * frameH;		
	}
	
	public Animation(Image image, int x, int y, 
			int rowFrameNum, int frameNum, 
			int frameW, int frameH, boolean isLooping) 
	{
		this(image, x, y, frameW, frameH, rowFrameNum, frameNum, frameW, frameH, isLooping);
	}
	
	@Override
	public void draw(Graphics2D g2d, ImageObserver observer) {
		if(isVisible()){
			enterFrame();
			g2d.drawImage(getImage(), 
					getX(), getY(),
					getX() + getW(), getY() + getH(), 
					frameX, frameY, 
					frameX + frameW, frameY + frameH, observer);	
		}
	};
	
	private void enterFrame(){
		if(isLooping == false && frame > maxFrame){
			return;
		}
		frameX = (frame % rowFrameNum) * frameW;
		frameY = ((frame/rowFrameNum) * frameH) % sheetH; 		
		frame++;
	}
	
	//maxFrame보다 클 경우 재생 안 한다.
	public void rePlay(){
		frame = 0;
	}
}
