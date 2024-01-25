package chinookMgr.backend;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.GenreEntity;
import chinookMgr.frontend.components.TableInspector;

public class Genre {
	public static GenreEntity getById(int id) {
		try (var session = HibernateUtil.getSession()) {
			return session.get(GenreEntity.class, id);
		}
	}

	public static TableInspector.Builder<GenreEntity> getTableInspectorBuilder() {
		return TableInspector.create(GenreEntity.class)
			.withQuerier("from GenreEntity where name like :search")
			.withCounter("select count(*) from GenreEntity where name like :search");
	}
}