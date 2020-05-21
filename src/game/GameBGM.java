package game;
import java.io.File;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class GameBGM {
	
	private MediaPlayer p;
	
	public GameBGM() {
		JFXPanel panel = new JFXPanel();
		File file = new File("gamebgm.mp3");
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
