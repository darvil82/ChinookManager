package chinookMgr.frontend;


import chinookMgr.backend.User;
import chinookMgr.backend.UserManager;
import chinookMgr.backend.entityHelpers.Track;
import chinookMgr.frontend.components.Toolbar;
import chinookMgr.frontend.toolViews.GenericTableView;
import chinookMgr.frontend.toolViews.TestView;
import chinookMgr.frontend.toolViews.TrackView;
import chinookMgr.frontend.toolViews.WelcomeView;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

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
		this.setMinimumSize(new Dimension(600, 400));
		this.setPreferredSize(new Dimension(800, 525));
		this.pack();
		this.build();
	}

	private void onViewStackChange(@Nullable ToolView newView) {
		this.viewContent.removeAll();

		if (newView == null) {
			this.txtAbsViewPath.setVisible(false);
			this.btnPrev.setEnabled(false);
			this.toolbar.deactivateAll();
			ViewStack.replace(new WelcomeView());
			this.toolbar.toggleOption("Inicio", true);
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

	private synchronized void onBackButtonChange(boolean enabled) {
		this.btnPrev.setEnabled(enabled);
	}

	public void build() {
		this.toolbar = new Toolbar();
		this.toolbarContainer.add(this.toolbar);
		this.btnPrev.addActionListener(e -> ViewStack.pop());

		this.toolbar.addOption("Inicio", e -> ViewStack.replace(new WelcomeView()));
		this.toolbar.addOption("test 1", e -> ViewStack.replace(new TestView()));
		this.toolbar.addOption("test 2", e -> ViewStack.replace(
			new GenericTableView<>("Canciones", Track.getTableInspectorBuilder().openViewOnRowClick(TrackView::new))
		));

		this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
			KeyStroke.getKeyStroke("ctrl BACK_SPACE"), "popViewStack"
		);
		this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
			KeyStroke.getKeyStroke("alt LEFT"), "popViewStack"
		);
		this.getRootPane().getActionMap().put("popViewStack", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!btnPrev.isEnabled()) return;
				ViewStack.pop();
			}
		});


		ViewStack.onViewChange = this::onViewStackChange;
		LoadingManager.onTaskChange = this::onLoadingTaskChange;
		UserManager.onUserChange = this::onUserChange;
		StatusManager.statusLabel = this.txtStatus;
		StatusManager.onBackButtonChange = this::onBackButtonChange;
		this.onViewStackChange(null);
	}
}