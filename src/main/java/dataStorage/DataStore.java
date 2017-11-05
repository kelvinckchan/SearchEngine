package dataStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;

public class DataStore {

	static KeywordCol keycol = new KeywordCol();
	static RankCol rankcol = new RankCol();
	static WordNoCol wordnocol = new WordNoCol();
	static Map<Integer, Row> RowMap = new HashMap<Integer, Row>();
	Map<Integer, Row> TempRowMap;
	int ExistedRowSize;
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(DataStore.class);
	static Map<String, ArrayList<String>> ContainedURLMap = new HashMap<String, ArrayList<String>>();;

	public DataStore() {
		ExistedRowSize = RowMap.size();
		TempRowMap = new HashMap<Integer, Row>();
	}

	public synchronized void Store(String ParsingURL, ArrayList<String> ContainedURLList) {
		RowMap.putAll(TempRowMap);
		TempRowMap.forEach((k, v) -> {
			int rowid = RowMap.size() + 1;
			Row r = v.setRowId(rowid);
			RowMap.put(rowid, r);
			StoreToCol(r);
		});
		ContainedURLMap.put(ParsingURL, ContainedURLList);
	}

	public static void print() {
//
//		RowMap.forEach((k, v) -> {
//			System.out.printf("Rid(k)-%s: {keyword:\"%s\", URL:\"%s\"}\n", k, v.Keyword, v.FromURL);
//			logger.debug("Rowid - {}: {keyword:{}, WordPos:{}, URL:{}}", k, v.Keyword, v.WordNo, v.FromURL);
//		});
//
//		ContainedURLMap.forEach((k, v) -> {
//			logger.debug("ContainedURLMap: \"{}\" => Contained({}): {}", k, v.size(), v);
//		});
	}

	public synchronized void addRow(String Keyword, int Rank, int WordNo, String FromURL,String Title) {
		TempRowMap.put(TempRowMap.size() + 1, new Row(TempRowMap.size() + 1, Keyword, WordNo, FromURL,Title));
	}

	public void StoreToCol(Row r) {
		keycol.addColObj(r.RowId, r.Keyword);
		// rankcol.addColObj(r.RowId, r.Rank);
		wordnocol.addColObj(r.RowId, r.WordNo);
	}

	public static synchronized Map<Integer, Row> getRowMap() {
		return RowMap;
	}

	public static synchronized Col getkeycol() {
		return keycol;
	}

	public static synchronized Col getrankcol() {
		return rankcol;
	}

	public static synchronized Col getwordnocol() {
		return wordnocol;
	}

	public static List<Row> SearchByKeywordResult(String keyword) {
		List<Row> result = new ArrayList<Row>();
		SearchByKeyword(keyword).stream().forEach(row->{
			if(result.stream().noneMatch(r-> Objects.equals(r.FromURL, row.FromURL)))
			result.add(row);
		});
		return result;
	}
	public static List<Row> SearchByPhrasedResult(String keyword) {
		List<Row> result = new ArrayList<Row>();
		SearchByPhrase(keyword).stream().forEach(row->{
			if(result.stream().noneMatch(r-> Objects.equals(r.FromURL, row.FromURL)))
			result.add(row);
		});
		return result;
	}
	public static List<Row> SearchByKeyword(String keyword) {
		Map<Integer, String> map = keycol.ColObj;
		return map.entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), keyword))
				.map(entry -> RowMap.get(entry.getKey())).collect(Collectors.toList());
	}

	public static List<Row> SearchByPhrase(String phrase) {
		List<Row> Last = new ArrayList<Row>();
		List<Row> Common = new ArrayList<Row>();
		for (String s : phrase.split("\\s+")) {
			List<Row> Rows = SearchByKeyword(s);
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
		}
		return Common;
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
