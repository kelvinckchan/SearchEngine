//package testcode;
//
//import java.lang.Character.UnicodeBlock;
//import java.util.HashSet;
//
//import webCrawler.URLQueue;
//
////import org.apache.commons.lang3.StringEscapeUtils;
//
//public class tester {
//
//	public static void main(String[] args) {
//		// System.out.println(StringEscapeUtils.escapeEcmaScript("简体"));
//
//		// String[] acii = StringEscapeUtils.escapeJava("简体").split("\\");
//		// for (String a : acii) {
//		// System.err.println(a);
//		// }
//		//
//		// String[] substr;
//		// System.out.println("以空字串简体 分割成子字串:");
//		// substr = new String("程式設accc简体 , 計範例").split("");
//		// for ( String element : substr )
//		// {
//		// System.out.println(element);
//		// }
//
//		// HashSet<UnicodeBlock> chineseUnicodeBlocks = new HashSet<UnicodeBlock>() {{
//		// add(UnicodeBlock.CJK_COMPATIBILITY);
//		// add(UnicodeBlock.CJK_COMPATIBILITY_FORMS);
//		// add(UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS);
//		// add(UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT);
//		// add(UnicodeBlock.CJK_RADICALS_SUPPLEMENT);
//		// add(UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION);
//		// add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
//		// add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A);
//		// add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B);
//		// add(UnicodeBlock.KANGXI_RADICALS);
//		// add(UnicodeBlock.IDEOGRAPHIC_DESCRIPTION_CHARACTERS);
//		// }};
//		//
//		// String mixedChinese = "查詢促進民間參與公共建設法（210ＢＯＴ法）";
//		//
//		// for (char c : mixedChinese.toCharArray()) {
//		// if (chineseUnicodeBlocks.contains(UnicodeBlock.of(c))) {
//		// System.out.println(c + " is chinese");
//		// } else {
//		// System.out.println(c + " is not chinese");
//		// }
//		// }
//		
//		System.out.println(URLQueue.getProcessedURLSize());
//		URLQueue.PushProcessedURL("url1");
//		System.out.println(URLQueue.getProcessedURLSize());
//	}
//
//}
