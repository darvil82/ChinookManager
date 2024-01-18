package chinookMgr.frontend.toolViews;

import chinookMgr.backend.db.entities.TrackEntity;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.components.TableInspector;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class TableTestView extends ToolView.Supplier<TrackEntity> {
	private JPanel mainPanel;
	private JPanel tableContainer;

	public TableTestView() {
		this.insertView(
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

	@Override
	protected TrackEntity submit() {
		return null;
	}
}