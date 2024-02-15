package chinookMgr.frontend.toolViews;

import chinookMgr.backend.db.entities.*;
import chinookMgr.backend.entityHelpers.Album;
import chinookMgr.backend.entityHelpers.Genre;
import chinookMgr.backend.entityHelpers.MediaType;
import chinookMgr.backend.Saveable;
import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.frontend.InputValidator;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.Utils;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.components.SaveOption;
import chinookMgr.frontend.components.TableComboBox;
import chinookMgr.frontend.components.TableInspector;
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
		this.buildForEntity();
	}

	public TrackView() {
		this.track = null;
		this.buildForNew();
	}

	@Override
	protected void buildForEntity() {
		super.buildForEntity();

		this.txtName.setText(this.track.getName());
		this.txtComposer.setText(this.track.getComposer());
		this.numPrice.setValue(this.track.getUnitPrice().doubleValue());
		this.selectedAlbum = Album.getById(this.track.getAlbumId());

		this.btnAlbum.setText(this.selectedAlbum.getTitle());
		this.selectedGenre = Genre.getById(this.track.getGenreId());
		this.btnGenre.setText(this.selectedGenre.getName());
		this.comboMediaType.setSelectedItem(MediaType.getById(this.track.getMediaTypeId()));
		this.playlistsPanel.setVisible(true);
		this.insertView(this.playlistsPanel, new GenericTableView<>(
			"Playlists", new TableInspector<>(
				(session, input) -> session.createQuery(
					"select p from PlaylistEntity p, PlaylistTrackEntity pt where trackId = :id and p.playlistId = pt.playlistId"
					, PlaylistEntity.class
				)
					.setParameter("id", this.track.getTrackId()),

				(session, input) -> session.createQuery("select count(*) from PlaylistTrackEntity where trackId = :id", Long.class)
					.setParameter("id", this.track.getTrackId())
			)
		));

		int millis = track.getMilliseconds();
		int minutes = millis / 60000;
		int seconds = (millis % 60000) / 1000;

		this.numMinutes.setValue(minutes);
		this.numSeconds.setValue(seconds);
	}

	@Override
	protected void build() {
		SwingUtilities.invokeLater(() -> this.txtName.grabFocus());

		this.getValidator().register(this.txtName, c -> !c.getText().isBlank(), "No se ha especificado un nombre");
		this.getValidator().register(this.btnAlbum, c -> this.selectedAlbum != null, "No se ha seleccionado un album");
		this.getValidator().register(this.btnGenre, c -> this.selectedGenre != null, "No se ha seleccionado un género");

		this.mediaTypeContainer.add(
			this.comboMediaType = new TableComboBox<>(MediaTypeEntity.class, MediaTypeEntity::getName)
		);
		Utils.attachViewSelectorToButton(this.btnAlbum, "album", Album.getTableInspector(), this::selectAlbum, () -> new AlbumView(this.selectedAlbum));
		Utils.attachViewSelectorToButton(this.btnGenre, "género", Genre.getTableInspector(), this::selectGenre, () -> new GenreView(this.selectedGenre));

		this.insertView(this.savePanel, new SaveOption<>(this));

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

		HibernateUtil.withSession(session -> {
			session.beginTransaction();

			this.track.setName(this.txtName.getText());
			this.track.setComposer(this.txtComposer.getText());
			this.track.setAlbumId(this.selectedAlbum.getAlbumId());
			this.track.setMilliseconds(
				((int)this.numMinutes.getValue() * 60000) + ((int)this.numSeconds.getValue() * 1000)
			);
			this.track.setUnitPrice(BigDecimal.valueOf((double)this.numPrice.getValue()));
			this.track.setMediaTypeId(this.comboMediaType.getSelectedEntity().getMediaTypeId());
			this.track.setGenreId(this.selectedGenre.getGenreId());

			session.merge(this.track);

			session.getTransaction().commit();
		});
	}

	@Override
	public void delete() {
		HibernateUtil.withSession(session -> {
			session.beginTransaction();
			session.remove(this.track);
			session.getTransaction().commit();
		});
	}

	@Override
	public boolean isDeletable() {
		return this.track != null;
	}
}