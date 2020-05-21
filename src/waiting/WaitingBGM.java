package waiting;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class WaitingBGM {
	private MediaPlayer p;
	public WaitingBGM() {
		JFXPanel panel = new JFXPanel();
		File file = new File("lobbybgm.mp3");
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
