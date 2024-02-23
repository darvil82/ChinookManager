package chinookMgr.backend.db.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Title", schema = "Chinook", catalog = "")
public class TitleEntity {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id", nullable = false)
	private int id;
	@Basic
	@Column(name = "name", nullable = false, length = 32)
	private String name;
	@Basic
	@Column(name = "roles", nullable = false)
	private byte roles;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getRoles() {
		return roles;
	}

	public void setRoles(byte roles) {
		this.roles = roles;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TitleEntity that = (TitleEntity)o;

		if (id != that.id) return false;
		if (roles != that.roles) return false;
		if (name != null ? !name.equals(that.name) : that.name != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (int)roles;
		return result;
	}

	public static TitleEntity getById(int id) {
		return EntityHelper.getById(TitleEntity.class, id);
	}
}