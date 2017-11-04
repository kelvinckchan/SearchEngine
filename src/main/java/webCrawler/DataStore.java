package webCrawler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

public class DataStore {

	KeywordCol keycol = new KeywordCol();
	RankCol rankcol = new RankCol();
	WordNoCol wordnocol = new WordNoCol();
	static Map<Integer, Row> RowMap = new HashMap<Integer, Row>();
	Map<Integer, Row> TempRowMap;
	int ExistedRowSize;
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(DataStore.class);

	public DataStore() {
		ExistedRowSize = RowMap.size();
		TempRowMap = new HashMap<Integer, Row>();
	}

	public synchronized void Store() {
		RowMap.putAll(TempRowMap);
		TempRowMap.forEach((k, v) -> {
			int rowid = RowMap.size() + 1;
			Row r = v.setRowId(rowid);
			RowMap.put(rowid, r);
			StoreToCol(r);
		});

	}

	public static void print() {

		RowMap.forEach((k, v) -> {
			System.out.printf("Rid(k)-%s: {keyword:\"%s\", Rank:\"%s\", URL:\"%s\"}\n", k, v.Keyword, v.Rank,
					v.FromURL);
			logger.debug("Rowid - {}: {keyword:{}, WordPos:{}, URL:{}}", k, v.Keyword, v.WordNo, v.FromURL);
		});

		logger.debug("Reuslt: " + RowMap.get(1).Keyword + " " + RowMap.get(1).FromURL);

	}

	public synchronized void addRow(String Keyword, int Rank, int WordNo, String FromURL) {

		TempRowMap.put(TempRowMap.size() + 1,
				new Row(TempRowMap.size() + 1, Keyword, Rank, WordNo, FromURL, Arrays.asList("hkbu.com")));
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

	public Row setRowId(int RowId) {
		this.RowId = RowId;
		return this;
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
