package testcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class testDS {

	public static void main(String[] args) {
		testDS app = new testDS();
		app.run();
	}

	KeywordCol keycol = new KeywordCol();
	RankCol rankcol = new RankCol();
	WordNoCol wordnocol = new WordNoCol();
	Map<Integer, Row> RowMap = new HashMap<Integer, Row>();

	public void run() {
		Store();
		RowMap.forEach((k, v) -> {
			System.out.println("m: " + k + "=> : " + v.Keyword + " - " + v.FromURL);
		});
		Search();
	}

	public void Search() {

		//
		Map<Object , Row> getKeysByValueResult = getKeysByValue(keycol.ColObj, "k1");
		System.out.println("KBV Result Row: " + getKeysByValueResult);
		getKeysByValueResult.forEach((id, v) -> {
			System.out.println("URL KBV: " + v.FromURL);
		});


		//
		sortByValue(rankcol.getColObjMap()).forEach((k, v) -> {
			System.out.println("Sorted by Rank: [Rank]: " + k + "=> [RowId]: " + v);
		});

	}

	public void Store() {

		Row r = new Row(1, "k1", 2, 1, "hkbu.edu", Arrays.asList("hkbu.com"));
		RowMap.put(1, r);
		StoreToCol(r);
		r = new Row(2, "k1", 1, 1, "hkbu.edu.hk", Arrays.asList("hkbu.com", "hkbu.edu"));
		RowMap.put(2, r);
		StoreToCol(r);
		r = new Row(3, "k2", 3, 2, "hkbu.com", Arrays.asList("hkbu.edu.hk", "hkbu.edu"));
		RowMap.put(3, r);
		StoreToCol(r);

	}

	public void StoreToCol(Row r) {
		keycol.addColObj(r.RowId, r.Keyword);
		rankcol.addColObj(r.RowId, r.Rank);
		wordnocol.addColObj(r.RowId, r.WordNo);
	}

	public void SearchKeyword() {

	}

	// public <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
	public <T, E> Map<Object, Row> getKeysByValue(Map<T, E> map, E value) {
		Map<Object, Row> result = map.entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), value))
				.map(Map.Entry::getKey)
				.collect(Collectors.toMap(key -> key, key -> RowMap.get(key), (a, b) -> a, HashMap::new));

		System.out.println(result);
		return result;
		// return map.entrySet().stream().filter(entry ->
		// Objects.equals(entry.getValue(), value)).map(Map.Entry::getKey)
		// .collect(Collectors.toSet());
	}

	public Map<Integer, Integer> sortByValue(Map<Integer, Integer> unsortedMap) {
		// unsortedMap.entrySet().stream().sorted();
		System.out.println(

				unsortedMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
								LinkedHashMap::new))
						.entrySet().stream().map(Map.Entry::getKey)
						.collect(Collectors.toMap(key -> key, key -> RowMap.get(key), (a, b) -> a, HashMap::new))

		);

		return unsortedMap;
	}

}

class Row {
	public int RowId;
	public String Keyword;
	public int Rank;
	public int WordNo;
	public String FromURL;
	public List<String> ContainedURL;
	// public ArrayList<Object> RowContent;

	public Row(int RowId, String Keyword, int Rank, int WordNo, String FromURL, List<String> ContainedURL) {
		// RowContent = new ArrayList<Object>();
		this.RowId = RowId;
		this.Keyword = Keyword;
		this.Rank = Rank;
		this.WordNo = WordNo;
		this.FromURL = FromURL;
		this.ContainedURL = ContainedURL;
	}

}

class KeywordCol extends Col {
	public KeywordCol() {
		ColObj = new HashMap<Integer, String>();
	}
}

class RankCol extends Col {
	public RankCol() {
		ColObj = new HashMap<Integer, Integer>();
	}
}

class WordNoCol extends Col {
	public WordNoCol() {
		ColObj = new HashMap<Integer, Integer>();
	}
}

class Col {
	public String ColName;
	public HashMap ColObj;

	public void addColObj(int rowId, Object value) {
		ColObj.put(rowId, value);
	}

	public HashMap getColObjMap() {
		return ColObj;
	}

	public Object getColObj(int rowId) {
		return ColObj.get(rowId);
	}
}