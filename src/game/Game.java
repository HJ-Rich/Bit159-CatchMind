package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import login.ButtonSound;
import login.JDBCModel;
import waiting.Waiting;

public class Game{
	
	private JFrame frame;
	private Canvas canvas;
	private JButton blackBtn, redBtn, greenBtn, blueBtn, yellowBtn, eraserBtn, clearBtn, exitBtn, startBtn;
	private JSlider slider;
	private PaintCanvas paintCanvas;
	private JLabel timerLabel, nickname1, grade1, score1, nickname2, grade2, score2, nickname3, grade3, score3, nickname4, grade4, score4;
	private Thread timer, nickname_and_score_Setter, startThread;
	private double i, j;
	private JDBCModel model;
	private JTextField chatInput;
	private JTextArea chatLog;
	private GameChat gameChat;
	private String nickName, answer;
	private boolean isowner;
	private GameBGM gameBGM;
	private static int gameNumber;
	private ButtonSound bs;
	
	
	
	public Game(String nickName, JDBCModel model, JFrame exFrame) {
		this.nickName = nickName;
		this.model = model;
		gameBGM = new GameBGM();
		gameBGM.playBGM();
		
		
		
		
		ImageIcon blackBtnIcon = new ImageIcon("blackBtnIcon.png");
		ImageIcon redBtnIcon = new ImageIcon("redBtnIcon.png");
		ImageIcon greenBtnIcon = new ImageIcon("greenBtnIcon.png");
		ImageIcon blueBtnIcon = new ImageIcon("blueBtnIcon.png");
		ImageIcon yellowBtnIcon = new ImageIcon("yellowBtnIcon.png");
		ImageIcon eraserBtnIcon = new ImageIcon("eraserBtnIcon.png");
		ImageIcon clearBtnIcon = new ImageIcon("clearBtnIcon.png");
		ImageIcon exitBtnIcon = new ImageIcon("exitBtnIcon.png");
		ImageIcon startBtnIcon = new ImageIcon("startBtnIcon.jpg");
		//---------------------------frame------------------------------
		frame = new JFrame("게임방");
		frame.setBounds(500,100,1024,768);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(exFrame);
		
		startBtn = new JButton(startBtnIcon);
		startBtn.setBounds(846, 367, 110, 36);
		frame.getContentPane().add(startBtn);
		
		timerLabel = new JLabel("");
		timerLabel.setBounds(222, 596, 133, 36);
		frame.getContentPane().add(timerLabel);
		
		blackBtn = new JButton(blackBtnIcon);
		blackBtn.setBounds(272, 546, 42, 70);
		frame.getContentPane().add(blackBtn);
		
		redBtn = new JButton(redBtnIcon);
		redBtn.setBounds(326, 546, 42, 70);
		frame.getContentPane().add(redBtn);
		
		greenBtn = new JButton(greenBtnIcon);
		greenBtn.setBounds(380, 546, 42, 70);
		frame.getContentPane().add(greenBtn);
		
		blueBtn = new JButton(blueBtnIcon);
		blueBtn.setBounds(434, 546, 42, 70);
		frame.getContentPane().add(blueBtn);
		
		yellowBtn = new JButton(yellowBtnIcon);
		yellowBtn.setBounds(488, 546, 42, 70);
		frame.getContentPane().add(yellowBtn);
		
		eraserBtn = new JButton(eraserBtnIcon);
		eraserBtn.setBounds(584, 578, 42, 38);
		frame.getContentPane().add(eraserBtn);
		
		clearBtn = new JButton(clearBtnIcon);
		clearBtn.setBounds(638, 566, 112, 50);
		frame.getContentPane().add(clearBtn);
		
		exitBtn = new JButton(exitBtnIcon);
		exitBtn.setBounds(894, 10, 106, 43);
		frame.getContentPane().add(exitBtn);
		
		slider = new JSlider();
		slider.setBackground(new Color(0, 153, 204));
		slider.setBounds(570, 527, 180, 36);
		frame.getContentPane().add(slider);
		
		paintCanvas = new PaintCanvas(Game.this);
		canvas = paintCanvas.getCanvas(); 
		canvas.setBounds(250, 157, 516, 345);
		frame.getContentPane().add(canvas);
		
		canvas.addMouseListener(new MouseAdapter() {         
	         @Override
	         public void mouseEntered(MouseEvent e) {
	        	if(!isowner) return;
	            Toolkit tk = Toolkit.getDefaultToolkit();
	            Image cursorimage=tk.getImage("pencilcursor.png");
	            Point point=new Point(0,0);
	            Cursor cursor=tk.createCustomCursor(cursorimage, point, "GG");
	            canvas.setCursor(cursor);
	         }
	      });

		nickname1 = new JLabel("");
		nickname1.setHorizontalAlignment(SwingConstants.CENTER);
		nickname1.setBounds(38, 157, 94, 26);
		frame.getContentPane().add(nickname1);
		
		grade1 = new JLabel("");
		grade1.setHorizontalAlignment(SwingConstants.CENTER);
		grade1.setBounds(38, 182, 94, 26);
		frame.getContentPane().add(grade1);
		
		score1 = new JLabel("");
		score1.setHorizontalAlignment(SwingConstants.CENTER);
		score1.setBounds(39, 207, 94, 26);
		frame.getContentPane().add(score1);
		
		nickname2 = new JLabel("");
		nickname2.setHorizontalAlignment(SwingConstants.CENTER);
		nickname2.setBounds(806, 154, 94, 26);
		frame.getContentPane().add(nickname2);
		
		grade2 = new JLabel("");
		grade2.setHorizontalAlignment(SwingConstants.CENTER);
		grade2.setBounds(806, 178, 94, 26);
		frame.getContentPane().add(grade2);
		
		score2 = new JLabel("");
		score2.setHorizontalAlignment(SwingConstants.CENTER);
		score2.setBounds(806, 202, 94, 26);
		frame.getContentPane().add(score2);
		
		nickname3 = new JLabel("");
		nickname3.setHorizontalAlignment(SwingConstants.CENTER);
		nickname3.setBounds(34, 269, 94, 26);
		frame.getContentPane().add(nickname3);
		
		grade3 = new JLabel("");
		grade3.setHorizontalAlignment(SwingConstants.CENTER);
		grade3.setBounds(34, 294, 94, 26);
		frame.getContentPane().add(grade3);
		
		score3 = new JLabel("");
		score3.setHorizontalAlignment(SwingConstants.CENTER);
		score3.setBounds(34, 318, 94, 26);
		frame.getContentPane().add(score3);
		
		nickname4 = new JLabel("");
		nickname4.setHorizontalAlignment(SwingConstants.CENTER);
		nickname4.setBounds(806, 266, 94, 26);
		frame.getContentPane().add(nickname4);
		
		grade4 = new JLabel("");
		grade4.setHorizontalAlignment(SwingConstants.CENTER);
		grade4.setBounds(806, 290, 94, 26);
		frame.getContentPane().add(grade4);
		
		score4 = new JLabel("");
		score4.setHorizontalAlignment(SwingConstants.CENTER);
		score4.setBounds(806, 314, 94, 26);
		frame.getContentPane().add(score4);
		
		chatInput = new JTextField();
		chatInput.setBounds(798, 624, 210, 26);
		frame.getContentPane().add(chatInput);
		chatInput.setColumns(10);
		
		chatLog = new JTextArea();
		JScrollPane scroll = new JScrollPane(chatLog);
		scroll.setBounds(798, 413, 210, 205);
		frame.getContentPane().add(scroll);
		
		
		frame.setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE);
		//---------------------------End of frame------------------------------
		
		
		//---------------------------button actions----------------------------
		
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				bs = new ButtonSound();
				int sel = JOptionPane.showConfirmDialog(frame, "게임 종료?");
				if(!(sel == JOptionPane.YES_OPTION)) {
					bs = new ButtonSound();
					return; 
				}
				bs = new ButtonSound();
				
				
				model.addScoreWhenExitRoom(nickName);
				int no = model.exitScoreReset(nickName);
	
				System.out.println("점수 추가 완료");
				
				//게임 종료 프로세스 이므로 db에서 본인의 isonline 값을 0으로 변경
				model.setOffline(model.getEmailByNickName(nickName));
				//room1593 테이블에서 본인의 정보를 삭제
				model.exitGame(nickName);
				
				//본인이 방장인 경우 room1593 테이블에서 본인이 있던 row의 isOwner 값을 0으로 변경
				if(model.isOwner(nickName)) {
					System.out.println("방장님 퇴장합니당");
					model.ownerExit(nickName);
					
					//새 방장 부여하기
					
					//1. 방에 남은 사람 없으면 1행에 isOwner 1값 부여
					if(model.isEmptyRoom()) {
						model.setOwner(1);
					}else {
					//2. 방에 남은 사람 있으면 가장 낮은 no값을 가진 사람에게 isOwner 1 부여  
						no = model.getFilledRow();
						model.setOwner(no);
					}
				}

				//채팅 서버 종료하기
				GameChatDTO gameChatDTO = new GameChatDTO();
				gameChatDTO.setCommand(Info.EXIT);
				try {
					gameChat.getWriter().writeObject(gameChatDTO);
					gameChat.getWriter().flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				//그림 서버 종료하기
				PaintDTO paintDTO = new PaintDTO();
				paintDTO.setSignal(3);
				try {
					paintCanvas.getWriter().writeObject(paintDTO);
					paintCanvas.getWriter().flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});
		
		
		exitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bs = new ButtonSound();
				int sel = JOptionPane.showConfirmDialog(frame, "대기실로 나가시겠습니까?!!!!!!!!");
				if(!(sel == JOptionPane.YES_OPTION)) {
					bs = new ButtonSound();
					return;
				}
				
				model.addScoreWhenExitRoom(nickName);
				int no = model.exitScoreReset(nickName);
				
				gameBGM.stopBGM();
				bs = new ButtonSound();
				System.out.println("점수 추가 완료");
				//혼자 있는 방이라면(무조건 본인이 방장)
				//자신의 방장값을 0으로 주고 1행에 방장값1 주고 room1593테이블에서 본인 삭제 
				if(model.isEmptyRoom()) {
					System.out.println("혼자네요");
					model.ownerExit(nickName);
					System.out.println("오너엑싯 수행");
					model.setOwner(1);
					System.out.println("1행에 오너 부여");
					model.exitGame(nickName);
					System.out.println("db에서 나가기");
				}
				
				//혼자 있는 방 아님 + 방장 | 다른이에게 방장 이양
				if(model.isOwner(nickName)) {
					model.ownerExit(nickName);
					model.exitGame(nickName);
					no = model.getFilledRow();
					System.out.println(no + "번에게 방장 이양합니다");
					model.setOwner(no);
					System.out.println("방장 이양 완료");
				}else model.exitGame(nickName);
				
				
				//채팅 서버 종료하기
				GameChatDTO gameChatDTO = new GameChatDTO();
				gameChatDTO.setCommand(Info.EXIT);
				try {
					gameChat.getWriter().writeObject(gameChatDTO);
					gameChat.getWriter().flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				//그림 서버 종료하기
				PaintDTO paintDTO = new PaintDTO();
				paintDTO.setSignal(3);
				try {
					paintCanvas.getWriter().writeObject(paintDTO);
					paintCanvas.getWriter().flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				new Waiting(frame, model.getEmailByNickName(nickName), model);
				frame.dispose();
			}
		});
		
		
		
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bs = new ButtonSound();
				answer = model.randomAnswer();
				if(isowner) {
					JOptionPane.showMessageDialog(frame, "출제하실 문제는 : " + answer);
				}
				startBtn.setEnabled(false);
			}
		});
		
		
		
		//닉네임과 이 방에서 맞춘 점수 갯수 나타내는 스레드
		Thread nickname_and_score_Setter = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					model.displayForRoom(1593, nickname1, nickname2, nickname3, nickname4, score1, score2, score3, score4);
					model.setGrade(nickname1.getText(), grade1);
					model.setGrade(nickname2.getText(), grade2);
					model.setGrade(nickname3.getText(), grade3);
					model.setGrade(nickname4.getText(), grade4);
					
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						System.out.println("닉넴 앤 스코어 세터 인터럽트 되어 종료합니다");
					}
				}
			}
		});
		nickname_and_score_Setter.setDaemon(true);
		nickname_and_score_Setter.start();

		//개인별 경험치를 프로그래스바에 나타내는 스레드
		Thread barSetter = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					//model.displayEXP(nickname1, nickname2, nickname3, nickname4);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						System.out.println("바 세터 인터럽트 되어 종료합니다");
					}
				}
				
			}
		});
		barSetter.setDaemon(true);
		//barSetter.start();
		
		Thread startThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (model.isOwner(nickName)) {
						startBtn.setBounds(846, 367, 110, 36);
						isowner = true;
						chatInput.setEnabled(false);
					}else {
						startBtn.setBounds(-200, -100, 110, 36);
						isowner = false;
						chatInput.setEnabled(true);
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						System.out.println("스타트 스레드 인터럽트 되어 종료합니다");
					}
				}
			}
		});
		startThread.setDaemon(true);
		startThread.start();

		gameChat = new GameChat(Game.this);
		
		
		
		JPanel panel = new JPanel(){
			Image background = new ImageIcon("gamebackground.png").getImage();
			@Override
			public void paint(Graphics g) {
				g.drawImage(background, 0, 0, frame);		
			};
		};
		panel.setBounds(-5,5,1024,768);
		frame.getContentPane().add(panel);
		
		frame.setResizable(false);
		frame.setVisible(true);
		
		
		
		
		
		frame.getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println(e.getX() + ", " + e.getY());
			}
		});
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	//GamePlayRoom Constructor
	
	
	//---------------------getter & setter-----------------------
	public JFrame getFrame() {return frame;}
	public Canvas getCanvas() {return canvas;}
	public JButton getBlackBtn() {return blackBtn;}
	public JButton getRedBtn() {return redBtn;}
	public JButton getGreenBtn() {return greenBtn;}
	public JButton getBlueBtn() {return blueBtn;}
	public JButton getYellowBtn() {return yellowBtn;}
	public JButton getEraserBtn() {return eraserBtn;}
	public JButton getClearBtn() {return clearBtn;}
	public JButton getExitBtn() {return exitBtn;}
	public JSlider getSlider() {return slider;}
	public PaintCanvas getPaintCanvas() {return paintCanvas;}
	public JTextArea getChatLog() {return chatLog;}
	public JTextField getChatInput() {return chatInput;}
	public String getNickName() {return nickName;}
	public JDBCModel getModel() {return model;}
	public boolean getIsOwner() {return isowner;}
	public String getAnswer() {return answer;}
	public JButton getStartBtn() {return startBtn;}
	public int getGameNumber() {return gameNumber;}
	
	
	
}