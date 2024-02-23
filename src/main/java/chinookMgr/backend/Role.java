package chinookMgr.backend;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

public enum Role {
	ADMIN,
	MANAGE_EMPLOYEES,
	MANAGE_CUSTOMERS,
	MANAGE_INVENTORY;

	public final byte flag_value;

	Role() {
		this.flag_value = (byte)(1 << this.ordinal());
	}

	public static @NotNull List<Role> getRolesFromFlags(byte flags) {
		return Stream.of(Role.values())
		   .filter(role -> (flags & role.flag_value) != 0)
		   .toList();
	}

	public static byte getFlagsFromRoles(@NotNull List<Role> roles) {
		return (byte)roles.stream()
		   .mapToInt(role -> role.flag_value)
		   .reduce(0, (a, b) -> a | b);
	}
}