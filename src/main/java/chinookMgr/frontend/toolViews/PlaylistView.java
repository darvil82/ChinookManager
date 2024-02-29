package chinookMgr.frontend.toolViews;

import chinookMgr.backend.Role;
import chinookMgr.backend.Saveable;
import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.PlaylistEntity;
import chinookMgr.backend.db.entities.TrackEntity;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.components.SaveOption;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PlaylistView extends ToolView implements Saveable {
	private JTextField txtName;
	private JTextField txtTotalDuration;
	private JPanel tracksPanel;
	private GenericTableView<TrackEntity> tracksTable;
	private JPanel mainPanel;
	private JPanel savePanel;
	private JPanel infoPanel;

	private PlaylistEntity currentPlaylist;

	public PlaylistView(PlaylistEntity playlist) {
		this.currentPlaylist = playlist;
		this.buildForEntity();
	}

	public PlaylistView() {
		this.currentPlaylist = null;
		this.buildForNew();
	}

	@Override
	protected void build() {
		super.build();

		this.insertView(this.savePanel, new SaveOption<>(this, false));
		this.getValidator().register(this.txtName, c -> !c.getText().isBlank(), "El nombre no puede estar vacío");
		this.getInputManager().register(Role.MANAGE_INVENTORY, this.txtName);
	}

	@Override
	protected void buildForEntity() {
		super.buildForEntity();

		this.txtName.setText(this.currentPlaylist.getName());
		this.initPlaylistData();
	}

	private void initPlaylistData() {
		this.insertView(this.tracksPanel, this.tracksTable = new GenericTableView<>(
			"Canciones", this.currentPlaylist.getTracksTableInspector()
				.onRowClick(GenericTableView.handleSpecial(this::removeSong, TrackView::new))
				.onNewButtonClick(this::addSong)
		));
		this.getInputManager().register(Role.MANAGE_INVENTORY, this.tracksTable);
		this.infoPanel.setBorder(BorderFactory.createTitledBorder("Detalles"));
		this.tracksPanel.setVisible(true);
		this.recalculateDuration();
	}

	private void removeSong(TrackEntity track) {
		this.currentPlaylist.removeTrack(track);
		this.onReMount();
	}

	private void addSong() {
		ViewStack.current().pushAwait(
			new GenericTableView<>("Añadir Canción", TrackEntity.getTableInspector().submitValueOnRowClick()),
			track -> {
				this.currentPlaylist.addTrack(track);
				this.recalculateDuration();
			}
		);
	}

	@Override
	public @NotNull String getName() {
		return this.currentPlaylist == null
			? "Nueva lista de reproducción"
			: "Lista de reproducción (" + this.currentPlaylist.getName() + ")";
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}

	@Override
	protected void onReMount(@Nullable ToolView prevView) {
		super.onReMount(prevView);
		this.recalculateDuration();
	}

	private void recalculateDuration() {
		// recalculating the total duration
		HibernateUtil.withSession(s -> {
			s.createQuery(
				"select sum(t.milliseconds) from PlaylistTrackEntity pt join TrackEntity t on pt.trackId = t.trackId where pt.playlistId = :playlistId",
				Long.class
			)
				.setParameter("playlistId", this.currentPlaylist.getPlaylistId())
				.uniqueResultOptional()
				.ifPresentOrElse(
					duration -> {
						int hours = (int) (duration / 3600000);
						int minutes = (int) ((duration % 3600000) / 60000);

						this.txtTotalDuration.setText(hours + " horas y " + minutes + " minutos");
					},
					() -> this.txtTotalDuration.setText("0 horas y 0 minutos")
				);
		});
	}

	@Override
	public void save() {
		boolean isNew = this.currentPlaylist == null;

		if (isNew) {
			this.currentPlaylist = new PlaylistEntity();
		}

		this.currentPlaylist.setName(this.txtName.getText());

		HibernateUtil.withSession(s -> {
			s.merge(this.currentPlaylist);
		});

		if (!isNew) {
			ViewStack.current().pop();
			return;
		};

		this.initPlaylistData();
		this.notifyChange();
	}

	@Override
	public void cancel() {
		ViewStack.current().pop();
	}

	@Override
	public boolean isDeletable() {
		return this.currentPlaylist != null;
	}

	@Override
	public void delete() {
		this.currentPlaylist.remove();
		ViewStack.current().pop();
	}
}