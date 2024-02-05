package chinookMgr.backend.entityHelpers;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.AlbumEntity;
import chinookMgr.backend.db.entities.TrackEntity;
import chinookMgr.frontend.components.TableInspector;
import org.jetbrains.annotations.NotNull;

public abstract class Album {
	private Album() {}

	public static AlbumEntity getById(int id) {
		return EntityHelper.getById(AlbumEntity.class, id);
	}

	public static TableInspector<AlbumEntity> getTableInspector() {
		return EntityHelper.getTableInspector(AlbumEntity.class, "title").submitValueOnRowClick();
	}

	public static TableInspector<TrackEntity> getTracksTableInspector(@NotNull AlbumEntity album) {
		var albumId = album.getAlbumId();

		return new TableInspector<>((session, search) ->
			session.createQuery("from TrackEntity where albumId = :albumId and name like :search", TrackEntity.class)
				.setParameter("albumId", albumId)
				.setParameter("search", "%" + search + "%"),

			(session, s) -> session.createQuery("select count(*) from TrackEntity where albumId = :albumId and name like :search", Long.class)
				.setParameter("albumId", albumId)
				.setParameter("search", "%" + s + "%")
		);
	}
}