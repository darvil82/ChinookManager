package chinookMgr.frontend;

import org.jetbrains.annotations.NotNull;

public interface ToolView extends View {
	public abstract @NotNull String getName();

	public default boolean disableBackButton() {
		return false;
	}
}