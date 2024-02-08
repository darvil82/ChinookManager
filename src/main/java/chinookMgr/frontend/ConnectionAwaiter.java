package chinookMgr.frontend;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class ConnectionAwaiter extends JDialog {
	private JButton btnExit;
	private JPanel mainPanel;

	public ConnectionAwaiter(@NotNull JFrame parent) {
		super(parent, true);
		this.setTitle("Conexión perdida");
		this.setContentPane(this.mainPanel);
		this.setMinimumSize(new Dimension(250, 120));
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(parent);
		this.btnExit.addActionListener(e -> System.exit(0));
	}
}