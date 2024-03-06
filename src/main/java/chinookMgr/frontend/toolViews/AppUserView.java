package chinookMgr.frontend.toolViews;

import chinookMgr.backend.Configuration;
import chinookMgr.backend.Saveable;
import chinookMgr.backend.User;
import chinookMgr.backend.UserManager;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.components.SaveOption;
import chinookMgr.frontend.toolViews.user.UserView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class AppUserView extends ToolView implements Saveable {
	private JPanel userViewPanel;
	private JPanel savePanel;
	private JPanel mainPanel;
	private JSpinner numRowsPerPage;
	private JButton btnLogout;

	private final UserView<User> userView;

	public AppUserView(@NotNull User user) {
		this.userView = new UserView<>(user);
		this.buildForEntity();
	}

	@Override
	protected void buildForEntity() {
		super.buildForEntity();

		this.insertView(this.userViewPanel, this.userView);
	}

	@Override
	protected void build() {
		super.build();

		this.btnLogout.addActionListener(e -> {
			UserManager.logout();
			ViewStack.current().replaceWithWelcome();
		});
		this.numRowsPerPage.setModel(new SpinnerNumberModel(Configuration.current().rowsPerPage, 15, 1000, 15));

		this.getInputManager().register(this.userView.getInputManager());
		this.getValidator().register(this.userView.getValidator());
		this.insertView(this.savePanel, new SaveOption<>(this, null));
	}

	@Override
	public void save() {
		this.userView.saveCurrent();
		Configuration.current().rowsPerPage = (int)this.numRowsPerPage.getValue();
		Configuration.current().save();
		UserManager.fireUserEntityUpdate();
	}

	@Override
	public @NotNull String getName() {
		return "Usuario";
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}
}