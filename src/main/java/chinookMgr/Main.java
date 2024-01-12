package chinookMgr;

import chinookMgr.frontend.MainMenu;

import javax.swing.*;


public class Main {
	public static void main(String[] args) {
		// use GTK look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		var app = new MainMenu();
		app.setLocationRelativeTo(null);
		app.setVisible(true);
	}
}