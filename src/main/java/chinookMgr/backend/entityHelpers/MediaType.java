package chinookMgr.backend.entityHelpers;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.GenreEntity;
import chinookMgr.backend.db.entities.MediaTypeEntity;
import chinookMgr.frontend.components.TableInspector;

public abstract class MediaType {
	private MediaType() {}

	public static MediaTypeEntity getById(int id) {
		return HibernateUtil.withSession(session -> {
			return session.get(MediaTypeEntity.class, id);
		});
	}

	public static TableInspector<MediaType> getTableInspector() {
		return EntityHelper.getTableInspector(MediaType.class, "name");
	}
}