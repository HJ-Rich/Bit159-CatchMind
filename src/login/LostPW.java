package login;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class LostPW implements KeyListener{
	
	private Login login;
	private JTextField inputEmail;
	private JFrame frame;
	private JDBCModel model;
	private ButtonSound bs;
	
	public LostPW(Login login) {
		this.login = login;
		this.model = login.getModel();
		bs = new ButtonSound();
		//-----------------------------frame--------------------------------
		frame = new JFrame("비밀번호 찾기");
		frame.setSize(300,250);
		frame.setLocationRelativeTo(login.getFrame());
		frame.getContentPane().setLayout(null);
		JButton cancel = new JButton("취소");
		cancel.setBounds(150, 140, 100, 30);
		frame.getContentPane().add(cancel);
		JButton done = new JButton("확인");
		done.setBounds(45, 140, 100, 30);
		frame.getContentPane().add(done);
		
		JLabel email = new JLabel("이메일 주소");
		email.setBounds(44, 56, 79, 37);
		frame.getContentPane().add(email);
		inputEmail = new JTextField(15);
		inputEmail.setBounds(135, 60, 120, 30);
		frame.getContentPane().add(inputEmail);
		frame.setVisible(true);
		inputEmail.addKeyListener(this);
		done.addKeyListener(this);
		cancel.addKeyListener(this);
		
		//-------------------------button actions------------------------------
		//X눌렀을 때			
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				login.backToLogin(frame, login);
			}
		});

		//이메일 입력 후 엔터 눌렀을 때
		inputEmail.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pwFinder();
			}
		});
		
		//확인 버튼 눌렀을 때
		done.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pwFinder();
			}
		});
		
		//취소 버튼 눌렀을 때
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login.backToLogin(frame, login);
			}
		});
		
	}
	//LostPW Constructor

	
	//-------------------------getter & setter-------------------------------
	public Login login() {	return login;	}
	public void setLogin(Login login) {	this.login = login;	}
	public JTextField getInputEmail() {	return inputEmail;	}
	public void setInputEmail(JTextField inputEmail) {	this.inputEmail = inputEmail;	}
	
	
	//----------------------------methods-----------------------------------
	@Override
	public void keyPressed(KeyEvent e) {	}
	@Override
	public void keyReleased(KeyEvent e) {	}
	//esc 눌렀을 때
	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == 27) {
			login.backToLogin(frame, login);
		}
	}
	
	//이메일 입력 후 임시비번 발송하는 메소드
	public void pwFinder() {
		bs = new ButtonSound();
		//1. 공백 검증
		String findEmail = inputEmail.getText();
		if(findEmail == null || findEmail.length() == 0) {
			JOptionPane.showMessageDialog(frame, "핸드폰 번호를 입력해주세요");
			return;
		}
		//2. 이메일 검증
		if(!model.hasEmail(findEmail)) {
			JOptionPane.showMessageDialog(frame, "일치하는 이메일 주소가 없습니다");
			return;
		}
		//3. 임시비번 발송
		model.setTempPW(findEmail, 1);
		Email mail = new Email(findEmail, login);
		model.setPW(findEmail, mail.getTempPW());
		JOptionPane.showMessageDialog(frame, "메일 발송 완료");
		login.backToLogin(frame, login);
		return;
	}

	
}
