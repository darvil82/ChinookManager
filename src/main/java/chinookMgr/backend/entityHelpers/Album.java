package chinookMgr.backend.entityHelpers;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.AlbumEntity;
import chinookMgr.frontend.components.TableInspector;

public abstract class Album {
	private Album() {}

	public static AlbumEntity getById(int id) {
		return EntityHelper.getById(AlbumEntity.class, id);
	}

	public static TableInspector<AlbumEntity> getTableInspectorBuilder() {
		return EntityHelper.getTableInspectorBuilder(AlbumEntity.class, "title").submitValueOnRowClick();
	}
}