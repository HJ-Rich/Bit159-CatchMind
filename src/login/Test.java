package login;

import java.io.File;
import java.io.IOException;

public class Test {
public static void main(String[] args) {
	
	File file = new File("button.wav");
	StringBuffer st = new StringBuffer();
	
	st.append("file/");
	st.append(file.getAbsolutePath());
	System.out.println(st.toString());
	String stt = new String(st);
	System.out.println(stt);
	
	System.out.println(stt.replace("\\", "/"));
}
}
