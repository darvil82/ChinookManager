package chinookMgr.frontend.toolViews;

import chinookMgr.backend.db.entities.AlbumEntity;
import chinookMgr.backend.entityHelpers.Album;
import chinookMgr.backend.entityHelpers.Artist;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.components.TableInspector;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class AlbumView extends ToolView {
	private JTextField txtTitle;
	private JButton btnArtist;
	private JPanel tracksPanel;
	private JPanel mainPanel;

	private AlbumEntity album;

	public AlbumView() {
		this.build();
	}

	public AlbumView(AlbumEntity album) {
		this.album = album;
		this.buildForEntity();
	}

	@Override
	protected void build() {
		this.txtTitle.setText(this.album.getTitle());
		this.btnArtist.setText(Artist.getById(this.album.getArtistId()).getName());
		this.insertView(
			this.tracksPanel,
			new GenericTableView<>("Canciones", Album.getTracksTableInspector(this.album)
				.openViewOnRowClick(TrackView::new))
		);
	}

	@Override
	public @NotNull String getName() {
		if (this.album == null)
			return "Nuevo álbum";
		else
			return "Álbum (" + this.txtTitle.getText() + ")";
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}
}