package login;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class LoginChatHandler extends Thread {
	private BufferedReader reader;
	private PrintWriter writer;
	private Socket socket;
	private List<LoginChatHandler> list;

	public LoginChatHandler(Socket socket, List<LoginChatHandler> list) throws IOException {
		this.socket = socket;
		this.list = list; 
 
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		
		this.setDaemon(true);
	}

	//핸들러의 스레드는 1:1관계에서 받은 닉네임과 메시지를 리스트 내의 모든 클라이언트에게 반사해주는 역할이다.
	public void run(){
		
		
		try{
			
			//1:1로 연결된 후 최초 수신된 메시지는 닉네임 값 하나다. 거기에 입장이라는 안내문구를 덧대어 broadcast 한다.
			String nickName = reader.readLine();
			broadcast("["+nickName+"]; 님 입장");
			
			String line;
			
			//입장 이후엔 계속해서 닉네임과, 받은 메시지를 뿌린다
			while(true){
				//보내온 메시지를 읽어서 구분자로 끊어서 배열로 담아둔다.
				line = reader.readLine();
				String[] rmsg = line.split(";");
				
				
				//exit을 받았을 경우
				if(line==null || line.equals("exit")){
					//exit을 리턴한다. broadcast가 아닌 점 유의.
					//클라이언트는 이 핸들러가 보낸 exit를 받았을 경우 소켓을 닫고 종료한다.
					//서버는 먼저 받았으니 보내고 종료하고, 클라이언트는 닉네임을 먼저 보냈으니 받고 종료한다. TCP 동기. 
					writer.println("exit");
					writer.flush();
					
					//1. 클라가 exit를 보냄.
					//2. 서버가 exit를 받음.
					//3. 서버가 1의 클라에게 exit를 보내고 소켓을 close한다.
					//4. 서버가 1의 클라를 리스트에서 제거하고 나머지 리스트들에게 1의 클라가 퇴장했음을 broadcast하고 break로 스레드 종료
					//5. 1의 클라가 exit를 받고 소켓을 close하고 종료한다.

					//1:1관계인 클라이언트에게 exit를 받고 exit를 보낸 후 클로즈한다.
					reader.close();
					writer.close();
					socket.close();
					
					
					list.remove(LoginChatHandler.this);
					//여기서 broadcast는 클라이언트와 1:1로 주고받고 있는 서버측의 소켓인데,
					//각 서버측의 소켓이 들고있는 메소드를 통해 broadcast하기 때문에, exit을 보내온 클라와 연결된 서버측의 소켓은
					//close해도 메시지를 보내는데 문제가 없다.
					broadcast("["+nickName+"]; 님 퇴장");
					break;
				}//if
				
				broadcast("["+rmsg[0]+"] "+ ";"+rmsg[1]);
			}//while

		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void broadcast(String msg){
		for(LoginChatHandler handler : list){
			handler.writer.println(msg);
			handler.writer.flush();
		}//for
	}
}
