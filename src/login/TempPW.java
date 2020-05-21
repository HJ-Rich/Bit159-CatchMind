package login;
import java.awt.Color;
import java.awt.FlowLayout;
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
import javax.swing.SwingConstants;

public class TempPW implements KeyListener, Runnable{
	
	private JButton done;
	private JPasswordField inputPW1, inputPW2;
	private JLabel PWV;
	private Login login;
	private JFrame frame;
	private String email;
	private JDBCModel model;
	 
	public TempPW (String email, Login login) {
		this.login = login;
		this.email = email;
		this.model = login.getModel();
		
		//--------------------------------frame-----------------------------------
		frame = new JFrame("비밀번호 변경");
		JOptionPane.showMessageDialog(login.getFrame(), "임시 비밀번호로 로그인하셨습니다");
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		frame.setSize(300,250);
		frame.setLocationRelativeTo(login.getFrame());
		JLabel pw1 = new JLabel("새 비밀번호");
		pw1.setSize(84, 37);
		frame.getContentPane().add(pw1);
		pw1.setLocation(36,10);
		JLabel pw2 = new JLabel("재입력");
		pw2.setSize(47, 37);
		frame.getContentPane().add(pw2);
		pw2.setLocation(60,56);
		inputPW1 = new JPasswordField(); 
		inputPW1.setSize(120, 37);
		frame.getContentPane().add(inputPW1);
		inputPW1.setLocation(124,10);
		inputPW2 = new JPasswordField();
		inputPW2.setSize(120, 37);
		frame.getContentPane().add(inputPW2);
		inputPW2.setLocation(124,60);
		PWV = new JLabel("비밀번호를 입력하세요");
		PWV.setHorizontalAlignment(SwingConstants.CENTER);
		PWV.setBounds(104, 95, 168, 24);
		frame.getContentPane().add(PWV);
		PWV.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton back = new JButton("취소");
		back.setSize(84, 40);
		frame.getContentPane().add(back);
		back.setLocation(160,153);
		done = new JButton("확인");
		done.setSize(84, 40);
		frame.getContentPane().add(done);
		done.setLocation(50,153);
		
		inputPW1.addKeyListener(this);
		inputPW2.addKeyListener(this);
		done.addKeyListener(this);
		back.addKeyListener(this);
		
		//-------------------------End of frame-------------------------------------
		
		
		//-------------------------Thread--------------------------------------------
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
		
		
		//-----------------------------button actions-------------------------------
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				login.backToLogin(frame, login);
			}
		});
		
		//비번 확인칸에서 Enter 입력했을 때
		inputPW2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tempPWVerifier();
			}
		});
		
		//확인 눌렀을 때
		done.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tempPWVerifier();
			}
		});
		
		//취소 눌렀을 때
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login.backToLogin(frame, login);
			}
		});
		//---------------------------End of button actions----------------------
		
	}	
	//TempPW Constructor
	
	
	//-----------------------------methods----------------------------------
	
	//비번입력 후 Enter 입력하거나 확인 눌렀을 때 작동하는 메소드
	public void tempPWVerifier() {
		
		//1. 공백 검증
		if(inputPW1.getPassword() == null || inputPW2.getPassword() == null || 
			inputPW1.getPassword().length == 0 || inputPW2.getPassword().length == 0) {
			JOptionPane.showMessageDialog(frame, "입력되지 않은 값이 있습니다");
			return;
		}
		//2. 동일성 검증
		if(!Arrays.equals(inputPW1.getPassword(), inputPW2.getPassword())) {
			JOptionPane.showMessageDialog(frame, "두 비밀번호가 다릅니다");
			return;
		}
		
		String newPW = new String(inputPW1.getPassword());
		
		//3. 변경여부 검증
		if(model.getPWByEmail(email).equals(newPW)) {
			JOptionPane.showMessageDialog(frame, "기존 비밀번호는 사용할 수 없습니다");	
			return;
		}
		
		//4. 비번 변경 적용
		model.setPW(email, newPW);
		model.setTempPW(email, 0);
		JOptionPane.showMessageDialog(frame, "비밀번호가 변경되었습니다");
		login.backToLogin(frame, login);
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {}
	@Override
	public void keyReleased(KeyEvent arg0) {}
	@Override
	public void keyTyped(KeyEvent arg0) {		
		if(arg0.getKeyChar() == 27) {
			login.backToLogin(frame, login);
		}
	}

	public void PWVSetter() {
		//1. 공백 검증
		if(inputPW1.getPassword() == null || inputPW2.getPassword() == null ||
			inputPW1.getPassword().length==0 || inputPW2.getPassword().length==0 ) {
			PWV.setText("");
			return;
		}
		//2. 동일성 검증
		if(!Arrays.equals(inputPW1.getPassword(), inputPW2.getPassword())) {
			PWV.setForeground(Color.red);
			PWV.setText("두 비밀번호가 다릅니다");
			return;
		}
		
		String newPW = new String(inputPW1.getPassword());
		
		//3. 변경여부 검증
		if(model.getPWByEmail(email).equals(newPW)) {
			PWV.setForeground(Color.red);
			PWV.setText("기존 비밀번호와 동일");
			return;
		}
		
		//4. 사용가능 비밀번호				
		PWV.setForeground(Color.blue);
		PWV.setText("사용가능한 비밀번호");
	}
	
	
	@Override
	public void run() {
		while (true) {
			if(!frame.isVisible()) {break;}

			if(inputPW1.isFocusOwner() || inputPW2.isFocusOwner()) {
				PWVSetter();
			}
		}
	}//run method

	
	
}
//TempPW class
