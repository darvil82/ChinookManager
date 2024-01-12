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
}