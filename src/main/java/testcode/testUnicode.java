package testcode;

import java.lang.Character.UnicodeBlock;
import java.lang.Character.UnicodeScript;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class testUnicode {

	public static void main(String[] args) {
		testUnicode t = new testUnicode();
		t.run();
	}

	public void run() {
		AtomicInteger atomicInteger = new AtomicInteger(0);

		String line = "浸大@Instagram哈哈\"どうも, \", 傻的嗎!(12/10)<gg>Nice!http://www.google.com";
//		 line = "I am , a boy!";
//		Stream.of(line.split("")).forEach(System.out::println);
		System.out.println(line);

		for (String s : line.split("[ \\pP+0-9\"\"<>\\t\\n\\x0B\\f\\r\\d|\\|]")) {
			// if s is not space only
			if (!s.trim().replaceAll("[ \\pP+0-9\\t\\n\\x0B\\f\\r\\d|\\|]", "").equals("")) {
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
							if (i == charArray.length - 1)
								System.err.println("[" + atomicInteger.incrementAndGet() + "]: "
										+ notChinese.stream().map(e -> e.toString()).reduce((acc, e) -> acc + e).get());
						} else {
							// if (大@ins{大}) this char == chinese && not Fist Char && last char != chinese
							if (lastChar != null && !containsTargetScript(lastChar)) {
								System.err.println("[" + atomicInteger.incrementAndGet() + "]: "
										+ notChinese.stream().map(e -> e.toString()).reduce((acc, e) -> acc + e).get());
							}
							// Is chinese, directly print out
							System.out.println("[" + atomicInteger.incrementAndGet() + "]: " + charArray[i]);
						}
						lastChar = charArray[i];
					}
				} else {
					// Not Contain Chinese, print directly
					System.err.println("[" + atomicInteger.incrementAndGet() + "]: " + s);
				}
			}
		}

	}

	List<UnicodeScript> TargetScript = Arrays.asList(Character.UnicodeScript.HAN, Character.UnicodeScript.HIRAGANA);

	public boolean containsTargetScript(Character c) {
		return containsTargetScript(String.valueOf(c));
	}
	public boolean containsTargetScript(String s) {
		return s.codePoints().anyMatch(codepoint -> TargetScript.contains(Character.UnicodeScript.of(codepoint)));
	}

	// HashSet<UnicodeBlock> chineseUnicodeBlocks = new HashSet<UnicodeBlock>() {
	// {
	// add(UnicodeBlock.HIRAGANA);
	// add(UnicodeBlock.KATAKANA);
	// add(UnicodeBlock.CJK_COMPATIBILITY);
	// add(UnicodeBlock.CJK_COMPATIBILITY_FORMS);
	// add(UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS);
	// add(UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT);
	// add(UnicodeBlock.CJK_RADICALS_SUPPLEMENT);
	// add(UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION);
	// add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
	// add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A);
	// add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B);
	// add(UnicodeBlock.KANGXI_RADICALS);
	// add(UnicodeBlock.IDEOGRAPHIC_DESCRIPTION_CHARACTERS);
	// }
	// };
	// public boolean isChineseChar(Character c) {
	// return chineseUnicodeBlocks.contains(UnicodeBlock.of(c));
	// }

}
