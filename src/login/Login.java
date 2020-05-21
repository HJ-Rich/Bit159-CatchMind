package login;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import waiting.Waiting;

public class Login implements KeyListener{
	private JLabel idLabel, pwLabel;
	private JTextField inputID;
	private JPasswordField inputPW;
	private JButton loginBtn, signUpBtn, lostIDBtn, lostPWBtn, updateBtn; 
	private JFrame frame;
	private JDBCModel model;
	private JTextField inputChat;
	private JTextArea chatLog;
	private JButton exitBtn;
	private LoginChat loginChat;
	private String tempNickName;
	private LoginBGM loginBGM;
	private ButtonSound bs;


	public Login() {
		
		model = new JDBCModel();
		frame = new JFrame("Catch Mind");
		
		
		//-------------------------frame design----------------------------
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		lostIDBtn = new JButton("이메일 찾기");
		lostIDBtn.setBounds(331, 596, 180, 50);
		frame.getContentPane().add(lostIDBtn);
		loginBtn = new JButton("로그인");
		loginBtn.setFocusable(false);
		loginBtn.setBounds(636, 416, 96, 90);
		frame.getContentPane().add(loginBtn);
		signUpBtn = new JButton("회원가입");
		signUpBtn.setBounds(331, 536, 180, 50);
		frame.getContentPane().add(signUpBtn);
		lostPWBtn = new JButton("비밀번호 찾기");
		lostPWBtn.setBounds(523, 596, 180, 50);
		frame.getContentPane().add(lostPWBtn);
		idLabel = new JLabel("이메일 주소");
		idLabel.setBounds(319, 416, 88, 40);
		frame.getContentPane().add(idLabel);
		idLabel.setFont(new Font("굴림", Font.BOLD, 15));
		pwLabel = new JLabel("\t\t\t비밀번호");
		pwLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		pwLabel.setBounds(319, 466, 78, 40);
		frame.getContentPane().add(pwLabel);
		pwLabel.setFont(new Font("굴림", Font.BOLD, 15));
		inputPW = new JPasswordField();
		inputPW.setBounds(424, 466, 200, 40);
		frame.getContentPane().add(inputPW);
		inputPW.setFont(new Font("굴림", Font.BOLD, 15));
		inputID = new JTextField();
		inputID.setBounds(424, 416, 200, 40);
		frame.getContentPane().add(inputID);
		inputID.setFont(new Font("굴림", Font.BOLD, 15));
		updateBtn = new JButton("회원정보수정");
		updateBtn.setBounds(523, 536, 180, 50);
		frame.getContentPane().add(updateBtn);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(1024,768);
		frame.setLocation(300,50);

		
		inputChat = new JTextField();
		inputChat.setBounds(12, 673, 248, 21);
		frame.getContentPane().add(inputChat);
		chatLog = new JTextArea();
		chatLog.setEditable(false);
		chatLog.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(chatLog);
		scrollPane.setBounds(12, 416, 248, 252);
		frame.getContentPane().add(scrollPane);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		exitBtn = new JButton("게임 종료");
		exitBtn.setBounds(865, 21, 129, 40);
		frame.getContentPane().add(exitBtn);
		
		//------------------------button Actions---------------------
		
		//X 눌렀을 때
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});
		
		//게임종료 눌렀을 때
		exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});
		
		//비번치고 엔터 눌렀을 떄
		inputPW.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loginVerifier(inputID.getText(), new String(inputPW.getPassword()));
			}
		});
		
		//로그인 클릭했을 때
		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loginVerifier(inputID.getText(), new String(inputPW.getPassword()));
			}
		});
		
		//회원가입 눌렀을 때
		signUpBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnDisabler();
				new SignUp(Login.this);
			}
		});
		
		//회원정보수정 눌렀을 때
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDisabler();
				new UpdateLogin(Login.this);
			}
		});
		
		//이메일 찾기 눌렀을 때
		lostIDBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnDisabler();
				new LostEmail(Login.this);
			}
		});
		
		//비번 찾기 눌렀을 때
		lostPWBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnDisabler();
				new LostPW(Login.this);
			}
		});
		
		//-----------------------test id & Key Listener----------------------------

		inputID.addKeyListener(this);
		inputPW.addKeyListener(this);
		loginBtn.addKeyListener(this);
		signUpBtn.addKeyListener(this);
		updateBtn.addKeyListener(this);
		lostIDBtn.addKeyListener(this);
		lostPWBtn.addKeyListener(this);
		inputChat.addKeyListener(this);
		
		JPanel panel = new JPanel(){
			Image background = new ImageIcon("login.png").getImage();
			@Override
			public void paint(Graphics g) {
				g.drawImage(background, 0, 0, frame);		
			};
		};
		panel.setBounds(0,-29,1024,768);
		frame.getContentPane().add(panel);
		frame.setVisible(true);
		
		//------------------------Login Chat-------------------------
		tempNickName = JOptionPane.showInputDialog(frame, "채팅용 닉네임을 입력해주세요");
		if(tempNickName == null || tempNickName.length() == 0) {
			tempNickName = "guest";
		}
		bs = new ButtonSound();
		loginChat = new LoginChat(tempNickName, Login.this);
		loginBGM = new LoginBGM();
		loginBGM.playBGM();
		
	}
	//practiceLogin Constructor
	
	//--------------------------getter & setter------------------------------
	
	public JTextField getInputID() {	return inputID;	}
	public void setInputID(JTextField inputID) {	this.inputID = inputID;	}
	public JPasswordField getInputPW() {	return inputPW;	}
	public void setInputPW(JPasswordField inputPW) {	this.inputPW = inputPW;	}
	public JButton getLoginBtn() {	return loginBtn;	}
	public void setLoginBtn(JButton loginBtn) {	this.loginBtn = loginBtn;	}
	public JButton getSignUpBtn() {	return signUpBtn;	}
	public void setSignUpBtn(JButton signUpBtn) {	this.signUpBtn = signUpBtn;	}
	public JButton getLostIDBtn() {	return lostIDBtn;	}
	public void setLostIDBtn(JButton lostIDBtn) {	this.lostIDBtn = lostIDBtn;	}
	public JButton getLostPWBtn() {	return lostPWBtn;	}
	public void setLostPWBtn(JButton lostPWBtn) {	this.lostPWBtn = lostPWBtn;	}
	public JButton getUpdateBtn() {	return updateBtn;	}
	public void setUpdateBtn(JButton updateBtn) {	this.updateBtn = updateBtn;	}
	public JFrame getFrame() {	return frame;	}
	public void setFrame(JFrame frame) {	this.frame = frame;	}
	public JDBCModel getModel() {	return model;	}
	public void setModel(JDBCModel model) {	this.model = model;	}
	public JTextArea getChatLog() {	return chatLog;	}
	public void setChatLog(JTextArea chatLog) {	this.chatLog = chatLog;	}
	public JTextField getInputChat() {	return inputChat;	}
	public void setInputChat(JTextField inputChat) {	this.inputChat = inputChat;	}
	public String getTempNickName() {return this.tempNickName;}
	public LoginBGM getLoginBGM() {return loginBGM;}
	//--------------------------End of getter & setter------------------------------
	
	
	@Override
	public void keyPressed(KeyEvent e) {	}
	@Override
	public void keyReleased(KeyEvent e) {	}
	@Override
	public void keyTyped(KeyEvent e) {	if(e.getKeyChar()==27) {exit();}	}
	
	//X눌렀을 때와 ESC눌렀을 때 작동할 종료 확인창
	public void exit() {
		bs = new ButtonSound();
		int sel = JOptionPane.showConfirmDialog(frame, "레알루 종료?");
		if(sel == JOptionPane.YES_OPTION) {
			bs = new ButtonSound();
			loginChat.exit();
			System.exit(0);
		} else {
			bs = new ButtonSound();
		}
	}
	
	//로그인 버튼과 비번입력창에서 작동할 메소드
	public void loginVerifier(String email, String pw) {
		bs = new ButtonSound();
		
		//--------------------공백 없음 검증------------------------------
		//이메일 공백
		if(email == null || email.length() == 0) {
			JOptionPane.showMessageDialog(frame, "이메일 주소를 입력하세요");
			return;
		}
		//비밀번호 공백
		if(pw == null || pw.length() == 0) {
			JOptionPane.showMessageDialog(frame, "비밀번호를 입력하세요");
			return;
		}
		
		//--------------------로그인 검증------------------------------
		//1. 없는 계정
		if (!model.hasEmail(email)) {
			JOptionPane.showMessageDialog(frame, "존재하지 않는 이메일주소 입니다");
			return;
		}
		//2. 있는 계정 + 틀린 비번
		if (!model.accountVerifier(email, pw)) {
			JOptionPane.showMessageDialog(frame, "비밀번호가 다릅니다");
			return;
		}
		//3. 있는 계정 + 맞는 비번 + 이미접속중
		if (!model.isNotOnline(email)) {
			JOptionPane.showMessageDialog(frame, "이미 접속중인 계정입니다");    
			return;
		}
		//4. 임시 비밀번호로 로그인
		if (model.isTempPW(email)) {
			new TempPW(email, Login.this);
			return;
		}
		
		//5. 로그인 성공
		model.setOnline(email);
		JOptionPane.showMessageDialog(frame, "로그인 성공");
		loginBGM.stopBGM();
		new Waiting(frame, inputID.getText(), model);
		inputID.setText("");
		inputPW.setText("");
		loginChat.exit();
		frame.dispose();
	}
	public void loginVerifierForUpdate(String email, String pw, JFrame frame) {
		bs = new ButtonSound();
		
		//--------------------공백 없음 검증------------------------------
		//이메일 공백
		if(email == null || email.length() == 0) {
			JOptionPane.showMessageDialog(frame, "이메일 주소를 입력하세요");
			return;
		}
		//비밀번호 공백
		if(pw == null || pw.length() == 0) {
			JOptionPane.showMessageDialog(frame, "비밀번호를 입력하세요");
			return;
		}
		
		//--------------------로그인 검증------------------------------
		//1. 없는 계정
		if (!model.hasEmail(email)) {
			JOptionPane.showMessageDialog(frame, "존재하지 않는 이메일주소 입니다");
			return;
		}
		//2. 있는 계정 + 틀린 비번
		if (!model.accountVerifier(email, pw)) {
			JOptionPane.showMessageDialog(frame, "비밀번호가 다릅니다");
			return;
		}

		//4. 로그인 성공
		JOptionPane.showMessageDialog(frame, "로그인 성공");
		inputID.setText("");
		inputPW.setText("");
		frame.dispose();
	}
	
	
	//로그인 이외 버튼 눌렀을 때 다른 버튼 비활성화 메소드
	public void btnDisabler() {
		inputID.setEnabled(false);
		inputPW.setEnabled(false);
		loginBtn.setEnabled(false);
		signUpBtn.setEnabled(false);
		lostIDBtn.setEnabled(false);
		lostPWBtn.setEnabled(false);
		updateBtn.setEnabled(false);
	}
	//로그인 이외 버튼 누른 후 로그인 창으로 돌아왔을 때 버튼 활성화 메소드
	public void backToLogin(JFrame frame, Login login) {
		bs = new ButtonSound();
		frame.setVisible(false);
		login.getInputID().setEnabled(true);
		login.getInputPW().setEnabled(true);
		login.getLoginBtn().setEnabled(true);
		login.getSignUpBtn().setEnabled(true);
		login.getUpdateBtn().setEnabled(true);
		login.getLostPWBtn().setEnabled(true);
		login.getLostIDBtn().setEnabled(true);
	}
	
	
	
	public static void main(String[] args) {
		new Login();
	}
}
//practiceLogin class






