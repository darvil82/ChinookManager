package chinookMgr.frontend.toolViews;

import chinookMgr.backend.UserManager;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.toolViews.user.LoginView;
import chinookMgr.frontend.toolViews.user.RegisterView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class WelcomeView extends ToolView {
	private JPanel mainPanel;
	private JButton btnLogin;
	private JLabel txtWelcome;
	private JButton btnRegister;

	public WelcomeView() {
		this.build();
		this.onReMount();
	}

	@Override
	protected void build() {
		this.btnLogin.addActionListener(e -> ViewStack.current().push(new LoginView()));
		this.btnRegister.addActionListener(e -> ViewStack.current().push(new RegisterView()));
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
	public boolean enableBackButton() {
		return false;
	}

	@Override
	public void onReMount(ToolView prevView) {
		if (!UserManager.isLoggedIn()) return;

		this.btnLogin.setVisible(false);
		this.btnRegister.setVisible(false);
		this.txtWelcome.setText("¡Bienvenido, " + UserManager.getCurrentUser().getFullName() + "!");
	}
}