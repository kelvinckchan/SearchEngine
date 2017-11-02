package testcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
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


public class testDataStructure {

	public static void main(String[] args) {
		testDataStructure app = new testDataStructure();

		app.run();
	}

	ArrayListMultimap<Object, ArrayList<Object>> RowMap = ArrayListMultimap.create();

	ArrayListMultimap<Object, Object> KeywordColumn = ArrayListMultimap.create();
	ArrayListMultimap<Object, Object> RankColumn = ArrayListMultimap.create();
	ArrayListMultimap<Object, Object> WordNoColumn = ArrayListMultimap.create();

	Multimap<Object, Object> InvertedMultimap;
	ArrayList<Object> ContainedUrlList;
	ArrayList<Object> Row;

	public void run() {
		create();
		Collection<Object> ResultList = SearchByColumn(KeywordColumn, "K1");
		for (Object RowID : ResultList) {
			System.out.println("RowID: " + RowID);
			System.out.println("[Get Row by id] : " + RowMap.get(RowID));
		}
		SortByValue();
	}

	public void create() {
		ContainedUrlList = new ArrayList<Object>();
		ContainedUrlList.add("hkbu.com");
		ContainedUrlList.add("hkbu.com.hk");

		KeywordColumn.put("id1", "K1");
		KeywordColumn.put("id2", "K1");
		RankColumn.put("id1", 2);
		RankColumn.put("id2", 1);
		WordNoColumn.put("id1", 1);
		WordNoColumn.put("id12", 1);

		Row = new ArrayList<Object>();
		Row.add("K1");
		Row.add("url1");
		Row.add(ContainedUrlList);
		Row.add(2);
		Row.add(1);
		RowMap.put("id1", Row);

		Row = new ArrayList<Object>();
		Row.add("K1");
		Row.add("url1");
		Row.add(ContainedUrlList);
		Row.add(1);
		Row.add(43);
		RowMap.put("id2", Row);

		// for (Entry<Object, Collection<Object>> e :
		// InvertedMultimap.asMap().get("K1")) {
		//
		// }
	}

	public Collection<Object> SearchByColumn(ArrayListMultimap<Object, Object> col, Object key) {
		InvertedMultimap = Multimaps.invertFrom(col, ArrayListMultimap.<Object, Object>create());
		System.out.println("InvertedMultimap: " + InvertedMultimap);
		Collection<Object> ResultList = InvertedMultimap.asMap().get(key);
		return ResultList;
	}

	private void SortByValue() {
		InvertedMultimap = Multimaps.invertFrom(RankColumn, ArrayListMultimap.<Object, Object>create());
		for (int i = 1; i <= RankColumn.size(); i++) {
			System.out.println("[Rank:" + i + "] => Rowid: " + InvertedMultimap.get(i));
		}
	}
}