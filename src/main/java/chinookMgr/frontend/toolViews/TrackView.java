package chinookMgr.frontend.toolViews;

import chinookMgr.backend.db.entities.*;
import chinookMgr.backend.Saveable;
import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.Utils;
import chinookMgr.frontend.components.SaveOption;
import chinookMgr.frontend.components.TableComboBox;
import chinookMgr.frontend.components.TableInspector;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.math.BigDecimal;

import static chinookMgr.backend.db.entities.EntityHelper.defaultSearch;

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
		this.selectedAlbum = AlbumEntity.getById(this.track.getAlbumId());
		this.selectedGenre = GenreEntity.getById(this.track.getGenreId());
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
		this.comboMediaType.setSelectedItem(MediaTypeEntity.getById(this.track.getMediaTypeId()));
		this.playlistsPanel.setVisible(true);
		this.insertView(this.playlistsPanel, new GenericTableView<>("Listas", this.track.getPlaylistsTableInspector()));

		int millis = track.getMilliseconds();
		int minutes = millis / 60000;
		int seconds = (millis % 60000) / 1000;

		this.numMinutes.setValue(minutes);
		this.numSeconds.setValue(seconds);
	}

	@Override
	protected void build() {
		SwingUtilities.invokeLater(() -> this.txtName.grabFocus());

		this.mediaTypeContainer.add(this.comboMediaType = new TableComboBox<>(MediaTypeEntity.class, MediaTypeEntity::getName));
		Utils.attachViewSelectorToButton(this.btnAlbum, () -> this.selectedAlbum, "album", AlbumEntity.getTableInspector(), a -> this.selectedAlbum = a, AlbumView::new);
		Utils.attachViewSelectorToButton(this.btnGenre, () -> this.selectedGenre, "género", GenreEntity.getTableInspector(), g -> this.selectedGenre = g, GenreView::new);

		this.insertView(this.savePanel, new SaveOption<>(this));

		this.numPrice.setModel(new SpinnerNumberModel(0, 0., 1000., 0.01));
		this.numPrice.setEditor(new JSpinner.NumberEditor(this.numPrice, "0.00 €"));

		this.numMinutes.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
		this.numMinutes.setEditor(new JSpinner.NumberEditor(this.numMinutes, "0 min"));
		this.numSeconds.setModel(new SpinnerNumberModel(0, 0, 59, 1));
		this.numSeconds.setEditor(new JSpinner.NumberEditor(this.numSeconds, "0 s"));


		this.getValidator().register(this.txtName, c -> !c.getText().isBlank(), "No se ha especificado un nombre");
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
			this.track.setName(this.txtName.getText());
			this.track.setComposer(this.txtComposer.getText());
			this.track.setAlbumId(this.selectedAlbum != null ? this.selectedAlbum.getAlbumId() : null);
			this.track.setMilliseconds(
				((int)this.numMinutes.getValue() * 60000) + ((int)this.numSeconds.getValue() * 1000)
			);
			this.track.setUnitPrice(BigDecimal.valueOf((double)this.numPrice.getValue()));
			this.track.setMediaTypeId(this.comboMediaType.getSelectedEntity().getMediaTypeId());
			this.track.setGenreId(this.selectedGenre != null ? this.selectedGenre.getGenreId() : null);

			session.merge(this.track);
		});
	}

	@Override
	public void delete() {
		TrackEntity.disable(this.track);
	}

	@Override
	public boolean isDeletable() {
		return this.track != null;
	}
}