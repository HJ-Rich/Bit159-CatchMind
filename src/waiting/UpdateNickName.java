package waiting;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import login.ButtonSound;
import login.JDBCModel;

public class UpdateNickName {
	
	private JFrame frame, exFrame;
	private JTextField inputNickName;
	private JDBCModel model;
	private Waiting waitingRoom;
	private String email;
	private JTextField myNickName;
	private ButtonSound bs;
	
	public UpdateNickName(Waiting waitingRoom) {
		this.waitingRoom = waitingRoom;
		this.model = waitingRoom.getModel();
		this.exFrame = waitingRoom.getFrame();
		this.myNickName = waitingRoom.getMyNickName();
		this.exFrame = waitingRoom.getFrame();
		this.email = waitingRoom.getEmail();
		bs = new ButtonSound();
		
		//------------------------------frame----------------------------
		frame = new JFrame("닉네임 변경");
		frame.setLocationRelativeTo(exFrame);
		frame.setSize(300,250);
		frame.getContentPane().setLayout(null);
		
		inputNickName = new JTextField();
		inputNickName.setBounds(45, 99, 199, 30);
		frame.getContentPane().add(inputNickName);
		inputNickName.setColumns(10);
		
		JButton done = new JButton("확인");
		done.setBounds(50, 139, 80, 41);
		frame.getContentPane().add(done);
		
		JButton cancel = new JButton("취소");
		cancel.setBounds(155, 139, 80, 41);
		frame.getContentPane().add(cancel);
		
		JLabel exNickName = new JLabel(model.getNickNameByEmail(email));
		exNickName.setHorizontalAlignment(SwingConstants.CENTER);
		exNickName.setBounds(138, 23, 134, 22);
		frame.getContentPane().add(exNickName);
		
		JLabel lblNewLabel_2 = new JLabel("변경할 닉네임 입력");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(50, 67, 185, 22);
		frame.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_1_1 = new JLabel("현재 닉네임");
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1.setBounds(-4, 23, 134, 22);
		frame.getContentPane().add(lblNewLabel_1_1);
		
		frame.setVisible(true);
		//------------------------------End of frame----------------------------
		
		
		//-----------------------------button actions------------------------
		//X눌렀을 때
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				bs = new ButtonSound();
				frame.dispose();
			}
		});
		
		//확인 눌렀을 때
		done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});
		
		inputNickName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});
		
		//취소 눌렀을 때
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bs = new ButtonSound();
				frame.dispose();
			}
		});

		//-----------------------------End of button actions------------------------
	}
	//WaitingRoomUpdate Constructor
	
	
	public void update() {
		bs = new ButtonSound();
		String newNickName = inputNickName.getText();
		model.setNickName(email, newNickName);
		myNickName.setText(newNickName);
		waitingRoom.getWaitingRoomChat().setNickName(newNickName);
		JOptionPane.showMessageDialog(frame, "닉네임 변경이 완료되었습니다");
		frame.dispose();
	}
	
	
	
}
//WaitingRoomUpdate Class

















