package chinookMgr.frontend.components.views;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class SongsView extends View {
	private JTextField inputSearch;
	private JButton btnNew;
	private JTable resultTable;
	private JSpinner numPage;
	private JButton btnForward;
	private JButton btnBack;
	private JLabel lblPagesTotal;
	private JPanel mainPanel;

	public SongsView() {
		// fit to parent
		this.mainPanel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		this.mainPanel.setAlignmentY(JComponent.CENTER_ALIGNMENT);
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}

	@Override
	public @NotNull String getName() {
		return "Canciones";
	}
}