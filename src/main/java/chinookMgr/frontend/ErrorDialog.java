package chinookMgr.frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorDialog extends JDialog {
	private JPanel contentPane;
	private JButton buttonOK;
	private JLabel icon;
	private JTextPane txtErrorDescription;
	private JTextPane txtTrace;
	private JScrollPane tracePanel;
	private JLabel lblShowTrace;

	private ErrorDialog(String message, Throwable throwable) {
		this.setContentPane(contentPane);
		this.setModal(true);
		this.getRootPane().setDefaultButton(buttonOK);
		this.setMinimumSize(new Dimension(300, 150));
		this.setPreferredSize(new Dimension(300, 150));
		this.txtErrorDescription.setText(message);

		this.lblShowTrace.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				showTracePanel();
			}
		});

		var strWriter = new StringWriter();
		var stream = new PrintWriter(strWriter);
		throwable.printStackTrace(stream);
		this.txtTrace.setText(strWriter.toString());

		this.icon.setIcon(UIManager.getIcon("OptionPane.errorIcon"));

		buttonOK.addActionListener(e -> onOK());
	}

	private void onOK() {
		// add your code here
		dispose();
	}

	private void showTracePanel() {
		this.tracePanel.setVisible(true);
		this.lblShowTrace.setVisible(false);
		this.setMinimumSize(new Dimension(500, 450));
		this.tracePanel.getVerticalScrollBar().setValue(0);
		this.pack();
	}

	public static void display(Component parent, String message, Throwable throwable) {
		var dialog = new ErrorDialog(message, throwable);
		dialog.pack();
		dialog.setLocationRelativeTo(parent);
		dialog.setVisible(true);
	}
}