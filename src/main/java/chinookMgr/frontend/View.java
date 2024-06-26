package chinookMgr.frontend;

import chinookMgr.shared.Builder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class View extends JFrame {
	private final @NotNull List<View> children = new ArrayList<>(0);
	private final @NotNull List<Consumer<View>> onReMountListeners = new ArrayList<>(0);

	protected void build() {}
	public abstract @NotNull JPanel getPanel();


	protected void onReMount(@Nullable ToolView prevView) {
		this.children.forEach(c -> c.onReMount(prevView));
		this.onReMountListeners.forEach(l -> l.accept(this));
	}

	public final void onReMount() {
		this.onReMount(null);
	}

	protected void insertView(@NotNull JPanel container, @NotNull Builder<? extends View> view) {
		this.insertView(container, view.build());
	}

	protected void insertView(@NotNull JPanel container, @NotNull View view) {
		// make sure the container has a BorderLayout
		if (!(container.getLayout() instanceof BorderLayout))
			container.setLayout(new BorderLayout());

		container.add(view.getPanel());
		this.children.add(view);
	}

	public void addOnReMountListener(@NotNull Consumer<View> listener) {
		this.onReMountListeners.add(listener);
	}

	public void removeOnReMountListener(@NotNull Consumer<View> listener) {
		this.onReMountListeners.remove(listener);
	}

	@Override
	public void dispose() {
		super.dispose();
		this.children.forEach(View::dispose);
		this.children.clear();
		this.onReMountListeners.clear();
	}
}