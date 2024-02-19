package chinookMgr.backend.entityHelpers;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.TrackEntity;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.components.TableInspector;
import chinookMgr.frontend.toolViews.TrackView;

public abstract class Track {
	private Track() {}

	public static TrackEntity getById(int id) {
		return EntityHelper.getById(TrackEntity.class, id);
	}

	public static TableInspector<TrackEntity> getTableInspector() {
		return new TableInspector<>(
			(session, search) -> session.createQuery("from TrackEntity where name like :search and enabled = true", TrackEntity.class)
				.setParameter("search", EntityHelper.defaultSearch(search)),

			(session, search) -> session.createQuery("select count(*) from TrackEntity where name like :search and enabled = true", Long.class)
				.setParameter("search", EntityHelper.defaultSearch(search))
		)
			.onNewButtonClick(() -> ViewStack.current().push(new TrackView()));
	}

	public static void disable(TrackEntity track) {
		HibernateUtil.withSession(s -> {
			track.setEnabled(false);

			s.createMutationQuery("delete PlaylistTrackEntity where trackId = :trackId")
				.setParameter("trackId", track.getTrackId())
				.executeUpdate();

			s.merge(track);
		});
	}
}