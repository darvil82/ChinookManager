package chinookMgr.frontend.toolViews.reports;

import chinookMgr.frontend.ToolView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ReportsView extends ToolView {
	private JPanel mainPanel;
	private JButton cancionesMásMenosCompradasButton;

	@Override
	public @NotNull String getName() {
		return "Reportes";
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}
}