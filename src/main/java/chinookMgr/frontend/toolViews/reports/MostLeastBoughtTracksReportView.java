package chinookMgr.frontend.toolViews.reports;

import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.toolViews.GenericTableView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MostLeastBoughtTracksReportView extends ToolView {
	private JPanel mostPanel;
	private JPanel leastPanel;
	private JPanel mainPanel;

	public MostLeastBoughtTracksReportView() {
		this.insertView(this.mostPanel, new GenericTableView<>("Canciones más compradas", TrackReportsTableInspectors.getBoughtTracksTableInspector(true)));
		this.insertView(this.leastPanel, new GenericTableView<>("Canciones menos compradas", TrackReportsTableInspectors.getBoughtTracksTableInspector(false)));
	}

	@Override
	public @NotNull String getName() {
		return "Canciones más/menos compradas";
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}
}