package chinookMgr.frontend;

import chinookMgr.backend.Role;
import chinookMgr.backend.UserManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Supplier;

public class InputManager {
	private final Hashtable<Supplier<Boolean>, List<Component>> componentTable = new Hashtable<>();
	private final List<InputManager> subManagers = new ArrayList<>(0);

	public void register(Supplier<Boolean> predicate, Component... components) {
		if (components.length == 0) {
			throw new IllegalArgumentException("At least one component is required");
		}

		this.componentTable.put(predicate, List.of(components));
	}

	public void register(@NotNull Role requiredRole, Component... components) {
		this.register(() -> UserManager.getCurrentUser().hasPermission(requiredRole), components);
	}

	public <T extends Component> T register(@NotNull Role requiredRole, T component) {
		this.register(requiredRole, new Component[]{component});
		return component;
	}

	public void register(InputManager manager) {
		this.subManagers.add(manager);
	}

	public void disableAll() {
		this.componentTable.values()
			.forEach(components -> components.forEach(component -> component.setEnabled(false)));

		this.subManagers.forEach(InputManager::disableAll);
	}

	public void enableAll() {
		this.componentTable.values()
			.forEach(components -> components.forEach(component -> component.setEnabled(true)));

		this.subManagers.forEach(InputManager::enableAll);
	}

	public void apply() {
		this.componentTable.forEach((predicate, components) -> {
			var result = predicate.get();
			components.forEach(component -> component.setEnabled(result));
		});

		this.subManagers.forEach(InputManager::apply);
	}
}