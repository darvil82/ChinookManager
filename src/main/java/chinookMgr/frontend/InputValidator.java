package chinookMgr.frontend;

import chinookMgr.shared.Pair;
import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class InputValidator {
	private final List<Validator<JComponent>> validators = new ArrayList<>();
	private final Hashtable<JComponent, Color> failedComponents = new Hashtable<>();


	public record Validator<T extends JComponent>(T component, Predicate<T> predicate, String errorMessage) {}

	@SuppressWarnings("unchecked")
	public <T extends JComponent> InputValidator register(T component, Predicate<T> predicate, String errorMessage) {
		if (component != null && validators.stream().anyMatch(v -> v.component() == component)) {
			throw new IllegalArgumentException("Component already registered");
		}

		validators.add((Validator<JComponent>) new Validator<>(component, predicate, errorMessage));
		return this;
	}

	public <T extends JComponent> InputValidator register(Supplier<Boolean> predicate, String errorMessage) {
		return this.register(null, c -> predicate.get(), errorMessage);
	}

	public boolean validate() {
		var errors = new ArrayList<String>();

		for (var validator : validators) {
			var component = validator.component();

			if (!validator.predicate.test(component)) {
				if (component != null && !this.failedComponents.containsKey(component)) {
					this.failedComponents.put(component, component.getBackground());
					component.setBackground(Color.decode("#4d3636"));
				}
				errors.add(validator.errorMessage);
				continue;
			}

			if (component != null && this.failedComponents.containsKey(component)) {
				component.setBackground(this.failedComponents.get(component));
				this.failedComponents.remove(component);
			}
		}

		if (!errors.isEmpty()) {
			JOptionPane.showMessageDialog(ViewStack.currentPanel(), String.join(".\n", errors) + ".", "Error", JOptionPane.ERROR_MESSAGE);
		}

		return errors.isEmpty();
	}
}