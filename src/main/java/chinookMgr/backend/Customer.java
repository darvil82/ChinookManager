package chinookMgr.backend;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.CustomerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
}