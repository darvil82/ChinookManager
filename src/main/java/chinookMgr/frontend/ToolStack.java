package chinookMgr.frontend;

import chinookMgr.frontend.components.Tool;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ToolStack {
	private static ArrayList<Tool> tools = new ArrayList<>();

	public static void push(@NotNull Tool tool) {
		tools.add(tool);
	}

	public static void pop() {
		tools.remove(tools.size() - 1);
	}

	public static @NotNull Tool getTop() {
		return tools.get(tools.size() - 1);
	}

	public static void clear() {
		tools.clear();
	}
}