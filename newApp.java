package www.dataStructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;
import com.google.common.collect.Table;
import com.google.common.primitives.Ints;

/**
 * Hello world!
 *
 * Keyword:{URL, containedURL} Name => array1 : wordNo Name => array2 : Rank
 * 
 * {url, keyword, wordNo} name {url, containedURL, rank} [0: url3| 1:
 * containedURL3| 2: wordno3| 3: 3] row{key => col1, col2}
 * 
 * ----------------------------------------------------------------------------------
 * 1 Get rank with fast algorithm 2 Return all ascending url at once
 * ----------------------------------------------------------------------------------
 * K1: array1 array2 K2: array1 K3: array1
 * 
 * 
 * 
 */

public class newApp {

	public static void main(String[] args) {
		newApp app = new newApp();
		app.create();
		app.searchKEY();
		app.sortByValue();
		// app.getValueArray("K1", 2);
		// app.findRank();
	}

	ArrayListMultimap<Object, ArrayList<Object>> table = ArrayListMultimap.create();

	ArrayListMultimap<Object, Object> KeywordColumn = ArrayListMultimap.create();
	ArrayListMultimap<Object, Object> FromURLColumn = ArrayListMultimap.create();
	ArrayListMultimap<Object, ArrayList<Object>> ContainedURLColumn = ArrayListMultimap.create();
	ArrayListMultimap<Object, Object> RankColumn = ArrayListMultimap.create();
	ArrayListMultimap<Object, Object> WordNoColumn = ArrayListMultimap.create();

	static // ArrayListMultimap<Object, ArrayList<Object>> column =
	// ArrayListMultimap.create();
	Multimap<Object, Object> invertedMultimap;
	ArrayList<Object> urlRow;
	ArrayList<Object> Row;

	public void create() {
		urlRow = new ArrayList<Object>();
		urlRow.add("containedURL1");
		urlRow.add("containedURL2");

		KeywordColumn.put("id1", "K1");
		KeywordColumn.put("id2", "K1");
		FromURLColumn.put("id1", "url1");
		ContainedURLColumn.put("id1", urlRow);

		RankColumn.put("id1", 2);
		RankColumn.put("id2", 1);
		WordNoColumn.put("id1", 1);

		Row = new ArrayList<Object>();
		Row.add("K1");
		Row.add("url1");
		Row.add(urlRow);
		Row.add(2);
		Row.add(1);
		table.put("id1", Row);

		Row = new ArrayList<Object>();
		Row.add("K1");
		Row.add("url1");
		Row.add(urlRow);
		Row.add(1);
		Row.add(43);
		table.put("id2", Row);

		// for (Entry<Object, Collection<Object>> e :
		// invertedMultimap.asMap().get("K1")) {
		//
		// }
	}

	public void searchKEY() {
		invertedMultimap = Multimaps.invertFrom(KeywordColumn, ArrayListMultimap.<Object, Object>create());

		System.out.println(invertedMultimap + "\n");

		// if (invertedMultimap.asMap().get("K1").iterator().hasNext()) {
		Collection<Object> resultList = invertedMultimap.asMap().get("K1");

		for (Object RowID : resultList) {
			System.out.println("RowID: " + RowID);

			System.out.println("[RowID] : " + table.get(RowID));

		}
		System.out.println();
	}

	private void sortByValue() {
		invertedMultimap = Multimaps.invertFrom(RankColumn, ArrayListMultimap.<Object, Object>create());
		for (int i = 1; i <= RankColumn.size(); i++) {
			System.out.println("[Rank::" + i + "] :" + invertedMultimap.get(i));
		}
	}
}
