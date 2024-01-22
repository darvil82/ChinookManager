package chinookMgr.frontend.toolViews;

import chinookMgr.backend.Album;
import chinookMgr.backend.Saveable;
import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.AlbumEntity;
import chinookMgr.backend.db.entities.TrackEntity;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.components.SaveOption;
import chinookMgr.frontend.toolViews.test.AlbumsView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class TrackView extends ToolView implements Saveable {
	private JTextField txtName;
	private JTextField txtComposer;
	private JPanel mainPanel;
	private JButton btnAlbum;
	private JPanel savePanel;
	private JSpinner spinner1;
	private JSpinner spinner2;
	private JSpinner spinner3;

	private final TrackEntity track;
	private AlbumEntity selectedAlbum;


	public TrackView(TrackEntity track) {
		this.track = track;

		this.txtName.setText(track.getName());
		this.txtComposer.setText(track.getComposer());

		this.selectedAlbum = Album.getById(track.getAlbumId());
		this.btnAlbum.setText(this.selectedAlbum.getTitle());

		this.btnAlbum.addActionListener(e -> ViewStack.pushAwait(new AlbumsView(true), this::selectAlbum));
		this.insertView(this.savePanel, new SaveOption(this));
	}

	private void selectAlbum(@NotNull AlbumEntity album) {
		this.selectedAlbum = album;
		this.btnAlbum.setText(album.getTitle());
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}

	@Override
	public @NotNull String getName() {
		return "Canción (" + this.txtName.getText() + ")";
	}

	@Override
	public void save() {
		try (var session = HibernateUtil.getSession()) {
			session.beginTransaction();

			this.track.setName(this.txtName.getText());
			this.track.setComposer(this.txtComposer.getText());
			this.track.setAlbumId(this.selectedAlbum.getAlbumId());

			session.merge(this.track);
			session.getTransaction().commit();
		}

		ViewStack.pop();
	}
}