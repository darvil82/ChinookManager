package chinookMgr.frontend.toolViews;

import chinookMgr.backend.db.entities.TrackEntity;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.View;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.components.TableInspector;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class TableTestView implements ToolView {
	private JPanel mainPanel;
	private JPanel tableContainer;

	public TableTestView() {
		View.insert(
			this.tableContainer,
			TableInspector.create(TrackEntity.class)
				.withQuerier("from TrackEntity where name like :search")
				.withCounter("select count(*) from TrackEntity where name like :search")
				.withRowClick(t -> ViewStack.push(new TrackView(t)))
		);

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