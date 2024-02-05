package chinookMgr.frontend.toolViews;

import chinookMgr.backend.Saveable;
import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.GenreEntity;
import chinookMgr.backend.entityHelpers.Genre;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.components.SaveOption;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class GenreView extends ToolView implements Saveable {
	private JTextField txtName;
	private JPanel mainPanel;
	private JPanel tracksPanel;
	private JPanel savePanel;

	private GenreEntity genre;

	public GenreView() {
		this.build();
	}

	public GenreView(GenreEntity genre) {
		this.genre = genre;
		this.buildForGenre();
	}

	private void buildForGenre() {
		this.build();
		this.txtName.setText(this.genre.getName());
		this.tracksPanel.setVisible(true);
		this.insertView(
			this.tracksPanel,
			new GenericTableView<>("Canciones", Genre.getTracksTableInspector(this.genre)
				.openViewOnRowClick(TrackView::new))
		);
	}

	private void build() {
		this.insertView(this.savePanel, new SaveOption<>(this));
	}

	@Override
	public @NotNull String getName() {
		if (this.genre == null)
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
		if (this.genre == null) {
			this.genre = new GenreEntity();
		}

		this.genre.setName(this.txtName.getText());

		try (var session = HibernateUtil.getSession()) {
			session.beginTransaction();
			session.persist(this.genre);
			session.getTransaction().commit();
		}
	}
}