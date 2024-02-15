package chinookMgr.frontend.components;

import chinookMgr.backend.Saveable;
import chinookMgr.frontend.*;
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
		this.build();
	}


	@Override
	protected void build() {
		if (parent.isDeletable()) {
			this.btnDelete.setVisible(true);
			this.btnDelete.addActionListener(e -> this.onDelete());
		}
		this.btnSave.addActionListener(e -> this.onSave());
		this.btnCancel.addActionListener(e -> this.onCancel());
	}

	private void whenDone() {
		ViewStack.current().pop();
	}

	private void onSave() {
		LoadingManager.pushPop("Guardando", () -> {
			if (!this.parent.getValidator().validate()) {
				StatusManager.showUpdate("No se pudo guardar el elemento");
				return;
			}
			this.parent.save();
			this.whenDone();
			StatusManager.showUpdate("Elemento guardado");
		});
	}

	private void onCancel() {
		this.parent.cancel();
		this.whenDone();
	}

	private void onDelete() {
		LoadingManager.pushPop("Eliminando", () -> {
			if (JOptionPane.showConfirmDialog(
				this.parent.getPanel(),
				"¿Está seguro que desea eliminar el elemento " + this.parent.getName() + "?",
				"Eliminar",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE
			) == JOptionPane.YES_OPTION) {
				this.parent.delete();
				StatusManager.showUpdate("Elemento eliminado");
				this.whenDone();
			} else {
				StatusManager.showUpdate("Eliminación cancelada");
			}
		});
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}

}