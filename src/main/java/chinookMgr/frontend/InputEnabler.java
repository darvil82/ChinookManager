package chinookMgr.frontend;

import chinookMgr.backend.Role;
import chinookMgr.backend.UserManager;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Supplier;

public class InputEnabler {
	private final Hashtable<Supplier<Boolean>, List<Component>> componentTable = new Hashtable<>();

	public void register(Supplier<Boolean> predicate, Component... components) {
		if (components.length == 0) {
			throw new IllegalArgumentException("At least one component is required");
		}

		this.componentTable.put(predicate, List.of(components));
	}

	public void register(Role requiredRole, Component... components) {
		this.register(() -> UserManager.getCurrentUser().hasPermission(requiredRole), components);
	}

	public void disableAll() {
		this.componentTable.values()
			.forEach(components -> components.forEach(component -> component.setEnabled(false)));
	}

	public void enableAll() {
		this.componentTable.values()
			.forEach(components -> components.forEach(component -> component.setEnabled(true)));
	}

	public void apply() {
		this.componentTable.forEach((predicate, components) -> {
			var result = predicate.get();
			components.forEach(component -> component.setEnabled(result));
		});
	}
}