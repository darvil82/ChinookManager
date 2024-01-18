package chinookMgr.frontend.toolViews;

import chinookMgr.backend.db.entities.TrackEntity;
import chinookMgr.frontend.ToolView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class TrackView implements ToolView {
	private JTextField txtName;
	private JTextField txtComposer;
	private JPanel mainPanel;
	private JTextField txtAlbum;

	public TrackView(TrackEntity track) {
		this.txtName.setText(track.getName());
		this.txtComposer.setText(track.getComposer());
		this.txtAlbum.setText(track.getAlbumId().toString());
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