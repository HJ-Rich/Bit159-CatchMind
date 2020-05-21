package login;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JTextArea;
import javax.swing.JTextField;

public class LoginChat implements Runnable {
	
	private PrintWriter writer;
	private BufferedReader reader;
	private Socket socket;
	private Login login;
	private JTextField inputChat;
	private JTextArea chatLog;
	private String nickName;
	private ChatSound cs;
	
	private final String serverIP = "14.138.202.117";
	private final int port = 1591;
	//private final String serverIP = "localhost";
	//private final int port = 5600;
	
	public LoginChat(String nickName,  Login login) {
		
		this.login = login;
		this.inputChat = login.getInputChat();
		this.chatLog = login.getChatLog();
		this.nickName = nickName;
		try {
			socket = new Socket(serverIP, port);
			writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			writer.println(nickName);
			writer.flush();
		} catch (UnknownHostException e) {
			System.out.println("서버 찾을 수 없음");
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			System.out.println("서버 연결 실패");
			e.printStackTrace();
			System.exit(0);
		}
		
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
		
	//----------------------------------------JTextField action------------------------------

		//채팅 입력 후 Enter 눌렀을 때 작동하는 메소드
		inputChat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = inputChat.getText();
				if(msg == null || msg.length() == 0) {	return;	}
				if(msg.equals("exit")) {
					writer.println("exit");
					writer.flush();
				}
				writer.println(nickName + ";" + msg);
				writer.flush();
				inputChat.setText("");
			}
		});
		
	}
	//LoginChat Constructor
	
	
	//-----------------------------------run method-------------------------------------
	public void run() {
		//스레드는 수신하는 메시지에 대해 적용되는 부분이다. 
		while (true) {
			
			try {
				//최초 읽는 메시지는 입장 메시지다. 그 이후부턴 나 또는 다른 클라이언트가 보낸 내용을 서버가 반사해준 내용이다.
				String rmsg = reader.readLine();
				cs = new ChatSound();
				//exit값을 받았을 경우엔 close 후에 종료한다.
				if(rmsg.equals("exit")) {
					socket.close();
					writer.close();
					reader.close();
					System.out.println(nickName + "은 exit를 수신하여 클라이언트를 종료합니다");
					break;
				}
				
				//exit값이 아닐 경우엔 구분자로 쪼갠 뒤에 메시지를 표현한다.
				String[] rmsgg = rmsg.split(";");
				chatLog.append(rmsgg[0] + rmsgg[1] + "\n");
				//JTextArea의 줄 갯수를 구해서 그 위치로 현재 위치를 옮겨준다.
				int position = chatLog.getText().length();
				chatLog.setCaretPosition(position);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} // while
	}
	//run method

	//로그인 창에서 X누르거나 게임종료 눌러서 종료할 때 필요한 메소드
	public void exit() {
		writer.println("exit");
		writer.flush();
	}
	
	
	
	
	
}
//LoginChat class