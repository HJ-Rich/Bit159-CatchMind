package login;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {
	
	private SignUp signUp;
	private Login login;
	private String vCode;
	private String tempPW;
	private JDBCModel model;
	
	public String getCode() {
		return vCode;
	}
	public String getTempPW() {
		return tempPW;
	}
	
	//회원 가입시 이용되는 코드메일 발송용 생성자.
	//영문대문자 2개 + 1~9숫자 4자리
	public Email(SignUp signUp) {
		this.signUp = signUp;
		
		char ranChar1 = (char) ((int)(Math.random()*(90-65+1))+65); 
		char ranChar2 = (char) ((int)(Math.random()*(90-65+1))+65); 
		int ranNum1 = (int)(Math.random()*(9-1+1))+1;
		int ranNum2 = (int)(Math.random()*(9-1+1))+1;
		int ranNum3 = (int)(Math.random()*(9-1+1))+1;
		int ranNum4 = (int)(Math.random()*(9-1+1))+1;
		
		vCode = new String(""+ranChar1+ranChar2+ranNum1+ranNum2+ranNum3+ranNum4);
		
        String userName = "Bit159.Richard"; // 네이버일 경우 네이버 계정, gmail경우 gmail 계정
        String password = "richbit159";   // 패스워드

        // SMTP 서버 정보를 설정한다.
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com"); 
        prop.put("mail.smtp.port", 465); 
        prop.put("mail.smtp.auth", "true"); 
        prop.put("mail.smtp.ssl.enable", "true"); 
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        
        System.out.println("세션 직전");	
        Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(userName));

            // Customer email input
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(signUp.getInputEmail().getText())); 

            // Subject
            message.setSubject("인증번호 : " + vCode); //메일 제목을 입력

            // Text
            message.setText("환영합니다" + "\n" + "인증 번호는 다음과 같습니다:" + "\n\n\n" + vCode);    //메일 내용을 입력

            // send the message
            Transport.send(message); ////전송
            System.out.println("인증메일 발송완료: " + signUp.getInputEmail().getText() +" : "+ vCode);
        } catch (AddressException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	
	
	}
	
	
	//비번찾기시 사용되는 생성자
	public Email(String email, Login login) {
		this.login = login;
		this.model = login.getModel();

		char ranChar1 = (char) ((int)(Math.random()*(90-65+1))+65); 
		char ranChar2 = (char) ((int)(Math.random()*(90-65+1))+65); 
		char ranNum1 = (char) ((int)(Math.random()*(9-1+1))+49);
		char ranNum2 = (char) ((int)(Math.random()*(9-1+1))+49);
		char ranNum3 = (char) ((int)(Math.random()*(9-1+1))+49);
		char ranNum4 = (char) ((int)(Math.random()*(9-1+1))+49);
		
		char[] temp = {ranChar1,ranChar2,ranNum1,ranNum2,ranNum3,ranNum4};
		tempPW = new String(temp);

		String userName = "Bit159.Richard"; // 네이버일 경우 네이버 계정, gmail경우 gmail 계정
		String password = "richbit159";   // 패스워드
		
		// SMTP 서버 정보를 설정한다.
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com"); 
		prop.put("mail.smtp.port", 465); 
		prop.put("mail.smtp.auth", "true"); 
		prop.put("mail.smtp.ssl.enable", "true"); 
		prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		
		Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		});
		
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(userName));
			
			// Customer email input
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(email)); 
			
			// Subject
			message.setSubject("임시 비밀번호 : " + tempPW); //메일 제목을 입력
			
			// Text
			message.setText("임시 비밀번호를 발급해드렸으니 로그인 후 변경바랍니다." + "\n\n\n" + tempPW);    //메일 내용을 입력
			
			// send the message
			Transport.send(message); ////전송
			System.out.println("임시 비밀번호 발송완료 : " + email + " : " + tempPW);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
//