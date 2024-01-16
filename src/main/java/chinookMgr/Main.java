package chinookMgr;

import chinookMgr.frontend.MainMenu;

import javax.swing.*;


public class Main {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// fallback to default, which is absolutely terrible and barely works
			if (JOptionPane.showConfirmDialog(
				null,
			"""
   				Ha ocurrido un error al establecer el tema. Se usará el tema por defecto de Java.
   				Tenga en cuenta que esto causará problemas visuales y no se recomienda usar la aplicación.
   				
   				¿Continuar de todas formas?
			""",
				"Error",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.ERROR_MESSAGE
			) == JOptionPane.NO_OPTION)
				System.exit(0);
		}

		var app = new MainMenu();
		app.setLocationRelativeTo(null);
		app.setVisible(true);
	}
}