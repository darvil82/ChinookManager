package chinookMgr.backend.db.entities;

import jakarta.persistence.*;

@Entity
@jakarta.persistence.Table(name = "PlaylistTrack", schema = "Chinook", catalog = "")
@jakarta.persistence.IdClass(chinookMgr.backend.db.entities.PlaylistTrackEntityPK.class)
public class PlaylistTrackEntity {
	@Id
	@jakarta.persistence.Column(name = "PlaylistId", nullable = false)
	private int playlistId;

	public int getPlaylistId() {
		return playlistId;
	}

	public void setPlaylistId(int playlistId) {
		this.playlistId = playlistId;
	}

	@Id
	@jakarta.persistence.Column(name = "TrackId", nullable = false)
	private int trackId;

	public int getTrackId() {
		return trackId;
	}

	public void setTrackId(int trackId) {
		this.trackId = trackId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PlaylistTrackEntity that = (PlaylistTrackEntity)o;

		if (playlistId != that.playlistId) return false;
		if (trackId != that.trackId) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = playlistId;
		result = 31 * result + trackId;
		return result;
	}
}