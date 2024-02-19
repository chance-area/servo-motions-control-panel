package ru.chancearea.servomotionscontrolpanel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;
import com.formdev.flatlaf.FlatDarculaLaf;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class DesktopLauncher extends JFrame {
	public DesktopLauncher() {
		double screenScaleFactor = Toolkit.getDefaultToolkit().getScreenResolution() / 96.0f;

		SwingUtilities.invokeLater(() -> {
			setTitle(GlobalConstants.APP_TITLE + " (v. " + GlobalConstants.APP_VERSION + ")");
			setSize(new Dimension((int) (GlobalVariables.windowWidth / screenScaleFactor + 14), (int) (GlobalVariables.windowHeight / screenScaleFactor + 37)));
			setPreferredSize(new Dimension(getWidth(), getHeight()));
			setLocationRelativeTo(null);

			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setUndecorated(false);
			setResizable(true);
			setLayout(new GridLayout(1, 1));
			setAlwaysOnTop(false);
			setAutoRequestFocus(true);
			setIconImage(new ImageIcon("./app_icon.png").getImage());

			add(new LwjglCanvas(new ServoMotionsControlPanel(this), getAppConfig()).getCanvas(), SwingConstants.CENTER);

			createBufferStrategy(1);
			setVisible(true);
			toFront();
			requestFocus();
		});
	}

	private LwjglApplicationConfiguration getAppConfig() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width         = (int) GlobalVariables.windowWidth;
		config.height        = (int) GlobalVariables.windowHeight;
		config.resizable     = false;
		config.vSyncEnabled  = true;
		config.forceExit     = false;
		config.backgroundFPS = GlobalConstants.FPS_LIMIT;
		config.foregroundFPS = GlobalConstants.FPS_LIMIT;
		config.r       = 8;
		config.g       = 8;
		config.b       = 8;
		config.a       = 8;
		config.samples = 4;
		config.depth   = 16;
		config.stencil = 2;
		config.useGL30 = true; // Warring: this is experimental!

		return config;
	}

	public static void main (String[] arg) {
		try {
			UIManager.setLookAndFeel(new FlatDarculaLaf());
		} catch (UnsupportedLookAndFeelException _e) { /* ignore */ }

		new DesktopLauncher();
	}
}
