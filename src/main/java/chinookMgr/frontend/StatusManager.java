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
		if (hideThread == null || !hideThread.isActive())
			statusLabel.setText("");
	}


}

class HideThread extends Thread {
	private boolean active = true;

	public HideThread() {
		this.start();
	}

	public boolean isActive() {
		return active;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			return;
		}

		StatusManager.statusLabel.setText("");
		this.active = false;
	}
}