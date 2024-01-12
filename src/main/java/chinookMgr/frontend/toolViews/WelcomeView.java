package chinookMgr.frontend.toolViews;

import chinookMgr.backend.UserManager;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.View;
import chinookMgr.frontend.ViewStack;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class WelcomeView implements ToolView {
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
	public void onReMount(ToolView prevView) {
		if (!UserManager.isLoggedIn()) return;

		this.btnLogin.setVisible(false);
		this.txtWelcome.setText("Bienvenido, " + UserManager.getCurrentUser().getFullName() + "!");
	}
}