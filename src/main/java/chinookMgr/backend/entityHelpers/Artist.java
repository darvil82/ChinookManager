package chinookMgr.backend.entityHelpers;

import chinookMgr.backend.db.entities.AlbumEntity;
import chinookMgr.backend.db.entities.ArtistEntity;
import chinookMgr.frontend.components.TableInspector;

import static chinookMgr.backend.entityHelpers.EntityHelper.defaultSearch;

public class Artist {
	public static ArtistEntity getById(int id) {
		return EntityHelper.getById(ArtistEntity.class, id);
	}

	public static TableInspector<ArtistEntity> getTableInspector() {
		return EntityHelper.getTableInspector(ArtistEntity.class, "name");
	}

	public static TableInspector<AlbumEntity> getAlbumsTableInspector(ArtistEntity artist) {
		return new TableInspector<>(
			(session, s) ->
				session.createQuery("from AlbumEntity where artistId = :id and title like :input", AlbumEntity.class)
					.setParameter("id", artist.getArtistId())
					.setParameter("input", defaultSearch(s))
			,
			(session, s) ->
				session.createQuery("select count(*) from AlbumEntity where artistId = :id and title like :input", Long.class)
					.setParameter("id", artist.getArtistId())
					.setParameter("input", defaultSearch(s))
		);
	}
}