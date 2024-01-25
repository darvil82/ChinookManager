package chinookMgr.backend;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.MediaTypeEntity;

public class MediaType {
	public static MediaTypeEntity getById(int id) {
		try (var session = HibernateUtil.getSession()) {
			return session.get(MediaTypeEntity.class, id);
		}
	}
}