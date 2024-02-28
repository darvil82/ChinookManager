package chinookMgr.frontend;

import chinookMgr.backend.Role;
import chinookMgr.backend.User;
import chinookMgr.backend.db.entities.*;
import chinookMgr.frontend.components.Toolbar;
import chinookMgr.frontend.toolViews.*;
import chinookMgr.frontend.toolViews.reports.ReportsView;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;


public abstract class UserToolbars {
	private UserToolbars() {}

	public static final Consumer<Toolbar> WELCOME = t -> t.addOption("Inicio", e -> ViewStack.current().replaceWithWelcome());

	public static final Consumer<Toolbar> TRACKS = t -> t.addOption(
		"Canciones",
		e -> ViewStack.current().replace(new GenericTableView<>("Canciones", TrackEntity.getTableInspector().openViewOnRowClick(TrackView::new)))
	);

	public static final Consumer<Toolbar> PLAYLISTS = t -> t.addOption(
		"Listas",
		e -> ViewStack.current().replace(new GenericTableView<>("Listas", PlaylistEntity.getTableInspector().openViewOnRowClick(PlaylistView::new)))
	);

	public static final Consumer<Toolbar> CUSTOMERS = t -> t.addOption(
		"Clientes",
		e -> ViewStack.current().replace(new GenericTableView<>("Clientes", CustomerEntity.getTableInspector().openViewOnRowClick(CustomerView::new)))
	);

	public static final Consumer<Toolbar> EMPLOYEES = t -> t.addOption(
		"Empleados",
		e -> ViewStack.current().replace(new GenericTableView<>("Empleados", EmployeeEntity.getTableInspector().openViewOnRowClick(EmployeeView::new)))
	);

	public static final Consumer<Toolbar> INVOICES = t -> t.addOption(
		"Facturas",
		e -> ViewStack.current().replace(new GenericTableView<>("Facturas", InvoiceEntity.getTableInspector().openViewOnRowClick(InvoiceView::new)))
	);

	public static final Consumer<Toolbar> ALBUMS = t -> t.addOption(
		"Albums",
		e -> ViewStack.current().replace(new GenericTableView<>("Álbumes", AlbumEntity.getTableInspector().openViewOnRowClick(AlbumView::new)))
	);

	public static final Consumer<Toolbar> ARTISTS = t -> t.addOption(
		"Artistas",
		e -> ViewStack.current().replace(new GenericTableView<>("Artistas", ArtistEntity.getTableInspector().openViewOnRowClick(ArtistView::new)))
	);


	public static final Consumer<Toolbar> REPORTS = t -> t.addOption(
		"Reportes",
		e -> ViewStack.current().replace(new ReportsView())
	);

	public static final Consumer<Toolbar> DEBUG = t -> {
		addTools(t, WELCOME, TRACKS, PLAYLISTS, ALBUMS, ARTISTS, CUSTOMERS, EMPLOYEES, INVOICES, REPORTS);
		t.addOption("Debug", e -> ViewStack.current().replace(new TestView()));
	};


	public static void initializeToolbarForUser(@NotNull User user, @NotNull Toolbar toolbar) {
		toolbar.removeAll();
		addTools(toolbar, WELCOME, TRACKS, PLAYLISTS, ALBUMS);

		if (user.hasPermission(Role.MANAGE_CUSTOMERS))
			addTools(toolbar, CUSTOMERS);

		if (user.hasPermission(Role.MANAGE_EMPLOYEES))
			addTools(toolbar, EMPLOYEES);

		if (user.hasPermission(Role.MANAGE_INVENTORY))
			addTools(toolbar, INVOICES);

		if (user.hasPermission(Role.SEE_REPORTS))
			addTools(toolbar, REPORTS);

		toolbar.toggleOption("Inicio", true);
	}

	@SafeVarargs
	private static void addTools(@NotNull Toolbar toolbar, @NotNull Consumer<Toolbar>... tools) {
        for (Consumer<Toolbar> tool : tools) {
            tool.accept(toolbar);
        }
	}
}