package chinookMgr;

import chinookMgr.frontend.MainMenu;
import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;


public class Main {
	public static void main(String[] args) {
		FlatOneDarkIJTheme.setup();

		var app = MainMenu.INSTANCE;
		app.setLocationRelativeTo(null);
		app.setVisible(true);
	}
}