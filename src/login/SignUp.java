package login;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Font;

public class SignUp implements KeyListener, Runnable {
	private Login login;
	private JLabel emailV, PWV, nickNameV, mobileV;
	private JTextField inputEmail, inputNickName, inputPhone, inputCode;
	private JPasswordField inputPW1, inputPW2;
	private JButton sendCode, checkCode, done, cancel;
	private String vCode;
	private JFrame frame;
	private JDBCModel model;
	private ButtonSound bs;
	
	public SignUp(Login login) {
		this.login = login;
		this.model = login.getModel();
		bs = new ButtonSound();
		
		//---------------------------frame-----------------------------
		frame = new JFrame("회원가입");
		frame.setSize(350,450);
		frame.setResizable(false);
		frame.setLocationRelativeTo(login.getFrame());
		frame.setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel emailLabel = new JLabel("이메일 주소");
		emailLabel.setBounds(38, 49, 99, 26);
		frame.getContentPane().add(emailLabel);
		
		JLabel codeLabel = new JLabel("인증코드");
		codeLabel.setBounds(38, 97, 99, 26);
		frame.getContentPane().add(codeLabel);
		
		JLabel pw1Label = new JLabel("비밀번호");
		pw1Label.setBounds(38, 183, 99, 26);
		frame.getContentPane().add(pw1Label);
		
		JLabel pw2Label = new JLabel("비밀번호 확인");
		pw2Label.setBounds(38, 219, 99, 26);
		frame.getContentPane().add(pw2Label);
		
		JLabel nickNameLabel = new JLabel("닉네임");
		nickNameLabel.setBounds(38, 275, 99, 26);
		frame.getContentPane().add(nickNameLabel);
		
		JLabel mobileLabel = new JLabel("핸드폰 번호");
		mobileLabel.setBounds(38, 315, 99, 26);
		frame.getContentPane().add(mobileLabel);
		
		done = new JButton("확인");
		done.setBounds(51, 381, 100, 30);
		frame.getContentPane().add(done);
		
		cancel = new JButton("취소");
		inputEmail = new JTextField();
		inputEmail.setBounds(128, 48, 120, 30);
		frame.getContentPane().add(inputEmail);
		inputEmail.setColumns(10);
		
		inputNickName = new JTextField();
		inputNickName.setColumns(10);
		inputNickName.setBounds(128, 266, 120, 30);
		frame.getContentPane().add(inputNickName);
		
		inputPhone = new JTextField();
		inputPhone.setColumns(10);
		inputPhone.setBounds(128, 314, 120, 30);
		frame.getContentPane().add(inputPhone);
		
		inputPW1 = new JPasswordField();
		inputPW1.setBounds(128, 179, 120, 30);
		frame.getContentPane().add(inputPW1);
		
		inputPW2 = new JPasswordField();
		inputPW2.setBounds(128, 215, 120, 30);
		frame.getContentPane().add(inputPW2);

		cancel.setBounds(187, 381, 100, 30);
		frame.getContentPane().add(cancel);
		
		inputCode = new JTextField();
		inputCode.setColumns(10);
		inputCode.setBounds(128, 96, 120, 30);
		frame.getContentPane().add(inputCode);
		
		emailV = new JLabel("");
		emailV.setHorizontalAlignment(SwingConstants.CENTER);
		emailV.setBounds(113, 78, 159, 15);
		frame.getContentPane().add(emailV);
		
		sendCode = new JButton("보내기");
		sendCode.setFont(new Font("굴림", Font.PLAIN, 12));
		sendCode.setBounds(260, 47, 72, 30);
		frame.getContentPane().add(sendCode);
		
		checkCode = new JButton("확인");
		checkCode.setFont(new Font("굴림", Font.PLAIN, 12));
		checkCode.setBounds(260, 95, 72, 28);
		frame.getContentPane().add(checkCode);
		
		PWV = new JLabel("");
		PWV.setHorizontalAlignment(SwingConstants.CENTER);
		PWV.setBounds(128, 248, 126, 15);
		frame.getContentPane().add(PWV);
		
		nickNameV = new JLabel("");
		nickNameV.setHorizontalAlignment(SwingConstants.CENTER);
		nickNameV.setBounds(128, 298, 126, 15);
		frame.getContentPane().add(nickNameV);
		
		mobileV = new JLabel("");
		mobileV.setHorizontalAlignment(SwingConstants.CENTER);
		mobileV.setBounds(113, 345, 144, 15);
		frame.getContentPane().add(mobileV);
		
		inputCode.setEnabled(false);
		checkCode.setEnabled(false);
		inputPW1.setEnabled(false);
		inputPW2.setEnabled(false);
		inputNickName.setEnabled(false);
		inputPhone.setEnabled(false);
		
		inputEmail.addKeyListener(this);
		inputNickName.addKeyListener(this);
		inputPhone.addKeyListener(this);
		inputCode.addKeyListener(this);
		inputPW1.addKeyListener(this);
		inputPW2.addKeyListener(this);
		sendCode.addKeyListener(this);
		checkCode.addKeyListener(this);
		done.addKeyListener(this);
		cancel.addKeyListener(this);
		
		frame.setVisible(true);
		//-------------------------End of frame------------------------------
		

		
		//-------------------------button actions---------------------------
		//X눌렀을 때
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				login.backToLogin(frame, login);
			}
		});
		
		//메일 입력 후 Enter 입력했을 때
		inputEmail.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				vCodeSend();
			}
		});
		
		//메일 입력후 발송 버튼 눌렀을 때
		sendCode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				vCodeSend();
			}
		});
		
		//코드 입력 후 Enter 입력했을 때
		inputCode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				vCodeCheck();
			}
		});
		
		//코드 입력 후 코드 확인 버튼 눌렀을 때
		checkCode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				vCodeCheck();
			}
		});
		
		//핸드폰 번호 입력 후 엔터 눌렀을 때
		inputPhone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				signDone();
			}
		});
		
		//확인 버튼 눌렀을 때
		done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				signDone();
			}
		});
		
		//취소 버튼 눌렀을 때
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				login.backToLogin(frame, login);
			}
		});
		//-----------------------End of button actions---------------------------
		
		
		//----------------------Thread--------------------------
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}
	//SignUp Constructor
	
	
	//------------------------getter & setter---------------------
	public JTextField getInputEmail() {	return inputEmail;	}
	
	//------------------------methods-------------------------------
	@Override
	public void keyPressed(KeyEvent arg0) {	}
	@Override
	public void keyReleased(KeyEvent arg0) {	}
	
	//esc 눌렀을 때 사용되는 메소드
	@Override
	public void keyTyped(KeyEvent arg0) {
		if(arg0.getKeyChar() == 27) {
			login.backToLogin(frame, login);
		}
	}

	//이메일 형식 검증하는 메소드
	public boolean isValidEmail(String email) {
		boolean result = false; 
		String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$"; 
		Pattern p = Pattern.compile(regex); Matcher m = p.matcher(email); 
		if(m.matches()) { 
			result = true; 
		} 
		return result; 
	}

	//이메일 입력 후 Enter 누르거나 코드발송 버튼 눌렀을 때 사용되는 메소드
	public void vCodeSend() {
		bs = new ButtonSound();
		//1. 공백 검증
		if(inputEmail.getText() == null || inputEmail.getText().length() == 0) {
			JOptionPane.showMessageDialog(frame, "이메일을 입력해주세요");
			return;
		}
		//2. 형식 검증
		if(!isValidEmail(inputEmail.getText())) {
			JOptionPane.showMessageDialog(frame, "이메일 형식을 확인해주세요");
			return;
		}
		//3. 중복 검증
		if(model.hasEmail(inputEmail.getText())) {
			JOptionPane.showMessageDialog(frame, "이미 사용중인 이메일입니다");
			return;
		}
		//4. 코드 발송
		vCode = new Email(SignUp.this).getCode();
		JOptionPane.showMessageDialog(frame, "인증코드가 발송되었습니다", "이메일 인증", JOptionPane.INFORMATION_MESSAGE);
		inputEmail.setEditable(false);
		inputCode.setEnabled(true);
		checkCode.setEnabled(true);
	}

	//입력한 인증번호를 확인하는 메소드
	public void vCodeCheck() {
		bs = new ButtonSound();
		if(vCode == null || vCode.length() == 0) {
			JOptionPane.showMessageDialog(frame, "코드가 아직 발송되지 않았습니다");
			return;
		}
		
		if(vCode.equals(inputCode.getText())) {
			JOptionPane.showMessageDialog(frame, "정상적으로 인증되었습니다", "이메일 인증", JOptionPane.INFORMATION_MESSAGE);
			inputCode.setEnabled(false);
			checkCode.setEnabled(false);
			sendCode.setEnabled(false);
			
			inputPW1.setEnabled(true);
			inputPW2.setEnabled(true);
			inputNickName.setEnabled(true);
			inputPhone.setEnabled(true);
			
		}else {
			JOptionPane.showMessageDialog(frame, "인증코드가 유효하지 않습니다", "이메일 인증", JOptionPane.INFORMATION_MESSAGE);
			inputNickName.setEnabled(false);
		}
	}

	//가입 완료 버튼 누르거나 핸드폰번호 입력 후 엔터 눌렀을 때 가입처리되는 메소드
	public void signDone() {
		bs = new ButtonSound();
		if(!emailV.getForeground().equals(Color.blue) ||
			!PWV.getForeground().equals(Color.blue) ||
			!nickNameV.getForeground().equals(Color.blue) ||
			!mobileV.getForeground().equals(Color.blue) ) {
			JOptionPane.showMessageDialog(frame, "입력값을 확인해주세요");
			return;
		}
		String userInputID = inputEmail.getText();
		String userInputPW1 = new String(inputPW1.getPassword());
		String userInputNickName = inputNickName.getText();
		String userInputPhone = inputPhone.getText();

		model.signUp(userInputID, userInputPW1, userInputNickName, userInputPhone);
		
		JOptionPane.showMessageDialog(frame, "회원가입이 완료되었습니다", "회원 가입", JOptionPane.INFORMATION_MESSAGE);
		login.backToLogin(frame, login);
	}

	
	//--------------------------스레드용 메소드----------------------
	public void emailVerifier() {
		//1. 공백 검증
		if(inputEmail.getText() == null || inputEmail.getText().length() == 0) {
			emailV.setText("");
			return;
		} 
		//2. 형식 검증
		if(!isValidEmail(inputEmail.getText())) {
			emailV.setForeground(Color.red);
			emailV.setText("유효하지 않은 이메일 형식");
			return;
		}
		//3. 중복 검증
		if(model.hasEmail(inputEmail.getText())) {
			emailV.setForeground(Color.red);
			emailV.setText("사용중인 이메일");
			return;
		}
		//4. 사용 가능
		emailV.setForeground(Color.blue);
		emailV.setText("사용가능한 이메일");
		return;
	}
	
	public void passwordVerifier() {
		//1. 공백 검증
		if(inputPW1.getPassword() == null || inputPW2.getPassword() == null
			|| inputPW1.getPassword().length == 0 || inputPW2.getPassword().length == 0) {
			PWV.setForeground(Color.black);
			PWV.setText("");
			return;
		}
		//2. 동일성 검증	
		if(!Arrays.equals(inputPW1.getPassword(), inputPW2.getPassword())) {
			PWV.setForeground(Color.red);
			PWV.setText("비밀번호가 다름");
			return;
		}
		//3. 사용 가능
		PWV.setForeground(Color.blue);
		PWV.setText("비밀번호 일치");
	}
	
	public void nickNameVerifier() {
		//1. 공백 검증
		if(inputNickName.getText() == null || inputNickName.getText().length() == 0) {
			nickNameV.setText("");
			return;
		}
		//2. 중복 검증
		if(model.hasNickName(inputNickName.getText())) {
			nickNameV.setForeground(Color.red);
			nickNameV.setText("사용중인 닉네임");
			return;
		}
		//3. 사용 가능
		nickNameV.setForeground(Color.blue);
		nickNameV.setText("사용 가능한 닉네임");
	}
	
	public void phoneVerifier() {
		//1. 공백 검증
		if(inputPhone.getText() == null || inputPhone.getText().length() == 0) {
			mobileV.setText("");
			return;
		}
		//2. 중복 검증
		if(model.hasPhone(inputPhone.getText())) {
			mobileV.setForeground(Color.red);
			mobileV.setText("사용중인 핸드폰 번호");
			return;
		}
		//3. 사용 가능
		mobileV.setForeground(Color.blue);
		mobileV.setText("사용 가능한 핸드폰 번호");
	}
	
	@Override
	public void run() {
		while(true) {
			if(!frame.isVisible()) {break;}
			if(inputEmail.isFocusOwner()) {emailVerifier();}
			if(inputPW1.isFocusOwner() || inputPW2.isFocusOwner()) {passwordVerifier();}
			if(inputNickName.isFocusOwner()) {nickNameVerifier();}
			if(inputPhone.isFocusOwner()) {phoneVerifier();}
		}
	}
	
	
}
