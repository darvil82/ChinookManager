package chinookMgr.frontend;


import chinookMgr.frontend.components.TestTool;
import chinookMgr.frontend.components.Toolbar;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {
	private JPanel mainPanel;
	private JPanel toolbarContainer;
	private JPanel toolContent;
	private JLabel txtCurrentToolName;
	private JButton btnPrev;
	private JLabel txtAbsToolPath;
	private Toolbar toolbar;

	public MainMenu() {
		super("Chinook Manager");
		this.setContentPane(this.mainPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(800, 500));
		this.pack();
		this.build();
	}

	public void build() {
		this.toolbar = new Toolbar();
		this.toolbarContainer.add(this.toolbar, BorderLayout.WEST);

		this.toolbar.addOption("test", e -> {
			this.toolContent.removeAll();
			this.toolContent.add(new TestTool().getPanel());
			this.toolContent.revalidate();
		});
		this.toolbar.addOption("test", e -> {
			this.toolContent.removeAll();
			this.toolContent.add(new TestTool().getPanel());
			this.toolContent.revalidate();
		});
		this.toolbar.addOption("test", e -> {
			this.toolContent.removeAll();
			this.toolContent.add(new TestTool().getPanel());
			this.toolContent.revalidate();
		});
		this.toolbar.addOption("test", e -> {
			this.toolContent.removeAll();
			this.toolContent.add(new TestTool().getPanel());
			this.toolContent.revalidate();
		});

	}
}