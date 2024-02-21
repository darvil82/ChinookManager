package chinookMgr.backend;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.CustomerEntity;
import chinookMgr.backend.entityHelpers.EntityHelper;
import chinookMgr.frontend.components.TableInspector;
import chinookMgr.shared.ListTableModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static chinookMgr.backend.entityHelpers.EntityHelper.defaultSearch;

public class Customer extends User<CustomerEntity> {
	private final @NotNull CustomerEntity entity;

	public Customer(@NotNull CustomerEntity entity) {
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
		return this.entity.getState();
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
	public @NotNull CustomerEntity getEntity() {
		return this.entity;
	}

	@Override
	public List<Role> getRoles() {
		return HibernateUtil.withSession(session -> {
			byte flags = session.createQuery(
				"select roles from CustomerEntity where customerId = :id",
				Byte.class
			)
				.setParameter("id", this.entity.getCustomerId())
				.getSingleResult();

			return Role.getRolesFromFlags(flags);
		});
	}


	public static CustomerEntity getById(int id) {
		return EntityHelper.getById(CustomerEntity.class, id);
	}

	public static TableInspector<CustomerEntity> getTableInspector() {
		return new TableInspector<>(
			(session, search) -> session.createQuery("from CustomerEntity where firstName like :search or lastName like :search", CustomerEntity.class)
				.setParameter("search", defaultSearch(search)),
			(session, search) -> session.createQuery("select count(*) from CustomerEntity where firstName like :search or lastName like :search", Long.class)
				.setParameter("search", defaultSearch(search)),

			Customer.getTableModel()
		);
	}

	public static @NotNull ListTableModel<CustomerEntity> getTableModel() {
		return new ListTableModel<>(List.of("Nombre", "Apellido", "Email"), (item, column) -> switch (column) {
			case 0 -> item.getFirstName();
			case 1 -> item.getLastName();
			case 2 -> item.getEmail();
			default -> item.toString();
		});
	}
}