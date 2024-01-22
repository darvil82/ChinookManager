package chinookMgr.frontend.toolViews.test;

import chinookMgr.backend.db.entities.AlbumEntity;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.components.TableInspector;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class AlbumsView extends ToolView {
	private JPanel mainPanel;
	private JPanel tableContainer;

	public AlbumsView() {
		this(false);
	}

	public AlbumsView(boolean selector) {
		this.insertView(
			this.tableContainer,
			TableInspector.create(AlbumEntity.class)
				.withQuerier("from AlbumEntity where title like :search")
				.withCounter("select count(*) from AlbumEntity where title like :search")
				.withRowClick(t -> {
					if (selector)
						ViewStack.popSubmit(t);
				})
		);
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}

	@Override
	public @NotNull String getName() {
		return "Álbumes";
	}
}