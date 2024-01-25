package chinookMgr.backend.entityHelpers;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.GenreEntity;
import chinookMgr.backend.db.entities.TrackEntity;
import chinookMgr.frontend.components.TableInspector;

public abstract class Track {
	private Track() {}

	public static TrackEntity getById(int id) {
		return EntityHelper.getById(TrackEntity.class, id);
	}

	public static TableInspector.Builder<TrackEntity> getTableInspectorBuilder() {
		return EntityHelper.getTableInspectorBuilder(TrackEntity.class, "name");
	}
}