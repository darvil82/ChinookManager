package chinookMgr.frontend;

import chinookMgr.backend.entityHelpers.Genre;
import chinookMgr.frontend.components.TableComboBox;
import chinookMgr.frontend.components.TableInspector;
import chinookMgr.frontend.toolViews.GenericTableView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class Utils {
	private Utils() {}

	public static <T> void attachViewSelectorToButton(
		@NotNull JButton button,
		@NotNull String title,
		@NotNull TableInspector<T> tableInspector,
		@NotNull Consumer<T> onSelection,
		@NotNull Supplier<ToolView> onNew
	) {
		button.addActionListener(e -> {
			// if user is holding ctrl...
			if ((e.getModifiers() & ActionEvent.CTRL_MASK) != 0) {
				WindowedToolView.display(MainMenu.INSTANCE, onNew.get());
				return;
			}

			ViewStack.current().pushAwait(
				new GenericTableView<>("Selección de " + title, tableInspector.submitValueOnRowClick()), onSelection
			);
		});
	}
}