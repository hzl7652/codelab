package http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import org.nutz.log.Log;
import org.nutz.log.Logs;

public class Orc {
	private static Log log = Logs.get(); 
	public static String picToCode(String path) throws IOException, InterruptedException{
		Process p = Runtime.getRuntime().exec("E:\\Program Files\\Tesseract-OCR\\tesseract.exe "+path.replaceAll("/", "\\") + " "+ path.substring(path.lastIndexOf("\\")+1,path.lastIndexOf(".")));
		p.waitFor();
		p.destroy();
		Scanner sc = new Scanner(new FileInputStream(path.substring(0,path.lastIndexOf("."))+".txt"));
		StringBuffer sb = new StringBuffer();
		while(sc.hasNextLine()){
			sb.append(sc.nextLine());
		}
		sc.close();
		return sb.toString();
	}
}
