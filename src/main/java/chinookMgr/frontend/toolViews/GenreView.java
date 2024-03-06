package chinookMgr.frontend.toolViews;

import chinookMgr.backend.Role;
import chinookMgr.backend.Saveable;
import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.GenreEntity;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.components.SaveOption;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class GenreView extends ToolView implements Saveable {
	private JTextField txtName;
	private JPanel mainPanel;
	private JPanel tracksPanel;
	private JPanel savePanel;
	private JPanel infoPanel;

	private GenreEntity currentGenre;

	public GenreView() {
		this.buildForNew();
	}

	public GenreView(GenreEntity currentGenre) {
		this.currentGenre = currentGenre;
		this.buildForEntity();
	}


	@Override
	protected void build() {
		this.insertView(this.savePanel, new SaveOption<>(this, Role.MANAGE_INVENTORY, false));

		this.getInputManager().register(Role.MANAGE_INVENTORY, this.txtName);
		this.getValidator().register(this.txtName, c -> !c.getText().isBlank(), "El nombre no puede estar vacío");
	}

	@Override
	protected void buildForEntity() {
		super.buildForEntity();
		this.txtName.setText(this.currentGenre.getName());

		this.initTracksPanel();
	}

	private void initTracksPanel() {
		this.tracksPanel.setVisible(true);
		this.infoPanel.setBorder(BorderFactory.createTitledBorder("Información"));
		this.insertView(
			this.tracksPanel,
			this.getInputManager().register(Role.MANAGE_INVENTORY, new GenericTableView<>("Canciones", GenreEntity.getTracksTableInspector(this.currentGenre)
				.openViewOnRowClick(TrackView::new)))
		);
	}

	@Override
	public @NotNull String getName() {
		if (this.currentGenre == null)
			return "Nuevo género";
		else
			return "Género (" + this.txtName.getText() + ")";
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}

	@Override
	public void save() {
		boolean isNew = this.currentGenre == null;

		if (isNew) {
			this.currentGenre = new GenreEntity();
		}

		this.currentGenre.setName(this.txtName.getText());

		HibernateUtil.withSession(session -> {
			session.merge(this.currentGenre);
		});

		if (!isNew) {
			ViewStack.current().pop();
			return;
		}

		this.initTracksPanel();
		this.notifyChange();
	}

	@Override
	public boolean isDeletable() {
		return this.currentGenre != null;
	}

	@Override
	public void cancel() {
		ViewStack.current().pop();
	}

	@Override
	public void delete() {
		this.currentGenre.remove();
		ViewStack.current().pop();
	}
}