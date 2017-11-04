package testcode;

import java.lang.Character.UnicodeBlock;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class testUnicode {

	public static void main(String[] args) {
		testUnicode t = new testUnicode();
		t.run();
	}

	public void run() {
		AtomicInteger atomicInteger = new AtomicInteger(0);
		// System.err.println(word.text());

		String line = "浸大@Instagram哈哈どうも, 傻的嗎!(12/10)Nice!";
System.out.println(line.replaceAll("[\\w+]", ""));
		
		// splite by space
		for (String s : line.split("[ \\t\\n\\x0B\\f\\r\\u00a0]")) {
			// if s is not space only
			if (!s.trim().replaceAll("[  \\t\\n\\x0B\\f\\r\\d|\\|]", "").equals("")) {

				// If contain Chinese/other character, split dividual character
				if (containsHanScript(s)) {
					Character lastChar = null;
					List<Character> notChinese = new ArrayList<Character>();
					char[] charArray = s.toCharArray();

					for (int i = 0; i < charArray.length; i++) {
						// If contain not Chinese Character, store in notChinese
						if (!isChineseChar(charArray[i])) {
							// if Last Character is chinese, create new list
							if (isChineseChar(lastChar)) {
								notChinese = new ArrayList<Character>();
							}
							// Store notChinese char
							notChinese.add(charArray[i]);
						} else {
							// Is chinese, directly print out
							System.out.println("*[" + atomicInteger.incrementAndGet() + "]: " + charArray[i]);
						}
						// if (大@ins{大} || 大@in{s}) print combined notChinese List<Charater> to String
						// this char = chinese && last char != chinese
						// or
						// this char != chinese && is last of the char array
						if (isChineseChar(charArray[i]) && lastChar != null && !isChineseChar(lastChar)
								|| !isChineseChar(charArray[i]) && i == charArray.length - 1) {
							System.err.println("[" + atomicInteger.incrementAndGet() + "]: "
									+ notChinese.stream().map(e -> e.toString()).reduce((acc, e) -> acc + e).get());
						}
						lastChar = charArray[i];
					}

				} else {
					// Not Contain Chinese, print directly
					System.err.println(
							"[" + atomicInteger.incrementAndGet() + "]: " + s + " C?: " + containsHanScript(s));
				}

			}
		}

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
