package chinookMgr.frontend.toolViews;

import chinookMgr.backend.Saveable;
import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.ArtistEntity;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.components.SaveOption;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ArtistView extends ToolView implements Saveable {
	private JTextField txtName;
	private JPanel albumsPanel;
	private JPanel mainPanel;
	private JPanel infoPanel;
	private JPanel savePanel;

	private ArtistEntity currentArtist;

	public ArtistView(ArtistEntity artist) {
		this.currentArtist = artist;
		this.buildForEntity();
	}

	public ArtistView() {
		this.buildForNew();
	}

	@Override
	protected void buildForEntity() {
		super.buildForEntity();
		this.txtName.setText(this.currentArtist.getName());
		this.initAlbumsPanel();
	}

	private void initAlbumsPanel() {
		this.albumsPanel.setVisible(true);
		this.infoPanel.setBorder(BorderFactory.createTitledBorder("Información"));
		this.insertView(
			this.albumsPanel,
			new GenericTableView<>("álbunes", ArtistEntity.getAlbumsTableInspector(this.currentArtist)
				.openViewOnRowClick(AlbumView::new))
		);
	}

	@Override
	protected void build() {
		super.build();
		this.insertView(this.savePanel, new SaveOption<>(this, false));
	}

	@Override
	public @NotNull String getName() {
		if (this.currentArtist != null) {
			return "Artista (" + this.currentArtist.getName() + ")";
		}

		return "Nuevo Artista";
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}

	@Override
	public void save() {
		boolean isNew = this.currentArtist == null;

		if (isNew) {
			this.currentArtist = new ArtistEntity();
		}

		this.currentArtist.setName(this.txtName.getText());

		HibernateUtil.withSession(s -> {
			s.merge(this.currentArtist);
		});

		if (!isNew) {
			ViewStack.current().pop();
			return;
		}

		this.initAlbumsPanel();
		this.notifyChange();
	}

	@Override
	public void cancel() {
		ViewStack.current().pop();
	}

	@Override
	public boolean isDeletable() {
		return this.currentArtist != null;
	}

	@Override
	public void delete() {
		HibernateUtil.withSession(s -> {
			s.remove(this.currentArtist);
		});

		ViewStack.current().pop();
	}
}