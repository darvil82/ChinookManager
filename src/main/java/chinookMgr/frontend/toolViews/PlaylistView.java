package chinookMgr.frontend.toolViews;

import chinookMgr.backend.Saveable;
import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.PlaylistEntity;
import chinookMgr.backend.db.entities.TrackEntity;
import chinookMgr.backend.entityHelpers.Playlist;
import chinookMgr.backend.entityHelpers.Track;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.components.SaveOption;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class PlaylistView extends ToolView implements Saveable {
	private JTextField txtName;
	private JTextField txtTotalDuration;
	private JPanel tracksPanel;
	private JPanel mainPanel;
	private JPanel savePanel;

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

		this.insertView(this.savePanel, new SaveOption<>(this));
	}

	@Override
	protected void buildForEntity() {
		super.buildForEntity();

		this.txtName.setText(this.currentPlaylist.getName());
		this.insertView(this.tracksPanel, new GenericTableView<>(
			"Canciones", Playlist.getTracksTableInspector(this.currentPlaylist)
				.onRowClick(this::onTrackClick)
				.onNewButtonClick(this::addSong)
			)
		);

		this.recalculateDuration();
	}

	private void onTrackClick(MouseEvent e, TrackEntity track) {
		// is the user holding the ctrl key?
		if (e.isControlDown()) {
			Playlist.removeTrack(this.currentPlaylist, track);
			this.onReMount();
			return;
		}

		// if not, then we just show the track details
		ViewStack.current().push(new TrackView(track));
	}

	private void addSong() {
		ViewStack.current().pushAwait(
			new GenericTableView<>("Añadir Canción", Track.getTableInspector().submitValueOnRowClick()),
			track -> {
				Playlist.addTrack(this.currentPlaylist, track);
				this.recalculateDuration();
			}
		);
	}

	@Override
	public @NotNull String getName() {
		return "Lista de reproducción (" + this.txtName.getText() + ")";
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
		if (this.currentPlaylist == null) {
			this.currentPlaylist = new PlaylistEntity();
		}

		this.currentPlaylist.setName(this.txtName.getText());

		HibernateUtil.withSession(s -> {
			s.merge(this.currentPlaylist);
		});
	}

	@Override
	public boolean isDeletable() {
		return this.currentPlaylist != null;
	}

	@Override
	public void delete() {
		System.out.println("not implemented yet!");
	}
}