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
	private final HashMap<ToolView.Submitter<?>, Consumer<?>> awaiters = new HashMap<>();
	public @Nullable Consumer<ToolView> onViewChange;
	public @Nullable Runnable onStackPop;

	private static List<ViewStack> stacks = new ArrayList<>();

	public static ViewStack current() {
		return stacks.getLast();
	}

	public static JPanel currentPanel() {
		return current().getTop().getPanel();
	}

	public static ViewStack pushViewStack() {
		var stack = new ViewStack();
		stacks.add(stack);
		return stack;
	}

	public static void popViewStack() {
		var cb = stacks.removeLast().onStackPop;

		if (cb != null)
			cb.run();

		if (!stacks.isEmpty())
			current().getTop().onReMount(); // Re-mount the top view
	}

	public static void popAllButLast() {
		while (stacks.size() > 1) {
			popViewStack();
		}
	}


	public void notifyViewChange() {
		if (onViewChange == null) return;

		if (views.isEmpty()) {
			onViewChange.accept(null);
			return;
		}

		var top = getTop();

		onViewChange.accept(top);
		top.getInputManager().apply();
	}

	public void push(@NotNull ToolView view) {
		views.add(view);
		notifyViewChange();
	}

	public <T> void pushAwait(@NotNull ToolView.Submitter<T> view, @NotNull Consumer<T> onPop) {
		views.add(view);
		awaiters.put(view, onPop);
		notifyViewChange();
	}

	public void replace(@NotNull ToolView view) {
		views.forEach(ToolView::dispose);
		views.clear();
		views.add(view);
		awaiters.clear();
		notifyViewChange();
	}

	public void replaceWithWelcome() {
		this.replace(new WelcomeView());
	}

	public void pop() {
		var prevTop = views.removeLast();
		prevTop.dispose();

		if (prevTop instanceof ToolView.Submitter<?> supplier) {
			awaiters.remove(supplier);
		}

		notifyViewChange();
		if (views.isEmpty()) return;
		getTop().onReMount(prevTop);
	}

	@SuppressWarnings("unchecked")
	public void popSubmit(@NotNull Object obj) {
		var prevTop = views.getLast();

		if (prevTop instanceof ToolView.Submitter<?> supplier) {
			var callback = (Consumer<Object>)awaiters.get(supplier);
			if (callback != null) {
				callback.accept(obj);
			}
		}

		pop();
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
		views.forEach(ToolView::dispose);
		views.clear();
		notifyViewChange();
	}
}