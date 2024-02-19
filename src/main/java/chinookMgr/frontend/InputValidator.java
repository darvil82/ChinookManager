package chinookMgr.frontend;

import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class InputValidator {
	private final List<Validator<JComponent>> validators = new ArrayList<>();


	public record Validator<T extends JComponent>(T component, Predicate<T> predicate, String errorMessage) {}

	@SuppressWarnings("unchecked")
	public <T extends JComponent> InputValidator register(T component, Predicate<T> predicate, String errorMessage) {
		validators.add((Validator<JComponent>) new Validator<>(component, predicate, errorMessage));
		return this;
	}

	public <T extends JComponent> InputValidator register(Supplier<Boolean> predicate, String errorMessage) {
		return this.register(null, c -> predicate.get(), errorMessage);
	}

	public boolean validate() {
		var errors = new ArrayList<String>();

		for (var validator : validators) {
			if (!validator.predicate.test(validator.component())) {
				if (validator.component() != null)
					validator.component().setBackground(Color.decode("#4d3636"));
				errors.add(validator.errorMessage);
			}
		}

		if (!errors.isEmpty()) {
			JOptionPane.showMessageDialog(ViewStack.currentPanel(), String.join(".\n", errors), "Error", JOptionPane.ERROR_MESSAGE);
		}

		return errors.isEmpty();
	}
}