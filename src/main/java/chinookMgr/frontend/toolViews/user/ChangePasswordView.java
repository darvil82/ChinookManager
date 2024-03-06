package chinookMgr.frontend.toolViews.user;

import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.ViewStack;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Arrays;

public class ChangePasswordView extends ToolView.Submitter<Byte[]> {
	private JPanel mainPanel;
	private JPasswordField inputNew;
	private JPasswordField inputNew2;
	private JButton btnOk;


	public ChangePasswordView() {
		this.build();
	}

	@Override
	protected void build() {
		super.build();

		this.btnOk.addActionListener(e -> this.onOk());
		this.getValidator().register(this.inputNew, p -> p.getPassword().length > 7, "La contraseña debe tener al menos 8 caracteres");
		this.getValidator().register(this.inputNew2, p -> Arrays.equals(p.getPassword(), this.inputNew.getPassword()), "Las contraseñas no coinciden");
	}

	private void onOk() {
		if (!this.getValidator().validate())
			return;

		var password = this.inputNew.getPassword();

		Byte[] newPwd = new Byte[password.length];
		for (int i = 0; i < password.length; i++)
			newPwd[i] = (byte)password[i];

		ViewStack.current().popSubmit(newPwd);
	}

	@Override
	public @NotNull String getName() {
		return "Cambiar contraseña";
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}
}