package chinookMgr.backend.entityHelpers;

import chinookMgr.backend.db.entities.AlbumEntity;
import chinookMgr.backend.db.entities.PlaylistEntity;
import chinookMgr.backend.db.entities.TrackEntity;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.components.TableInspector;
import chinookMgr.frontend.toolViews.PlaylistView;
import chinookMgr.frontend.toolViews.TrackView;
import org.jetbrains.annotations.NotNull;

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

	public static TableInspector<TrackEntity> getTracksTableInspector(@NotNull PlaylistEntity album) {
		var playlistId = album.getPlaylistId();

		return new TableInspector<>(
			(session, search) -> session.createQuery("select t from PlaylistTrackEntity pt join TrackEntity t on pt.trackId = t.trackId where pt.playlistId = :listId and t.name like :search", TrackEntity.class)
				.setParameter("listId", playlistId)
				.setParameter("search", defaultSearch(search)),

			(session, search) -> session.createQuery("select count(*) from PlaylistTrackEntity pt join TrackEntity t on pt.trackId = t.trackId where pt.playlistId = :listId and t.name like :search", Long.class)
				.setParameter("listId", playlistId)
				.setParameter("search", defaultSearch(search))
		);
	}
}