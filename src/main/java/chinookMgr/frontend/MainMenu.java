package chinookMgr.frontend;


import chinookMgr.backend.User;
import chinookMgr.backend.UserManager;
import chinookMgr.frontend.components.views.SongsView;
import chinookMgr.frontend.components.views.TestView;
import chinookMgr.frontend.components.views.View;
import chinookMgr.frontend.components.Toolbar;
import chinookMgr.frontend.components.views.WelcomeView;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {
	private JPanel mainPanel;
	private JPanel toolbarContainer;
	private JPanel viewContent;
	private JLabel txtCurrentViewName;
	private JButton btnPrev;
	private JLabel txtAbsViewPath;
	private JScrollPane pathScrollPane;
	private JProgressBar progressBar;
	private JLabel txtStatus;
	private JButton btnAccount;
	private Toolbar toolbar;

	public MainMenu() {
		super("Chinook Manager");
		this.setContentPane(this.mainPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(800, 500));
		this.pack();
		this.build();
	}

	private void onViewStackChange(@Nullable View newView) {
		this.viewContent.removeAll();

		if (newView == null) {
			this.txtAbsViewPath.setVisible(false);
			this.btnPrev.setEnabled(false);
			this.toolbar.deactivateAll();
			ViewStack.replace(new WelcomeView());
		} else {
			this.txtCurrentViewName.setText(newView.getName());
			this.viewContent.add(newView.getPanel());
			this.txtAbsViewPath.setText(ViewStack.getAbsPath());
			this.txtAbsViewPath.setVisible(true);
			this.btnPrev.setEnabled(!newView.disableBackButton());
		}

		SwingUtilities.invokeLater(() -> {
			this.pathScrollPane.getHorizontalScrollBar().setValue(this.pathScrollPane.getHorizontalScrollBar().getMaximum());
		});
		this.viewContent.revalidate();
		this.viewContent.repaint();
	}

	private void onLoadingTaskChange(@Nullable LoadingManager.LoadingTask task) {
		if (task == null) {
			this.progressBar.setValue(0);
			this.progressBar.setMaximum(0);
			StatusManager.clear();
			this.progressBar.setIndeterminate(false);
		} else {
			this.progressBar.setValue(task.getProgress());
			this.progressBar.setMaximum(task.getMax());
			this.txtStatus.setText(task.getStatus());
			StatusManager.setStatus(task.getStatus());
			this.progressBar.setIndeterminate(task.isIndeterminate());
		}
	}

	private void onUserChange(@Nullable User<?> newUser) {
		this.btnAccount.setVisible(newUser != null);
	}

	public void build() {
		this.toolbar = new Toolbar();
		this.toolbarContainer.add(this.toolbar);
		this.btnPrev.addActionListener(e -> ViewStack.pop());

		this.toolbar.addOption("Inicio", e -> ViewStack.replace(new WelcomeView()));
		this.toolbar.addOption("test 1", e -> ViewStack.replace(new TestView()));
		this.toolbar.addOption("test 2", e -> ViewStack.replace(new SongsView()));

		ViewStack.onViewChange = this::onViewStackChange;
		LoadingManager.onTaskChange = this::onLoadingTaskChange;
		UserManager.onUserChange = this::onUserChange;
		StatusManager.statusLabel = this.txtStatus;
		this.onViewStackChange(null);
	}
}