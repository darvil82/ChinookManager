package chinookMgr.frontend;

import chinookMgr.backend.Configuration;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.function.Consumer;

public abstract class StatusManager {
	public static JLabel statusLabel;
	private static HideThread hideThread;
	public static Consumer<Boolean> onBackButtonChange;

	public static void showUpdate(@NotNull String status) {
		statusLabel.setText(status);
		if (hideThread != null) hideThread.interrupt();
		hideThread = new HideThread();
	}

	public static void setStatus(@NotNull String status) {
		if (hideThread != null) hideThread.interrupt();
		statusLabel.setText(status);
	}

	public static void clearStatus() {
		if (hideThread == null)
			statusLabel.setText("");
	}

	public static void disableBackButton() {
		if (onBackButtonChange != null) onBackButtonChange.accept(false);
	}

	public static void enableBackButton() {
		if (onBackButtonChange != null) onBackButtonChange.accept(true);
	}


	private static class HideThread extends Thread {
		public HideThread() {
			this.start();
		}

		@Override
		public void run() {
			try {
				Thread.sleep(Configuration.current().statusMessageDelay);
			} catch (InterruptedException e) {
				StatusManager.hideThread = null;
				return;
			}

			StatusManager.statusLabel.setText("");
			StatusManager.hideThread = null;
		}
	}
}