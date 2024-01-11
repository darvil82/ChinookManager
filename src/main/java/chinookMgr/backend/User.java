package chinookMgr.backend;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class User<T> {
	public abstract @NotNull String getFirstName();
	public abstract @NotNull String getLastName();
	public abstract @NotNull String getAddress();
	public abstract @NotNull String getCity();
	public abstract @NotNull String getState();
	public abstract @NotNull String getCountry();
	public abstract @NotNull String getPostalCode();
	public abstract @NotNull String getPhone();
	public abstract @NotNull String getEmail();

	public abstract @NotNull T getEntity();
	public abstract List<Role> getRoles();

	public @NotNull String getFullName() {
		return this.getFirstName() + " " + this.getLastName();
	}

	public boolean hasPermission(@NotNull Role role) {
		return this.getRoles().contains(role) || this.getRoles().contains(Role.ADMIN);
	}
}