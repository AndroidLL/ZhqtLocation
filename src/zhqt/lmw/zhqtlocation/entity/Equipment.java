package zhqt.lmw.zhqtlocation.entity;

import java.io.Serializable;

public class Equipment implements Serializable
{
	
	/*"count":"1","gid":"100000","id":"1","text":"100000",
	 * "iconCls":"icon-car01","time":"2014-03-18 23:59:59.0"}
	*/
	private int count;
	private int gid;
	private int id;
	private int text;
	private String iconCls;
	private String timie;
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getGid() {
		return gid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getText() {
		return text;
	}
	public void setText(int text) {
		this.text = text;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getTimie() {
		return timie;
	}
	public void setTimie(String timie) {
		this.timie = timie;
	}
	
	
	
}
