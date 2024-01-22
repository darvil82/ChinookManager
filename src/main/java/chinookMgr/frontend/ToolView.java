package chinookMgr.frontend;

import org.jetbrains.annotations.NotNull;

public abstract class ToolView extends View {
	public abstract @NotNull String getName();

	public boolean disableBackButton() {
		return false;
	}

	public static abstract class Supplier<T> extends ToolView { }
}