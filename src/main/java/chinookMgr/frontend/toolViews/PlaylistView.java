package chinookMgr.frontend.toolViews;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.PlaylistEntity;
import chinookMgr.backend.entityHelpers.Playlist;
import chinookMgr.frontend.ToolView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PlaylistView extends ToolView {
	private JTextField txtName;
	private JTextField txtTotalDuration;
	private JPanel tracksPanel;
	private JButton btnAddSong;
	private JPanel mainPanel;

	private final PlaylistEntity currentPlaylist;

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
	}

	@Override
	protected void buildForEntity() {
		super.buildForEntity();

		this.txtName.setText(this.currentPlaylist.getName());
		this.insertView(this.tracksPanel, new GenericTableView<>(
			"Canciones", Playlist.getTracksTableInspector(this.currentPlaylist)
		));

		this.onReMount(null);
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
}