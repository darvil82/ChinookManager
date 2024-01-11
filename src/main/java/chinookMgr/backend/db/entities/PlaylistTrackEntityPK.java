package chinookMgr.backend.db.entities;

import java.io.Serializable;

public class PlaylistTrackEntityPK implements Serializable {
	private int playlistId;
	private int trackId;

	public int getPlaylistId() {
		return playlistId;
	}

	public void setPlaylistId(int playlistId) {
		this.playlistId = playlistId;
	}

	public int getTrackId() {
		return trackId;
	}

	public void setTrackId(int trackId) {
		this.trackId = trackId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PlaylistTrackEntityPK)) return false;
		PlaylistTrackEntityPK that = (PlaylistTrackEntityPK) o;
		return getPlaylistId() == that.getPlaylistId() &&
			getTrackId() == that.getTrackId();
	}

	@Override
	public int hashCode() {
		return 31;
	}
}