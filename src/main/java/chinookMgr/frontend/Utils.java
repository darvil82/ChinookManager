package chinookMgr.frontend;

import chinookMgr.frontend.components.TableInspector;
import chinookMgr.frontend.toolViews.GenericTableView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Utils {
	private Utils() {}

	public static <TView extends ToolView, TEnt> void attachViewSelectorToButton(
		@NotNull JButton button,
		@NotNull Supplier<TEnt> valueRef, @NotNull String title,
		@NotNull TableInspector<TEnt> tableInspector,
		@NotNull Consumer<TEnt> onSelection,
		@NotNull Function<TEnt, TView> onNew
	) {
		var atAttachValue = valueRef.get();
		button.setText(atAttachValue == null ? "Seleccionar " + title + "..." : atAttachValue.toString());

		button.addActionListener(e -> {
			if ((e.getModifiers() & ActionEvent.SHIFT_MASK) != 0) {
				var value = valueRef.get();
				if (value == null) return;
				WindowedToolView.display(MainMenu.INSTANCE, onNew.apply(value));
				return;
			}

			if ((e.getModifiers() & ActionEvent.CTRL_MASK) != 0) {
				onSelection.accept(null);
				button.setText("Seleccionar " + title + "...");
				return;
			}

			ViewStack.current().pushAwait(
				new GenericTableView<>("Selección de " + title, tableInspector.submitValueOnRowClick()),
				v -> {
					onSelection.accept(v);
					button.setText(v.toString());
				}
			);
		});
	}


	public static @NotNull String formatMillis(long millis) {
		int minutes = (int) (millis / (1000 * 60)) % 60;
		int seconds = (int) (millis / 1000) % 60;
		return String.format("%02d:%02d", minutes, seconds);
	}


	public static void addViewStackHotkeys(@NotNull RootPaneContainer frame, @NotNull JButton btnPrev) {
		frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
			KeyStroke.getKeyStroke("ctrl W"), "popViewStack"
		);
		frame.getRootPane().getActionMap().put("popViewStack", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!btnPrev.isEnabled()) return;
				ViewStack.current().pop();
			}
		});
	}
}