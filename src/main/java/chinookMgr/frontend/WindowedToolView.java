package chinookMgr.frontend;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class WindowedToolView extends JDialog {
	private final ViewStack viewStack;
	private JPanel mainPanel;
	private JPanel toolPanel;
	private JScrollPane pathScrollPane;
	private JLabel txtCurrentViewName;
	private JLabel txtAbsViewPath;
	private JButton btnPrev;

	private WindowedToolView(@Nullable JFrame parent, @NotNull ToolView view) {
		super(parent, true);
		this.viewStack = ViewStack.pushViewStack();
		this.viewStack.onViewChange = this::onViewStackChange;
		this.viewStack.push(view);
		this.btnPrev.addActionListener(e -> this.viewStack.pop());
		this.setContentPane(this.mainPanel);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setMinimumSize(new Dimension(450, 400));
		this.pack();
		this.setLocationRelativeTo(parent);
	}

	private void onViewStackChange(@Nullable ToolView newView) {
		if (newView == null) {
			this.dispose();
			return;
		}

		this.toolPanel.removeAll();
		this.toolPanel.add(newView.getPanel());
		this.setTitle(newView.getName());
		this.txtCurrentViewName.setText(newView.getName());
		this.txtAbsViewPath.setText(this.viewStack.getAbsPath());
		this.btnPrev.setEnabled(newView.enableBackButton());
		SwingUtilities.invokeLater(() -> {
			this.pathScrollPane.getHorizontalScrollBar().setValue(this.pathScrollPane.getHorizontalScrollBar().getMaximum());
		});
		this.toolPanel.revalidate();
		this.toolPanel.repaint();
		this.pack();
	}

	@Override
	public void dispose() {
		super.dispose();
		ViewStack.popViewStack();
	}

	public static void display(@Nullable JFrame parent, @NotNull ToolView view) {
		new WindowedToolView(parent, view).setVisible(true);
	}
}