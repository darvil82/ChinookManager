package chinookMgr.frontend;

import chinookMgr.shared.Builder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public interface View {

	public abstract @NotNull JPanel getPanel();

	public default void onReMount(@Nullable ToolView prevView) {}

	public static void insert(@NotNull JPanel container, @NotNull Builder<? extends View> view) {
		insert(container, view.build());
	}

	public static void insert(@NotNull JPanel container, @NotNull View view) {
		container.add(view.getPanel());
	}
}