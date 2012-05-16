package image;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;

import javax.swing.JFrame;

public class Test1 extends JFrame{

	public Test1(){
		this.setTitle("画图测试");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400, 400);
		
		
		Canvas c = new MyCanvas();
		c.setSize(300, 300);
		this.add(c);
	}
	
	
	
	
	
	public static void main(String[] args) {
		Test1 t = new Test1();
		t.setVisible(true);
	}
}
class MyCanvas extends Canvas {
	
	public MyCanvas(){
	}
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.draw(new Line2D.Double(10,10,25,10));
		g2.draw(new Line2D.Double(10,15,25,15));
		g2.draw(new Line2D.Double(10,20,25,20));
		
		QuadCurve2D qc = new QuadCurve2D.Double();
		qc.setCurve(30, 50, 35, 70, 80, 10);
		g2.draw(qc);
		
	}
}
