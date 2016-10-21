package online_image;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.*;

public class GridPic {
	
	GFrame gg;
	BufferedImage[][] pics = new BufferedImage[1][1];
	boolean runn = true;
	boolean pulse = true;
	BufferedImage def;
	
	public GridPic(){
		try{ def = ImageIO.read(getClass().getResource("removed.png"));} catch (IOException e) {}
		gg = new GFrame();
		ExecutorService exe = Executors.newFixedThreadPool(pics.length*pics[0].length);
		for(int y=0;y<pics.length;y++){
			for(int x=0;x<pics[0].length;x++){
				CD t = new CD(y,x);
				exe.execute(t);
			}
		}
	}
	
	public class D implements Runnable{
		int y;
		int x;
		public D(int yy, int xx){
			y=yy;
			x=xx;
		}
		public void run() {
			while(true){
				if(runn){
					reset(y,x);
				}
				gg.canvas[y][x].repaint();
			}
		}
	}
	
	public class CD implements Runnable{
		int y;
		int x;
		public CD(int yy, int xx){
			y=yy;
			x=xx;
		}
		public void run() {
			while(true){
				if(pulse){
					pulse = false;
					creset(y,x);
				}
				gg.canvas[y][x].repaint();
				//System.out.println(pulse);
			}
		}
		public void creset(int y, int x){
			pics[y][x] = null;
			while(true){
				String f4 = genHex(rando(4,7),true);
				try{
					//pics[y][x] = ImageIO.read(new URL("http://gallery.photo.net/photo/"+f3+"-md.jpg"));
					gg.canvas[y][x].repaint();
					pics[y][x] = ImageIO.read(new URL("http://i.imgur.com/"+f4+".jpg"));
					//System.out.println(f3);
				}catch(Exception ex){
					//System.out.println("Failed at: "+f3);
				}
				if(pics[y][x] != null){
					if(!same(pics[y][x],def)){
						break;
					}else{
						pics[y][x] = null;
					}
				}
			}
		}
	}
	
	public String genHex(int l, boolean c){
		String hchars = "0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
		String chars = "0123456789";
		String m = "";
		for(int i=0;i<l;i++){
			if(c){
				m += hchars.charAt((int)(Math.random()*hchars.length()));
			}else{
				m += chars.charAt((int)(Math.random()*chars.length()));
			}
		}
		return m;
	}
	
	public int rando(int l, int h){
		return (int)(Math.random()*(h-l+1)+l);
	}
	
	public int[][] setall(int[][] a, int n){
		for(int y =0;y<a.length;y++){
			for(int x =0;x<a.length;x++){
				a[y][x] = n;
			}
		}
		return a;
	}
	
	public void reset(int y, int x){
		BufferedImage temp = null;
		while(true){
			int f3 = rando(0,10200000);
			String f4 = genHex(rando(4,7),true);
			try{
				//pics[y][x] = ImageIO.read(new URL("http://gallery.photo.net/photo/"+f3+"-md.jpg"));
				if(runn){
					temp = ImageIO.read(new URL("http://i.imgur.com/"+f4+".jpg"));
					if(!same(temp,def) && temp != null){
						pics[y][x] = temp;
						break;
					}
				}
				//System.out.println(f3);
			}catch(Exception ex){
				//System.out.println("Failed at: "+f3);
			}
		}
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
	
	public class GFrame extends JFrame{
		
		Canvas[][] canvas = new Canvas[pics.length][pics[0].length];
		
		public GFrame(){
			setLayout(new GridLayout(canvas[0].length,canvas.length));
			setSize(800,480);
			setLocation(100,100);
			for(int y=0;y<canvas.length;y++){
				for(int x=0;x<canvas[y].length;x++){
					canvas[y][x] = new Canvas(y,x);
					add(canvas[y][x]);
				}
			}
			addWindowListener(new WindowListener(){
				public void windowActivated(WindowEvent arg0) {
				}
				public void windowClosed(WindowEvent arg0) {
				}
				public void windowClosing(WindowEvent arg0) {
					System.exit(0);
				}
				public void windowDeactivated(WindowEvent arg0) {
				}
				public void windowDeiconified(WindowEvent arg0) {
				}
				public void windowIconified(WindowEvent arg0) {
				}
				public void windowOpened(WindowEvent arg0) {
				}
			});
			
			addMouseListener(new MouseListener(){
				public void mouseClicked(MouseEvent e) {
				}
				public void mouseEntered(MouseEvent e) {
				}
				public void mouseExited(MouseEvent e) {
				}
				public void mousePressed(MouseEvent e) {
					runn = bswitch(runn);
					pulse = true;
				}
				public void mouseReleased(MouseEvent e) {
				}
			});
			setVisible(true);
		}
		
		public boolean bswitch(boolean b){
			if(b){
				b = false;
			}else{
				b = true;
			}
			return b;
		}
		
		public class Canvas extends JPanel{
			int y;
			int x;
			public Canvas(int yp,int xp){
				y=yp;
				x=xp;
			}
			public void paintComponent(Graphics g){
				super.paintComponent(g);
				setBackground(Color.WHITE);
				if(pics[y][x] != null){
					g.drawImage(pics[y][x], 0, 0, getWidth(), getHeight(), this);
				}else{
					g.setColor(Color.WHITE);
					g.drawRect(0, 0, getWidth(), getHeight());
					g.setFont(new Font(Font.MONOSPACED,1,80));
					g.setColor(Color.RED);
					g.drawString("LOADING",100,100);
				}
				repaint();
			}
		}
	}
	
	public static void main(String[] args) {
		new GridPic();
	}

}
