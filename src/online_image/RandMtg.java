package online_image;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.*;

public class RandMtg {
	
	Window ww;
	LinkedList<BufferedImage> cards = new LinkedList<BufferedImage>();
	int times = 10;
	BufferedImage def;
	
	public class Validator implements Runnable{
		Thread t;
		String tname;
		
		public Validator(String threadName){
			tname = threadName;
		}
		
		public void run(){
			ww.revalidate();
			ww.repaint();
			System.out.println("GO");
			try {
				Thread.sleep(1);
			}catch (InterruptedException e) {}
		}
		public void start () {
		         t = new Thread (this, tname);
		         t.start();
		   }
	}
	
	public RandMtg(){
		ww = new Window();
		new Validator("hi").start();
		try{ def = ImageIO.read(getClass().getResource("Image.jpg"));} catch (IOException e) {}
		
		for(int i=0;i<times;i++){
			BufferedImage now;
			while(true){
				now = getCard((int)(Math.random()*420617+1));
				if(!same(now, def)){
					break;
				}
			}
			//now = samp(now,2);
			cards.add(now);
			System.out.println("got "+(i+1)+" out of "+times);
			ww.repaint();
		}
		while(true){
			ww.crush();
			ww.revalidate();
			try{
			Thread.sleep(1);
			}catch(Exception ex){}
		}
	}
	
	public BufferedImage getCard(int index){
		BufferedImage image = null;
		try{
			image = ImageIO.read(new URL("http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid="+index+"&type=card"));
		}catch(Exception ex){}
		return image;
	}
	
	public boolean same(BufferedImage b0, BufferedImage b1){
		boolean r=true;
		for(int y=0;y<b0.getHeight();y+=60){
			for(int x=0;x<b0.getWidth();x+=60){
				try{
					if(b0.getRGB(x, y) != b1.getRGB(x, y)){
						r=false;
					}
				}catch(Exception e){}
			}
		}
		return r;
	}
	
	public BufferedImage samp(BufferedImage b, int d){
		BufferedImage samp = new BufferedImage(b.getWidth(),b.getHeight(),BufferedImage.TYPE_INT_ARGB);
		for(int y=0;y<b.getHeight();y+=d){
			for(int x=0;x<b.getWidth();x+=d){
				samp.setRGB(x, y, b.getRGB(x,y));
			}
		}
		return samp;
	}
	
	public class Window extends JFrame{
		
		GridBagConstraints c;
		
		public void crush(){
			c.ipadx = getWidth()/c.gridwidth;
			c.ipady = getHeight()/c.gridheight;
		}
		
		public Window(){
			setSize(800,480);
			setLayout(new GridBagLayout());
			c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridheight = 1;
			c.gridwidth = 2;
			c.ipadx = getWidth()/c.gridwidth;
			c.ipady = getHeight()/c.gridheight;
			addWindowListener(new WindowListener(){
				public void windowActivated(WindowEvent arg0) {
				}
				public void windowClosed(WindowEvent e) {
				}
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
				public void windowDeactivated(WindowEvent e) {
				}
				public void windowDeiconified(WindowEvent e) {
				}
				public void windowIconified(WindowEvent e) {
				}
				public void windowOpened(WindowEvent e) {
				}	
			});
			c.anchor = GridBagConstraints.PAGE_END;
			//c.gridx = 0;
			//c.gridy = 0;
			add(new JScrollPane(new LTest()),c);
			//c.gridx = 1;
			c.anchor = GridBagConstraints.PAGE_START;
			//c.gridy = 0;
			add(new JScrollPane(new RTest()),c);
			setVisible(true);
		}
		
		public class LTest extends JPanel{
			
			Canvas canvas;
			
			public LTest(){
				setLayout(new GridLayout());
				canvas = new Canvas();
				setSize(500,500);
				add(canvas);
			}
			
			public class Canvas extends JPanel{
				public void paintComponent(Graphics g){
					Graphics2D g2 = (Graphics2D) g.create();
					g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					super.paintComponent(g);
					setBackground(Color.WHITE);
					double scale = 0.85;
					try{
						int tsize = 20;
						int margin = 5;
						g2.setColor(Color.BLACK);
						g2.setFont(new Font(Font.SANS_SERIF,1,tsize));
						for(int i=0;i<cards.size();i++){
							g2.drawString(i+1 + ": " + cards.get(i).toString(), margin, tsize+i*(tsize+margin));
						}
						
						setPreferredSize(new Dimension(cards.get(0).toString().length()*tsize+(margin*2),tsize+cards.size()*(tsize+margin)));
						
					}catch(Exception ex){}
				}
			}
		}
		
		public class RTest extends JPanel{
			
			Canvas canvas;
			
			public RTest(){
				setLayout(new GridLayout());
				canvas = new Canvas();
				add(canvas);
			}
			
			public class Canvas extends JPanel{
				public void paintComponent(Graphics g){
					Graphics2D g2 = (Graphics2D) g.create();
					g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					super.paintComponent(g);
					setBackground(Color.WHITE);
					double scale = 0.85;
					try{
						ww.crush();
						int w = (int)(cards.get(0).getWidth(this)*scale);
						int h = (int)(cards.get(0).getHeight(this)*scale);
						double fw = ((this.getParent().getWidth())/w);
						for(int i=0;i<cards.size();i++){
							g2.drawImage(cards.get(i),(int)((i%fw)*w),(int)(Math.floor(i/fw)*h),(int)w,(int)h,this);
						}

						setPreferredSize(new Dimension((int)(w*fw),(int)(h*(1+Math.floor(cards.size()/fw)))));
						
					}catch(Exception ex){}
				}
			}
		}
	}

	public static void main(String[] args){
		new RandMtg();
	}
}
