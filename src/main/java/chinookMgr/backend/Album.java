package chinookMgr.backend;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.AlbumEntity;

public abstract class Album {
	private Album() {}

	public static AlbumEntity getById(int id) {
		try (var session = HibernateUtil.getSession()) {
			return session.get(AlbumEntity.class, id);
		}
	}
}