package chinookMgr.frontend;

import chinookMgr.shared.Builder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public abstract class View {
	private final @NotNull ArrayList<View> children = new ArrayList<>(0);

	protected void build() {}
	public abstract @NotNull JPanel getPanel();


	protected void onReMount(@Nullable ToolView prevView) {
		this.children.forEach(c -> c.onReMount(prevView));
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
}