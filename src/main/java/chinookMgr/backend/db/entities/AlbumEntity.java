package chinookMgr.backend.db.entities;

import chinookMgr.frontend.components.TableInspector;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import static chinookMgr.backend.db.entities.EntityHelper.defaultSearch;

@Entity
@jakarta.persistence.Table(name = "Album", schema = "Chinook", catalog = "")
public class AlbumEntity {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@jakarta.persistence.Column(name = "AlbumId", nullable = false)
	private int albumId;

	public static AlbumEntity getById(int id) {
		return EntityHelper.getById(AlbumEntity.class, id);
	}

	public static TableInspector<AlbumEntity> getTableInspector() {
		return EntityHelper.getTableInspector(AlbumEntity.class, "title").submitValueOnRowClick();
	}

	public static TableInspector<TrackEntity> getTracksTableInspector(@NotNull AlbumEntity album) {
		var albumId = album.getAlbumId();

		return new TableInspector<>(
			(session, search) -> session.createQuery("from TrackEntity where albumId = :albumId and name like :search and enabled = true", TrackEntity.class)
				.setParameter("albumId", albumId)
				.setParameter("search", defaultSearch(search)),

			(session, search) -> session.createQuery("select count(*) from TrackEntity where albumId = :albumId and name like :search and enabled = true", Long.class)
				.setParameter("albumId", albumId)
				.setParameter("search", defaultSearch(search))
		);
	}

	public int getAlbumId() {
		return albumId;
	}

	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}

	@Basic
	@Column(name = "Title", nullable = false, length = 160)
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Basic
	@Column(name = "ArtistId", nullable = false)
	private int artistId;

	public int getArtistId() {
		return artistId;
	}

	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AlbumEntity that = (AlbumEntity)o;

		if (albumId != that.albumId) return false;
		if (artistId != that.artistId) return false;
		if (title != null ? !title.equals(that.title) : that.title != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = albumId;
		result = 31 * result + (title != null ? title.hashCode() : 0);
		result = 31 * result + artistId;
		return result;
	}

	@Override
	public String toString() {
		return this.getTitle();
	}
}