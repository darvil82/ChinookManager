package chinookMgr.frontend.toolViews;

import chinookMgr.backend.db.entities.PlaylistEntity;
import chinookMgr.backend.db.entities.TrackEntity;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.components.TableInspector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class GenericTableView<T> extends ToolView.Supplier<T> {
	private final @NotNull JPanel mainPanel;
	private final @NotNull String name;
	private final @NotNull TableInspector<T> inspector;

	public GenericTableView(@NotNull String name, @NotNull TableInspector<T> inspector) {
		this.mainPanel = new JPanel();
		this.name = name;
		this.inspector = inspector;

		this.insertView(this.mainPanel, inspector);

		this.build();
	}

	@Override
	public void setEnabled(boolean b) {
		this.inspector.setEnabled(b);
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}

	@Override
	public @NotNull String getName() {
		return this.name;
	}


	public static <T> BiConsumer<MouseEvent, T> handleSpecial(Consumer<T> onSpecialClick, Function<T, ToolView> view) {
		return (e, track) -> {
			// is the user holding the ctrl key?
			if (e.isControlDown()) {
				onSpecialClick.accept(track);
				return;
			}

			ViewStack.current().push(view.apply(track));
		};
	}
}