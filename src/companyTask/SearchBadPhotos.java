package companyTask;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.nutz.lang.Files;

public class SearchBadPhotos {


	public static void main(String[] args) throws Exception {

		String srcPath = "D:\\work-info\\官网照片文件";
		String destPath = "D:\\work-info\\官网照片文件-bad";
		new File(destPath).mkdirs();
		List<File> files = scanFiles(srcPath);
		System.out.println("共查询到："+files.size()+"张照片");
		copyFiles(srcPath,destPath,files);
	}
	public static void copyFiles(String srcPath,String destPath,List<File> results){
		int len = srcPath.length();
		for(File file : results){
			String path = file.getAbsolutePath().substring(len+1);
			try {
				Files.copyFile(file, new File(destPath,path));
			} catch (IOException e) {
				System.out.println("copy error:"+file.getAbsolutePath());
			}
		}
			
	}
	public static List<File> scanFiles(String dirPath){
		File dir = new File(dirPath);
		List<File> result = new ArrayList<File>();
		if(! dir.exists() || !dir.isDirectory()){
			return result;
		}
		File[] files = dir.listFiles();
		for(int i=0;i<files.length;i++){
			if(files[i].isHidden()) continue;
			else{
				if(files[i].isDirectory()){
					if(files[i].getName().startsWith(".")) continue;
					result.addAll(scanFiles(files[i].getAbsolutePath()));
				}else{
					if(files[i].isFile()){
						if(files[i].getName().endsWith(".jpg")){
							if(testPhoto(files[i])){
								result.add(files[i]);
							}else{
								continue;
							}
						}else{
							System.out.println("other type:"+files[i].getAbsolutePath());
						}
					}
				}
			}
		}
		return result;
		
		
	}
	public static boolean testPhoto(File photo) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(photo);
		} catch (IOException e) {
			System.out.print("error:"+photo.getAbsolutePath());
			return false;
		}
		if(image.getWidth() >150 || image.getHeight()>200){
			return true;
		}else{
			return false;
		}
		
	}
	public static String getSuffix(String file){
		int index = file.lastIndexOf(".");
		if(index != -1){
			return file.substring(index+1);
		}else{
			return "";
		}
	}
}
