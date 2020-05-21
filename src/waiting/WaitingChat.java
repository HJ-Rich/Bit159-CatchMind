package waiting;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import login.ButtonSound;
import login.ChatSound;
import login.JDBCModel;
import login.Login;

public class WaitingChat {
	private Socket socket;
	private ObjectOutputStream writer;
	private ObjectInputStream reader;
	private Waiting waitingRoom;
	private JTextArea chatLog;
	private JTextField chatInput;
	private String nickName, receivedName;
	private JDBCModel model;
	private JButton sendBtn,logoutBtn, exitBtn;
	private JFrame frame;
	private ButtonSound bs;
	private ChatSound cs;
	private WaitingBGM waitingBGM;
	private JComboBox<String> chatCombo;
	
	//private final String serverIP = "localhost";
	private final String serverIP = "14.138.202.117";
	private final int port = 1592;
	
	
	public WaitingChat(Waiting waitingRoom) {
		
		//채팅 클라이언트를 호출한 WaitingRoom클래스로부터 입력부, 출력부, 닉네임, 모델, 보내기버튼, 프레임을 넘겨받는다.
		this.waitingRoom = waitingRoom;
		this.chatLog = waitingRoom.getChatLog();
		this.chatInput = waitingRoom.getChatInput();
		this.nickName = waitingRoom.getMyNickName().getText();
		this.model = waitingRoom.getModel();
		this.sendBtn = waitingRoom.getSendBtn();
		this.frame = waitingRoom.getFrame();
		this.exitBtn = waitingRoom.getExitBtn();
		this.logoutBtn = waitingRoom.getLogoutBtn();
		this.waitingBGM = waitingRoom.getWaitingBGM();
		this.chatCombo = waitingRoom.getChatCombo();
		this.receivedName = waitingRoom.getReceivedName();
		
		//서버 연결부
		try {
			socket = new Socket(serverIP, port);
			writer = new ObjectOutputStream(socket.getOutputStream());
			reader = new ObjectInputStream(socket.getInputStream());
		} catch (UnknownHostException e) {
			System.out.println("서버를 찾을 수 없습니다");
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			System.out.println("서버와 연결이 되지 않았습니다");
			e.printStackTrace();
			System.exit(0);
		}

		// 스레드생성
		Thread chattingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// 최초 1회 서버로 닉네임 보내기
				WaitingChatDTO dto = new WaitingChatDTO();
				dto.setCommand(Info.JOIN);
				dto.setNickName(nickName);
				try {
					writer.writeObject(dto);
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				// 서버로 부터 받기
				WaitingChatDTO userDTO = null;
				while (true) {
					try {
						userDTO = (WaitingChatDTO) reader.readObject();
						cs = new ChatSound();
						if (userDTO.getCommand() == Info.EXIT) {
							reader.close();
							writer.close();
							socket.close();
							System.out.println(nickName + "은 EXIT을 수신하여 클라이언트를 종료합니다");
							break;
						} else if (userDTO.getCommand() == Info.SEND) {
							chatLog.append(userDTO.getMessage() + "\n"); // 그게아니면 area에 받은값 출력
							int pos = chatLog.getText().length();
							chatLog.setCaretPosition(pos);// 텍스트 길어져도 새 입력값 화면보여주기
						} else if(userDTO.getCommand() == Info.MESSAGE) {
							JOptionPane.showMessageDialog(frame, userDTO.getMessage(), userDTO.getNickName()+"님의 쪽지", JOptionPane.INFORMATION_MESSAGE);
							
						} else if(userDTO.getCommand() == Info.WHISPER) {
							chatLog.append(userDTO.getMessage() + "\n");
							//chatLog.append(userDTO.getSendWhisper() + "\n");

							int pos = chatLog.getText().length();
							chatLog.setCaretPosition(pos);
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		});
		chattingThread.setDaemon(true);
		chattingThread.start();
		
		//--------------------------------------button actions-------------------------
		//보내기 버튼 눌렀을 때
		sendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chat();
			}
		});
		
		//채팅 입력 후 Enter 눌렀을 때
		chatInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chat();
			}
		});
		
		//---------------------------------End of button actions-------------------------
		
	}
	//WaitingRoomChat Constructor
	
	//---------------------------------------methods--------------------------------------
	
	//chatInput 또는 sendBtn 누를 때 작동할 메소드
		public void chat() {
			String data = chatInput.getText();
			if (data == null || data.length() == 0) {
				return;
			}
			WaitingChatDTO dto = new WaitingChatDTO();
			// infoDTO.setMessage(data);
			if (data.equals("/exit")) {
				dto.setCommand(Info.EXIT); // exit입력하면 종료
			} else if(chatCombo.getSelectedIndex() == 0) {
				dto.setCommand(Info.SEND); // 그게아니면 다 메세지로 send
				dto.setMessage(data);
				dto.setNickName(nickName);
				
			} else if(chatCombo.getSelectedIndex() >= 2) {
				dto.setCommand(Info.WHISPER);
				dto.setNickName(nickName);
				dto.setReceivedName(chatCombo.getItemAt(chatCombo.getSelectedIndex()));
				
				dto.setMessage(data);
				
			}
			try {
				writer.writeObject(dto);
				writer.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			chatInput.setText("");
		}
		
		//logoutBtn 눌렀을 때 작동할 메소드
		public void logout() {
			bs = new ButtonSound();
			int sel = JOptionPane.showConfirmDialog(frame, "로그아웃 하시겠습니까?", "로그아웃", JOptionPane.YES_NO_OPTION);
			if (sel != JOptionPane.YES_OPTION) {
				bs = new ButtonSound();
				return;
			}
			waitingBGM.stopBGM();
			bs = new ButtonSound();
			WaitingChatDTO dto = new WaitingChatDTO();
			dto.setCommand(Info.EXIT);
			try {
				writer.writeObject(dto);
				writer.flush();
				model.setOffline(model.getEmailByNickName(nickName));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			frame.dispose();
			new Login();
		}
		
		//exitBtn 눌렀을 때 작동할 메소드
		public void exit() {
			bs = new ButtonSound();
			int sel = JOptionPane.showConfirmDialog(frame, "종료하시겠습니까?", "종료", JOptionPane.YES_NO_OPTION);
			if (!(sel == JOptionPane.YES_OPTION)) {
				bs = new ButtonSound();
				return;
			}
			bs = new ButtonSound();			
			model.setOffline(model.getEmailByNickName(nickName));
			
			try {
				WaitingChatDTO dto = new WaitingChatDTO();
				dto.setCommand(Info.EXIT);
				writer.writeObject(dto);
				writer.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			System.exit(0);
		}
		
		public void join() {
			bs = new ButtonSound();
			WaitingChatDTO dto = new WaitingChatDTO();
			dto.setCommand(Info.EXIT);
			try {
				writer.writeObject(dto);
				writer.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		public void dtoWrite(WaitingChatDTO dto) { //=======================================================추가
			try {
				writer.writeObject(dto);
				writer.flush();
				
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
		//------------------------------------End of methods--------------------------------------

		
		
		
		public void setNickName(String newNickName) {this.nickName = newNickName;} 
		
		
}
//WaitingRoomChat class














