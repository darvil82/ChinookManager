package chinookMgr.frontend.toolViews;

import chinookMgr.backend.db.entities.TrackEntity;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.components.TableInspector;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class GenericTableView<T> extends ToolView.Supplier<T> {
	private final @NotNull JPanel mainPanel;
	private final @NotNull String name;

	public GenericTableView(@NotNull String name, boolean selector, @NotNull TableInspector.Builder<T> builder) {
		this.mainPanel = new JPanel();
		this.name = name;

		this.insertView(
			this.mainPanel,
			builder
		);
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}

	@Override
	public @NotNull String getName() {
		return this.name;
	}
}