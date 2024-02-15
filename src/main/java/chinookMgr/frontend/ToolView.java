package chinookMgr.frontend;

import org.jetbrains.annotations.NotNull;

public abstract class ToolView extends View {
	private final InputValidator validator = new InputValidator();

	protected void buildForNew() {
		this.build();
	}

	protected void buildForEntity() {
		this.build();
	}

	public InputValidator getValidator() {
		return this.validator;
	}

	public abstract @NotNull String getName();

	public boolean enableBackButton() {
		return true;
	}

	public static abstract class Supplier<T> extends ToolView { }
}