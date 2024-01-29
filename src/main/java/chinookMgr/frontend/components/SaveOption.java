package chinookMgr.frontend.components;

import chinookMgr.backend.Saveable;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.View;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class SaveOption<T extends ToolView & Saveable> extends View {
	private JButton btnSave;
	private JButton btnCancel;
	private JPanel mainPanel;
	private JButton btnDelete;

	private final @NotNull T parent;

	public SaveOption(@NotNull T parent) {
		this.parent = parent;
		if (parent.isDeletable()) {
			this.btnDelete.setVisible(true);
			this.btnDelete.addActionListener(e -> this.onDelete());
		}
		this.btnSave.addActionListener(e -> parent.save());
		this.btnCancel.addActionListener(e -> parent.cancel());
	}

	private void onDelete() {
		if (JOptionPane.showConfirmDialog(
			this.parent.getPanel(),
			"¿Está seguro que desea eliminar el elemento " + this.parent.getName() + "?",
			"Eliminar",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.WARNING_MESSAGE
		) == JOptionPane.YES_OPTION) {
			this.parent.delete();
		}
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}
}