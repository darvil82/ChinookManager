package chinookMgr.backend.entityHelpers;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.GenreEntity;
import chinookMgr.backend.db.entities.TrackEntity;
import chinookMgr.frontend.components.TableInspector;

public abstract class Genre {
	private Genre() {}

	public static GenreEntity getById(int id) {
		try (var session = HibernateUtil.getSession()) {
			return session.get(GenreEntity.class, id);
		}
	}

	public static TableInspector<GenreEntity> getTableInspector() {
		return EntityHelper.getTableInspector(GenreEntity.class, "name").submitValueOnRowClick();
	}

	public static TableInspector<TrackEntity> getTracksTableInspector(GenreEntity genre) {
		var genreId = genre.getGenreId();

		return new TableInspector<>((session, search) ->
			session.createQuery("from TrackEntity where genreId = :genreId and name like :search", TrackEntity.class)
				.setParameter("genreId", genreId)
				.setParameter("search", "%" + search + "%"),

			(session, s) -> session.createQuery("select count(*) from TrackEntity where genreId = :genreId and name like :search", Long.class)
				.setParameter("genreId", genreId)
				.setParameter("search", "%" + s + "%")
		);
	}
}