package chinookMgr.backend;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.CustomerEntity;
import chinookMgr.backend.db.entities.EmployeeEntity;
import chinookMgr.frontend.components.TableInspector;
import chinookMgr.shared.ListTableModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static chinookMgr.backend.entityHelpers.EntityHelper.defaultSearch;

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
	public @Nullable String getAddress() {
		return this.entity.getAddress();
	}

	@Override
	public @Nullable String getCity() {
		return this.entity.getCity();
	}

	@Override
	public @Nullable String getState() {
		return this.entity.getAddress();
	}

	@Override
	public @Nullable String getCountry() {
		return this.entity.getCountry();
	}

	@Override
	public @Nullable String getPostalCode() {
		return this.entity.getPostalCode();
	}

	@Override
	public @Nullable String getPhone() {
		return this.entity.getPhone();
	}

	@Override
	public @Nullable String getFax() {
		return this.entity.getFax();
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

	public static TableInspector<EmployeeEntity> getTableInspector() {
		return new TableInspector<>(
			(session, search) -> session.createQuery("from EmployeeEntity where firstName like :search or lastName like :search", EmployeeEntity.class)
				.setParameter("search", defaultSearch(search)),
			(session, search) -> session.createQuery("select count(*) from EmployeeEntity where firstName like :search or lastName like :search", Long.class)
				.setParameter("search", defaultSearch(search)),

			Employee.getTableModel()
		);
	}


	public static @NotNull ListTableModel<EmployeeEntity> getTableModel() {
		return new ListTableModel<>(List.of("Nombre", "Apellido", "Email"), (item, column) -> switch (column) {
			case 0 -> item.getFirstName();
			case 1 -> item.getLastName();
			case 2 -> item.getEmail();
			default -> item.toString();
		});
	}
}