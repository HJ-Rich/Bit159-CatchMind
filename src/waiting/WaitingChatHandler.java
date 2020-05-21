package waiting;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class WaitingChatHandler extends Thread{ //핸들러가 소켓,IO(reader,writer) 를 쥐고있음.
    private ObjectInputStream reader;
    private ObjectOutputStream writer;
    private Socket socket;
    private List<WaitingChatHandler> list;
    private String nickName, receivedName;
  
    
    public WaitingChatHandler(Socket socket, List<WaitingChatHandler> list) {
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
        WaitingChatDTO dto=null;
        nickName=null;
            //클라이언트로부터 받기
            //닉네임을 먼저 받는다.
        try {
            while(true) {
                dto=(WaitingChatDTO)reader.readObject();
                
                if(dto.getCommand()==Info.JOIN) { //입장시
                    nickName=dto.getNickName();
                    WaitingChatDTO sendDTO = new WaitingChatDTO();
                    sendDTO.setCommand(Info.SEND);
                    sendDTO.setMessage(nickName+"님 입장하셨습니다");
                    broadcast(sendDTO);
                    
                }else if(dto.getCommand()==Info.EXIT) { //종료시
                    WaitingChatDTO sendDTO = new WaitingChatDTO();
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
                    WaitingChatDTO sendDTO = new WaitingChatDTO();
                    sendDTO.setCommand(Info.SEND);
                    String msg =dto.getMessage();
                    nickName = dto.getNickName();
                    sendDTO.setMessage("["+nickName+"]: "+msg);  
                    
                    broadcast(sendDTO);
                    
                }else if(dto.getCommand()==Info.MESSAGE) {
                	messageCast(dto);
                	
                }else if(dto.getCommand()==Info.WHISPER) {    
                	WaitingChatDTO sendDTO = new WaitingChatDTO();
                	receivedName =  dto.getReceivedName();
                    sendDTO.setMessage("["+nickName+ " -> " + receivedName + "]  : "+ dto.getMessage());                	
                    sendDTO.setCommand(Info.SEND);
                	sendDTO.setReceivedName(receivedName);
                	
                    whisper(sendDTO);
                    
                }
            }//while          
        }catch(IOException e) {
            e.printStackTrace();
        }catch(ClassNotFoundException e1) {
            e1.printStackTrace();
        }    
    }
    public void broadcast(WaitingChatDTO dto) {
        for(WaitingChatHandler cho : list) {
            try {
                cho.writer.writeObject(dto);
                cho.writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
    }
    
    public void messageCast(WaitingChatDTO dto) {
    	for(WaitingChatHandler handler : list) {
    		if(handler.nickName.equalsIgnoreCase(dto.getReceivedName())){
    			try {
					handler.writer.writeObject(dto);
					handler.writer.flush();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
    			
    		}
    			
    	}
    }
    
    public void whisper(WaitingChatDTO dto) {
    	for(WaitingChatHandler handler : list) {
    		if(handler.nickName.equalsIgnoreCase(dto.getReceivedName())){ 
    			try {
					handler.writer.writeObject(dto);
					handler.writer.flush();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
    			
    		}else if(handler.nickName.equalsIgnoreCase(this.nickName)) {
    			try {
					handler.writer.writeObject(dto);
					handler.writer.flush();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
    			
    	}
    }
  
}//ChatHandler
