package chinookMgr.backend;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

public enum Role {
	ADMIN("Administrador"),
	MANAGE_EMPLOYEES("Gestionar empleados"),
	MANAGE_CUSTOMERS("Gestionar clientes"),
	MANAGE_INVENTORY("Gestionar inventario"),;

	public final byte flag_value;
	public final @NotNull String name;

	Role(@NotNull String name) {
		this.name = name;
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

	@Override
	public String toString() {
		return this.name;
	}
}