package chinookMgr.backend.entityHelpers;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.PlaylistEntity;
import chinookMgr.backend.db.entities.PlaylistTrackEntity;
import chinookMgr.backend.db.entities.TrackEntity;
import chinookMgr.frontend.ListTableModel;
import chinookMgr.frontend.StatusManager;
import chinookMgr.frontend.Utils;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.components.TableInspector;
import chinookMgr.frontend.toolViews.PlaylistView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import java.util.List;

import static chinookMgr.backend.entityHelpers.EntityHelper.defaultSearch;

public abstract class Playlist {
	private Playlist() {}

	public static PlaylistEntity getById(int id) {
		return EntityHelper.getById(PlaylistEntity.class, id);
	}

	public static TableInspector<PlaylistEntity> getTableInspector() {
		return EntityHelper.getTableInspector(PlaylistEntity.class, "name")
			.onNewButtonClick(() -> ViewStack.current().push(new PlaylistView()));
	}

	public static TableInspector<TrackEntity> getTracksTableInspector(@NotNull PlaylistEntity playlist) {
		var playlistId = playlist.getPlaylistId();

		return new TableInspector<>(
			(session, search) -> session.createQuery("select t from PlaylistTrackEntity pt join TrackEntity t on pt.trackId = t.trackId where pt.playlistId = :listId and t.name like :search", TrackEntity.class)
				.setParameter("listId", playlistId)
				.setParameter("search", defaultSearch(search)),

			(session, search) -> session.createQuery("select count(*) from PlaylistTrackEntity pt join TrackEntity t on pt.trackId = t.trackId where pt.playlistId = :listId and t.name like :search", Long.class)
				.setParameter("listId", playlistId)
				.setParameter("search", defaultSearch(search)),

			new ListTableModel<>(List.of("Nombre", "Duración"), (item, column) -> column == 1 ? Utils.formatMillis(item.getMilliseconds()) : item.toString())
		);
	}

	public static void addTrack(@NotNull PlaylistEntity playlist, @NotNull TrackEntity track) {
		HibernateUtil.withSession(s -> {
			// first check if the track is already in the playlist
			s.createQuery("from PlaylistTrackEntity where playlistId = :playlistId and trackId = :trackId", PlaylistTrackEntity.class)
				.setParameter("playlistId", playlist.getPlaylistId())
				.setParameter("trackId", track.getTrackId())
				.uniqueResultOptional()
				.ifPresentOrElse(t -> {
					JOptionPane.showMessageDialog(
						ViewStack.currentPanel(), "La canción seleccionada ya está en la lista", "Error", JOptionPane.ERROR_MESSAGE
					);
				}, () -> {
					var playlistTrack = new PlaylistTrackEntity();
					playlistTrack.setPlaylistId(playlist.getPlaylistId());
					playlistTrack.setTrackId(track.getTrackId());

					s.persist(playlistTrack);
					StatusManager.showUpdate("Canción añadida a la lista");
				});
		});
	}

	public static void removeTrack(@NotNull PlaylistEntity playlist, @NotNull TrackEntity track) {
		HibernateUtil.withSession(s -> {
			s.createMutationQuery("delete from PlaylistTrackEntity where playlistId = :playlistId and trackId = :trackId")
				.setParameter("playlistId", playlist.getPlaylistId())
				.setParameter("trackId", track.getTrackId())
				.executeUpdate();

			StatusManager.showUpdate("Canción eliminada de la lista");
		});
	}

	public static void remove(@NotNull PlaylistEntity playlist) {
		HibernateUtil.withSession(s -> {
			s.createMutationQuery("delete from PlaylistTrackEntity where playlistId = :playlistId")
				.setParameter("playlistId", playlist.getPlaylistId())
				.executeUpdate();

			s.remove(playlist);
		});
	}
}