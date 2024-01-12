package chinookMgr.frontend.components;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

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

	public void deactivateAll() {
		for (var button : this.getComponents())
			if (button instanceof ToolButton btn)
				btn.setActive(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

	public class ToolButton extends JButton {
		private boolean isActive = false;

		public ToolButton(String name, ActionListener listener) {
			super(name);
			this.addActionListener(e -> {
				Toolbar.this.deactivateAll();
				this.setActive(true);
				listener.actionPerformed(e);
			});
		}

		public void setActive(boolean active) {
			this.isActive = active;
			this.repaint();
		}

		public boolean isActive() {
			return this.isActive;
		}

		@Override
		protected void paintComponent(Graphics g) {
			{
				if (this.getModel().isPressed())
					g.setColor(new Color(255, 255, 255, 25));
				else if (this.isActive())
					g.setColor(new Color(255, 255, 255, 50));
				else if (this.getModel().isRollover() || this.isActive())
					g.setColor(new Color(0, 0, 0, 50));
				else
					g.setColor(new Color(0, 0, 0, 0));

				g.fillRect(0, 0, this.getWidth(), this.getHeight());
			}

			g.setColor(Color.WHITE);
			g.setFont(g.getFont().deriveFont(Font.BOLD, 15f));

			g.drawString(this.getText(), 10, this.getHeight() / 2 + g.getFontMetrics().getHeight() / 2 - 2);
		}
	}
}