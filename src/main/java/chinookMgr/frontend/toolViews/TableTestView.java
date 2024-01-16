package chinookMgr.frontend.toolViews;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.ArtistEntity;
import chinookMgr.backend.db.entities.TrackEntity;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.View;
import chinookMgr.frontend.components.TableInspector;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jetbrains.annotations.NotNull;

import javax.sound.midi.Track;
import javax.swing.*;
import java.util.function.Function;

public class TableTestView implements ToolView {
	private JPanel mainPanel;
	private JPanel tableContainer;

	public TableTestView() {
		View.insert(this.tableContainer, new TableInspector<TrackEntity>(TrackEntity.class, (q, input) -> {
			return q.apply("name like ?1").setParameter(1, "%" + input + "%");
		}));

	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}

	@Override
	public @NotNull String getName() {
		return "Table Test";
	}
}