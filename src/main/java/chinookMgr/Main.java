package chinookMgr;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.frontend.ConnectionAwaiter;
import chinookMgr.frontend.MainMenu;
import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;

import javax.swing.*;


public class Main {
	public static void main(String[] args) {
		FlatOneDarkIJTheme.setup();

		var app = MainMenu.INSTANCE;
		var connectionLostDialog = new ConnectionAwaiter(app);

		if (!HibernateUtil.init(b -> connectionLostDialog.setVisible(!b))) {
			JOptionPane.showMessageDialog(
				app,
				"No se pudo establecer conexión con la base de datos",
				"Error",
				JOptionPane.ERROR_MESSAGE
			);
			System.exit(1);
		}

		app.setLocationRelativeTo(null);
		app.setVisible(true);
	}
}