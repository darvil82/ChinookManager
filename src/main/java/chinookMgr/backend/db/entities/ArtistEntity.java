package chinookMgr.backend.db.entities;

import jakarta.persistence.*;

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
}