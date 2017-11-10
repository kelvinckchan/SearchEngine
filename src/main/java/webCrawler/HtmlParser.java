package webCrawler;

import java.io.IOException;
import java.lang.Character.UnicodeBlock;
import java.lang.Character.UnicodeScript;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

import dataStorage.DataStore;

public class HtmlParser implements Callable<ArrayList<String>> {

	private URL ParsingURL;
	private String TitleText;
	DataStore ds;
	private int MaxProcessedURLPoolSize, MaxUnprocessedQueueSize;
	private ArrayList<String> ContainedURLList;

	private final org.slf4j.Logger logger;

	public HtmlParser(URL ParsingURL, int MaxProcessedURLPoolSize, int MaxUnprocessedQueueSize) {
		this.ParsingURL = ParsingURL;
		this.MaxProcessedURLPoolSize = MaxProcessedURLPoolSize;
		this.MaxUnprocessedQueueSize = MaxUnprocessedQueueSize;

		this.logger = LoggerFactory.getLogger(HtmlParser.class);
		addToProcessedURL(this.ParsingURL.toString());
	}

	public void addToProcessedURL(String url) {
		URLQueue.PushProcessedURLQueue(url);
	}

//	public void addToUnProcessedURL(ArrayList<String> linksList) {
//
//		Iterator<String> iterator = ContainedURLList.iterator();
//		while (URLQueue.getProcessedURLQueueSize() < MaxProcessedURLPoolSize && iterator.hasNext()) {
//			while (URLQueue.getProcessedURLQueueSize() < MaxProcessedURLPoolSize
//					&& URLQueue.getUnprocessedURLQueueSize() < MaxUnprocessedQueueSize && iterator.hasNext()) {
//				String url = iterator.next();
//				if (!URLQueue.ProcessedURLisContain(url) && !URLQueue.UnprocessedURLisContain(url)) {
//					URLQueue.PushUnProcessedURLQueue(url);
//				}
//				iterator.remove();
//			}
//			System.err.printf("%s - waiting:{Un=%s ED=%s List=%s}\n", Thread.currentThread().getName(),
//					URLQueue.getUnprocessedURLQueueSize(), URLQueue.getProcessedURLQueueSize(),
//					ContainedURLList.size());
//		}
//
//		// for (String l : linksList) {
//		// if (URLQueue.getUnprocessedURLQueueSize() >= MaxUnprocessedQueueSize) {
//		// break;
//		// } else if (!URLQueue.ProcessedURLisContain(l) &&
//		// !URLQueue.UnprocessedURLisContain(l))
//		// URLQueue.PushUnProcessedURLQueue(l);
//		// }
//	}

	@Override
	public ArrayList<String> call() throws Exception {
		return Parser(ParsingURL);
	}

	public ArrayList<String> Parser(URL url) {
		try {
			Document doc = Jsoup.parse(url, 50000);
			// System.out.println(doc);
			ds = new DataStore();

			// Get title
			Elements WebTitles = doc.select("title");
			for (Element t : WebTitles) {
				TitleText = t.text();
			}

			// Get words
//			System.out.println(doc.select("body").text());
			ExtractKeywords(doc.select("body").text());
//			ExtractKeywords(doc.select("p"));
			
			
			// Get URLs
			Elements Links = doc.select("a");
			ContainedURLList = Links.stream().filter(l -> !l.absUrl("href").isEmpty()).map(l -> l.absUrl("href"))
					.collect(Collectors.toCollection(ArrayList::new));
			ds.Store(ParsingURL.toString(), ContainedURLList);
			// addToUnProcessedURL(ContainedURLList);
		} catch (UnsupportedMimeTypeException ex) {
			logger.debug(ex.getLocalizedMessage() + "\t{}", ParsingURL.toString());
			logger.debug("Remove UnsupportedMimeType URL: " + ParsingURL.toString() + " =>"
					+ URLQueue.RemoveErrorURLFromProcessedURLQueue(ParsingURL.toString()));
			URLQueue.addErrorURL(ParsingURL.toString());
		} catch (IOException e) {
			e.printStackTrace();
			logger.debug(e.getLocalizedMessage() + "\t{}", ParsingURL.toString());
			logger.debug("Remove IOException URL: " + ParsingURL.toString() + " =>"
					+ URLQueue.RemoveErrorURLFromProcessedURLQueue(ParsingURL.toString()));
			URLQueue.addErrorURL(ParsingURL.toString());
		}
		return ContainedURLList;
	}

//	public void ExtractKeywords(Elements e) {
//		List<Element> Line = e.stream()
//				.filter(line -> !line.text().replaceAll("[ \\t\\n\\x0B\\f\\r\\d|\\|]", "").equals(""))
//				.collect(Collectors.toList());
//		KeywordsSeparator(Line);
//	}

	public void StoreKeywordRow(String key, int WordPos) {
		ds.addRow(key, 1, WordPos, ParsingURL.toString(), TitleText);
	}

	// separate keywords
	public void ExtractKeywords(String line) {
		AtomicInteger counter = new AtomicInteger(0);
		String regex = "[ \u00a0<>\\pP+0-9\"\"\\t\\x0B\\f\\r\\d|\\|\\s+]";
//		Lines.forEach(line -> {
			// splite by space and special characters
			for (String s : line.replaceAll("&nbsp", "").split(regex)) {
				// if s is not space only
				if (!s.trim().replaceAll(regex, "").equals("") && !s.isEmpty()) {
					// If contain Chinese/other character, split dividual character
					if (ContainsTargetScript(s)) {
						Character lastChar = null;
						String NonChineseString = "";
						char[] charArray = s.toCharArray();
						for (int i = 0; i < charArray.length; i++) {
							// If contain not Chinese Character, store in notChinese
							if (!containsTargetScript(charArray[i])) {
								// if Last Character is chinese, clear list
								if (containsTargetScript(lastChar))
									NonChineseString = "";
								// Store notChinese char
								NonChineseString += charArray[i];
								// if(大@in{s}) this char != chinese && is last of the char array
								if (i == charArray.length - 1) {
									StoreKeywordRow(NonChineseString, counter.incrementAndGet());
								}
							} else {
								// if (大@ins{大}) this char == chinese && not Fist Char && last char != chinese
								if (lastChar != null && !containsTargetScript(lastChar)) {
									StoreKeywordRow(NonChineseString, counter.incrementAndGet());
								} else {
									// Is chinese, directly print out
									StoreKeywordRow(String.valueOf(charArray[i]), counter.incrementAndGet());
								}
							}
							lastChar = charArray[i];
						}
					} else {
						// Not Contain Chinese, print directly
						StoreKeywordRow(s, counter.incrementAndGet());
					}
				}
			}
//		});
	}

	static List<UnicodeScript> TargetScript = Arrays.asList(Character.UnicodeScript.HAN,
			Character.UnicodeScript.TAI_VIET, Character.UnicodeScript.BOPOMOFO, Character.UnicodeScript.HIRAGANA,
			Character.UnicodeScript.HANGUL, Character.UnicodeScript.KATAKANA, Character.UnicodeScript.YI,
			Character.UnicodeScript.COMMON, Character.UnicodeScript.MIAO, Character.UnicodeScript.LISU);

	public boolean containsTargetScript(Character c) {
		return ContainsTargetScript(String.valueOf(c));
	}

	public boolean ContainsTargetScript(String s) {
		return s.codePoints().anyMatch(codepoint -> TargetScript.contains(Character.UnicodeScript.of(codepoint)));
	}

}