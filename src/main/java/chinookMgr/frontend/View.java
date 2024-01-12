package chinookMgr.frontend;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public interface View {

	public abstract @NotNull JPanel getPanel();

	public default void onReMount(@Nullable ToolView prevView) {}
}