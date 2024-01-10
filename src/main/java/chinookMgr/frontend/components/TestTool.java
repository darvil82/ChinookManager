package chinookMgr.frontend.components;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class TestTool extends Tool {
	private JPanel mainContainer;

	public TestTool() {

	}

	@Override
	public JPanel getPanel() {
		return this.mainContainer;
	}

	@Override
	public @NotNull String getName() {
		return "Test";
	}
}