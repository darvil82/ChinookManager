package chinookMgr.frontend;

import chinookMgr.frontend.components.TableInspector;
import chinookMgr.frontend.toolViews.GenericTableView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
			// if user is holding ctrl...
			if ((e.getModifiers() & ActionEvent.CTRL_MASK) != 0) {
				var value = valueRef.get();
				if (value == null) return;
				WindowedToolView.display(MainMenu.INSTANCE, onNew.apply(value));
				return;
			}

			ViewStack.current().pushAwait(
				new GenericTableView<>("Selección de " + title, tableInspector.submitValueOnRowClick()), v -> {
					onSelection.accept(v);
					button.setText(v.toString());
				}
			);
		});
	}
}