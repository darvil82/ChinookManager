package chinookMgr.frontend.toolViews;

import chinookMgr.backend.UserManager;
import chinookMgr.frontend.LoadingManager;
import chinookMgr.frontend.StatusManager;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.ViewStack;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class LoginView extends ToolView {
	private JTextField inputEmail;
	private JPanel mainPanel;
	private JPasswordField inputPassword;
	private JButton btnLogin;

	public LoginView() {
		SwingUtilities.invokeLater(() -> this.inputEmail.grabFocus());
		this.inputEmail.addActionListener(e -> this.login());
		this.inputPassword.addActionListener(e -> this.login());
		this.btnLogin.addActionListener(e -> this.login());
	}

	private void login() {
		this.btnLogin.setEnabled(false);
		this.inputEmail.setEnabled(false);
		this.inputPassword.setEnabled(false);
		StatusManager.disableBackButton();

		LoadingManager.pushPop("Iniciando sesión...", () -> {
			boolean loginResult = UserManager.login(this.inputEmail.getText().trim(), new String(this.inputPassword.getPassword()));
			StatusManager.enableBackButton();

			if (loginResult) {
				ViewStack.pop();
				StatusManager.showUpdate("Sesión iniciada.");
			} else {
				JOptionPane.showMessageDialog(
					this.mainPanel,
					"El correo electrónico o la contraseña son incorrectos",
					"Error",
					JOptionPane.ERROR_MESSAGE
				);

				this.btnLogin.setEnabled(true);
				this.inputEmail.setEnabled(true);
				this.inputPassword.setEnabled(true);
				this.inputPassword.grabFocus();
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