package chinookMgr.frontend.toolViews;

import chinookMgr.backend.db.entities.ArtistEntity;
import chinookMgr.backend.entityHelpers.Artist;
import chinookMgr.frontend.ToolView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ArtistView extends ToolView {
	private JTextField txtName;
	private JPanel albumsPanel;
	private JPanel mainPanel;

	private final ArtistEntity currentArtist;

	public ArtistView(ArtistEntity artist) {
		this.currentArtist = artist;
		this.buildForEntity();
	}

	@Override
	protected void buildForEntity() {
		this.txtName.setText(this.currentArtist.getName());
		this.albumsPanel.setVisible(true);
		this.insertView(
			this.albumsPanel,
			new GenericTableView<>("álbum", Artist.getAlbumsTableInspector(this.currentArtist)
				.openViewOnRowClick(AlbumView::new))
		);
	}

	@Override
	public @NotNull String getName() {
		return "Artista (" + this.txtName.getText() + ")";
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}

}