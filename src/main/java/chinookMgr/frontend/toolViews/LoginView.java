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
		this.build();
	}


	@Override
	protected void build() {
		SwingUtilities.invokeLater(() -> this.inputEmail.grabFocus());
		this.inputEmail.addActionListener(e -> this.login());
		this.inputPassword.addActionListener(e -> this.login());
		this.btnLogin.addActionListener(e -> this.login());

		this.getValidator().register(this.inputEmail, e -> !e.getText().isBlank(), "El e-mail no puede estar vacío");
		this.getValidator().register(this.inputPassword, e -> e.getPassword().length > 0, "La contraseña no puede estar vacía");
	}

	private void login() {
		if (!this.getValidator().validate())
			return;

		StatusManager.disableBackButton();

		LoadingManager.pushPop("Iniciando sesión...", () -> {

			boolean loginResult = UserManager.login(this.inputEmail.getText().trim(), new String(this.inputPassword.getPassword()));
			StatusManager.enableBackButton();

			if (!loginResult) {
				JOptionPane.showMessageDialog(
					this.mainPanel,
					"El e-mail o la contraseña son incorrectos",
					"Error",
					JOptionPane.ERROR_MESSAGE
				);

				this.inputPassword.grabFocus();
				return;
			}

			ViewStack.current().pop();
			StatusManager.showUpdate("Sesión iniciada.");
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