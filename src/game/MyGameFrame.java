package game;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;

//飞机游戏的主窗口

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
	public void paint(Graphics g)//自动被调用
	{	
		Color c=g.getColor(); 
		
		g.drawImage(bg, 0, 0,null);
		plane.drawself(g);//画飞机
		
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
			Font f=new Font("宋体",Font.BOLD,50);
			g.setFont(f);
			g.drawString("时间："+period+"秒",(int)plane.x,(int)plane.y);
		}
		}
		
		g.setColor(c);
	}
	
	//帮助我们反复画窗口
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
	
	//键盘定义
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

	public void launchFrame()//初始化窗口
	{
		this.setTitle("ysy飞机作战");
		this.setVisible(true);
		this.setSize(Constitu.GAME_WIDTH, Constitu.GAME_HEIGHT);
		this.setLocation(300, 300);
		
		this.addWindowListener(new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent e) {
			
			System.exit(0);
		}
		});
		
		new PaintThread().start();//启动线程(被画窗口)
		addKeyListener(new KeyMonitor());//增加键盘监听
		
		//初始化50个炮弹
		for(int i=0;i<shells.length;i++)
		{
			shells[i]=new shell();
		}
	}
	public static void main(String[] args) {
		MyGameFrame f=new MyGameFrame();
		f.launchFrame();
	}
	
	//双缓冲
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
