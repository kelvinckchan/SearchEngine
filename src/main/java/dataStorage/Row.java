package dataStorage;

public class Row {
	public int RowId;
	public String Keyword;
	// public int Rank;
	public int WordNo;
	public String FromURL;
	public String Title;
	// public ArrayList<Object> RowContent;

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
}
