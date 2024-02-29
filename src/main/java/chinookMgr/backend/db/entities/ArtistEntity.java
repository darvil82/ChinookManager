package chinookMgr.backend.db.entities;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.components.TableInspector;
import chinookMgr.frontend.toolViews.ArtistView;
import chinookMgr.frontend.toolViews.GenericTableView;
import jakarta.persistence.*;

import static chinookMgr.backend.db.entities.EntityHelper.defaultSearch;

@Entity
@jakarta.persistence.Table(name = "Artist", schema = "Chinook", catalog = "")
public class ArtistEntity {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@jakarta.persistence.Column(name = "ArtistId", nullable = false)
	private int artistId;



	public int getArtistId() {
		return artistId;
	}

	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}

	@Basic
	@Column(name = "Name", nullable = true, length = 120)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ArtistEntity that = (ArtistEntity)o;

		if (artistId != that.artistId) return false;
		if (name != null ? !name.equals(that.name) : that.name != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = artistId;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return this.name;
	}


	public static ArtistEntity getById(int id) {
		return EntityHelper.getById(ArtistEntity.class, id);
	}

	public static TableInspector<ArtistEntity> getTableInspector() {
		return EntityHelper.getTableInspector(ArtistEntity.class, "name")
			.onNewButtonClick(() -> ViewStack.current().push(new ArtistView()));
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
		).onNewButtonClick(() -> {
			ViewStack.current().pushAwait(
				new GenericTableView<>("Seleccionar álbum", AlbumEntity.getTableInspector().submitValueOnRowClick()),
				album -> {
					album.setArtistId(artist.getArtistId());

					HibernateUtil.withSession(s -> {
						s.merge(album);
					});
				}
			);
		});
	}
}