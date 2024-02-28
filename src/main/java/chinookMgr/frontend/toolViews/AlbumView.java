package chinookMgr.frontend.toolViews;

import chinookMgr.backend.Saveable;
import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.AlbumEntity;
import chinookMgr.backend.db.entities.ArtistEntity;
import chinookMgr.backend.db.entities.TrackEntity;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.Utils;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.components.SaveOption;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class AlbumView extends ToolView implements Saveable {
	private JTextField txtTitle;
	private JButton btnArtist;
	private JPanel tracksPanel;
	private JPanel mainPanel;
	private JPanel savePanel;
	private JPanel infoPanel;

	private AlbumEntity currentAlbum;
	private ArtistEntity artist;

	public AlbumView() {
		this.buildForNew();
	}

	public AlbumView(AlbumEntity currentAlbum) {
		this.currentAlbum = currentAlbum;
		this.artist = ArtistEntity.getById(this.currentAlbum.getArtistId());
		this.buildForEntity();
	}

	@Override
	protected void buildForEntity() {
		super.buildForEntity();
		this.txtTitle.setText(this.currentAlbum.getTitle());
		this.initTracksView();
	}

	@Override
	protected void build() {
		Utils.attachViewSelectorToButton(
			this.btnArtist,
			() -> this.artist, "Artista",
			ArtistEntity.getTableInspector(),
			e -> this.artist = e,
			ArtistView::new
		);

		this.insertView(this.savePanel, new SaveOption<>(this, false));

		this.getValidator().register(this.btnArtist, c -> this.artist != null, "Seleccione un artista");
		this.getValidator().register(this.txtTitle, c -> !this.txtTitle.getText().isBlank(), "Ingrese un título");
	}

	private void initTracksView() {
		this.tracksPanel.setVisible(true);
		this.infoPanel.setBorder(BorderFactory.createTitledBorder("Información"));
		this.insertView(
			this.tracksPanel,
			new GenericTableView<>("Canciones", AlbumEntity.getTracksTableInspector(this.currentAlbum)
				.openViewOnRowClick(TrackView::new)
				.onRowClick(GenericTableView.handleSpecial(this::removeSong, TrackView::new))
				.onNewButtonClick(() -> ViewStack.current().pushAwait(
					new GenericTableView<>("Canciones", TrackEntity.getTableInspector().submitValueOnRowClick()),
					this::addSong
				))
			));
	}

	private void removeSong(TrackEntity track) {
		this.currentAlbum.removeTrack(track);
		this.onReMount();
	}

	private void addSong(TrackEntity track) {
		track.setAlbumId(this.currentAlbum.getAlbumId());
		HibernateUtil.withSession(s -> {
			s.merge(track);
		});
		this.onReMount();
	}

	@Override
	public @NotNull String getName() {
		if (this.currentAlbum == null)
			return "Nuevo álbum";
		else
			return "Álbum (" + this.txtTitle.getText() + ")";
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}

	@Override
	public void save() {
		boolean isNew = this.currentAlbum == null;

		if (isNew) {
			this.currentAlbum = new AlbumEntity();
		}

		this.currentAlbum.setTitle(this.txtTitle.getText());
		this.currentAlbum.setArtistId(this.artist.getArtistId());

		HibernateUtil.withSession(s -> {
			s.merge(this.currentAlbum);
		});

		if (!isNew) {
			ViewStack.current().pop();
			return;
		}

		this.initTracksView();
		ViewStack.current().notifyViewChange(); // to update the title
		this.onReMount(); // make saveOption notice the change of isDeletable
	}

	@Override
	public boolean isDeletable() {
		return this.currentAlbum != null;
	}

	@Override
	public void delete() {
		this.currentAlbum.remove();
		ViewStack.current().pop();
	}

	@Override
	public void cancel() {
		ViewStack.current().pop();
	}
}