package chinookMgr.backend.db.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Track", schema = "Chinook", catalog = "")
public class TrackEntity {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "TrackId", nullable = false)
	private int trackId;
	@Basic
	@Column(name = "Name", nullable = false, length = 200)
	private String name;
	@Basic
	@Column(name = "AlbumId", nullable = true)
	private Integer albumId;
	@Basic
	@Column(name = "MediaTypeId", nullable = false)
	private int mediaTypeId;
	@Basic
	@Column(name = "GenreId", nullable = true)
	private Integer genreId;
	@Basic
	@Column(name = "Composer", nullable = true, length = 220)
	private String composer;
	@Basic
	@Column(name = "Milliseconds", nullable = false)
	private int milliseconds;
	@Basic
	@Column(name = "Bytes", nullable = true)
	private Integer bytes;
	@Basic
	@Column(name = "UnitPrice", nullable = false, precision = 2)
	private BigDecimal unitPrice;

	public int getTrackId() {
		return trackId;
	}

	public void setTrackId(int trackId) {
		this.trackId = trackId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Integer albumId) {
		this.albumId = albumId;
	}

	public int getMediaTypeId() {
		return mediaTypeId;
	}

	public void setMediaTypeId(int mediaTypeId) {
		this.mediaTypeId = mediaTypeId;
	}

	public Integer getGenreId() {
		return genreId;
	}

	public void setGenreId(Integer genreId) {
		this.genreId = genreId;
	}

	public String getComposer() {
		return composer;
	}

	public void setComposer(String composer) {
		this.composer = composer;
	}

	public int getMilliseconds() {
		return milliseconds;
	}

	public void setMilliseconds(int milliseconds) {
		this.milliseconds = milliseconds;
	}

	public Integer getBytes() {
		return bytes;
	}

	public void setBytes(Integer bytes) {
		this.bytes = bytes;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TrackEntity that = (TrackEntity)o;

		if (trackId != that.trackId) return false;
		if (mediaTypeId != that.mediaTypeId) return false;
		if (milliseconds != that.milliseconds) return false;
		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		if (albumId != null ? !albumId.equals(that.albumId) : that.albumId != null) return false;
		if (genreId != null ? !genreId.equals(that.genreId) : that.genreId != null) return false;
		if (composer != null ? !composer.equals(that.composer) : that.composer != null) return false;
		if (bytes != null ? !bytes.equals(that.bytes) : that.bytes != null) return false;
		if (unitPrice != null ? !unitPrice.equals(that.unitPrice) : that.unitPrice != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = trackId;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (albumId != null ? albumId.hashCode() : 0);
		result = 31 * result + mediaTypeId;
		result = 31 * result + (genreId != null ? genreId.hashCode() : 0);
		result = 31 * result + (composer != null ? composer.hashCode() : 0);
		result = 31 * result + milliseconds;
		result = 31 * result + (bytes != null ? bytes.hashCode() : 0);
		result = 31 * result + (unitPrice != null ? unitPrice.hashCode() : 0);
		return result;
	}


	@Override
	public String toString() {
		return this.name;
	}
}