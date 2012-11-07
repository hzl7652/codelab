package markdown;

import com.petebevin.markdown.MarkdownProcessor;

public class MarkdownUtils {

	public static String generate(String source){
		MarkdownProcessor mp = new MarkdownProcessor();
		String html = mp.markdown("##markdown##, `hello world` \n \n	lang:java \n	main class{\n	helloword \n	}\n\n *#asdfadf#*");
		System.out.println(html);
		return html;
	}
	public static void main(String[] args) {
		generate("source");
	}
}
