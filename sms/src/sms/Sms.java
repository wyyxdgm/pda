package sms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Sms {
	public static int invokeHttpTest() throws Exception {
		URL url = new URL(
				"http://jiekou.56dxw.com/sms/HttpInterface.aspx?comid=��ҵID&username=�û���"
						+ "&userpwd=����&handtel=�ֻ���&sendcontent=��������Ϊ���֣�С��ͨ����"
						+ "&sendtime=��ʱʱ��&smsnumber=����ƽ̨");
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.connect();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				httpCon.getInputStream()));
		String line = in.readLine();
		System.out.println(" </p>     result:   " + line);
		int i_ret = httpCon.getResponseCode();
		String sRet = httpCon.getResponseMessage();
		System.out.println("sRet   is:   " + sRet);
		System.out.println("i_ret   is:   " + i_ret);
		return 0;
	}

	public static void main(String[] args) {

	}
}
