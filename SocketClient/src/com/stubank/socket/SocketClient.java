package com.stubank.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stubank.socketclient.R;

public class SocketClient extends Activity {
	EditText et_socket_host;
	EditText et_socket_port;
	EditText et_edit_message;
	EditText et_socket_token;
	Button btn_connect_socket;
	Button btn_close_socket;
	Button btn_send_message;
	TextView tv_history_message;
	boolean isConnected = false;
	String host = "192.168.1.153";
	int port = 3008;
	String token = "ee6131806b7714d6ed2106844ffe9e2a";//ee6131806b7714d6ed2106844ffe9e2a
	private Socket socket = null;
	private BufferedReader in = null;
	private PrintWriter out = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();
		btn_connect_socket.setOnClickListener(new ConnectSocket());
		btn_close_socket.setOnClickListener(new CloseSocket());
		btn_send_message.setOnClickListener(new SendMessage());
	}

	private void init() {
		et_socket_host = (EditText) findViewById(R.id.et_socket_host);
		et_socket_port = (EditText) findViewById(R.id.et_socket_port);
		et_edit_message = (EditText) findViewById(R.id.et_edit_message);
		et_socket_token = (EditText) findViewById(R.id.et_socket_token);
		btn_connect_socket = (Button) findViewById(R.id.btn_connect_socket);
		btn_close_socket = (Button) findViewById(R.id.btn_close_socket);
		btn_send_message = (Button) findViewById(R.id.btn_send_message);
		tv_history_message = (TextView) findViewById(R.id.tv_history_message);
	}

	class ConnectSocket implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (!TextUtils.isEmpty(et_socket_host.getText())) {
				host = et_socket_host.getText().toString();
			}
			if (!TextUtils.isEmpty(et_socket_port.getText())
					&& TextUtils.isDigitsOnly(et_socket_port.getText())) {
				port = Integer.parseInt(et_socket_port.getText().toString());
			}
			if (!TextUtils.isEmpty(et_socket_token.getText())) {
				token = et_socket_token.getText().toString();
			}
			if (connect == null) {
				startConnect();
			} else {
				tip("has connected already!");
			}
		}
	}

	class CloseSocket implements OnClickListener {

		@Override
		public void onClick(View v) {
			reset();
			tip("disconnected success！");
		}
	}

	public void reset() {
		if (socket != null) {
			try {
				socket.close();
				if (in != null)
					in.close();
				if (out != null)
					out.close();
				in = null;
				out = null;
				socket = null;
				connect = null;
				isConnected = false;
			} catch (IOException e) {
				e.printStackTrace();
				tip(e.getMessage());
			}
		}
	}

	class SendMessage implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			if (!TextUtils.isEmpty(et_edit_message.getText())) {
				if (socket != null && socket.isConnected()) {
					if (!socket.isOutputShutdown()) {
						String msg = et_edit_message.getText().toString();
						et_edit_message.setText("");
						try {
							JSONObject obj = new JSONObject();
							obj.put("msg", msg);
							obj.put("action", "message");
							out.println(obj.toString());
							tv_history_message
									.setText(tv_history_message.getText()
											.toString() + ("我:" + msg) + "\n");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} else {
						tip("连接异常！");
					}
				} else {
					tip("输出数据异常！");
				}
			} else {
				tip("没有消息！");
			}
		}
	}

	public void tip(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@SuppressLint("HandlerLeak")
	Handler inHandler = new Handler() {
		public void handleMessage(Message message) {
			String msg = (String) message.obj;
			tv_history_message.setText(tv_history_message.getText().toString()
					+ ("server:" + msg) + "\n");
		}
	};
	@SuppressLint("HandlerLeak")
	Handler tipHandler = new Handler() {
		public void handleMessage(Message message) {
			String msg = (String) message.obj;
			tip(msg);
		}
	};
	String msg = "";
	Thread connect = null;

	public void startConnect() {
		connect = new Thread() {
			public void run() {
				if (!isConnected) {
					try {
						socket = new Socket(host, port);
						socket.setKeepAlive(true);
						in = new BufferedReader(new InputStreamReader(
								socket.getInputStream()));
						out = new PrintWriter(
								new BufferedWriter(new OutputStreamWriter(
										socket.getOutputStream())), true);
						if (socket.isConnected()) {
							isConnected = true;
							Message m = tipHandler.obtainMessage();
							m.obj = "connected success!";
							tipHandler.sendMessage(m);
							if (!socket.isOutputShutdown()) {
								JSONObject obj = new JSONObject();
								try {
									// 首次连接必须传的两个值，之后不用
									obj.put("action", "connect");
									obj.put("token", token);
									out.println(obj.toString());
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							while (isConnected) {
								try {
									if (!socket.isInputShutdown()) {
										msg = in.readLine();
										if (msg != null && msg != "") {
											Message mm = inHandler
													.obtainMessage();
											mm.obj = msg;
											inHandler.sendMessage(mm);
										}
									}
									try {
										socket.sendUrgentData(0xFF);
									} catch (Exception ex) {
										Message mmm = tipHandler
												.obtainMessage();
										mmm.obj = "server shut down!";
										tipHandler.sendMessage(mmm);
										reset();
										try {
											Thread.sleep(5000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										startConnect();
										break;
									}
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
						Message m = tipHandler.obtainMessage();
						m.obj = e1.getMessage();
						tipHandler.sendMessage(m);
						connect = null;
					} catch (IOException e1) {
						e1.printStackTrace();
						Message m = tipHandler.obtainMessage();
						String s = e1.getMessage();
						if (s.indexOf("ECONNREFUSED") != -1) {
							m.obj = "服务器未启动或地址错误！";
						}
						tipHandler.sendMessage(m);
						reset();
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						startConnect();
					}
				} else {
					Message m = tipHandler.obtainMessage();
					m.obj = "has connected already!";
					tipHandler.sendMessage(m);
				}
			}
		};
		connect.start();
	}
}
