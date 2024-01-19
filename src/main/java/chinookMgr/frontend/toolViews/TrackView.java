package chinookMgr.frontend.toolViews;

import chinookMgr.backend.Album;
import chinookMgr.backend.db.entities.TrackEntity;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.toolViews.test.AlbumsView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class TrackView extends ToolView {
	private JTextField txtName;
	private JTextField txtComposer;
	private JPanel mainPanel;
	private JButton btnAlbum;

	public TrackView(TrackEntity track) {
		this.txtName.setText(track.getName());
		this.txtComposer.setText(track.getComposer());
		this.btnAlbum.setText(Album.getById(track.getAlbumId()).getTitle());
		this.btnAlbum.addActionListener(e ->
			ViewStack.pushAwait(new AlbumsView(true), a -> this.btnAlbum.setText(a.getTitle()))
		);
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}

	@Override
	public @NotNull String getName() {
		return "Canción (" + this.txtName.getText() + ")";
	}
}