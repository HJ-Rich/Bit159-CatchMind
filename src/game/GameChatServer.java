package game;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameChatServer {
    
	private ServerSocket serverSocket;
    private List<GameChatHandler> list;
    private final int port=1594;

    public GameChatServer() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println(port + "포트 게임 챗 서버 대기중!");
            list = new ArrayList<GameChatHandler>();
            while (true) {
                Socket socket = serverSocket.accept();
                GameChatHandler handler = new GameChatHandler(socket, list);
                handler.start();
                list.add(handler);
            }
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        new GameChatServer();
    }
}
