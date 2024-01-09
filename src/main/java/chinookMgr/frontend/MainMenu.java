package chinookMgr.frontend;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {
	private JPanel mainPanel;

	public MainMenu() {
		super("Chinook Manager");
		this.setContentPane(this.mainPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(400, 300));
		this.pack();
	}

	public void build() {

	}
}