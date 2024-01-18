package chinookMgr.frontend.components;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.frontend.ListTableModel;
import chinookMgr.frontend.View;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicSpinnerUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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

	private final String querier;
	private final String counter;
	private Class<T> entClass;
	private Consumer<T> onRowClick;

	private int pageCount = 0;
	private int currentPage = 0;


	private TableInspector(
		Class<T> clazz,
		@NotNull String querier,
		@NotNull String counter
	) {
		this.querier = querier;
		this.counter = counter;
		this.entClass = clazz;
		this.build();
		this.updateData();
	}

	public static <T> Builder<T> create(Class<T> clazz) {
		return new Builder(clazz);
	}

	private void build() {
		this.resultTable.setModel(new ListTableModel<>(new ArrayList<>(), List.of("Item")));
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

		this.inputSearch.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				TableInspector.this.updateData();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				TableInspector.this.updateData();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				TableInspector.this.updateData();
			}
		});
	}

	private ListTableModel<T> getTableModel() {
		return (ListTableModel<T>)this.resultTable.getModel();
	}

	private void updateData() {
		this.getTableModel().clear();

		try (var session = HibernateUtil.getSession()) {
			long valueCount = session.createQuery(this.counter, Long.class)
				.setParameter("search", "%" + this.inputSearch.getText() + "%")
				.uniqueResult();

			this.setMaxPage((int)Math.ceil((double)valueCount / PAGE_SIZE));

			if (valueCount == 0) {
				this.clearData();
				return;
			}

			this.setPage(0);
			this.txtResultCount.setText(valueCount + " resultado/s");
		}
	}

	private void clearData() {
		this.getTableModel().clear();
		this.btnForward.setEnabled(false);
		this.txtResultCount.setText("0 resultados");
	}

	private void setPage(int page) {
		try (var session = HibernateUtil.getSession()) {
			this.getTableModel().clear();

			session.createQuery(this.querier, this.entClass)
				.setParameter("search", "%" + this.inputSearch.getText() + "%")
				.setMaxResults(PAGE_SIZE)
				.setFirstResult(page * PAGE_SIZE)
				.stream()
				.forEach(r -> this.getTableModel().addItem(r));
		}

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

	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}

	public static class Builder<T> implements chinookMgr.shared.Builder<TableInspector<T>> {
		private Class<T> entClass;
		private String querier;
		private String counter;
		private Consumer<T> onRowClick;

		private Builder(Class<T> clazz) {
			this.entClass = clazz;
		}

		public Builder<T> withQuerier(@NotNull String querier) {
			this.querier = querier;
			return this;
		}

		public Builder<T> withCounter(@NotNull String counter) {
			this.counter = counter;
			return this;
		}

		public Builder<T> withRowClick(@NotNull Consumer<T> onRowClick) {
			this.onRowClick = onRowClick;
			return this;
		}

		@Override
		public TableInspector<T> build() {
			var ti = new TableInspector<>(this.entClass, this.querier, this.counter);
			ti.onRowClick = this.onRowClick;
			return ti;
		}
	}
}