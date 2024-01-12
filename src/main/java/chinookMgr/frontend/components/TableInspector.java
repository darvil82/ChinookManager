package chinookMgr.frontend.components;

import chinookMgr.frontend.View;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class TableInspector<T> implements View {
	private JTextField inputSearch;
	private JButton btnNew;
	private JTable resultTable;
	private JSpinner numPage;
	private JButton btnForward;
	private JButton btnBack;
	private JLabel lblPagesTotal;
	private JPanel mainPanel;

	public TableInspector() {
	}


	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}

}