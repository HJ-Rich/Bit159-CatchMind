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

public class LostEmail implements KeyListener{
	
	private Login login;
	private JFrame frame;
	private JTextField inputMobile;
	private JDBCModel model;
	private ButtonSound bs;
	
	public LostEmail(Login login) {
		this.login = login;
		this.model = login.getModel();
		bs = new ButtonSound();
		//-------------------------frame-----------------------------------
		frame = new JFrame("아이디 찾기");
		frame.setTitle("이메일 찾기");
		frame.setSize(300,250);
		frame.setLocationRelativeTo(login.getFrame());
		frame.getContentPane().setLayout(null);
		
		JLabel mobile = new JLabel("핸드폰 번호");
		mobile.setSize(77, 37);
		frame.getContentPane().add(mobile);
		mobile.setLocation(45,57);
		inputMobile = new JTextField(10);
		inputMobile.setSize(120, 30);
		frame.getContentPane().add(inputMobile);
		inputMobile.setLocation(130,60);
		JButton cancel = new JButton("취소");
		cancel.setSize(100, 30);
		frame.getContentPane().add(cancel);
		cancel.setLocation(150,140);
		JButton done = new JButton("확인");
		done.setSize(100, 30);
		frame.getContentPane().add(done);
		done.setLocation(45,140);
		frame.setVisible(true);
		inputMobile.addKeyListener(this);
		done.addKeyListener(this);
		cancel.addKeyListener(this);
		
		//--------------------button actions---------------------------------
		//X눌렀을 때
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				login.backToLogin(frame, login);
			}
		});

		//번호 입력 후 Enter 눌렀을 때
		inputMobile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				emailFinder();
			}
		});
		
		//번호 입력 후 확인 눌렀을 때
		done.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				emailFinder();
			}
		});
		
		//취소 눌렀을 때
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login.backToLogin(frame, login);
			}
		});
		
	}
	//LostID Constructor

	@Override
	public void keyPressed(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar()==27) {
			login.backToLogin(frame, login);
		};
	}
	
	//이메일 찾기 메소드
	public void emailFinder() {
		bs = new ButtonSound();
		//1. 공백 검증
		String findMobile = inputMobile.getText();
		String foundEmail = null;
		if(findMobile == null || findMobile.length() == 0) {
			JOptionPane.showMessageDialog(frame, "핸드폰 번호를 입력해주세요");
			return;
		}
		//2. 핸드폰 번호 검증
		if((foundEmail = model.getEmailByPhone(findMobile)) == null) {
			JOptionPane.showMessageDialog(frame, "일치하는 아이디가 없습니다");
			return;
		}
		//3. 이메일 반환
		JOptionPane.showMessageDialog(frame, "이메일 주소는 " + foundEmail + " 입니다");
		login.backToLogin(frame, login);
		return;
	}
	
	
}
