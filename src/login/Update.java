package login;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Update implements KeyListener {
	
	private Login login;
	private JButton done;
	private JButton cancel;
	private JTextField inputNickName;
	private JPasswordField inputPW1;
	private JPasswordField inputPW2;
	private JTextField inputMobile;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JFrame frame;
	private String inputEmail;
	private JDBCModel model;
	private ButtonSound bs;
	 
	public Update(Login login, String inputEmail) {
		this.login = login;
		this.inputEmail = inputEmail;
		this.model = login.getModel();
		bs = new ButtonSound();
		
		//-----------------------------frame--------------------------------
		frame = new JFrame("회원정보수정");
		frame.setVisible(true);
		frame.setSize(300,300);
		frame.setLocationRelativeTo(login.getFrame());
		frame.getContentPane().setLayout(null);

		
		done = new JButton("확인");
		done.setBounds(42, 209, 100, 30);
		frame.getContentPane().add(done);
		
		cancel = new JButton("취소");
		cancel.setBounds(154, 209, 100, 30);
		frame.getContentPane().add(cancel);
		
		inputNickName = new JTextField();
		inputNickName.setBounds(125, 118, 138, 30);
		frame.getContentPane().add(inputNickName);
		inputNickName.setColumns(10);
		
		inputPW1 = new JPasswordField();
		inputPW1.setBounds(125, 18, 138, 30);
		frame.getContentPane().add(inputPW1);
		
		inputPW2 = new JPasswordField();
		inputPW2.setBounds(125, 58, 138, 30);
		frame.getContentPane().add(inputPW2);
		
		inputMobile = new JTextField();
		inputMobile.setBounds(125, 158, 138, 30);
		frame.getContentPane().add(inputMobile);
		inputMobile.setColumns(10);
		
		lblNewLabel = new JLabel("새로운 비밀번호");
		lblNewLabel.setBounds(25, 18, 107, 30);
		frame.getContentPane().add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("재입력");
		lblNewLabel_1.setBounds(76, 58, 49, 30);
		frame.getContentPane().add(lblNewLabel_1);
		
		lblNewLabel_2 = new JLabel("새 닉네임");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_2.setBounds(53, 117, 61, 30);
		frame.getContentPane().add(lblNewLabel_2);
		
		lblNewLabel_3 = new JLabel("새 핸드폰 번호");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_3.setBounds(16, 157, 97, 30);
		frame.getContentPane().add(lblNewLabel_3);
		
		inputNickName.setText(model.getNickNameByEmail(inputEmail));
		inputMobile.setText(model.getPhoneByEmail(inputEmail));
		
		inputPW1.addKeyListener(this);		
		inputPW2.addKeyListener(this);		
		done.addKeyListener(this);
		cancel.addKeyListener(this);
		//-------------------------End of frame-------------------------------
		
		
		//----------------------------button actions-----------------------------
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				login.backToLogin(frame, login);
			}
		});
		
		inputPW2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateVerifier();
			}
		});
		
		inputNickName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateVerifier();
			}
		});
		
		inputMobile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateVerifier();
			}
		});
		
		done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateVerifier();
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login.backToLogin(frame, login);
			}
		});
		//-----------------------------End of button actions----------------------
	}
	//Update Constructor
	
	
	//----------------------------------methods------------------------------
	@Override
	public void keyPressed(KeyEvent arg0) {	}
	@Override
	public void keyReleased(KeyEvent arg0) {}
	@Override
	public void keyTyped(KeyEvent arg0) {
		if(arg0.getKeyChar() == 27) {
			login.backToLogin(frame, login);
		}
	}
	
	//비번 입력 후 엔터 누르거나 확인 눌렀을 때
	public void updateVerifier() {
		bs = new ButtonSound();
		String newPW1 = new String(inputPW1.getPassword());
		String newPW2 = new String(inputPW2.getPassword());
		String newNickName = inputNickName.getText();
		String newPhone = inputMobile.getText();
		
		//1. 비밀번호 동일성 검증
		if(!newPW1.equals(newPW2)) {
			JOptionPane.showMessageDialog(frame, "두 비밀번호가 일치하지 않습니다.");
			return;
		}
		
		//2. 닉네임, 핸드폰 공백 검증
		if(newNickName == null || newPhone == null ||
			newNickName.length() == 0 || newPhone.length() == 0 ) {
			JOptionPane.showMessageDialog(frame, "입력되지 않은 값이 있습니다");
			return;
		}
		
		//3. 닉네임 중복 검증
		if(model.hasNickName(newNickName)) {
			if(!model.getEmailByNickName(newNickName).equals(inputEmail)) {
				JOptionPane.showMessageDialog(frame, "사용중인 닉네임입니다");
				return;
			}
		}
		
		//4. 핸드폰 중복 검증
		if(model.hasPhone(newPhone)) {
			if(!model.getPhoneByEmail(inputEmail).equals(newPhone)) {
				JOptionPane.showMessageDialog(frame, "사용중인 번호입니다");
				return;
			}
		}
		
		//5. 비번 변경 하는 경우
		if(!(newPW1 == null || newPW2 == null || newPW1.length() == 0 || newPW2.length()==0)) {
			model.setPW(inputEmail, newPW1);
		}
		model.setNickName(inputEmail, newNickName);	
		model.setPhone(inputEmail, newPhone);	
		JOptionPane.showMessageDialog(frame, "정보 변경이 완료되었습니다");
		login.backToLogin(frame, login);
		return;
	}
}

















