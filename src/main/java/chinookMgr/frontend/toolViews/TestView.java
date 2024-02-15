package chinookMgr.frontend.toolViews;

import chinookMgr.frontend.StatusManager;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.ViewStack;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class TestView extends ToolView {
	private JPanel mainContainer;
	private JButton openSubButton;
	private JLabel txt;
	private final int current;

	public TestView(int current) {
		this.current = current;
		this.build();
	}

	public TestView() {
		this(0);
	}

	@Override
	protected void build() {
		this.openSubButton.addActionListener(e -> {
			ViewStack.current().push(new TestView(current + 1));
			StatusManager.showUpdate("Opened subview " + (current + 1) + "!");
		});
		this.txt.setText("Current: " + current);
	}


	@Override
	public @NotNull JPanel getPanel() {
		return this.mainContainer;
	}

	@Override
	public @NotNull String getName() {
		return "Test" + this.current;
	}
}