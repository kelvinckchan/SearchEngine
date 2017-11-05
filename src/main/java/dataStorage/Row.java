package dataStorage;

public class Row {
	private int RowId;
	private String Keyword;
	// private int Rank;
	private int WordNo;
	private String FromURL;
	private String Title;
	// private ArrayList<Object> RowContent;

	public Row(int RowId, String Keyword, int WordNo, String FromURL, String Title) {
		// RowContent = new ArrayList<Object>();
		this.RowId = RowId;
		this.Keyword = Keyword;
		// this.Rank = Rank;
		this.WordNo = WordNo;
		this.FromURL = FromURL;
		this.Title = Title;
	}

	public Row setRowId(int RowId) {
		this.RowId = RowId;
		return this;
	}

	public int getRowId() {
		return this.RowId;
	}

	public String getKeyword() {
		return this.Keyword;
	}

	public int getWordNo() {
		return this.WordNo;
	}

	public String getFromURL() {
		return this.FromURL;
	}

	public String getTitle() {
		return this.Title;
	}
}
