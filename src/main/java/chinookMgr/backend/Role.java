package chinookMgr.backend;

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

	public static List<Role> getRolesFromFlags(byte flags) {
		return Stream.of(Role.values())
		   .filter(role -> (flags & role.flag_value) != 0)
		   .toList();
	}
}