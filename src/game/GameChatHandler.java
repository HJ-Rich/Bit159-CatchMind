package game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class GameChatHandler extends Thread {
	   private ObjectInputStream reader;
	    private ObjectOutputStream writer;
	    private Socket socket;
	    private List<GameChatHandler> list;
	  
	    
	    public GameChatHandler(Socket socket, List<GameChatHandler> list) {
	          this.socket=socket;
	          this.list=list;         
	          try {
	              writer=new ObjectOutputStream(socket.getOutputStream());
	              reader=new ObjectInputStream(socket.getInputStream());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public void run() {      
	        GameChatDTO dto=null;
	        String nickName=null;
	            //클라이언트로부터 받기
	            //닉네임을 먼저 받는다.
	        try {
	            while(true) {
	                dto=(GameChatDTO)reader.readObject();
	                System.out.println("메시지 수신했음");
	                System.out.println(dto.getCommand());
	                if(dto.getCommand()==Info.JOIN) { //입장시
	                    nickName=dto.getNickName();
	                    GameChatDTO sendDTO = new GameChatDTO();
	                    sendDTO.setCommand(Info.SEND);
	                    sendDTO.setMessage(nickName+"님 입장하셨습니다");
	                    broadcast(sendDTO);
	                    
	                }else if(dto.getCommand()==Info.EXIT) { //종료시
	                    GameChatDTO sendDTO = new GameChatDTO();
	                    //나가려고 exit를 보낸 클라이언트에게 답변 보내기
	                    sendDTO.setCommand(Info.EXIT);
	                    writer.writeObject(sendDTO);
	                    writer.flush();
	                    
	                    reader.close();
	                    writer.close();
	                    socket.close();
	                    //남아있는 클라이언트에게 퇴장메세지 보내기
	                    list.remove(this);
	                    
	                    sendDTO.setCommand(Info.SEND);
	                    sendDTO.setMessage(nickName+"님 퇴장하셨습니다");
	                    
	                    broadcast(sendDTO);                    
	                    break;
	                    
	                }else if(dto.getCommand()==Info.SEND) {
	                	System.out.println("일반 메시지 수신");
	                    GameChatDTO sendDTO = new GameChatDTO();
	                    sendDTO.setCommand(Info.SEND);
	                    sendDTO.setNickName(dto.getNickName());
	                    String msg =dto.getMessage();
	                    nickName = dto.getNickName();
	                    sendDTO.setMessage("["+nickName+"]: "+msg);  
	                    
	                    broadcast(sendDTO);
	                    System.out.println("일반메시지 브로드캐스트 완료");
	                }else if (dto.getCommand() == Info.ANSWER) {
	                	System.out.println("서버소켓 정답 딱지 받음");
	                	broadcast(dto);
	                	System.out.println("서버소켓 정답 딱지 보냄");
	                }
	            }//while          
	        }catch(IOException e) {
	            e.printStackTrace();
	        }catch(ClassNotFoundException e1) {
	            e1.printStackTrace();
	        }    
	    }
	    public void broadcast(GameChatDTO dto) {
	        for(GameChatHandler cho : list) {
	            try {
	                cho.writer.writeObject(dto);
	                cho.writer.flush();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            
	        }
	    }
}
