package chinookMgr.frontend.toolViews;

import chinookMgr.backend.Album;
import chinookMgr.backend.Genre;
import chinookMgr.backend.MediaType;
import chinookMgr.backend.Saveable;
import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.AlbumEntity;
import chinookMgr.backend.db.entities.GenreEntity;
import chinookMgr.backend.db.entities.MediaTypeEntity;
import chinookMgr.backend.db.entities.TrackEntity;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.components.SaveOption;
import chinookMgr.frontend.components.TableComboBox;
import chinookMgr.frontend.components.TableInspector;
import chinookMgr.frontend.toolViews.test.AlbumsView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.math.BigDecimal;

public class TrackView extends ToolView implements Saveable {
	private JTextField txtName;
	private JTextField txtComposer;
	private JPanel mainPanel;
	private JButton btnAlbum;
	private JPanel savePanel;
	private JSpinner numMinutes;
	private JSpinner numPrice;
	private JSpinner numSeconds;
	private JPanel playlistsPanel;
	private JPanel mediaTypeContainer;
	private JButton btnGenre;
	private TableComboBox<MediaTypeEntity> comboMediaType;

	private TrackEntity track;
	private AlbumEntity selectedAlbum;
	private GenreEntity selectedGenre;


	public TrackView(TrackEntity track) {
		this.track = track;
		this.buildForTrack();
	}

	public TrackView() {
		this.track = null;
		this.buildForNew();
	}

	private void buildForNew() {
		this.build();
	}

	private void buildForTrack() {
		this.build();

		this.txtName.setText(this.track.getName());
		this.txtComposer.setText(this.track.getComposer());
		this.numPrice.setValue(this.track.getUnitPrice().doubleValue());
		this.selectedAlbum = Album.getById(this.track.getAlbumId());
		this.btnAlbum.setText(this.selectedAlbum.getTitle());
		this.selectedGenre = Genre.getById(this.track.getGenreId());
		this.btnGenre.setText(this.selectedGenre.getName());
		this.comboMediaType.setSelectedItem(MediaType.getById(this.track.getMediaTypeId()));

		int millis = track.getMilliseconds();
		int minutes = millis / 60000;
		int seconds = (millis % 60000) / 1000;

		this.numMinutes.setValue(minutes);
		this.numSeconds.setValue(seconds);
	}

	private void build() {
		SwingUtilities.invokeLater(() -> this.txtName.grabFocus());

		this.btnAlbum.addActionListener(e -> ViewStack.pushAwait(new AlbumsView(true), this::selectAlbum));
		this.mediaTypeContainer.add(
			this.comboMediaType = new TableComboBox<>(MediaTypeEntity.class, MediaTypeEntity::getName)
		);
		this.btnGenre.addActionListener(e -> ViewStack.pushAwait(
			new GenericTableView<>("Géneros", false, Genre.getTableInspectorBuilder()), this::selectGenre
		));

		this.insertView(this.savePanel, new SaveOption(this));

		this.numPrice.setModel(new SpinnerNumberModel(0, 0., 1000., 0.01));
		this.numPrice.setEditor(new JSpinner.NumberEditor(this.numPrice, "0.00 €"));

		this.numMinutes.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
		this.numMinutes.setEditor(new JSpinner.NumberEditor(this.numMinutes, "0 min"));
		this.numSeconds.setModel(new SpinnerNumberModel(0, 0, 59, 1));
		this.numSeconds.setEditor(new JSpinner.NumberEditor(this.numSeconds, "0 s"));
	}

	private void selectAlbum(@NotNull AlbumEntity album) {
		this.selectedAlbum = album;
		this.btnAlbum.setText(album.getTitle());
	}

	private void selectGenre(@NotNull GenreEntity genre) {
		this.selectedGenre = genre;
		this.btnGenre.setText(genre.getName());
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}

	@Override
	public @NotNull String getName() {
		if (this.track == null)
			return "Nueva canción";
		else
			return "Canción (" + this.txtName.getText() + ")";
	}

	@Override
	public void save() {
		boolean isNew = this.track == null;

		if (isNew)
			this.track = new TrackEntity();

		try (var session = HibernateUtil.getSession()) {
			session.beginTransaction();

			this.track.setName(this.txtName.getText());
			this.track.setComposer(this.txtComposer.getText());
			this.track.setAlbumId(this.selectedAlbum.getAlbumId());
			this.track.setMilliseconds(
				((int)this.numMinutes.getValue() * 60000) + ((int)this.numSeconds.getValue() * 1000)
			);
			this.track.setUnitPrice(BigDecimal.valueOf((double)this.numPrice.getValue()));
			this.track.setMediaTypeId(this.comboMediaType.getSelectedEntity().getMediaTypeId());

			if (isNew)
				session.persist(this.track);
			else
				session.merge(this.track);

			session.getTransaction().commit();
		}

		ViewStack.pop();
	}

}