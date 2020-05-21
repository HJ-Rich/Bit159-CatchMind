package waiting;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import game.Game;
import login.JDBCModel;
import login.Login;
import javax.swing.JComboBox;

public class Waiting {
	private JComboBox<String> chatCombo;
	private JButton enterB, makingB, logoutBtn, exitBtn, sendBtn, updateBtn, rankBtn;
	private JTable roomTable, userTable;
	private DefaultTableModel roomModel, userModel;
	private String nickname, receivedName;
	private JTextArea chatLog;
	private JTextField chatInput, myNickname, myLevel;
	private Socket socket;
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	private int score;
	private String email;
	private JProgressBar bar;
	private Login login;
	private JLabel loginGamer;
	private JDBCModel model;
	private JFrame frame, exFrame;
	private WaitingChat waitingChat;
	private WaitingBGM waitingBGM;
	// 생성자
	public Waiting(JFrame exFrame, String email, JDBCModel model) {
		
		//-------------------------------frame-------------------------------------
		this.exFrame = exFrame;
		this.email = email;
		this.model = model;
		
		frame = new JFrame();
		frame.setTitle("캐치마인드");
		frame.setSize(1024, 768);
		frame.setLocationRelativeTo(exFrame);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		frame.getContentPane().setBackground(SystemColor.inactiveCaption);
		
		JPanel statusP = new JPanel();
		statusP.setLayout(null);
		statusP.setBackground(new Color(211, 211, 211));
		statusP.setBorder(new LineBorder(Color.BLACK, 1));
		statusP.setBounds(706, 456, 285, 195);
		sendBtn = new JButton("보내기");
		sendBtn.setBounds(579, 659, 115, 22);

		// 대기방 리스트 테이블 =====================================================
		String[] roomField = { "NO", "제목", "방장", "인원", "비고" };
		roomModel = new DefaultTableModel(roomField, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		roomTable = new JTable(roomModel);
		roomTable.setRowHeight(40); // 테이블 행 높이 설정
		roomTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		roomTable.getColumnModel().getColumn(1).setPreferredWidth(400);
		roomTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		roomTable.getTableHeader().setReorderingAllowed(false);
		JScrollPane scroll1 = new JScrollPane(roomTable);
		scroll1.setBounds(22, 70, 672, 366);

		// 접속중인 유저 테이블 ======================================================
		String[] userField = { "닉네임", "레벨", "위치" };
		userModel = new DefaultTableModel(userField, 0) {
			@Override
			public boolean isCellEditable(int row, int column) { // 수정, 입력 불가
				return false;
			}
		};
		userTable = new JTable(userModel);
		userTable.setRowHeight(25);
//      table2.setAutoCreateRowSorter(true);
		userTable.getTableHeader().setReorderingAllowed(false);
		JScrollPane scroll2 = new JScrollPane(userTable);
		scroll2.setBounds(706, 70, 285, 366);
		scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		// 채팅창 및 채팅입력창=======================================================
		chatLog = new JTextArea();
		chatLog.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		chatLog.setEditable(false);
		chatLog.setBounds(22, 465, 653, 184);
		JScrollPane scroll3 = new JScrollPane(chatLog);
		scroll3.setBounds(22, 456, 672, 195);
		scroll3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		chatInput = new JTextField();
		chatInput.setBounds(125, 659, 450, 21);

		// 내 정보 관련 ===========================================================
		JLabel nameL = new JLabel("닉네임");
		nameL.setBounds(22, 51, 55, 28);
		statusP.add(nameL);

		nickname = model.showNickname(email);
		myNickname = new JTextField(nickname);
		myNickname.setHorizontalAlignment(SwingConstants.CENTER);
		myNickname.setEditable(false);
		myNickname.setBounds(89, 51, 119, 20);
		statusP.add(myNickname);

		JLabel levelL = new JLabel("레벨");
		levelL.setBounds(22, 106, 37, 15);
		statusP.add(levelL);

		score = model.getScore(email);
		int lev = (int) (score / 10);

		if (score == 0) {
			myLevel = new JTextField("신입생");
		} else
			myLevel = new JTextField(lev + "");

		myLevel.setHorizontalAlignment(SwingConstants.CENTER);

		myLevel.setEditable(false);
		myLevel.setBounds(89, 102, 119, 20);
		statusP.add(myLevel);

		JLabel winL = new JLabel("경험치");
		winL.setBounds(22, 150, 55, 15);
		statusP.add(winL);

		// 컨테이너에 각 패널,스크롤,버튼 올리기====================================
		Container c = frame.getContentPane();
		c.add(scroll1);
		c.add(scroll2);
		c.add(scroll3);
		c.add(chatInput);
		c.add(sendBtn);
		c.add(statusP);

		JLabel lblNewLabel = new JLabel("내 정보");
		lblNewLabel.setFont(new Font("굴림", Font.BOLD, 14));
		lblNewLabel.setBounds(113, 17, 57, 15);
		statusP.add(lblNewLabel);

		bar = new JProgressBar();
		bar.setToolTipText("");
		bar.setBounds(89, 150, 146, 15);
		if (score % 100 == 0) {
			bar.setValue(0);
		} else {
			bar.setValue(score % 100);
		}
		statusP.add(bar);

		loginGamer = new JLabel("접속자수: ");
		loginGamer.setBounds(708, 38, 87, 22);
		frame.getContentPane().add(loginGamer);
		updateBtn = new JButton("정보수정");
		updateBtn.setBounds(236, 25, 97, 36);
		frame.getContentPane().add(updateBtn);
		makingB = new JButton("방만들기");
		makingB.setBounds(125, 25, 97, 36);
		frame.getContentPane().add(makingB);
		
		rankBtn = new JButton("랭킹");
		rankBtn.setBounds(456, 25, 87, 36);
		frame.getContentPane().add(rankBtn);
		
		enterB = new JButton("참여하기");
		enterB.setBounds(22, 25, 91, 35);
		frame.getContentPane().add(enterB);
		logoutBtn = new JButton("로그아웃");
		logoutBtn.setBounds(818, 25, 87, 35);
		frame.getContentPane().add(logoutBtn);
		exitBtn = new JButton("종료");
		exitBtn.setBounds(910, 25, 81, 35);
		frame.getContentPane().add(exitBtn);
		JButton button = new JButton("쪽 지");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WaitingChatDTO messageDTO = new WaitingChatDTO();
				receivedName = JOptionPane.showInputDialog(frame, "닉네임을 입력해주세요", "쪽지보내기", JOptionPane.OK_CANCEL_OPTION);
				if(receivedName==null) return;
				
				if(model.isOnline(receivedName) == true) {
					JOptionPane.showMessageDialog(frame, "오프라인이거나 없는 유저입니다.");
					
				}else {
					String message = JOptionPane.showInputDialog(frame, "메시지 입력", "쪽지 보내기", JOptionPane.OK_CANCEL_OPTION);		
					messageDTO.setNickName(nickname);
					messageDTO.setCommand(Info.MESSAGE);
					messageDTO.setReceivedName(receivedName);
					messageDTO.setMessage(message);
					
					waitingChat.dtoWrite(messageDTO);		
					
				}
				
			}
		});
		button.setBounds(347, 25, 97, 36);
		frame.getContentPane().add(button);
		
		chatCombo = new JComboBox<String>();
		chatCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				if(chatCombo.getSelectedIndex() == 1) {
					receivedName = JOptionPane.showInputDialog(frame, "귓속말을 보낼 닉네임을 입력해주세요.", "귓속말");
					
					if(model.isOnline(receivedName) == true) {
						JOptionPane.showMessageDialog(frame, "오프라인이거나 없는 유저입니다.");
						return;
					}
											
					chatCombo.addItem(receivedName);
					chatCombo.setSelectedItem(receivedName);	
				
				}
				
			}
		});
		chatCombo.setBounds(22, 658, 89, 24);
		chatCombo.addItem("전체채팅");
		chatCombo.addItem("귓속말");
		frame.getContentPane().add(chatCombo);
		
		JPanel panel = new JPanel(){
			Image background = new ImageIcon("waiting.jpg").getImage();
			@Override
			public void paint(Graphics g) {
				g.drawImage(background, 0, 0, frame);      
			};
		};
		panel.setBounds(0,0,1024,768);
		frame.getContentPane().add(panel);
		
		waitingBGM = new WaitingBGM();
		waitingBGM.playBGM();
		

		frame.setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE);
		frame.setVisible(true);
		//-------------------------------End of Frame-------------------------------------


		roomTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					for (int index = 0; index < 2; index++) {
						if (roomTable.getSelectedRow() == index) {
							waitingBGM.stopBGM();
							waitingChat.join();
							
							String nickname = model.getNickNameByEmail(email);

							int no = 0;
							try{
								no = model.getEmptyRow();
							}catch(NoSuchElementException e2) {
								JOptionPane.showMessageDialog(frame, "풀방입니다");
								break;
							}
							model.joinGame(nickname, no);
							
							new Game(nickname, model, frame);
							frame.dispose();
						}
					}
					System.out.println("더블클릭");
				} else if (e.getClickCount() == 1) {
					System.out.println(roomTable.getSelectedRow() + "번째방");
					System.out.println();
					System.out.println(userTable.getRowCount() + "명 있음");
				}
			}
		});
		userTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3) {
					System.out.println("우클릭");
				}
			}
		});
		
		
		//---------------------------------Thread---------------------------------------------		
		
		//방 목록 보여주는 스레드
		Thread showRoom = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (!(frame.isVisible())) {
						break;
					}
					roomTable.setModel(model.getRooms());
					roomTable.getColumnModel().getColumn(0).setPreferredWidth(50);
					roomTable.getColumnModel().getColumn(1).setPreferredWidth(400);
					roomTable.getColumnModel().getColumn(2).setPreferredWidth(100);
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		showRoom.setDaemon(true);
		showRoom.start();
		
		
		//접속한 닉네임 리스트 보여주는 스레드
		Thread onlineNickNames = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (!(frame.isVisible())) {
						break;
					}
					userTable.setModel(model.online());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		onlineNickNames.setDaemon(true);
		onlineNickNames.start();
		
		
		//접속자수 표현해주는 스레드
		Thread isOnline = new Thread(new Runnable() {
			public void run() {
				while (true) {
					loginGamer.setText("접속자수: " + userTable.getRowCount());
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		isOnline.setDaemon(true);
		isOnline.start();
		
		//----------------------------End of Thread---------------------------------------------
		
		
		//채팅구현
		waitingChat = new WaitingChat(Waiting.this);
		
		
		//----------------------------------button actions--------------------------------------------
		
		//개인정보수정 버튼 눌렀을 때
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new UpdateNickName(Waiting.this);
			}
		});
		
		//방만들기 버튼
		makingB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CreateRoom(Waiting.this, model);
			}
		});
		
		
		//X 눌렀을 때
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				waitingChat.exit();
			}
		});
		
		//로그아웃 버튼 눌렀을 때
		logoutBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				waitingChat.logout();
			}
		});
		
		//게임 종료 버튼 눌렀을 때
		exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				waitingChat.exit();
			}
		});
		
		rankBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Ranking(Waiting.this);
			}
		});
	      enterB.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                waitingChat.join();
                    
                    String nickname = model.getNickNameByEmail(email);

                    int no = 0;
                    try{
                        no = model.getEmptyRow();
                    }catch(NoSuchElementException e2) {
                        JOptionPane.showMessageDialog(frame, "풀방입니다");
                        return;
                    }
                    model.joinGame(nickname, no);
                    
                    new Game(nickname, model, frame);
                    frame.dispose();
	            }
	        });
		//---------------------------------End of button actions--------------------------------------------
	      
	}
	//WaitingRoom Constructor


	
	
	
	//----------------------------------getter & setter---------------------------
	public String getNickname() {return nickname;}
	public void setNickname(String nickname2) {this.nickname = nickname2;}
	public DefaultTableModel getModel1() {return roomModel;}
	public void setModel1(DefaultTableModel model1) {this.roomModel = model1;}
	public JDBCModel getModel() {return model;}
	public JTextArea getChatLog() {return chatLog;}
	public JTextField getChatInput() {return chatInput;}
	public JTextField getMyNickName() {return myNickname;}
	public JButton getSendBtn() {return sendBtn;}
	public JFrame getFrame() {return frame;}
	public JButton getExitBtn() {return exitBtn;}
	public JButton getLogoutBtn() {return exitBtn;}
	public String getEmail() {return email;}
	public WaitingChat getWaitingRoomChat() {return waitingChat;}
	public WaitingBGM getWaitingBGM() {return waitingBGM;}
	public JComboBox<String> getChatCombo(){ return chatCombo;}
	public String getReceivedName() { return receivedName;}
	//----------------------------End of getter & setter---------------------
}
//WaitingRoom class