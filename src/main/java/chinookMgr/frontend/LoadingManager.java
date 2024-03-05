package chinookMgr.frontend;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Consumer;

public class LoadingManager {
	private static ArrayList<LoadingTask> tasks = new ArrayList<>();
	public static Consumer<LoadingTask> onTaskChange;

	public static void push(String status, int max) {
		tasks.add(new LoadingTask(status, 0, max));
		notifyTaskChange();
	}

	public static void pushIntermediate(String status) {
		tasks.add(new LoadingTask(status, 0, -1));
		notifyTaskChange();
	}

	public static void pushPop(String status, Runnable runnable) {
		pushIntermediate(status);

		new Thread(() -> {
			try {
				runnable.run();
			} finally {
				pop();
			}
		}).start();
	}

	public static void pop() {
		tasks.removeLast();
		notifyTaskChange();
	}

	public static void progress(@NotNull String status) {
		getTop().progress();
		getTop().setStatus(status);
		notifyTaskChange();
	}

	public static void progress() {
		getTop().progress();
		notifyTaskChange();
	}

	private static @NotNull LoadingManager.LoadingTask getTop() {
		return tasks.getLast();
	}

	private static void notifyTaskChange() {
		if (onTaskChange == null) return;

		onTaskChange.accept(tasks.isEmpty() ? null : getTop());
	}

	public static class LoadingTask {
		private String status;
		private final int max;
		private int progress;

		public LoadingTask(@NotNull String status, int progress, int max) {
			this.status = status;
			this.progress = progress;
			this.max = max;
		}

		public void progress() {
			if (this.progress >= this.max) return;
			this.progress++;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getStatus() {
			return status;
		}

		public int getProgress() {
			return progress;
		}

		public int getMax() {
			return max;
		}

		public boolean isIndeterminate() {
			return this.max == -1;
		}
	}
}