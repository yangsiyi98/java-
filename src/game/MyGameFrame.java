package game;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;

//�ɻ���Ϸ��������

public class MyGameFrame extends Frame{
	
	Image planeim=GameUtil.getImage("images/plane.png");
	Image bg=GameUtil.getImage("images/bg.jpg");
	
	Plane plane=new Plane(planeim,250,250);
	shell[] shells=new shell[50]; 
	
	Explode bao;
	
	
	Date startTime=new Date();
	Date endTime=new Date();
	int period;
	
	
	@Override
	public void paint(Graphics g)//�Զ�������
	{	
		Color c=g.getColor(); 
		
		g.drawImage(bg, 0, 0,null);
		plane.drawself(g);//���ɻ�
		
		for(int i=0;i<shells.length;i++){
		shells[i].draw(g);
		
		boolean peng=shells[i].getRect().intersects(plane.getRect());
		if(peng)
		{
			plane.live=false;
			
			if(bao==null){
			bao =new Explode(plane.x, plane.y);
			endTime=new Date();
			period=(int)((endTime.getTime()-startTime.getTime())/1000);
			}
			bao.draw(g);
			
		}
		if(plane.live==false){
			g.setColor(Color.WHITE);
			Font f=new Font("����",Font.BOLD,50);
			g.setFont(f);
			g.drawString("ʱ�䣺"+period+"��",(int)plane.x,(int)plane.y);
		}
		}
		
		g.setColor(c);
	}
	
	//�������Ƿ���������
	class PaintThread extends Thread{
		 @Override
		public void run() {
			while(true)
			{
				repaint();
				
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	//���̶���
	class KeyMonitor extends KeyAdapter
	{

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			plane.addDirection(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			plane.minusDirection(e);
		}
		
	}

	public void launchFrame()//��ʼ������
	{
		this.setTitle("ysy�ɻ���ս");
		this.setVisible(true);
		this.setSize(Constitu.GAME_WIDTH, Constitu.GAME_HEIGHT);
		this.setLocation(300, 300);
		
		this.addWindowListener(new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent e) {
			
			System.exit(0);
		}
		});
		
		new PaintThread().start();//�����߳�(��������)
		addKeyListener(new KeyMonitor());//���Ӽ��̼���
		
		//��ʼ��50���ڵ�
		for(int i=0;i<shells.length;i++)
		{
			shells[i]=new shell();
		}
	}
	public static void main(String[] args) {
		MyGameFrame f=new MyGameFrame();
		f.launchFrame();
	}
	
	//˫����
	private Image offScreenImage=null;
	public void updata(Graphics g)
	{
		if(offScreenImage==null)
			offScreenImage=this.createImage(Constitu.GAME_WIDTH,Constitu.GAME_HEIGHT);
			
		Graphics gOff=offScreenImage.getGraphics();
		paint(gOff);
		g.drawImage(offScreenImage, 0, 0, null);
	}
}
