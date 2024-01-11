package chinookMgr.frontend;

import chinookMgr.frontend.components.views.View;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ViewStack {
	private static ArrayList<View> views = new ArrayList<>();
	public static @Nullable Consumer<View> onViewChange;

	private static void notifyViewChange() {
		if (onViewChange == null) return;
		onViewChange.accept(views.size() == 0 ? null : getTop());
	}

	public static void push(@NotNull View view) {
		views.add(view);
		getTop().onReMount(null);
		notifyViewChange();
	}

	public static void replace(@NotNull View view) {
		views.clear();
		views.add(view);
		getTop().onReMount(null);
		notifyViewChange();
	}

	public static void pop() {
		var prevTop = getTop();
		views.remove(views.size() - 1);
		notifyViewChange();
		getTop().onReMount(prevTop);
	}

	public static @NotNull View getTop() {
		return views.get(views.size() - 1);
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