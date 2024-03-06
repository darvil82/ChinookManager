package chinookMgr.frontend.toolViews;

import chinookMgr.backend.Configuration;
import chinookMgr.backend.Saveable;
import chinookMgr.backend.User;
import chinookMgr.backend.UserManager;
import chinookMgr.frontend.StatusManager;
import chinookMgr.frontend.Theme;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.components.SaveOption;
import chinookMgr.frontend.toolViews.user.UserView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Objects;

public class AppUserView extends ToolView implements Saveable {
	private JPanel userViewPanel;
	private JPanel savePanel;
	private JPanel mainPanel;
	private JSpinner numRowsPerPage;
	private JButton btnLogout;
	private JComboBox<Theme> comboTheme;
	private JSpinner numStatusDelay;
	private JButton btnReset;

	private final UserView<User> userView;
	private final Theme OLD_THEME = Theme.getCurrent();
	private boolean previewedTheme = false;


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

		this.btnReset.addActionListener(e -> this.resetConfigInputs());
		this.comboTheme.setModel(new DefaultComboBoxModel<>(Theme.values()));
		this.comboTheme.setSelectedItem(Configuration.current().theme);
		// show preview of new theme
		this.comboTheme.addActionListener(e -> this.previewTheme());

		this.btnLogout.addActionListener(e -> {
			UserManager.logout();
			ViewStack.current().replaceWithWelcome();
		});

		this.numRowsPerPage.setModel(new SpinnerNumberModel(Configuration.current().rowsPerPage, 15, 1000, 15));
		this.numStatusDelay.setModel(new SpinnerNumberModel(Configuration.current().statusMessageDelay, 150, 5000, 150));

		this.getInputManager().register(this.userView.getInputManager());
		this.getValidator().register(this.userView.getValidator());
		this.insertView(this.savePanel, new SaveOption<>(this, null));
	}

	private void resetConfigInputs() {
		Configuration cfg = Configuration.getDefault();
		this.comboTheme.setSelectedItem(cfg.theme);
		this.numRowsPerPage.setValue(cfg.rowsPerPage);
		this.numStatusDelay.setValue(cfg.statusMessageDelay);
	}

	private void previewTheme() {
		Theme theme = (Theme)this.comboTheme.getSelectedItem();
		if (theme == null || theme == this.OLD_THEME || this.previewedTheme) return;

		theme.apply();
		StatusManager.showUpdate("Previsualizando tema '" + theme.name + "'...");
		this.previewedTheme = true;
	}

	@Override
	public void save() {
		this.userView.saveCurrent();

		final var cfg = Configuration.current();

		if (cfg.theme != this.comboTheme.getSelectedItem()) {
			cfg.theme = (Theme)Objects.requireNonNull(this.comboTheme.getSelectedItem());
			this.previewedTheme = false;
		}

		cfg.rowsPerPage = (int)this.numRowsPerPage.getValue();
		cfg.statusMessageDelay = (int)this.numStatusDelay.getValue();

		cfg.save();
		UserManager.fireUserEntityUpdate();
	}

	@Override
	public void dispose() {
		if (this.previewedTheme)
			this.OLD_THEME.apply();
		super.dispose();
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