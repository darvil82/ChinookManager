package chinookMgr.frontend.components.views;

import chinookMgr.backend.UserManager;
import chinookMgr.frontend.LoadingManager;
import chinookMgr.frontend.StatusManager;
import chinookMgr.frontend.ViewStack;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class LoginView extends View {
	private JTextField inputEmail;
	private JPanel mainPanel;
	private JPasswordField inputPassword;
	private JButton btnLogin;

	public LoginView() {
		this.btnLogin.addActionListener(e -> this.login());
	}

	private void login() {
		this.btnLogin.setEnabled(false);

		LoadingManager.push("Iniciando sesión...", () -> {
			if (UserManager.login(this.inputEmail.getText(), new String(this.inputPassword.getPassword()))) {
				ViewStack.pop();
				StatusManager.showUpdate("Sesión iniciada.");
			} else {
				JOptionPane.showMessageDialog(
					null,
					"El correo electrónico o la contraseña son incorrectos",
					"Error",
					JOptionPane.ERROR_MESSAGE
				);
				this.btnLogin.setEnabled(true);
			}
		});
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}

	@Override
	public @NotNull String getName() {
		return "Iniciar Sesión";
	}
}