package chinookMgr.backend;

import chinookMgr.frontend.ViewStack;

public interface Saveable {
	void save();

	default void cancel() {
		ViewStack.pop();
	}
}