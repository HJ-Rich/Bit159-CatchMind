package login;

import java.io.File;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class ButtonSound {
	
	public ButtonSound() {
		JFXPanel panel = new JFXPanel();
		File file = new File("button.wav");
		String path = "file:/" + file.getAbsolutePath();
		Media m = new Media(path.replace("\\", "/"));
		MediaPlayer p = new MediaPlayer(m);
	    p.play();
	}
	
}
