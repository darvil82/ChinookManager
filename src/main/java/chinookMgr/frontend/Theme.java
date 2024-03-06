package chinookMgr.frontend;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.*;

import javax.swing.*;

public enum Theme {
	ONE_DARK("One Dark"),
	LIGHT("Claro"),
	DARK("Oscuro"),
	DRACULA("Dracula"),
	MONOKAI("Monokai"),
	COBALT("Cobalto"),
	NORD("Nord"),
	XCODE("Xcode");

	public final String name;
	private static Theme current;

	Theme(String name) {
		this.name = name;
	}

	public void apply() {
		switch (this) {
			case ONE_DARK -> FlatOneDarkIJTheme.setup();
			case LIGHT -> FlatLightLaf.setup();
			case DARK -> FlatDarkLaf.setup();
			case DRACULA -> FlatDraculaIJTheme.setup();
			case MONOKAI -> FlatMonokaiProIJTheme.setup();
			case COBALT -> FlatCobalt2IJTheme.setup();
			case NORD -> FlatNordIJTheme.setup();
			case XCODE -> FlatXcodeDarkIJTheme.setup();
		}

		SwingUtilities.invokeLater(FlatDarkLaf::updateUI);
		current = this;
	}

	public static Theme getCurrent() {
		return current;
	}

	@Override
	public String toString() {
		return this.name;
	}
}