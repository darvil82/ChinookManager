package chinookMgr.frontend.components;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.frontend.ListTableModel;
import chinookMgr.frontend.View;
import org.hibernate.query.Query;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

public class TableInspector<T> implements View {
	private JTextField inputSearch;
	private JButton btnNew;
	private JTable resultTable;
	private JSpinner numPage;
	private JButton btnForward;
	private JButton btnBack;
	private JLabel lblPagesTotal;
	private JPanel mainPanel;
	private JLabel txtResultCount;
	public static final int PAGE_SIZE = 100;

	private Querier query;
	private Class<T> entClass;
	private int pageCount = 0;
	private int currentPage = 0;

	@FunctionalInterface
	public interface Querier {
		@NotNull Query where(@NotNull Function<String, Query> w, @Nullable String input);
	}


	public TableInspector(Class<T> clazz, @NotNull Querier query) {
		this.query = query;
		this.entClass = clazz;
		this.build();
		this.updateData();
	}

	private void build() {
		this.resultTable.setModel(new ListTableModel<>(new ArrayList<>(), List.of("Item")));
		this.resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.btnForward.addActionListener(e -> this.setPage(++this.currentPage));
		this.btnBack.addActionListener(e -> this.setPage(--this.currentPage));
		this.numPage.addChangeListener(e -> this.setPage((int)this.numPage.getValue() - 1));
		this.btnForward.setEnabled(true);
		this.btnBack.setEnabled(true);

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
		long valueCount;

		try (var session = HibernateUtil.getSession()) {
			valueCount = (long)this.query.where(
				q -> session.createQuery("select count(*) from " + this.entClass.getSimpleName() + " where " + q, Long.class),
				this.inputSearch.getText()
			).getSingleResult();

			this.setMaxPage((int)Math.ceil((double)valueCount / PAGE_SIZE));

			if (valueCount == 0) {
				this.clearData();
				return;
			}

			this.setPage(0);
		}

		this.resultTable.revalidate();
		this.txtResultCount.setText(valueCount + " resultado/s");
	}

	private void clearData() {
		this.getTableModel().clear();
		this.txtResultCount.setText("0 resultados");
		this.resultTable.revalidate();
		this.resultTable.repaint();
	}

	private void setPage(int page) {
		try (var session = HibernateUtil.getSession()) {
			this.getTableModel().clear();

			this.query.where(
				q -> session.createQuery("from " + this.entClass.getSimpleName() + " where " + q, this.entClass),
				this.inputSearch.getText()
			)
				.setMaxResults(PAGE_SIZE)
				.setFirstResult(page * PAGE_SIZE)
				.stream()
				.forEach(r -> this.getTableModel().addItem((T)r));
		}

		this.resultTable.revalidate();
		this.resultTable.repaint();
		this.numPage.setValue(page + 1);
		this.currentPage = page;

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

}