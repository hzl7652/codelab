package zip;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

	/** 
	 *  解压
	 */
	public static boolean unPack(String zipFile ,String destPath){
		
		if(zipFile == null || zipFile.trim().length()==0 || destPath == null || destPath.trim().length() == 0) return false;
		
		File source = new File(zipFile);
		if(! source.exists()) return false;
		File destFile = new File(destPath);
		destFile.mkdirs();
		destPath = destFile.getAbsolutePath()+File.separator;
		try{
			byte[] b = new byte[1024];
			int length ;
			ZipFile zip = new ZipFile(source);
			@SuppressWarnings("rawtypes")
			Enumeration entries = zip.entries();
			ZipEntry zipEntry = null;
			while(entries.hasMoreElements()){
				zipEntry = (ZipEntry) entries.nextElement();
				File loadFile = new File(destPath + zipEntry.getName());
				if(zipEntry.isDirectory()){
					loadFile.mkdirs();
				}else{
					if(!loadFile.getParentFile().exists()){
						loadFile.getParentFile().mkdirs();
					}
					OutputStream os = new FileOutputStream(loadFile);
					InputStream in = zip.getInputStream(zipEntry);
					while((length = in.read(b)) > 0){
						os.write(b, 0, length);
					}
				}
			}
			
		}catch(Exception e){
			System.out.println(e.getMessage());;
			return false;
		}
		
		return true;
	}
	/**
	 *  
	 * @param file
	 * @param destFile
	 * @return
	 */
	public static boolean pack(File file,String destFile){
		File destZip = new File(destFile);
		String abspath = destZip.getAbsolutePath();
		new File(abspath.substring(abspath.lastIndexOf(File.separator))).mkdirs();
		ZipOutputStream zos = null;
		boolean result = true;
		try{
			byte[] b = new byte[1024];
			int length ;
			zos = new ZipOutputStream(new FileOutputStream(destZip));
			if(file.exists() && !file.isDirectory()){
				zos.putNextEntry(new ZipEntry(file.getName()));
				InputStream is = new FileInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(is);
				while((length = bis.read(b)) != -1){
					zos.write(b,0,length);
				}
				is.close();
			}
			
		}catch(Exception e){
			e.printStackTrace();
			result =  false;
		}finally{
			if(zos != null){
				try {
					zos.closeEntry();
					zos.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return result;
	}
	public static boolean pack(File[] files ,String destFile){
		
		File destZip = new File(destFile);
		String abspath = destZip.getAbsolutePath();
		new File(abspath.substring(abspath.lastIndexOf(File.separator))).mkdirs();
		ZipOutputStream zos = null;
		boolean result =true;
		try{
			byte[] b = new byte[1024];
			int length ;
			zos = new ZipOutputStream(new FileOutputStream(destZip));
			if(files.length ==0) return false;
			for(File file : files){
			if(file.exists() && !file.isDirectory()){
				//System.out.println(file.getName());
				InputStream is = new FileInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(is);
				
				zos.putNextEntry(new ZipEntry(file.getName()));
				while((length = bis.read(b)) != -1){
					zos.write(b,0,length);
				}
				is.close();
			}
			}
			zos.closeEntry();
			zos.close();
		}catch(Exception e){
			e.printStackTrace();
			result = false;
		}finally{
			if(zos != null){
				try {
					zos.closeEntry();
					zos.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return result;
	}
	
	
	public static void main(String[] args) {
		File[] jars = new File("lib/").listFiles();
		pack(jars, "jars.zip");
		
		unPack("jars.zip", "lib2/");
	}
}
