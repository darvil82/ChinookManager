package chinookMgr.frontend.components.views;

import chinookMgr.backend.UserManager;
import chinookMgr.frontend.ViewStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class WelcomeView extends View {
	private JPanel mainPanel;
	private JButton btnLogin;
	private JLabel txtWelcome;

	public WelcomeView() {
		this.btnLogin.addActionListener(e -> ViewStack.push(new LoginView()));
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}

	@Override
	public @NotNull String getName() {
		return "Inicio";
	}

	@Override
	public boolean disableBackButton() {
		return true;
	}

	@Override
	public void onReMount(View prevView) {
		if (!UserManager.isLoggedIn()) return;

		this.btnLogin.setVisible(false);
		this.txtWelcome.setText("Bienvenido, " + UserManager.getCurrentUser().getFullName() + "!");
	}
}