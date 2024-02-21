package chinookMgr.backend;

import chinookMgr.backend.db.entities.CustomerEntity;
import chinookMgr.backend.db.entities.EmployeeEntity;
import chinookMgr.shared.ListTableModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface User {
	@NotNull String getFirstName();
	@NotNull String getLastName();
	@Nullable String getAddress();
	@Nullable String getCity();
	@Nullable String getState();
	@Nullable String getCountry();
	@Nullable String getPostalCode();
	@Nullable String getPhone();
	@Nullable String getFax();
	@NotNull String getEmail();

	List<Role> getRoles();

	default @NotNull String getFullName() {
		return this.getFirstName() + " " + this.getLastName();
	}

	default boolean hasPermission(@NotNull Role role) {
		return this.getRoles().contains(role) || this.getRoles().contains(Role.ADMIN);
	}

	static @NotNull <T extends User> ListTableModel<T> getTableModel() {
		return new ListTableModel<>(List.of("Nombre", "Apellido", "Email"), (item, column) -> switch (column) {
			case 0 -> item.getFirstName();
			case 1 -> item.getLastName();
			case 2 -> item.getEmail();
			default -> item.toString();
		});
	}
}