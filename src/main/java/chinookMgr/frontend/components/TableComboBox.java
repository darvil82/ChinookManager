package chinookMgr.frontend.components;

import chinookMgr.backend.db.HibernateUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class TableComboBox<T> extends JComboBox<T> {
	private final @NotNull Class<T> entityClass;
	private final @NotNull Function<T, String> displayField;
	private @Nullable Consumer<T> onSelect;

	@SuppressWarnings("unchecked")
	public TableComboBox(
		@NotNull Class<T> entityClass,
		@NotNull Function<T, String> displayer
	) {
		super();
		this.entityClass = entityClass;
		this.displayField = displayer;
		this.setData();
		this.setRenderer(new DefaultListCellRenderer() {
			@Override
			@SuppressWarnings("unchecked")
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				var component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value != null) {
					var displayValue = TableComboBox.this.displayField.apply((T)value);
					((JLabel)component).setText(displayValue);
				}
				return component;
			}
		});
		this.addActionListener(e -> {
			if (this.onSelect == null)
				return;

			var item = (T)this.getSelectedItem();
			if (item != null)
				this.onSelect.accept(item);
		});
	}

	public TableComboBox<T> onSelect(@NotNull Consumer<T> onSelect) {
		this.onSelect = onSelect;
		return this;
	}

	@SuppressWarnings("unchecked")
	public T getSelectedEntity() {
		return (T)super.getSelectedItem();
	}

	public void setSelectedEntity(T item) {
		super.setSelectedItem(item);
	}

	private void setData() {
		this.removeAllItems();

		HibernateUtil.withSession(session -> {
			session.createQuery("from " + this.entityClass.getSimpleName(), this.entityClass)
				.getResultList()
				.forEach(this::addItem);
		});
	}

	public void refresh() {
		this.setData();
	}
}