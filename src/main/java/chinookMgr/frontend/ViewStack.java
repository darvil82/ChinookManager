package chinookMgr.frontend;

import chinookMgr.frontend.toolViews.WelcomeView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class ViewStack {
	private final ArrayList<ToolView> views = new ArrayList<>();
	private final HashMap<ToolView.Supplier<?>, Consumer<?>> awaiters = new HashMap<>();
	public @Nullable Consumer<ToolView> onViewChange;
	public @Nullable Runnable onStackPop;

	private static List<ViewStack> stacks = new ArrayList<>();

	public static ViewStack current() {
		return stacks.getLast();
	}

	public static JPanel currentPanel() {
		return current().getTop().getPanel();
	}

	public static void pushViewStack(ViewStack stack) {
		stacks.add(stack);
	}

	public static ViewStack pushViewStack() {
		var stack = new ViewStack();
		pushViewStack(stack);
		return stack;
	}

	public static void popViewStack() {
		var cb = stacks.removeLast().onStackPop;

		if (cb != null)
			cb.run();
	}

	public static void popAllButLast() {
		while (stacks.size() > 1) {
			popViewStack();
		}
	}


	private void notifyViewChange() {
		if (onViewChange == null) return;
		onViewChange.accept(views.isEmpty() ? null : getTop());
	}

	public void push(@NotNull ToolView view) {
		views.add(view);
		notifyViewChange();
	}

	public <T> void pushAwait(@NotNull ToolView.Supplier<T> view, @NotNull Consumer<T> onPop) {
		views.add(view);
		awaiters.put(view, onPop);
		notifyViewChange();
	}

	public void replace(@NotNull ToolView view) {
		views.clear();
		views.add(view);
		awaiters.clear();
		notifyViewChange();
	}

	public void replaceWithWelcome() {
		this.replace(new WelcomeView());
	}

	public void pop() {
		var prevTop = getTop();
		views.removeLast();

		if (prevTop instanceof ToolView.Supplier<?> supplier) {
			awaiters.remove(supplier);
		}

		notifyViewChange();
		if (views.isEmpty()) return;
		getTop().onReMount(prevTop);
	}

	@SuppressWarnings("unchecked")
	public void popSubmit(@NotNull Object obj) {
		var prevTop = getTop();
		views.removeLast();

		if (prevTop instanceof ToolView.Supplier<?> supplier) {
			var callback = (Consumer<Object>)awaiters.get(supplier);
			if (callback != null) {
				callback.accept(obj);
				awaiters.remove(supplier);
			}
		}

		notifyViewChange();
		if (views.isEmpty()) return;
		getTop().onReMount(prevTop);
	}

	public @NotNull ToolView getTop() {
		return views.getLast();
	}

	public @NotNull String getAbsPath() {
		var path = new StringBuilder();
		for (int i = 0; i < views.size() - 1; i++)
			path.append(views.get(i).getName())
				.append(" > ");
		return path.toString();
	}

	public void clear() {
		views.clear();
		notifyViewChange();
	}
}