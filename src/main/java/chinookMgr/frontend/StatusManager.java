package chinookMgr.frontend;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public abstract class StatusManager {
	public static JLabel statusLabel;
	private static HideThread hideThread;

	public static void showUpdate(@NotNull String status) {
		statusLabel.setText(status);
		if (hideThread != null) hideThread.interrupt();
		hideThread = new HideThread();
	}

	public static void setStatus(@NotNull String status) {
		if (hideThread != null) hideThread.interrupt();
		statusLabel.setText(status);
	}

	public static void clear() {
		if (hideThread == null)
			statusLabel.setText("");
	}


	static class HideThread extends Thread {
		public HideThread() {
			this.start();
		}

		@Override
		public void run() {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				StatusManager.hideThread = null;
				return;
			}

			StatusManager.statusLabel.setText("");
			StatusManager.hideThread = null;
		}
	}
}