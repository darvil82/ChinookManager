package chinookMgr;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.frontend.MainMenu;
import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;


public class Main {
	public static void main(String[] args) {
		FlatOneDarkIJTheme.setup();
		// disable hibernate logging


		HibernateUtil.init(b -> System.out.println("Hibernate: " + b));

		var app = MainMenu.INSTANCE;
		app.setLocationRelativeTo(null);
		app.setVisible(true);
	}
}