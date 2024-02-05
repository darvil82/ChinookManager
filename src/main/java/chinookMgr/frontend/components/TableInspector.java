package chinookMgr.frontend.components;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.frontend.ListTableModel;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.View;
import chinookMgr.frontend.ViewStack;
import chinookMgr.shared.Querier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSpinnerUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class TableInspector<T> extends View {
	private JTextField inputSearch;
	private JButton btnNew;
	private JTable resultTable;
	private JSpinner numPage;
	private JButton btnForward;
	private JButton btnBack;
	private JLabel lblPagesTotal;
	private JPanel mainPanel;
	private JLabel txtResultCount;
	private JScrollPane tableScrollPane;
	public static final int PAGE_SIZE = 100;

	private final Querier<T> querier;
	private final Querier<Long> counter;
	private Consumer<T> onRowClick;
	private final ListTableModel<T> tableModel;

	private int pageCount = 0;
	private int currentPage = 0;


	public TableInspector(
		@NotNull Querier<T> querier,
		@NotNull Querier<Long> counter,
		@NotNull ListTableModel<T> tableModel
	) {
		this.querier = querier;
		this.counter = counter;
		this.tableModel = tableModel;
		this.build();
		this.updateData();
	}

	public TableInspector(
		@NotNull Querier<T> querier,
		@NotNull Querier<Long> counter
	) {
		this(querier, counter, new ListTableModel<>());
	}


	private void build() {
		SwingUtilities.invokeLater(() -> this.inputSearch.requestFocus());
		this.resultTable.setModel(this.tableModel);

		this.resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.resultTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (TableInspector.this.onRowClick == null) return;
				var pos = TableInspector.this.resultTable.rowAtPoint(e.getPoint());
				TableInspector.this.onRowClick.accept(TableInspector.this.getTableModel().getItemAt(pos));
			}
		});
		this.btnForward.addActionListener(e -> this.setPage(this.currentPage + 1));
		this.btnBack.addActionListener(e -> this.setPage(this.currentPage - 1));
		this.numPage.addChangeListener(e -> this.setPage((int)this.numPage.getValue() - 1));
		this.numPage.setUI(new BasicSpinnerUI() {
			protected Component createNextButton() {
				return null;
			}

			protected Component createPreviousButton() {
				return null;
			}
		});

		// set bottom border
		this.numPage.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 0, 0, 0.4f)));

		this.inputSearch.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				TableInspector.this.updateData();
			}
		});
	}


	@SuppressWarnings("unchecked")
	private ListTableModel<T> getTableModel() {
		return (ListTableModel<T>)this.resultTable.getModel();
	}

	private void updateData() {
		this.getTableModel().clear();
		var search = this.inputSearch.getText();

		HibernateUtil.withSession(session -> {
			long valueCount = this.counter.apply(session, search)
				.uniqueResult();

			this.setMaxPage((int)Math.ceil((double)valueCount / PAGE_SIZE));

			if (valueCount == 0) {
				this.clearData();
				return;
			}

			this.setPage(0);
			this.txtResultCount.setText(valueCount + " resultado/s");
		});
	}

	private void clearData() {
		this.getTableModel().clear();
		this.btnForward.setEnabled(false);
		this.txtResultCount.setText("0 resultados");
	}

	private void setPage(int page) {
		var search = this.inputSearch.getText();

		HibernateUtil.withSession(session -> {
			this.getTableModel().clear();

			this.querier.apply(session, search)
				.setMaxResults(PAGE_SIZE)
				.setFirstResult(page * PAGE_SIZE)
				.stream()
				.forEach(r -> this.getTableModel().addItem(r));
		});

		this.numPage.setValue(page + 1);
		this.currentPage = page;
		this.tableScrollPane.getVerticalScrollBar().setValue(0);

		this.btnBack.setEnabled(page > 0);
		this.btnForward.setEnabled(page < this.pageCount - 1);
	}

	private void setMaxPage(int maxPage) {
		maxPage = Math.max(maxPage, 1);
		this.numPage.setModel(new SpinnerNumberModel(1, 1, maxPage, 1));
		this.lblPagesTotal.setText("/ " + maxPage);
		this.pageCount = maxPage;
	}

	public TableInspector<T> onRowClick(@NotNull Consumer<T> onRowClick) {
		this.onRowClick = onRowClick;
		return this;
	}

	public TableInspector<T> submitValueOnRowClick() {
		this.onRowClick = ViewStack.current()::popSubmit;
		return this;
	}

	public TableInspector<T> openViewOnRowClick(@NotNull Function<T, ToolView> ctor) {
		this.onRowClick = item -> ViewStack.current().push(ctor.apply(item));
		return this;
	}

	public TableInspector<T> onNewButtonClick(@NotNull Runnable onClick) {
		this.btnNew.setVisible(true);
		this.btnNew.addActionListener(e -> onClick.run());
		return this;
	}

	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}

	@Override
	protected void onReMount(@Nullable ToolView prevView) {
		this.inputSearch.requestFocus();
		int prevScroll = this.tableScrollPane.getVerticalScrollBar().getValue();
		int tableSelection = this.resultTable.getSelectedRow();

		this.setPage(this.currentPage); // refetch data in case it changed
		this.tableScrollPane.getVerticalScrollBar().setValue(prevScroll); // restore scroll

		if (tableSelection == -1) return;

		// restore selection
		tableSelection = Math.min(tableSelection, this.resultTable.getRowCount() - 1);
		this.resultTable.setRowSelectionInterval(tableSelection, tableSelection);
	}
}