package chinookMgr.frontend.toolViews.reports;

import chinookMgr.backend.db.entities.InvoiceLineEntity;
import chinookMgr.backend.db.entities.TrackEntity;
import chinookMgr.frontend.components.TableInspector;
import chinookMgr.frontend.toolViews.TrackView;
import chinookMgr.shared.ListTableModel;

import java.util.List;
import java.util.Objects;

import static chinookMgr.backend.db.entities.EntityHelper.defaultSearch;

public final class TrackReportsTableInspectors {
	private TrackReportsTableInspectors() { }


	public static TableInspector<InvoiceLineEntity> getBoughtTracksTableInspector(boolean most) {
		return new TableInspector<>(
			(session, search) ->
				session.createQuery(
					"select il from TrackEntity t join InvoiceLineEntity il on il.trackId = t.trackId where t.name like :search group by il order by sum(il.quantity) "
						+ (most ? "desc" : "asc"),
					InvoiceLineEntity.class
				)
					.setParameter("search", defaultSearch(search)),

			(session, search) ->
				session.createQuery("select count(*) from InvoiceLineEntity il join TrackEntity t on il.trackId = t.trackId where t.name like :search", Long.class)
					.setParameter("search", defaultSearch(search)),

			new ListTableModel<>(List.of("Canción", "Cantidad"), (i, c) -> switch (c) {
				case 0 -> Objects.requireNonNull(TrackEntity.getById(i.getTrackId())).getName();
				case 1 -> String.valueOf(i.getQuantity());
				default -> throw new IndexOutOfBoundsException();
			})
		).openViewOnRowClick(il -> new TrackView(TrackEntity.getById(il.getTrackId())));
	}
}