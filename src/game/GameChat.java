package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import login.ChatSound;
import login.JDBCModel;
import waiting.Waiting;

public class GameChat {
	private Socket socket;
	private ObjectOutputStream writer;
	private ObjectInputStream reader;
	private Game game;
	private JTextArea chatLog;
	private JTextField chatInput;
	private String nickName;
	private JDBCModel model;
	private JButton logoutBtn, exitBtn;
	private JFrame frame;
	private static int gameNumber;
	private ChatSound cs;
	
	//private final String serverIP = "localhost";
	private final String serverIP = "14.138.202.117";
	private final int port = 1594;
	
	
	public GameChat(Game game) {
		//채팅 클라이언트를 호출한 WaitingRoom클래스로부터 입력부, 출력부, 닉네임, 모델, 보내기버튼, 프레임을 넘겨받는다.
		this.game = game;
		this.chatLog = game.getChatLog();
		this.chatInput = game.getChatInput();
		this.nickName = game.getNickName();
		this.model = game.getModel();
		this.frame = game.getFrame();
		
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
				GameChatDTO dto = new GameChatDTO();
				dto.setCommand(Info.JOIN);
				dto.setNickName(nickName);
				try {
					writer.writeObject(dto);
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				// 서버로 부터 받기
				GameChatDTO userDTO = null;
				while (true) {
					try {
						userDTO = (GameChatDTO) reader.readObject();
						cs = new ChatSound();
						System.out.println("서버에서 받았습니다");
						System.out.println(userDTO.getCommand());
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
							System.out.println("당신은 방장입니까" + game.getIsOwner());
							
							//방장이 아니면 리턴으로 탈출
							if((game.getIsOwner())) {
								System.out.println("방장 true 이후 진입은 하는거냐");
								System.out.println(game.getAnswer());
								if(!(game.getAnswer() == null) && !(game.getAnswer().length() == 0)) {
									System.out.println("null 검증은 통과하는거냐");
									//방장이면 정답 검증. 정답이면 정답과 유저이름, 정답신호를 설정하여 서버소켓으로 발송
									String answer = game.getAnswer();
									System.out.println(answer);
									String userGuess = userDTO.getMessage().substring(userDTO.getMessage().length()-answer.length());
									System.out.println(userGuess);
									
									if(answer.equals(userGuess)) {
										String answerUser = userDTO.getNickName();
										GameChatDTO answerDTO = new GameChatDTO();
										answerDTO.setNickName(answerUser);
										answerDTO.setMessage(answer);
										answerDTO.setCommand(Info.ANSWER);
										writer.writeObject(answerDTO);
										writer.flush();
										System.out.println("정답 보냈음");
										model.addScoreInGame(answerUser);
									}
								}
							}
						}else if (userDTO.getCommand()==Info.ANSWER) {
							String answerUser = userDTO.getNickName();
							String answer = userDTO.getMessage();
							new GameAnswer();
							JOptionPane.showMessageDialog(frame, answerUser + "님 정답입니다! : " + answer);
							model.ownerPassToAnswerUser(answerUser);
							game.getStartBtn().setEnabled(true);
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
		
		//채팅 입력 후 Enter 눌렀을 때
		chatInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chat();
				System.out.println("채팅 보냅니다~");
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
			GameChatDTO dto = new GameChatDTO();
			// infoDTO.setMessage(data);
			if (data.equals("/exit")) {
				dto.setCommand(Info.EXIT); // exit입력하면 종료
			} else {
				dto.setCommand(Info.SEND); // 그게아니면 다 메세지로 send
				dto.setMessage(data);
				dto.setNickName(nickName);
			}
			try {
				writer.writeObject(dto);
				writer.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			chatInput.setText("");
		}

		//X 눌렀을 때 작동할 메소드
		public void exit() {
			int sel = JOptionPane.showConfirmDialog(frame, "종료하시겠습니까?", "종료", JOptionPane.YES_NO_OPTION);
			if (!(sel == JOptionPane.YES_OPTION)) return;
			
			model.setOffline(model.getEmailByNickName(game.getNickName()));
			System.out.println("셋 오프라인 완료");
			GameChatDTO dto = new GameChatDTO();
			dto.setCommand(Info.EXIT);
			try {
				writer.writeObject(dto);
				writer.flush();
				writer.close();
				reader.close();
				socket.close();
				System.exit(0);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
		
		public void backToWaiting() {
			int sel = JOptionPane.showConfirmDialog(frame, "대기실로 나가시겠습니까?!");
			if(!(sel == JOptionPane.YES_OPTION)) return;
				GameChatDTO dto = new GameChatDTO();
				dto.setCommand(Info.EXIT);
				try {
					writer.writeObject(dto);
					writer.flush();
					new Waiting(frame, model.getEmailByNickName(nickName), model);
					frame.dispose();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		}
		
		
		
		
		
		
		//------------------------------------End of methods--------------------------------------

		
		
		public ObjectOutputStream getWriter() {return writer;}
		public ObjectInputStream getReader() {return reader;}
		public Socket getSocket() {return socket;}
		public void setNickName(String newNickName) {this.nickName = newNickName;} 
		
}
