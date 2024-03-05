package chinookMgr.frontend;

import org.jetbrains.annotations.NotNull;

public abstract class ToolView extends View {
	private final InputValidator validator = new InputValidator();
	private final InputManager enabler = new InputManager();

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

	public InputManager getInputManager() {
		return enabler;
	}

	@Override
	public abstract @NotNull String getName();

	public boolean enableBackButton() {
		return true;
	}

	public static abstract class Submitter<T> extends ToolView { }
}