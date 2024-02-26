package chinookMgr.frontend.toolViews;

import chinookMgr.frontend.ToolView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Arrays;

public class RegisterView extends ToolView {
	private JTextField inputEmail;
	private JPasswordField inputPassword;
	private JButton btnRegister;
	private JPanel mainPanel;
	private JPasswordField inputPassword2;

	public RegisterView() {
		this.build();
	}

	@Override
	protected void build() {
		super.build();

		this.btnRegister.addActionListener(e -> this.register());
		this.getValidator().register(this.inputEmail, c -> c.getText().matches(".+@.+\\..+"), "El email no es válido");
		this.getValidator().register(this.inputPassword, c -> c.getPassword().length > 7, "La contraseña debe tener al menos 8 caracteres");
		this.getValidator().register(this.inputPassword2, c -> Arrays.equals(c.getPassword(), this.inputPassword.getPassword()), "Las contraseñas no coinciden");
	}

	private void register() {
		if (!this.getValidator().validate()) return;


	}

	@Override
	public @NotNull String getName() {
		return "Registrarse";
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}
}