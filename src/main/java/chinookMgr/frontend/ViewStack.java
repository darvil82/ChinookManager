package chinookMgr.frontend;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class ViewStack {
	private static final ArrayList<ToolView> views = new ArrayList<>();
	private static final HashMap<ToolView.Supplier<?>, Consumer<?>> awaiters = new HashMap<>();
	public static @Nullable Consumer<ToolView> onViewChange;


	private static void notifyViewChange() {
		if (onViewChange == null) return;
		onViewChange.accept(views.isEmpty() ? null : getTop());
	}

	public static void push(@NotNull ToolView view) {
		views.add(view);
		notifyViewChange();
	}

	public static <T> void pushAwait(@NotNull ToolView.Supplier<T> view, @NotNull Consumer<T> onPop) {
		views.add(view);
		awaiters.put(view, onPop);
		notifyViewChange();
	}

	public static void replace(@NotNull ToolView view) {
		views.clear();
		views.add(view);
		awaiters.clear();
		notifyViewChange();
		getTop().onMount(null);
	}

	public static void pop() {
		var prevTop = getTop();
		views.removeLast();

		if (prevTop instanceof ToolView.Supplier<?> supplier) {
			awaiters.remove(supplier);
		}

		notifyViewChange();
		getTop().onMount(prevTop);
	}

	@SuppressWarnings("unchecked")
	public static void popSubmit(@NotNull Object obj) {
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
		getTop().onMount(prevTop);
	}

	public static @NotNull ToolView getTop() {
		return views.getLast();
	}

	public static @NotNull String getAbsPath() {
		var path = new StringBuilder();
		for (int i = 0; i < views.size() - 1; i++)
			path.append(views.get(i).getName())
				.append(" > ");
		return path.toString();
	}

	public static void clear() {
		views.clear();
		notifyViewChange();
	}
}