package chinookMgr.frontend.toolViews;

import chinookMgr.backend.db.entities.AlbumEntity;
import chinookMgr.backend.db.entities.ArtistEntity;
import chinookMgr.backend.entityHelpers.Album;
import chinookMgr.backend.entityHelpers.Artist;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.Utils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class AlbumView extends ToolView {
	private JTextField txtTitle;
	private JButton btnArtist;
	private JPanel tracksPanel;
	private JPanel mainPanel;

	private AlbumEntity album;
	private ArtistEntity artist;

	public AlbumView() {
		this.buildForNew();
	}

	public AlbumView(AlbumEntity album) {
		this.album = album;
		this.artist = Artist.getById(this.album.getArtistId());
		this.buildForEntity();
	}

	@Override
	protected void buildForEntity() {
		super.buildForEntity();
		this.txtTitle.setText(this.album.getTitle());
	}

	@Override
	protected void build() {
		Utils.attachViewSelectorToButton(
			this.btnArtist,
			() -> this.artist, "Artista",
			Artist.getTableInspector(),
			e -> this.artist = e,
			ArtistView::new
		);

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