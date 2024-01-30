package chinookMgr.frontend;

import chinookMgr.backend.Role;
import chinookMgr.backend.User;
import chinookMgr.backend.entityHelpers.Track;
import chinookMgr.frontend.components.Toolbar;
import chinookMgr.frontend.toolViews.GenericTableView;
import chinookMgr.frontend.toolViews.TrackView;
import chinookMgr.frontend.toolViews.WelcomeView;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionListener;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class UserToolbars {
	public static final Consumer<Toolbar> TRACKS = t -> t.addOption(
		"Canciones",
		e -> ViewStack.replace(new GenericTableView<>("Canciones", Track.getTableInspector().openViewOnRowClick(TrackView::new)))
	);

	public static final Consumer<Toolbar> CUSTOMERS = t -> t.addOption("Clientes", e -> ViewStack.replace(new TrackView()));

	public static final Consumer<Toolbar> EMPLOYEES = t -> t.addOption("Empleados", e -> ViewStack.replace(new TrackView()));

	public static final Consumer<Toolbar> INVOICES = t -> t.addOption("Facturas", e -> ViewStack.replace(new TrackView()));


	public static void initializeToolbarForUser(@NotNull User<?> user, @NotNull Toolbar toolbar) {
		toolbar.removeAll();
		toolbar.addOption("Inicio", e -> ViewStack.replace(new WelcomeView()));

		if (user.hasPermission(Role.MANAGE_CUSTOMERS))
			setTools(toolbar, CUSTOMERS);

		if (user.hasPermission(Role.MANAGE_EMPLOYEES))
			setTools(toolbar, EMPLOYEES);

		if (user.hasPermission(Role.MANAGE_INVENTORY))
			setTools(toolbar, TRACKS, INVOICES);
	}

	@SafeVarargs
	private static void setTools(@NotNull Toolbar toolbar, @NotNull Consumer<Toolbar>... tools) {
        for (Consumer<Toolbar> tool : tools) {
            tool.accept(toolbar);
        }
	}
}