package chinookMgr.frontend.toolViews;

import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.components.TableInspector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class GenericTableView<T> extends ToolView.Supplier<T> {
	private final @NotNull JPanel mainPanel;
	private final @NotNull String name;

	public GenericTableView(@NotNull String name, @NotNull TableInspector<T> inspector) {
		this.mainPanel = new JPanel();
		this.name = name;

		this.insertView(
			this.mainPanel,
			inspector
		);

		this.build();
	}

	@Override
	protected void build() {

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