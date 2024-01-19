package chinookMgr.frontend.toolViews.test;

import chinookMgr.backend.db.entities.AlbumEntity;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.components.TableInspector;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class AlbumsView extends ToolView.Supplier<AlbumEntity> {
	private JPanel mainPanel;
	private JPanel tableContainer;
	private AlbumEntity selectedAlbum;

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
					if (selector) {
						this.selectedAlbum = t;
						ViewStack.pop();
					} else {
//						ViewStack.push(new TrackView(t));
					}
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

	@Override
	protected AlbumEntity submit() {
		return this.selectedAlbum;
	}
}