package chinookMgr.backend;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.EmployeeEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Employee extends User<EmployeeEntity> {
	private final @NotNull EmployeeEntity entity;

	public Employee(@NotNull EmployeeEntity entity) {
		this.entity = entity;
	}

	@Override
	public @NotNull String getFirstName() {
		return this.entity.getFirstName();
	}

	@Override
	public @NotNull String getLastName() {
		return this.entity.getLastName();
	}

	@Override
	public @NotNull String getAddress() {
		return this.entity.getAddress();
	}

	@Override
	public @NotNull String getCity() {
		return this.entity.getCity();
	}

	@Override
	public @NotNull String getState() {
		return this.entity.getAddress();
	}

	@Override
	public @NotNull String getCountry() {
		return this.entity.getCountry();
	}

	@Override
	public @NotNull String getPostalCode() {
		return this.entity.getPostalCode();
	}

	@Override
	public @NotNull String getPhone() {
		return this.entity.getPhone();
	}

	@Override
	public @NotNull String getEmail() {
		return this.entity.getEmail();
	}

	@Override
	public @NotNull EmployeeEntity getEntity() {
		return this.entity;
	}

	@Override
	public List<Role> getRoles() {
		return HibernateUtil.withSession(session -> {
			byte flags = session.createQuery(
					"select t.roles from EmployeeEntity e join e.title t where e.employeeId = ?",
					Byte.class
				)
				.setParameter(0, this.entity.getEmployeeId())
				.getSingleResult();

			return Role.getRolesFromFlags(flags);
		});
	}
}