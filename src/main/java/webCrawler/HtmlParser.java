package webCrawler;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser {

	public HtmlParser() {

	}

	public static void main(String[] args) {
		HtmlParser p = new HtmlParser();
		p.Parser("./fetched.html");
	}

	public void Parser(String fileName) {
		Parser(new File(fileName));
	}

	public Document JsoupParseToDocument(File htmlFile) {
		try {
			return Jsoup.parse(htmlFile, "UTF-8", "http://www.hkbu.edu.hk/eng/main/index.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void Parser(File htmlFile) {

		Document doc = JsoupParseToDocument(htmlFile);
		System.out.println(doc);
		
		// Get title
		Elements WebTitles = doc.select("title");
		for (Element t : WebTitles) {
			String TitleText = t.text();
			// System.err.println(TitleText);
		}

		// Get words
		Elements Words = doc.select("a");
		AtomicInteger atomicInteger = new AtomicInteger(0);
		Words.stream().filter(w -> {
			String text = w.text().replaceAll("[\\t\\n\\x0B\\f\\r\\d|\\|]", "");
			return !text.equals("");
		}).forEach(word -> {
			// System.err.println(word.text());
			String[] SingleWord = word.text().split("[ \\t\\n\\x0B\\f\\r\\u00a0]");
			for (String s : SingleWord) {
				if (!s.trim().replaceAll("[\\t\\n\\x0B\\f\\r\\d|\\|]", "").equals("")) {
					// System.err.println("[" + atomicInteger.incrementAndGet() + "]: " + s);
				}
			}
		});

		// Get URLs0
		Elements Links = doc.select("[href]");
		Links.stream().filter(l -> {
			return !l.absUrl("href").isEmpty();
		}).forEach(link -> {
			System.err.println(link.absUrl("href"));
		});

	}

}
