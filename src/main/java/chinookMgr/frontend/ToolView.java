package chinookMgr.frontend;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class ToolView extends View {
	private final InputValidator validator = new InputValidator();

	protected void buildForNew() {
		this.build();
	}

	protected void buildForEntity() {
		this.build();
	}

	protected final void notifyChange() {
		this.onReMount();
		ViewStack.current().notifyViewChange();
	}

	public InputValidator getValidator() {
		return this.validator;
	}

	@Override
	public abstract @NotNull String getName();

	public boolean enableBackButton() {
		return true;
	}

	public @NotNull List<Class<? extends ToolView>> getAffectedViews() {
		return List.of();
	}

	public static abstract class Supplier<T> extends ToolView { }
}