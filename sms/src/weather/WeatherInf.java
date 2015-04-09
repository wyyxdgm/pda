package weather;

public class WeatherInf {

	private OneDayWeatherInf[] weatherInfs;
	private String dressAdvise;// ���½���
	private String washCarAdvise;// ϴ������
	private String coldAdvise;// ��ð����
	private String sportsAdvise;// �˶�����
	private String ultravioletRaysAdvise;// �����߽���

	public WeatherInf() {
		dressAdvise = "";
		washCarAdvise = "";
		coldAdvise = "";
		sportsAdvise = "";
		ultravioletRaysAdvise = "";
	}

	public void printInf() {

		System.out.println(dressAdvise);
		System.out.println(washCarAdvise);
		System.out.println(coldAdvise);
		System.out.println(sportsAdvise);
		System.out.println(ultravioletRaysAdvise);
		for (int i = 0; i < weatherInfs.length; i++) {
			System.out.println(weatherInfs[i]);
		}

	}

	public OneDayWeatherInf[] getWeatherInfs() {
		return weatherInfs;
	}

	public void setWeatherInfs(OneDayWeatherInf[] weatherInfs) {
		this.weatherInfs = weatherInfs;
	}

	public String getDressAdvise() {
		return dressAdvise;
	}

	public void setDressAdvise(String dressAdvise) {
		this.dressAdvise = dressAdvise;
	}

	public String getWashCarAdvise() {
		return washCarAdvise;
	}

	public void setWashCarAdvise(String washCarAdvise) {
		this.washCarAdvise = washCarAdvise;
	}

	public String getColdAdvise() {
		return coldAdvise;
	}

	public void setColdAdvise(String coldAdvise) {
		this.coldAdvise = coldAdvise;
	}

	public String getSportsAdvise() {
		return sportsAdvise;
	}

	public void setSportsAdvise(String sportsAdvise) {
		this.sportsAdvise = sportsAdvise;
	}

	public String getUltravioletRaysAdvise() {
		return ultravioletRaysAdvise;
	}

	public void setUltravioletRaysAdvise(String ultravioletRaysAdvise) {
		this.ultravioletRaysAdvise = ultravioletRaysAdvise;
	}

}