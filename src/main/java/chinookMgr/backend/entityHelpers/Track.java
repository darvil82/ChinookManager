package chinookMgr.backend.entityHelpers;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.GenreEntity;
import chinookMgr.backend.db.entities.TrackEntity;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.components.TableInspector;
import chinookMgr.frontend.toolViews.TrackView;

public abstract class Track {
	private Track() {}

	public static TrackEntity getById(int id) {
		return EntityHelper.getById(TrackEntity.class, id);
	}

	public static TableInspector.Builder<TrackEntity> getTableInspectorBuilder() {
		return EntityHelper.getTableInspectorBuilder(TrackEntity.class, "name")
			.withNewClick(() -> ViewStack.push(new TrackView()));
	}
}