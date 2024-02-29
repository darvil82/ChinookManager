package chinookMgr.frontend;

import org.jetbrains.annotations.NotNull;

public abstract class ToolView extends View {
	private final InputValidator validator = new InputValidator();
	private final InputEnabler enabler = new InputEnabler();

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

	public InputEnabler getInputManager() {
		return enabler;
	}

	@Override
	public abstract @NotNull String getName();

	public boolean enableBackButton() {
		return true;
	}

	public static abstract class Supplier<T> extends ToolView { }
}