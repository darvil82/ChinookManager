package chinookMgr.frontend.components;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.stream.Stream;

public class Toolbar extends JComponent {
	public Toolbar() {
		var layout = new GridLayout(0, 1);
		this.setLayout(layout);
		this.setMinimumSize(new Dimension(100, 0));
	}

	public void addOption(@NotNull String name, @NotNull ActionListener listener) {
		var button = new ToolButton(name, listener);
		this.add(button);
		this.revalidate();
	}

	private Stream<ToolButton> getButtons() {
		return Stream.of(this.getComponents())
			.filter(c -> c instanceof ToolButton)
			.map(c -> (ToolButton)c);
	}

	public void toggleOption(@NotNull String name, boolean active) {
		this.getButtons()
			.filter(b -> b.getText().equals(name))
			.findFirst()
			.ifPresent(b -> b.setActive(active));
	}

	public void toggleOption(@NotNull String name) {
		this.getButtons()
			.filter(b -> b.getText().equals(name))
			.findFirst()
			.ifPresent(b -> b.setActive(!b.isActive()));
	}

	public void deactivateAll() {
		this.getButtons()
			.forEach(b -> b.setActive(false));
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

	public class ToolButton extends JButton {
		private boolean isActive = false;
		private final Color originalBackground = this.getBackground();

		public ToolButton(String name, ActionListener listener) {
			super(name);
			this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			this.setFont(this.getFont().deriveFont(16f).deriveFont(Font.BOLD));
			this.addActionListener(e -> {
				Toolbar.this.deactivateAll();
				this.setActive(true);
				listener.actionPerformed(e);
			});
		}

		public void setActive(boolean active) {
			this.isActive = active;
			this.setBackground(active ? new Color(0, 0, 0.05f, 0.4f) : this.originalBackground);
			this.repaint();
		}

		public boolean isActive() {
			return this.isActive;
		}
	}
}