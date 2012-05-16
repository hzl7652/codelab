package companyTask;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

public class scalePhoto {

	static int WIDTH = 150;
	static int HEIGHT = 200;
	public static void main(String[] args) throws Exception {
		String srcpath = "D:\\work-info\\官网照片文件-bad2";
		String destpath = "D:\\work-info\\官网照片文件-bad2-fix";
		new File(destpath).mkdirs();
		List<File> files = SearchBadPhotos.scanFiles(srcpath);
		for(File file : files){
			File destDir = new File(destpath,file.getAbsolutePath().substring(srcpath.length()+1,file.getAbsolutePath().lastIndexOf("\\")));
			destDir.mkdirs();
			File destFile = new File(destpath,file.getAbsolutePath().substring(srcpath.length()+1));
			System.out.println(destFile.getAbsolutePath());
			scale(file, destFile);
			break;
		}
	}

	public static boolean scale(File photo,File destFile) throws IOException {
		Image image = ImageIO.read(photo);
		int w = image.getWidth(null);
		int h = image.getHeight(null);
		if(w==WIDTH && h == HEIGHT) return true;
		
		int[] new_w_h = scaleWandH(w,h);
		
		BufferedImage bi = new BufferedImage(WIDTH,HEIGHT, BufferedImage.SCALE_SMOOTH);
		 Graphics2D gw = (Graphics2D) bi.getGraphics();
		 int startW = 0;
		 int startH = 0;
		 if(WIDTH==new_w_h[0]){
			 startH = (HEIGHT-new_w_h[1])/2;
		 }else{
			 if(HEIGHT == new_w_h[1]){
				 startW = (WIDTH-new_w_h[0])/2;
			 }
		 }
		 gw.setBackground(Color.WHITE);
		 gw.clearRect(0, 0, WIDTH, HEIGHT);
		 gw.drawImage(image,startW, startH, new_w_h[0], new_w_h[1], null);
		 gw.dispose();
		 ImageIO.write(bi, "jpg", destFile);
		 return true;
		
	}
	public static int[] scaleWandH(int w,int h){
		int[] wh = new int[2];
		float sw = WIDTH /((float)w);
		float sh = HEIGHT /((float)h);
			if(sw >sh){
				w = Math.round(w * sh);
				h = Math.round(h * sh);
			}else{
				if(sw<sh){
					w = Math.round(w*sw);
					h = Math.round(h*sw);
				}else{
					w = Math.round(w*sw);
					h = Math.round(h*sh);
				}
			}
			wh[0] = w;
			wh[1] = h;
			return wh;
	}

}
