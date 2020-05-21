package login;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class LoginChatServer {
	private ServerSocket serverSocket;
	private List<LoginChatHandler> list;
 
	private final int port = 1591;
	
	public LoginChatServer() { 
		try {
			serverSocket = new ServerSocket(port);
			System.out.println(port+"포트 로그인 챗 서버 대기중");

			list = new ArrayList<LoginChatHandler>();

			while (true) {
				Socket socket = serverSocket.accept();
				LoginChatHandler handler = new LoginChatHandler(socket, list);
				handler.start();
				list.add(handler);
			} // while
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) {
		new LoginChatServer();
	}
}
