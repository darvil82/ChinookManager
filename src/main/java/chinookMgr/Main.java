package chinookMgr;

import chinookMgr.backend.Configuration;
import chinookMgr.backend.UserManager;
import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.frontend.ConnectionAwaiter;
import chinookMgr.frontend.ErrorDialog;
import chinookMgr.frontend.MainMenu;
import chinookMgr.shared.Utils;
import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;

import javax.swing.*;


public class Main {
	public static void main(String[] args) {
		Configuration.loadFromFile();
		FlatOneDarkIJTheme.setup();

		var app = MainMenu.INSTANCE;
		var connectionLostDialog = new ConnectionAwaiter(app);

		try {
			HibernateUtil.init(b -> connectionLostDialog.setVisible(!b));
		} catch (Exception e) {
			ErrorDialog.display(app, "No se pudo establecer conexión con la base de datos", e);
			System.exit(1);
		}

		app.setLocationRelativeTo(null);
		app.setVisible(true);
	}
}