package chinookMgr.frontend;

import org.jetbrains.annotations.NotNull;

public abstract class ToolView extends View {
	public abstract @NotNull String getName();

	public boolean enableBackButton() {
		return true;
	}

	public static abstract class Supplier<T> extends ToolView { }
}