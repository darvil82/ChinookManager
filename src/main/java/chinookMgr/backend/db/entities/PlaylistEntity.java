package chinookMgr.backend.db.entities;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.frontend.StatusManager;
import chinookMgr.frontend.Utils;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.components.TableInspector;
import chinookMgr.frontend.toolViews.PlaylistView;
import chinookMgr.shared.ListTableModel;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;

import static chinookMgr.backend.db.entities.EntityHelper.defaultSearch;

@Entity
@jakarta.persistence.Table(name = "Playlist", schema = "Chinook", catalog = "")
public class PlaylistEntity {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@jakarta.persistence.Column(name = "PlaylistId", nullable = false)
	private int playlistId;

	public int getPlaylistId() {
		return playlistId;
	}

	public void setPlaylistId(int playlistId) {
		this.playlistId = playlistId;
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

		PlaylistEntity that = (PlaylistEntity)o;

		if (playlistId != that.playlistId) return false;
		if (name != null ? !name.equals(that.name) : that.name != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = playlistId;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return name;
	}


	public static PlaylistEntity getById(int id) {
		return EntityHelper.getById(PlaylistEntity.class, id);
	}

	public static TableInspector<PlaylistEntity> getTableInspector() {
		return EntityHelper.getTableInspector(PlaylistEntity.class, "name")
			.onNewButtonClick(() -> ViewStack.current().push(new PlaylistView()));
	}

	public TableInspector<TrackEntity> getTracksTableInspector() {
		return new TableInspector<>(
			(session, search) -> session.createQuery("select t from PlaylistTrackEntity pt join TrackEntity t on pt.trackId = t.trackId where pt.playlistId = :listId and t.name like :search", TrackEntity.class)
				.setParameter("listId", this.playlistId)
				.setParameter("search", defaultSearch(search)),

			(session, search) -> session.createQuery("select count(*) from PlaylistTrackEntity pt join TrackEntity t on pt.trackId = t.trackId where pt.playlistId = :listId and t.name like :search", Long.class)
				.setParameter("listId", this.playlistId)
				.setParameter("search", defaultSearch(search)),

			new ListTableModel<>(List.of("Nombre", "Duración"), (item, column) -> column == 1 ? Utils.formatMillis(item.getMilliseconds()) : item.toString())
		);
	}

	public void addTrack(@NotNull TrackEntity track) {
		HibernateUtil.withSession(s -> {
			// first check if the track is already in the playlist
			s.createQuery("from PlaylistTrackEntity where playlistId = :playlistId and trackId = :trackId", PlaylistTrackEntity.class)
				.setParameter("playlistId", this.getPlaylistId())
				.setParameter("trackId", track.getTrackId())
				.uniqueResultOptional()
				.ifPresentOrElse(t -> {
					JOptionPane.showMessageDialog(
						ViewStack.currentPanel(), "La canción seleccionada ya está en la lista", "Error", JOptionPane.ERROR_MESSAGE
					);
				}, () -> {
					var playlistTrack = new PlaylistTrackEntity();
					playlistTrack.setPlaylistId(this.getPlaylistId());
					playlistTrack.setTrackId(track.getTrackId());

					s.persist(playlistTrack);
					StatusManager.showUpdate("Canción añadida a la lista");
				});
		});
	}

	public void removeTrack(@NotNull TrackEntity track) {
		HibernateUtil.withSession(s -> {
			s.createMutationQuery("delete from PlaylistTrackEntity where playlistId = :playlistId and trackId = :trackId")
				.setParameter("playlistId", this.getPlaylistId())
				.setParameter("trackId", track.getTrackId())
				.executeUpdate();

			StatusManager.showUpdate("Canción eliminada de la lista");
		});
	}

	public void remove() {
		HibernateUtil.withSession(s -> {
			s.createMutationQuery("delete from PlaylistTrackEntity where playlistId = :playlistId")
				.setParameter("playlistId", this.getPlaylistId())
				.executeUpdate();

			s.remove(this);
		});
	}
}