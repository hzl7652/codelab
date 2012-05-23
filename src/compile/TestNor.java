package compile;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

public class TestNor {

	public static void main(String[] args) {
		args = new String[]{"-extdirs","/home/tt/workspace/java/nor/deps/",
				"-sourcepath","/home/tt/workspace/java/nor/src/",
				"-6","-d","/home/tt/workspace/java/nor/testbin/",
				"/home/tt/workspace/java/nor/src/org/nutz/template/Start.java"};
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new org.eclipse.jdt.internal.compiler.batch.Main(new PrintWriter(baos),new PrintWriter(baos),false,null,null).compile(args);
		System.out.println(baos.toString());
	}
}
