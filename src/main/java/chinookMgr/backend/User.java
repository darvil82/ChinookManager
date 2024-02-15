package chinookMgr.backend;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class User<T> {
	public abstract @NotNull String getFirstName();
	public abstract @NotNull String getLastName();
	public abstract @Nullable String getAddress();
	public abstract @Nullable String getCity();
	public abstract @Nullable String getState();
	public abstract @Nullable String getCountry();
	public abstract @Nullable String getPostalCode();
	public abstract @Nullable String getPhone();
	public abstract @Nullable String getFax();
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