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
	private @Nullable Consumer<T> selectorHandler;

	public GenericTableView(@NotNull String name, @NotNull TableInspector<T> inspector) {
		this.mainPanel = new JPanel();
		this.name = name;

		this.insertView(
			this.mainPanel,
			inspector.onRowClick(this::onRowClick)
		);
	}

	public GenericTableView<T> attachSelectionHandler(@NotNull Consumer<T> handler) {
		this.selectorHandler = handler;
		return this;
	}

	public GenericTableView<T> attachSelectionView(@NotNull Function<T, ToolView> viewCtor) {
		this.selectorHandler = e -> ViewStack.push(viewCtor.apply(e));
		return this;
	}

	private void onRowClick(T el) {
		if (this.selectorHandler != null)
			this.selectorHandler.accept(el);
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