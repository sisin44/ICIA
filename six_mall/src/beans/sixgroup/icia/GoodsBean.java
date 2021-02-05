package beans.sixgroup.icia;

import javax.servlet.http.HttpServletRequest;

public class GoodsBean {
	private String word;
	private String goCode;
	private String goName;
	private int goPrice;
	private int goStock;
	private String goImage;
	private String seCode;
	private String seName;
	private String goBImage;
	private String mId;
	private int goQty;
	private String date;
	private String mName;
	
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getGoBImage() {
		return goBImage;
	}
	public void setGoBImage(String goBImage) {
		this.goBImage = goBImage;
	}
	public String getGoCode() {
		return goCode;
	}
	public String getmId() {
		return mId;
	}
	public void setmId(String mId) {
		this.mId = mId;
	}
	public int getGoQty() {
		return goQty;
	}
	public void setGoQty(int goQty) {
		this.goQty = goQty;
	}
	public void setGoCode(String goCode) {
		this.goCode = goCode;
	}
	public String getGoName() {
		return goName;
	}
	public void setGoName(String goName) {
		this.goName = goName;
	}
	public int getGoPrice() {
		return goPrice;
	}
	public void setGoPrice(int goPrice) {
		this.goPrice = goPrice;
	}
	public int getGoStock() {
		return goStock;
	}
	public void setGoStock(int goStock) {
		this.goStock = goStock;
	}
	public String getGoImage() {
		return goImage;
	}
	public void setGoImage(String goImage) {
		this.goImage = goImage;
	}
	public String getSeCode() {
		return seCode;
	}
	public void setSeCode(String seCode) {
		this.seCode = seCode;
	}
	public String getSeName() {
		return seName;
	}
	public void setSeName(String seName) {
		this.seName = seName;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
}
