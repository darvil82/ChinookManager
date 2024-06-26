package chinookMgr.backend.db.entities;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.frontend.components.TableInspector;
import jakarta.persistence.*;

@Entity
@jakarta.persistence.Table(name = "MediaType", schema = "Chinook", catalog = "")
public class MediaTypeEntity {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@jakarta.persistence.Column(name = "MediaTypeId", nullable = false)
	private int mediaTypeId;



	public int getMediaTypeId() {
		return mediaTypeId;
	}

	public void setMediaTypeId(int mediaTypeId) {
		this.mediaTypeId = mediaTypeId;
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

		MediaTypeEntity that = (MediaTypeEntity)o;

		if (mediaTypeId != that.mediaTypeId) return false;
		if (name != null ? !name.equals(that.name) : that.name != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = mediaTypeId;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		return result;
	}


	public static MediaTypeEntity getById(int id) {
		return HibernateUtil.withSession(session -> {
			return session.get(MediaTypeEntity.class, id);
		});
	}

	public static TableInspector<MediaTypeEntity> getTableInspector() {
		return EntityHelper.getTableInspector(MediaTypeEntity.class, "name");
	}
}