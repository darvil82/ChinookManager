package chinookMgr.backend.entityHelpers;

import chinookMgr.backend.db.entities.ArtistEntity;
import chinookMgr.backend.db.entities.TrackEntity;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.components.TableInspector;
import chinookMgr.frontend.toolViews.TrackView;

public class Artist {
	public static ArtistEntity getById(int id) {
		return EntityHelper.getById(ArtistEntity.class, id);
	}

	public static TableInspector<ArtistEntity> getTableInspector() {
		return EntityHelper.getTableInspector(ArtistEntity.class, "name");
//			.onNewButtonClick(() -> ViewStack.current().push(new TrackView()));
	}
}