package online_image;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

public class GGame {
	
	BufferedImage guess;
	GFrame gg;
	int[][] cover = new int[15][15];
	
	public GGame(){
		gg = new GFrame();
		reset();
		while(true){
			gg.repaint();
		}
	}
	
	public String genHex(int l, boolean c){
		String hchars = "0123456789abcdef";
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
		return (int)(Math.random()*(h-l)+l+1);
	}
	
	public int[][] setall(int[][] a, int n){
		for(int y =0;y<a.length;y++){
			for(int x =0;x<a.length;x++){
				a[y][x] = n;
			}
		}
		return a;
	}
	
	public void reset(){
		guess = null;
		cover = setall(cover, 0);
		while(true){
			int f0 = (int)(Math.random()*5000+40);
			String f1 = genHex((int)(Math.random()*2+9),false);
			String f2 = genHex(10,true);
			int f3 = rando(0,10200000);
			try{
				guess = ImageIO.read(new URL("http://gallery.photo.net/photo/"+f3+"-md.jpg"));
				//guess = ImageIO.read(new URL("http://farm5.static.flickr.com/4029/4247714390_7a33b1f313.jpg"));
				//System.out.println("Fail: "+f0+" - "+f1+" - "+f2);
				System.out.println(f3);
			}catch(Exception ex){
				System.out.println("Failed at: "+f3);
			}
			if(guess != null){
				break;
			}
		}
		//double scl = (double)(gg.getHeight()-10)/guess.getHeight();
		//gg.setSize((int)(guess.getWidth()*scl),gg.getHeight());
	}
	
	public class GFrame extends JFrame{
		
		Canvas canvas;
		
		public GFrame(){
			setSize(800,480);
			setLocation(100,100);
			canvas = new Canvas();
			add(canvas);
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
					try{
						int yyy = (int)( (double)(e.getY()-31) / ( (double)(canvas.getHeight()-50) / cover.length ) );
						int xxx = (int)( (double)e.getX() / ( (double)canvas.getWidth() / cover[0].length ) );
						
						//if(cover[yyy][xxx] == 0){
							cover[yyy][xxx] = 1;
						//}else{
						//	cover[yyy][xxx] = 0;
						//}
					}catch(Exception ex){
						
					}
					Rectangle m = new Rectangle(e.getX(),e.getY(),1,1);
					if(m.intersects(new Rectangle(5, getHeight()-50+5, 120, 40))){
						cover = setall(cover, 1);
					}
					if(m.intersects(new Rectangle(140, getHeight()-50+5, 120, 40))){
						guess = null;
						reset();
					}
				}
				public void mouseReleased(MouseEvent e) {
				}
			});
			setVisible(true);
		}
		
		public class Canvas extends JPanel{
			double xs = 0;
			double ys = 0;
			public void paintComponent(Graphics g){
				super.paintComponent(g);
				setBackground(Color.WHITE);
				g.setColor(Color.GRAY);
				g.fill3DRect(0, getHeight()-50, getWidth(), 50, true);
				g.setColor(Color.LIGHT_GRAY);
				g.fill3DRect(5, getHeight()-50+5, 120, 40, true);
				g.fill3DRect(140, getHeight()-50+5, 120, 40, true);
				g.setColor(Color.BLACK);
				g.setFont(new Font(Font.MONOSPACED,1,20));
				g.drawString("Reveal", 10, getHeight()-50+35);
				g.drawString("New", 145, getHeight()-50+35);
				g.setColor(Color.RED);
				if(guess != null){
					g.drawImage(guess, 0, 0, getWidth(), getHeight()-50, this);
					for(int y=0;y<cover.length;y++){
						for(int x=0;x<cover[y].length;x++){
							if(cover[y][x]==0){
								xs = (double)getWidth()/(double)cover[0].length;
								ys = ((double)getHeight()-(double)50)/(double)cover.length;
								g.fillRect((int)(xs*(double)x),(int)(ys*(double)y),(int)xs+1,(int)ys+1);
							}
						}
					}
				}else{
					int fs = 60;
					g.setFont(new Font(Font.MONOSPACED,1,fs));
					String t = "LOADING IMAGE";
					g.drawString(t, (int)(getWidth()/2-((t.length()*fs*0.60)/2)), (getHeight()-10)/2);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		new GGame();
	}

}
