package waiting;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import login.ButtonSound;
import login.JDBCModel;

public class CreateRoom {
    private JButton makingBtn,cancelBtn;
    private JCheckBox check;
    private JLabel roomNameL;
    private JTextField roomName;
    private JTextField roomPw;
    private Waiting roomForm;
    private DefaultTableModel model1;
    private WaitingChatDTO dto;
    private JDBCModel model;
    private JFrame frame;
    private ButtonSound bs;
    
    public CreateRoom(Waiting roomForm, JDBCModel model) {
    	bs = new ButtonSound();
        //--------------------------frame--------------------------
    	frame = new JFrame("방 만들기");
    	this.roomForm = roomForm;
    	this.model = model;
    	
        frame.getContentPane().setLayout(null);
        makingBtn = new  JButton("방만들기");
        makingBtn.setBounds(50,250,30,30);
        cancelBtn = new JButton("취소");
        cancelBtn.setBounds(50,250,40,30);
        
        JPanel p =new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.setBounds(0, 150, 284, 33);
        p.add(makingBtn);
        p.add(cancelBtn);
        frame.getContentPane().add(p);
        
        roomNameL = new JLabel("방 제목");
        roomNameL.setBounds(12, 10, 65, 43);
        frame.getContentPane().add(roomNameL);
        
        roomName = new JTextField();
        roomName.setBounds(89, 21, 145, 21);
        frame.getContentPane().add(roomName);
        roomName.setColumns(10);
        
        JLabel RoomPwL = new JLabel("비밀번호");
        RoomPwL.setBounds(12, 97, 65, 43);
        frame.getContentPane().add(RoomPwL);
        
        roomPw = new JTextField();
        roomPw.setColumns(10);
        roomPw.setEnabled(false);
        roomPw.setBounds(89, 108, 145, 21);
   
        frame.getContentPane().add(roomPw);
        
        JLabel checkL = new JLabel("상태");
        checkL.setBounds(12, 55, 65, 33);
        frame.getContentPane().add(checkL);
        
        check = new JCheckBox("비공개");
        check.setBounds(89, 60, 115, 23);
        frame.getContentPane().add(check);
        
        frame.setTitle("방만들기");
        frame.setBounds(500,300,300,224);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        //--------------------------End of frame--------------------------
        
        
        //--------------------------button actions-----------------------
        
        //방만들기 버튼 눌렀을 때
        makingBtn.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		bs = new ButtonSound();
                dto=new WaitingChatDTO();
                int isopen=0;
                String pw=null;
                System.out.println("방 생성");
                String name = roomName.getText();
                String roomMaker = roomForm.getNickname();
                if(check.isSelected()) {
                    isopen=1;
                }else isopen=0;
                int status = 0;
                pw= roomPw.getText();
                dto.setIsopen(isopen);
                dto.setStatus(status);
                model.makingRoom(name, roomMaker,dto,pw);
                frame.dispose();
        	}
        });
        
        //취소 버튼 눌렀을 때
        cancelBtn.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		bs = new ButtonSound();
        		frame.dispose();
        	}
        });
        
        //비공개 체크 or 체크 해제 했을 때
        check.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		bs = new ButtonSound();
        		if(check.isSelected()) {
                    roomPw.setText("");
                    roomPw.setEnabled(true);
                }else if(!check.isSelected()) {
                    roomPw.setText("");
                    roomPw.setEnabled(false);
                }
        	}
        });
        
        //--------------------------End of button actions-----------------------
        
    }
    //WaitingRoomCreate Constructor
    

}
//WaitingRoomCreate class










