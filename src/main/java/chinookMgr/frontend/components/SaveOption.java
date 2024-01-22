package chinookMgr.frontend.components;

import chinookMgr.backend.Saveable;
import chinookMgr.frontend.View;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class SaveOption extends View {
	private JButton btnSave;
	private JButton btnCancel;
	private JPanel mainPanel;

	public SaveOption(Saveable parent) {
		this.btnSave.addActionListener(e -> parent.save());
		this.btnCancel.addActionListener(e -> parent.cancel());
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}
}