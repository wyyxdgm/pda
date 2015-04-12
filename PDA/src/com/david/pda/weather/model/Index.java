package com.david.pda.weather.model;

public class Index {
	private String title;// "穿衣"
	private String zs;// "较冷"
	private String tipt;// "穿衣指数"
	private String des;// "建议着厚外套加毛衣等服装。年老体弱者宜着大衣、呢外套加羊毛衫。"

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\t\t").append("title:").append(this.title).append('\n');
		sb.append("\t\t").append("zs:").append(this.zs).append('\n');
		sb.append("\t\t").append("tipt:").append(this.tipt).append('\n');
		sb.append("\t\t").append("des:").append(this.des).append('\n');
		return sb.toString();
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getZs() {
		return zs;
	}

	public void setZs(String zs) {
		this.zs = zs;
	}

	public String getTipt() {
		return tipt;
	}

	public void setTipt(String tipt) {
		this.tipt = tipt;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

}
