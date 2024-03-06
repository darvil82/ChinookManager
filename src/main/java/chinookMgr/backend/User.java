package chinookMgr.backend;

import chinookMgr.backend.db.entities.CustomerEntity;
import chinookMgr.backend.db.entities.EmployeeEntity;
import chinookMgr.shared.ListTableModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class User {
	public abstract @NotNull Integer getId();

	public abstract @NotNull String getFirstName();
	public abstract void setFirstName(@NotNull String firstName);

	public abstract @NotNull String getLastName();
	public abstract void setLastName(@NotNull String lastName);

	public abstract @Nullable String getAddress();
	public abstract void setAddress(@Nullable String address);

	public abstract @Nullable String getCity();
	public abstract void setCity(@Nullable String city);

	public abstract @Nullable String getState();
	public abstract void setState(@Nullable String state);

	public abstract @Nullable String getCountry();
	public abstract void setCountry(@Nullable String country);

	public abstract @Nullable String getPostalCode();
	public abstract void setPostalCode(@Nullable String postalCode);

	public abstract @Nullable String getPhone();
	public abstract void setPhone(@Nullable String phone);

	public abstract @Nullable String getFax();
	public abstract void setFax(@Nullable String fax);

	public abstract @NotNull String getEmail();
	public abstract void setEmail(@NotNull String email);

	public abstract byte[] getPassword();
	public abstract void setPassword(byte[] password);

	public abstract @NotNull List<Role> getRoles();

	public abstract void setRoles(@NotNull List<Role> roles);

	public void addRole(@NotNull Role role) {
		var roles = new ArrayList<>(this.getRoles());
		if (roles.contains(role)) return;
		roles.add(role);
		this.setRoles(roles);
	}

	public void removeRole(@NotNull Role role) {
		var roles = new ArrayList<>(this.getRoles());
		roles.remove(role);
		this.setRoles(roles);
	}


	public @NotNull String getFullName() {
		return this.getFirstName() + " " + this.getLastName();
	}

	public boolean hasPermission(@NotNull Role role) {
		var roles = this.getRoles();
		return roles.contains(role) || roles.contains(Role.ADMIN); // Admins have all permissions
	}

	@Override
	public String toString() {
		return this.getFullName();
	}

	public static @NotNull <T extends User> ListTableModel<T> getTableModel() {
		return new ListTableModel<>(List.of("Nombre", "Apellido", "Email"), (item, column) -> switch (column) {
			case 0 -> item.getFirstName();
			case 1 -> item.getLastName();
			case 2 -> item.getEmail();
			default -> item.toString();
		});
	}
}