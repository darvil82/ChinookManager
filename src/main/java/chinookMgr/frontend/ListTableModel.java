package chinookMgr.frontend;


import org.jetbrains.annotations.NotNull;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class ListTableModel<T> extends AbstractTableModel implements Iterable<T> {
	protected final List<T> items = new ArrayList<>();
	private final List<String> columnNames;
	private final BiFunction<T, Integer, String> cellRenderer;

	@FunctionalInterface
	public interface CellRenderer<T> {
		@NotNull String render(@NotNull T item, int column);
	}


	public ListTableModel() {
		this.columnNames = List.of("Elemento");
		this.cellRenderer = (item, column) -> item.toString();
	}

	public ListTableModel(List<String> columnNames, BiFunction<T, Integer, String> cellRenderer) {
		this.columnNames = columnNames;
		this.cellRenderer = cellRenderer;
	}

	@Override
	public int getRowCount() {
		return this.items.size();
	}

	@Override
	public int getColumnCount() {
		return this.columnNames.size();
	}

	@Override
	public String getColumnName(int column) {
		return this.columnNames.get(column);
	}

	public void addItem(T p) {
		this.items.add(p);
		this.fireTableRowsInserted(this.getRowCount() - 1, this.getRowCount() - 1);
	}

	public void removeItemAt(int index) {
		this.items.remove(index);
		this.fireTableRowsDeleted(index, index);
	}

	public T getItemAt(int index) {
		return this.items.get(index);
	}

	public void setItemAt(int index, T item) {
		this.items.set(index, item);
		this.fireTableRowsUpdated(index, index);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return this.cellRenderer.apply(this.getItemAt(rowIndex), columnIndex);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		this.setItemAt(rowIndex, (T)aValue);
	}

	public void clear() {
		if (this.items.isEmpty()) return;
		this.items.clear();
		this.fireTableDataChanged();
	}

	@Override
	public @NotNull Iterator<T> iterator() {
		return this.items.iterator();
	}

	public boolean isEmpty() {
		return this.items.isEmpty();
	}

	public Stream<T> stream() {
		return this.items.stream();
	}
}