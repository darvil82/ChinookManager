package chinookMgr.frontend.toolViews;

import chinookMgr.backend.Role;
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

import javax.swing.*;

public class AlbumView extends ToolView implements Saveable {
	private JTextField txtTitle;
	private JButton btnArtist;
	private JPanel tracksPanel;
	private JPanel mainPanel;
	private JPanel savePanel;
	private JPanel infoPanel;

	private AlbumEntity currentAlbum;
	private ArtistEntity currentArtist;

	public AlbumView() {
		this.buildForNew();
	}

	public AlbumView(AlbumEntity currentAlbum) {
		this.currentAlbum = currentAlbum;
		this.currentArtist = ArtistEntity.getById(this.currentAlbum.getArtistId());
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
			() -> this.currentArtist, "Artista",
			ArtistEntity.getTableInspector(),
			e -> this.currentArtist = e,
			ArtistView::new
		);

		this.insertView(this.savePanel, new SaveOption<>(this, Role.MANAGE_INVENTORY, false));

		this.getValidator().register(this.btnArtist, c -> this.currentArtist != null, "Seleccione un artista");
		this.getValidator().register(this.txtTitle, c -> !this.txtTitle.getText().isBlank(), "Ingrese un título");

		this.getInputManager().register(Role.MANAGE_INVENTORY, this.txtTitle, this.btnArtist);
	}

	private void initTracksView() {
		this.tracksPanel.setVisible(true);
		this.infoPanel.setBorder(BorderFactory.createTitledBorder("Información"));
		this.insertView(
			this.tracksPanel,
			this.getInputManager().register(Role.MANAGE_INVENTORY, new GenericTableView<>("Canciones", AlbumEntity.getTracksTableInspector(this.currentAlbum)
				.openViewOnRowClick(TrackView::new)
				.onRowClick(GenericTableView.handleSpecial(this::removeSong, TrackView::new))
				.onNewButtonClick(() -> ViewStack.current().pushAwait(
					new GenericTableView<>("Añadir canción al álbum", TrackEntity.getTableInspector().submitValueOnRowClick()),
					this::addSong
				))
			)));
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
		this.currentAlbum.setArtistId(this.currentArtist.getArtistId());

		HibernateUtil.withSession(s -> {
			s.merge(this.currentAlbum);
		});

		if (!isNew) {
			ViewStack.current().pop();
			return;
		}

		this.initTracksView();
		this.notifyChange();
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