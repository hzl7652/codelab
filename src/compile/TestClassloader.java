package compile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.nutz.lang.Files;

public class TestClassloader {
	@Before
	public void setUp(){
		String[] args = new String[]{"-extdirs","/home/tt/workspace/java/nor/deps/",
				"-sourcepath","/home/tt/workspace/java/nor/src/",
				"-6","-d","/home/tt/workspace/java/nor/testbin/",
				"/home/tt/workspace/java/nor/src/org/nutz/template/Start.java"};
		new org.eclipse.jdt.internal.compiler.batch.Main(new PrintWriter(new ByteArrayOutputStream()),new PrintWriter(new ByteArrayOutputStream()),false,null,null).compile(args);
	}
	@Test
	public void testNorClassloader() throws ClassNotFoundException, MalformedURLException{
		ClassLoader cl = new NorClassLoader(new URL[]{new File("/home/tt/workspace/java/nor/testbin/").toURI().toURL(),
				new File("/home/tt/workspace/java/nor/deps/jetty-xml-7.2.2.v20101205.jar").toURI().toURL()
		},Thread.currentThread().getContextClassLoader());
		Class<?> clazz = cl.loadClass("org.nutz.template.Start");
		Assert.assertEquals("org.nutz.template.Start", clazz.getName());
		clazz = cl.loadClass("org.eclipse.jetty.xml.XmlParser");
		Assert.assertEquals("org.eclipse.jetty.xml.XmlParser", clazz.getName());
	}
	@After
	public void tearDown(){
		Files.deleteDir(new File("/home/tt/workspace/java/nor/testbin/"));
	}
}
