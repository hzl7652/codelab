package zhouyi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ZYGUI {
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("chicken");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		CkPanel ck = new CkPanel();
		frame.getContentPane().add(ck);
		frame.pack();
		frame.setVisible(true);
	}

	public static class CkPanel extends JPanel {
		public CkPanel() {
			setBackground(Color.white);
			setPreferredSize(new Dimension(480, 480));
		}

		public void paintComponent(Graphics page) {
			super.paintComponent(page);
			page.setColor(Color.black);
			page.drawOval(140, 120, 200, 160);
			page.setColor(Color.white);
			page.fillOval(100, 220, 80, 64);
			page.setColor(Color.black);
			page.drawOval(100, 220, 80, 64);
			page.drawLine(220, 275, 160, 350);
			page.drawLine(260, 275, 320, 350);
			int[] x = { 95, 112, 122 };
			int[] y = { 296, 274, 278 };
			page.drawPolygon(x, y, 3);

		}

	}
}
