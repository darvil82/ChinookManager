package chinookMgr.frontend.toolViews.reports;

import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.toolViews.TrackView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

public class ReportsView extends ToolView {
	private JPanel mainPanel;
	private JPanel productsPanel;
	private JPanel clientsPanel;

	public ReportsView() {
		this.productsPanel.setLayout(new GridLayout(0, 1));
		this.clientsPanel.setLayout(new GridLayout(0, 1));
	}

	@Override
	public @NotNull String getName() {
		return "Reportes";
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}

	{
		this.addProductsButtons(
			new ReportButton("Canciones más/menos compradas", MostLeastBoughtTracksReportView::new)
//			new ReportButton("Canciones más/menos usadas en listas", TrackView::new),
//			new ReportButton("Artistas más/menos populares", TrackView::new)
		);

		this.addClientsButtons(
//			new ReportButton("Con mayor/menor facturación", TrackView::new)
		);
	}


	private record ReportButton(@NotNull String name, @NotNull Supplier<ToolView> view) { }

	private void addProductsButtons(@NotNull ReportButton... buttons) {
		for (var button : buttons)
			createButton(button, this.productsPanel);
	}

	private void addClientsButtons(@NotNull ReportButton... buttons) {
		for (var button : buttons)
			createButton(button, this.clientsPanel);
	}

	private JButton createButton(@NotNull ReportButton rButton, @NotNull JPanel panel) {
		var button = new JButton(rButton.name);
		button.setAlignmentX(JButton.LEFT_ALIGNMENT);
		button.addActionListener(e -> ViewStack.current().push(rButton.view.get()));
		panel.add(button);
		return button;
	}
}