package webCrawler;

import java.io.IOException;
import java.lang.Character.UnicodeBlock;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

public class HtmlParser implements Runnable {

	private URL ParsingURL;

	public HtmlParser(URL ParsingURL) {
		this.ParsingURL = ParsingURL;
	}

	private final org.slf4j.Logger logger = LoggerFactory.getLogger(HtmlParser.class);

	public static void main(String[] args) {
		try {
			new Thread(new HtmlParser(new URL("http://www.hkbu.edu.hk/tch/main/index.jsp"))).start();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		Parser(ParsingURL);
	}

	public void Parser(URL url) {
		try {
			Document doc = Jsoup.parse(url, 5000);
//			System.out.println(doc);

			// Get title
			Elements WebTitles = doc.select("title");
			for (Element t : WebTitles) {
				String TitleText = t.text();
				System.err.println(TitleText);
			}

			// Get words
			Elements aTagWordsString = doc.select("a");
			AtomicInteger atomicInteger = new AtomicInteger(0);
			aTagWordsString.stream().filter(line -> {
				String text = line.text().replaceAll("[\\t\\n\\x0B\\f\\r\\d|\\|]", "");
				return !text.equals("");
			}).forEach(line -> {
				// System.err.println(word.text());
				for (String s : line.text().split("[ \\t\\n\\x0B\\f\\r\\u00a0]")) {
					if (!s.trim().replaceAll("[\\t\\n\\x0B\\f\\r\\d|\\|]", "").equals("")) {

						if (containsHanScript(s)) {
							Character lastChar = null;
							List<Character> notChinese = new ArrayList<Character>();
							char[] charArray = s.toCharArray();
							for (int i = 0; i < charArray.length; i++) {
								if (!isChineseChar(charArray[i])) {
									if (lastChar != null && isChineseChar(lastChar)) {
										notChinese = new ArrayList<Character>();
									}
									notChinese.add(charArray[i]);
								} else {
									System.out.println("[" + atomicInteger.incrementAndGet() + "]: " + charArray[i]);
								}
								// 大@ins
								// 大@ins大
								if (isChineseChar(charArray[i]) && lastChar != null && !isChineseChar(lastChar)
										|| !isChineseChar(charArray[i]) && i == charArray.length - 1) {
									System.err.println("[" + atomicInteger.incrementAndGet() + "]: " + notChinese
											.stream().map(e -> e.toString()).reduce((acc, e) -> acc + e).get());
								}
								lastChar = charArray[i];
							}

						} else {
							System.err.println(
									"[" + atomicInteger.incrementAndGet() + "]: " + s + " C?: " + containsHanScript(s));
						}

					}
				}
			});

			// Get URLs
			Elements Links = doc.select("[href]");
			Links.stream().filter(l -> {
				return !l.absUrl("href").isEmpty();
			}).forEach(link -> {
				// System.err.println(link.absUrl("href"));
			});
			logger.info("[Parsedurl]: " + url.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	PriorityQueue<URL> ProcessedURL = new PriorityQueue<URL>();
	PriorityQueue<URL> UnprocessedURL = new PriorityQueue<URL>();

	public void addToProcessedURL(URL url) {
		ProcessedURL.add(url);

	}

	public void addToUnProcessedURL(URL url) {
		if (!ProcessedURL.contains(url))
			UnprocessedURL.add(url);
	}

	HashSet<UnicodeBlock> chineseUnicodeBlocks = new HashSet<UnicodeBlock>() {
		{
			add(UnicodeBlock.CJK_COMPATIBILITY);
			add(UnicodeBlock.CJK_COMPATIBILITY_FORMS);
			add(UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS);
			add(UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT);
			add(UnicodeBlock.CJK_RADICALS_SUPPLEMENT);
			add(UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION);
			add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
			add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A);
			add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B);
			add(UnicodeBlock.KANGXI_RADICALS);
			add(UnicodeBlock.IDEOGRAPHIC_DESCRIPTION_CHARACTERS);
		}
	};

	public boolean containsHanScript(String s) {
		return s.codePoints()
				.anyMatch(codepoint -> Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN);
	}

	public boolean isChineseChar(Character c) {
		return chineseUnicodeBlocks.contains(UnicodeBlock.of(c));
	}

}
