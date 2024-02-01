package chinookMgr.backend;

import chinookMgr.frontend.ViewStack;

public interface Saveable {
	void save();

	default void cancel() {
		ViewStack.current().pop();
	}

	default boolean isDeletable() {
		return false;
	}

	default void delete() {}
}