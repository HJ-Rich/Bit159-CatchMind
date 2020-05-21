package waiting;

import java.io.Serializable;

enum Info{
    JOIN, EXIT, SEND, MESSAGE, WHISPER
}
public class WaitingChatDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private String nickName, message,email, pw, phone,title,owner,roomPw,receivedName, sendWhisper, receivedWhisper;
    private  int isonline,no,people,status,level,isopen;
    public String getSendWhisper() {
		return sendWhisper;
	}

	public void setSendWhisper(String sendWhisper) {
		this.sendWhisper = sendWhisper;
	}

	public String getReceivedWhisper() {
		return receivedWhisper;
	}

	public void setReceivedWhisper(String receivedWhisper) {
		this.receivedWhisper = receivedWhisper;
	}
	private Info command;

    public void setReceivedName(String receivedName) {
    	this.receivedName = receivedName;
    }
    
    public String getReceivedName() {
    	return receivedName;
    }
    
    public String getNickName() {
        return nickName;
    }
    public String getTitle() {
        return title;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public String getOwner() {
        return owner;
    }
    public String getRoomPw() {
        return roomPw;
    }
    public int getNo() {
        return no;
    }
    public int getPepple() {
        return people;
    }
    public int getStatus() {
    	
        return status;
    }
    public int getIsopen() {
        
        return isopen;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public void setRoomPw(String roomPw) {
        this.roomPw = roomPw;
    }
    public void setNo(int no) {
        this.no = no;
    }
    public void setPepple(int pepple) {
        this.people = pepple;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public void setIsopen(int isopen) {
        this.isopen = isopen;
    }
    public String getEmail() {
        return email;
    }
    public String getPw() {
        return pw;
    }
    public String getPhone() {
        return phone;
    }
    public int getIsonline() {
        return isonline;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setIsonline(int isonline) {
        this.isonline = isonline;
    }
    public String getMessage() {
        return message;
    }
    public Info getCommand() {
        return command;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setCommand(Info command) {
        this.command = command;
    }
    
    
}
