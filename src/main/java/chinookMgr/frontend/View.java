package chinookMgr.frontend;

import chinookMgr.shared.Builder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;

public abstract class View {
	private ArrayList<View> children;

	public abstract @NotNull JPanel getPanel();

	protected void onReMount(@Nullable ToolView prevView) {
		if (this.children == null) return;
		this.children.forEach(c -> c.onReMount(prevView));
	}

	protected void insertView(@NotNull JPanel container, @NotNull Builder<? extends View> view) {
		this.insertView(container, view.build());
	}

	protected void insertView(@NotNull JPanel container, @NotNull View view) {
		container.add(view.getPanel());
		if (this.children == null) this.children = new ArrayList<>();
		this.children.add(view);
	}
}