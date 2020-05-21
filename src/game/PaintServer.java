package game;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class PaintServer {
	private ServerSocket serverSocket;
	private List<PaintHandler> list;
 
	private final int port = 1593;
	//private final int port = 9950;
	
	public PaintServer() { 
		try {
			serverSocket = new ServerSocket(port);
			System.out.println(port+"포트 페인트 서버 대기중");

			list = new ArrayList<PaintHandler>();

			while (true) {
				Socket socket = serverSocket.accept();
				PaintHandler handler = new PaintHandler(socket, list);
				handler.start();
				list.add(handler);
			} // while
		} catch (Exception e) {
		} 
	}

	public static void main(String[] args) {
		new PaintServer();
	}
}
