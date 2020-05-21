package game;
import java.io.File;
import java.io.FileInputStream;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javazoom.jl.player.Player;

public class GameAnswer  {

	private MediaPlayer p;
	
	public GameAnswer() {
		JFXPanel panel = new JFXPanel();
		File file = new File("answer.mp3");
		String path = "file:/" + file.getAbsolutePath();
		Media m = new Media(path.replace("\\", "/"));
		p = new MediaPlayer(m);
		p.play();
	}

}
