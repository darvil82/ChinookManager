package chinookMgr.frontend.toolViews;

import chinookMgr.backend.UserManager;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.ViewStack;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class WelcomeView extends ToolView {
	private JPanel mainPanel;
	private JButton btnLogin;
	private JLabel txtWelcome;

	public WelcomeView() {
		this.btnLogin.addActionListener(e -> ViewStack.current().push(new LoginView()));
		this.onReMount(null);
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}

	@Override
	public @NotNull String getName() {
		return "Inicio";
	}

	public boolean enableBackButton() {
		return false;
	}

	@Override
	public void onReMount(ToolView prevView) {
		if (!UserManager.isLoggedIn()) return;

		this.btnLogin.setVisible(false);
		this.txtWelcome.setText("Bienvenido, " + UserManager.getCurrentUser().getFullName() + "!");
	}
}