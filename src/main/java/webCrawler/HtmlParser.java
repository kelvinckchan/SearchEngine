package webCrawler;

import java.io.IOException;
import java.lang.Character.UnicodeBlock;
import java.lang.Character.UnicodeScript;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

public class HtmlParser implements Runnable {

	private URL ParsingURL;
	// private URLQueue URLQueue;
	private PriorityQueue<String> ProcessedURL = URLQueue.getProcessedURL();
	private PriorityQueue<String> UnprocessedURL = URLQueue.getUnprocessedURL();
	DataStore ds;
	private final org.slf4j.Logger logger;

	public HtmlParser(URL ParsingURL) {
		this.ParsingURL = ParsingURL;
		this.logger = LoggerFactory.getLogger(HtmlParser.class);
		addToProcessedURL(this.ParsingURL.toString());
	}

	// public HtmlParser(URL ParsingURL, URLQueue URLQueue) {
	// this.ParsingURL = ParsingURL;
	// // this.URLQueue = URLQueue;
	// }

	public void addToProcessedURL(String url) {
		URLQueue.PushProcessedURL(url);
	}

	public void addToUnProcessedURL(String url) {
		if (!URLQueue.ProcessedURLisContain(url) && !URLQueue.UnprocessedURLisContain(url))
			URLQueue.PushUnProcessedURL(url);
	}

	@Override
	public void run() {
		Parser(ParsingURL);

	}

	public void Parser(URL url) {
		try {
			Document doc = Jsoup.parse(url, 50000);
			// System.out.println(doc);
			ds = new DataStore();

			// Get title
			Elements WebTitles = doc.select("title");
			for (Element t : WebTitles) {
				String TitleText = t.text();
				// System.err.println(TitleText);
			}

			// Get words
			Elements aTagWordsString = doc.select("a");
			List<Element> Line = aTagWordsString.stream()
					.filter(line -> !line.text().replaceAll("[ \\t\\n\\x0B\\f\\r\\d|\\|]", "").equals(""))
					.collect(Collectors.toList());

			KeywordsSeparator(Line);

			// Get URLs
			Elements Links = doc.select("a");
			// logger.info("[<a>]: " + Links);
			Links.stream().filter(l -> {
				return !l.absUrl("href").isEmpty();
			}).forEach(link -> {
				addToUnProcessedURL(link.absUrl("href"));
				// logger.info("{}\t=>\t{}", link, link.absUrl("href"));
			});

			ds.Store();

		} catch (UnsupportedMimeTypeException ex) {
			logger.debug(ex.getLocalizedMessage() + "\t{}", ParsingURL.toString());
			System.out.println("Remove Errorn URL: " + ProcessedURL.remove(ParsingURL.toString()));
		} catch (IOException e) {
			e.printStackTrace();
			logger.debug(e.getLocalizedMessage() + "\t{}", ParsingURL.toString());
			System.out.println("Remove Errorn URL: " + ProcessedURL.remove(ParsingURL.toString()));
		}
	}

	public void StoreKeywordRow(String key, int WordPos) {
		ds.addRow(key, 1, WordPos, ParsingURL.toString());
	}

	public void KeywordsSeparator(List<Element> Line) {
		AtomicInteger atomicInteger = new AtomicInteger(0);
		// separate keywords
		Line.forEach(line -> {
			// splite by space
			for (String s : line.text().replaceAll("[\\pP+0-9&nsbp<>]", "")
					.split("[ \\t\\n\\x0B\\f\\r\\d\\u00a0|\\|]")) {
				// if s is not space only
				if (!s.trim().replaceAll("[ \\t\\n\\x0B\\f\\r\\d|\\|]", "").equals("")) {
					// If contain Chinese/other character, split dividual character
					if (containsTargetScript(s)) {
						Character lastChar = null;
						List<Character> notChinese = new ArrayList<Character>();
						char[] charArray = s.toCharArray();
						for (int i = 0; i < charArray.length; i++) {
							// If contain not Chinese Character, store in notChinese
							if (!containsTargetScript(charArray[i])) {
								// if Last Character is chinese, clear list
								if (containsTargetScript(lastChar)) {
									notChinese.clear();
								}
								// Store notChinese char
								notChinese.add(charArray[i]);
								// if(大@in{s}) this char != chinese && is last of the char array
								if (i == charArray.length - 1) {
									// System.err.println("[" + atomicInteger.incrementAndGet() + "]: " + notChinese
									// .stream().map(e -> e.toString()).reduce((acc, e) -> acc + e).get());
									String keyword = notChinese.stream().map(c -> c.toString()).reduce((a, b) -> a + b)
											.get();
									StoreKeywordRow(keyword, atomicInteger.incrementAndGet());
								}
							} else {
								// if (大@ins{大}) this char == chinese && not Fist Char && last char != chinese
								if (lastChar != null && !containsTargetScript(lastChar)) {
									// System.err.println("[" + atomicInteger.incrementAndGet() + "]: " + notChinese
									// .stream().map(e -> e.toString()).reduce((acc, e) -> acc + e).get());
									String keyword = notChinese.stream().map(e -> e.toString())
											.reduce((acc, e) -> acc + e).get();
									StoreKeywordRow(keyword, atomicInteger.incrementAndGet());
								} else {
									// Is chinese, directly print out
									// System.out.println("[" + atomicInteger.incrementAndGet() + "]: " +
									// charArray[i]);
									StoreKeywordRow(String.valueOf(charArray[i]), atomicInteger.incrementAndGet());
								}
							}
							lastChar = charArray[i];
						}
					} else {
						// Not Contain Chinese, print directly
						// System.err.println("[" + atomicInteger.incrementAndGet() + "]: " + s);
						StoreKeywordRow(s, atomicInteger.incrementAndGet());
					}
				}
			}
		});
	}

	List<UnicodeScript> TargetScript = Arrays.asList(Character.UnicodeScript.HAN, Character.UnicodeScript.HIRAGANA);

	public boolean containsTargetScript(Character c) {
		return containsTargetScript(String.valueOf(c));
	}

	public boolean containsTargetScript(String s) {
		return s.codePoints().anyMatch(codepoint -> TargetScript.contains(Character.UnicodeScript.of(codepoint)));
	}

}