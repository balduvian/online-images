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
	LinkedList<Color> colors = new LinkedList<Color>();
	int times = 200;
	BufferedImage def;
	BufferedImage resample;
	
	public BufferedImage crop(BufferedImage b, int x1, int y1, int x2, int y2){
		BufferedImage nn = new BufferedImage(x2-x1,y2-y1,BufferedImage.TYPE_INT_ARGB);
		for(int y=y1;y<y2;y++){
			for(int x=x1;x<x2;x++){
				nn.setRGB(x-x1, y-y1, b.getRGB(x,y));
			}
		}
		return nn;
	}
	
	public Color getAvg(BufferedImage b){
		LinkedList<Color> cs = new LinkedList<Color>();
		for(int y=0;y<b.getHeight();y+=3){
			for(int x=0;x<b.getWidth();x+=3){
				cs.add(new Color(b.getRGB(x, y)));
			}
		}
		int[] t = new int[3];
		for(int i=0;i<cs.size();i++){
			t[0] += cs.get(i).getRed();
			t[1] += cs.get(i).getGreen();
			t[2] += cs.get(i).getBlue();
		}
		return new Color(t[0]/cs.size(),t[1]/cs.size(),t[2]/cs.size());
	}
	
	public void resample(BufferedImage s, LinkedList<BufferedImage> pool, LinkedList<Color> cpool){
		int across = s.getWidth();
	}
	
	public RandMtg(){
		ww = new Window();
		try{ def = ImageIO.read(getClass().getResource("Image.jpg"));} catch (IOException e) {}
		try{ resample = ImageIO.read(getClass().getResource("resample.jpg"));} catch (IOException e) {}
		//
		def = crop(def,18,36,207,173);
		//
		for(int i=0;i<times;i++){
			BufferedImage now;
			while(true){
				now = getCard((int)(Math.random()*420617+1));
				if(!same(now, def)){
					getAvg(now);
					break;
				}
			}
			//now = samp(now,2);
			cards.add(now);
			colors.add(getAvg(now));
			System.out.println("got "+(i+1)+" out of "+times);
			ww.repaint();
		}
		while(true){
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
		try{
			image = crop(image,18,36,207,173);
		}catch(Exception ex){
			ex.printStackTrace();
		}
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
		
		public Window(){
			setSize(800,480);
			setLayout(new GridLayout());
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
			//add(new JScrollPane(new LTest()));
			add(new JScrollPane(new RTest()));
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
					double scale = 2;
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
					double scale = 1;
					try{
						int w = (int)(cards.get(0).getWidth(this)*scale);
						int h = (int)(cards.get(0).getHeight(this)*scale);
						double fw = ((this.getParent().getWidth())/w);
						for(int i=0;i<cards.size();i++){
							g2.drawImage(cards.get(i),(int)((i%fw)*w),(int)(Math.floor(i/fw)*h),(int)w,(int)h,this);
							g2.setColor(colors.get(i));
							g2.fillRect((int)((i%fw)*w),(int)(Math.floor(i/fw)*h),(int)w-50,(int)h-50);
						}
						revalidate();
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
