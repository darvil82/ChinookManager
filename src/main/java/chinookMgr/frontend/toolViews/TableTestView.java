package chinookMgr.frontend.toolViews;

import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.View;
import chinookMgr.frontend.components.TableInspector;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class TableTestView implements ToolView {
	private JPanel mainPanel;
	private JPanel tableContainer;

	public TableTestView() {
		View.insert(this.tableContainer, new TableInspector());
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}

	@Override
	public @NotNull String getName() {
		return "Table Test";
	}
}