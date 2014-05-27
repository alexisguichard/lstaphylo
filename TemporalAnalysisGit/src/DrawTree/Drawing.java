package DrawTree;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import scala.Serializable;


public abstract class Drawing implements Serializable  {
	
	private int x=0;
	private int y=0;
	private int width=0;
	private int height=0;
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public void setPosition(int x2, int y2, int width, int height) {
		// TODO Auto-generated method stub
		setX(x2);setY(y2);setWidth(width); setHeight(height);
	}
	
	public int widthString(String s){
		Font font = new Font("Verdana", Font.PLAIN, 10);
		FontMetrics metrics = new FontMetrics(font){
			private static final long serialVersionUID = 1L;
		};
		Rectangle2D bounds = metrics.getStringBounds(s, null);
		return (int) bounds.getWidth()+50;
		
	}
	
	public abstract void draw(Graphics g);
	public abstract int calculPosition( Drawing d, int minX, int y);

}
