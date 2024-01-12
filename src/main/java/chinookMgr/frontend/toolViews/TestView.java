package chinookMgr.frontend.toolViews;

import chinookMgr.frontend.StatusManager;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.View;
import chinookMgr.frontend.ViewStack;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class TestView implements ToolView {
	private JPanel mainContainer;
	private JButton openSubButton;
	private JLabel txt;
	private int current;

	public TestView(int current) {
		this.current = current;
		this.openSubButton.addActionListener(e -> {
			ViewStack.push(new TestView(current + 1));
			StatusManager.showUpdate("Opened subview " + (current + 1) + "!");
		});
		this.txt.setText("Current: " + current);
	}

	public TestView() {
		this(0);
	}

	@Override
	public JPanel getPanel() {
		return this.mainContainer;
	}

	@Override
	public @NotNull String getName() {
		return "Test" + this.current;
	}
}