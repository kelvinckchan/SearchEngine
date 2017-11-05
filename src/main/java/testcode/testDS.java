package testcode;

import static org.mockito.Matchers.anyObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class testDS {

//	public static void main(String[] args) {
//		testDS app = new testDS();
//		app.run();
//	}

	KeywordCol keycol = new KeywordCol();
	RankCol rankcol = new RankCol();
	WordNoCol wordnocol = new WordNoCol();
	Map<Integer, Row> RowMap = new HashMap<Integer, Row>();

	public void run() {
		Store();
		RowMap.forEach((k, v) -> {
			System.out.printf("Rid(k)-%s: {keyword:\"%s\", WordPos:%s, Rank:\"%s\", URL:\"%s\"}\n", k, v.Keyword,
					v.WordNo, v.Rank, v.FromURL);
		});

		System.out.println("==================================================================");
		Search();
	}

	public void Search() {

		// Map<Integer, Row> getKeysByValueResult = getKeysByValue(keycol.ColObj, "k1");
		// System.out.println("KBV Result Row: " + getKeysByValueResult);
		// getKeysByValueResult.forEach((id, v) -> {
		// System.out.println("URL KBV: " + v.FromURL);
		// });

		// sortByValue(rankcol.getColObjMap()).forEach((k, v) -> {
		// System.out.println("Sorted by Rank: [Rank]: " + k + "=> [RowId]: " + v);
		// });

		// Map<Integer, Row> ResultedRows = getSortedRowByColumnValue(keycol.ColObj,
		// "k1");
		//
		// ResultedRows.forEach((rank, row) -> {
		// System.out.println("Rank: " + rank + "=> " + " Rid:" + ((Row) row).RowId + "
		// =" + ((Row) row).FromURL);
		// });
		//
		// ArrayList<String> URLList = ResultedRows.entrySet().stream().map(row ->
		// row.getValue().FromURL)
		// .collect(Collectors.toCollection(ArrayList::new));

		// URLList.forEach(l -> System.out.println(l));

		List<Row> result = SearchByPhase("k1 k3 k4");
		System.out.println("Phrase Search[\"k1 k3 k4\"]: ");
		result.stream().forEach(r -> {
			System.out.println(r.FromURL);
		});
	}

	public List<Row> SearchByPhase(String phrase) {
		// List<String> LastRows = new ArrayList<String>();
		// List<String> CommonURL = new ArrayList<String>();

		// Map<String, Integer> Last = new HashMap();
		// Map<String, Integer> Common = new HashMap();
		List<Row> Last = new ArrayList();
		List<Row> Common = new ArrayList();

		for (String s : phrase.split("\\s+")) {
			Map<Integer, Row> ResultRows = getSortedRowByColumnValue(keycol.ColObj, s);
			List<Row> Rows = SearchByKeyword(s);

			// Map<String, Integer> URLwithWPos = Rows.stream().collect(Collectors.toMap(r
			// -> r.FromURL, r -> r.WordNo));
			if (!Last.isEmpty()) {
				for (Row r : Rows) {
					if (Last.stream()
							.anyMatch(lastrow -> Objects.equals(lastrow.FromURL, r.FromURL)
									&& Objects.equals(lastrow.FromURL, r.FromURL)
									&& Objects.equals(lastrow.WordNo, r.WordNo - 1))
							&& !Common.stream().anyMatch(commonrow -> Objects.equals(commonrow.FromURL, r.FromURL))) {
						Common.add(r);
					}  
				}
				Rows = Common;
			}
			Last = Rows;
			// if (!Last.isEmpty()) {
			// for (Entry<String, Integer> u : URLwithWPos.entrySet()) {
			// if (Last.containsKey(u.getKey()) && Last.get(u.getKey()) == u.getValue() - 1)
			// {
			// Common.put(u.getKey(), u.getValue());
			// } else if (Common.containsKey(u.getKey())) {
			// Common.remove(u.getKey());
			// }
			// }
			// URLwithWPos = Common;
			// }
			// Last = URLwithWPos;

			// List<String> ExistInURLs = ResultRows.entrySet().stream().map(entry ->
			// entry.getValue().FromURL)
			// .collect(Collectors.toList());
			// System.out.println("Keyword: " + s + " => " + ExistInURLs);
			// if (!LastRows.isEmpty()) {
			// // CommonURL = ContainedURL.stream().filter(link ->
			// // LastRow.contains(link)).collect(Collectors.toList());
			// for (String theseURL : ExistInURLs) {
			// if (LastRows.contains(theseURL) && !CommonURL.contains(theseURL))
			// CommonURL.add(theseURL);
			// }
			// ExistInURLs = CommonURL;
			// }
			// LastRows = ExistInURLs;
		}

		// System.err.println("CommonURL: => " + CommonURL);

		// Iterator<String> keys =
		// Stream.of(phrase.split("\\s+")).collect(Collectors.toList()).iterator();
		//
		// Map<String, Map<String, Row>> KeyWithRows = Stream.of(phrase.split("\\s+"))
		// .collect(Collectors.toMap(k -> k, k ->
		// getSortedRowByColumnValue(keycol.ColObj, k)));
		//
		// KeyWithRows.entrySet().stream().filter(key -> {
		// if (ContainedURL == null)
		// ContainedURL = key.getValue().entrySet().stream().map(entry ->
		// entry.getValue().FromURL)
		// .collect(Collectors.toList());
		//
		// return true;
		// });
		return Common;
	}
	public List<Row> SearchByKeyword(String keyword) {
		Map<Integer, String> map = keycol.ColObj;
		return map.entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), keyword))
				.map(entry -> RowMap.get(entry.getKey())).collect(Collectors.toList());
	}
	List<String> ContainedURL = null;
	// public <T, E> Object[] getRowIDByColumnValue(Map<T, E> map, E value) {
	// return map.entrySet().stream().filter(entry ->
	// Objects.equals(entry.getValue(), value)).map(Map.Entry::getKey);
	// }

	// Yes!!!
	private <T, E> Map<Integer, Row> getSortedRowByColumnValue(Map<T, E> map, E value) {
		return map.entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), value)).map(Map.Entry::getKey)
				.collect(Collectors.toMap(key -> RowMap.get(key).Rank, key -> RowMap.get(key), (a, b) -> a,
						HashMap::new));
	}

	public <T, E> Map<Object, Row> getKeysByValue(Map<T, E> map, E value) {
		return map.entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), value)).map(Map.Entry::getKey)
				.collect(Collectors.toMap(key -> key, key -> RowMap.get(key), (a, b) -> a, LinkedHashMap::new));
	}

	// notused
	public Map<Integer, Integer> sortByValue(Map<Integer, Integer> unsortedMap) {
		System.out.println(

				unsortedMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
								LinkedHashMap::new))
						.entrySet().stream().map(Map.Entry::getKey)
						.collect(Collectors.toMap(key -> key, key -> RowMap.get(key), (a, b) -> a, LinkedHashMap::new))

		);

		return unsortedMap;
	}

	public void Store() {

		Row r = new Row(1, "k1", 2, 1, "hkbu.com.r", Arrays.asList("hkbu.com"));
		RowMap.put(1, r);
		StoreToCol(r);
		r = new Row(2, "k1", 1, 1, "hkbu.edu.hk", Arrays.asList("hkbu.com", "hkbu.edu"));
		RowMap.put(2, r);
		StoreToCol(r);
		r = new Row(3, "k1", 3, 1, "hkbu.com.l", Arrays.asList("hkbu.edu.hk", "hkbu.edu"));
		RowMap.put(3, r);
		StoreToCol(r);
		r = new Row(4, "k3", 4, 2, "hkbu.com.l", Arrays.asList("hkbu.edu.hk", "hkbu.edu"));
		RowMap.put(4, r);
		StoreToCol(r);
		r = new Row(5, "k4", 4, 3, "hkbu.com.l", Arrays.asList("hkbu.edu.hk", "hkbu.edu"));
		RowMap.put(5, r);
		StoreToCol(r);
		r = new Row(6, "k4", 5, 3, "hkbu.com.r", Arrays.asList("hkbu.edu.hk", "hkbu.edu"));
		RowMap.put(6, r);
		StoreToCol(r);
		r = new Row(7, "k3", 5, 2, "hkbu.com.r", Arrays.asList("hkbu.edu.hk", "hkbu.edu"));
		RowMap.put(7, r);
		StoreToCol(r);

	}

	public void StoreToCol(Row r) {
		keycol.addColObj(r.RowId, r.Keyword);
		rankcol.addColObj(r.RowId, r.Rank);
		wordnocol.addColObj(r.RowId, r.WordNo);
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
