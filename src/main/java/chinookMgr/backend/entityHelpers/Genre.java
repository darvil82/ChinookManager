package chinookMgr.backend.entityHelpers;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.GenreEntity;
import chinookMgr.frontend.components.TableInspector;

public abstract class Genre {
	private Genre() {}

	public static GenreEntity getById(int id) {
		try (var session = HibernateUtil.getSession()) {
			return session.get(GenreEntity.class, id);
		}
	}

	public static TableInspector<GenreEntity> getTableInspectorBuilder() {
		return EntityHelper.getTableInspectorBuilder(GenreEntity.class, "name").submitValueOnRowClick();
	}
}