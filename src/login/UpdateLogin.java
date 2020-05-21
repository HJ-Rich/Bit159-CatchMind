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

public class UpdateLogin implements KeyListener {

	private Login login;
	private JTextField inputEmail;
	private JPasswordField inputPW;
	private JFrame frame;
	private JDBCModel model;
	private ButtonSound bs;

	public UpdateLogin(Login login) {
		this.login = login;
		this.model = model;

		// ------------------------------frame-------------------------------
		frame = new JFrame("회원정보수정");
		frame.setSize(300, 250);
		frame.setVisible(true);
		frame.setLocationRelativeTo(login.getFrame());
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("이메일주소");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 44, 122, 33);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("비밀번호");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(10, 85, 122, 33);
		frame.getContentPane().add(lblNewLabel_1);

		inputEmail = new JTextField();
		inputEmail.setBounds(115, 44, 135, 30);
		frame.getContentPane().add(inputEmail);
		inputEmail.setColumns(10);

		inputPW = new JPasswordField();
		inputPW.setBounds(115, 85, 135, 30);
		frame.getContentPane().add(inputPW);

		JButton cancel = new JButton("취소");
		cancel.setBounds(150, 140, 100, 30);
		frame.getContentPane().add(cancel);

		JButton done = new JButton("확인");
		done.setBounds(45, 140, 100, 30);
		frame.getContentPane().add(done);

		inputEmail.addKeyListener(this);
		inputPW.addKeyListener(this);
		done.addKeyListener(this);
		cancel.addKeyListener(this);
		// --------------------------End of frame------------------------------

		// ------------------------button actions------------------------------
		// X눌렀을 때
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				login.backToLogin(frame, login);
			}
		});

		// 비번 입력 후 Enter 눌렀을 때
		inputPW.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateLoginVerifier();
			}
		});

		// 확인 눌렀을 때
		done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateLoginVerifier();
			}
		});

		// 취소 눌렀을 때
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login.backToLogin(frame, login);
			}
		});
	}
	// UpdateLogin Constructor

	// --------------------------methods--------------------------------
	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == 27) {
			login.backToLogin(frame, login);
		}
	}

	// 정보수정을 위해 로그인하는 메소드
	public void updateLoginVerifier() {
		login.loginVerifierForUpdate(inputEmail.getText(), new String(inputPW.getPassword()), frame);
		new Update(login, inputEmail.getText());
	}
}
