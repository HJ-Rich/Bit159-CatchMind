package login;
import java.io.File;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class LoginBGM {
	private MediaPlayer p;
	public LoginBGM() {
		JFXPanel panel = new JFXPanel();
		File file = new File("loginbgm.mp3");
		String path = "file:/" + file.getAbsolutePath();
		Media m = new Media(path.replace("\\", "/"));
		p = new MediaPlayer(m);

	}

	public void playBGM() {
		p.setCycleCount(p.INDEFINITE);
		p.play();
	}
	public void stopBGM() {
		p.dispose();
	}
}
