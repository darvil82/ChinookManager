package chinookMgr.frontend.toolViews.user;

import chinookMgr.backend.UserManager;
import chinookMgr.frontend.LoadingManager;
import chinookMgr.frontend.StatusManager;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.ViewStack;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Arrays;

public class RegisterView extends ToolView {
	private JTextField inputEmail;
	private JPasswordField inputPassword;
	private JButton btnRegister;
	private JPanel mainPanel;
	private JPasswordField inputPassword2;
	private JTextField inputFirstName;
	private JTextField inputLastName;

	public RegisterView() {
		this.build();
	}

	@Override
	protected void build() {
		super.build();

		this.btnRegister.addActionListener(e -> this.register());

		this.getValidator().register(this.inputFirstName, c -> !c.getText().isBlank(), "El nombre no puede estar vacío");
		this.getValidator().register(this.inputLastName, c -> !c.getText().isBlank(), "El apellido no puede estar vacío");
		this.getValidator().register(this.inputEmail, c -> c.getText().matches(".+@.+\\..+") && UserManager.isEmailAvailable(c.getText()), "El email no es válido");
		this.getValidator().register(this.inputPassword, c -> c.getPassword().length > 7, "La contraseña debe tener al menos 8 caracteres");
		this.getValidator().register(this.inputPassword2, c -> Arrays.equals(c.getPassword(), this.inputPassword.getPassword()), "Las contraseñas no coinciden");
	}

	private void register() {
		if (!this.getValidator().validate()) return;

		StatusManager.disableBackButton();
		LoadingManager.pushPop("Registrando usuario...", () -> {
			boolean result = UserManager.registerCustomer(
				this.inputEmail.getText(), new String(this.inputPassword.getPassword()), this.inputFirstName.getText(), this.inputLastName.getText(), true
			);

			StatusManager.enableBackButton();

			if (!result) {
				JOptionPane.showMessageDialog(
					this.mainPanel,
					"Hubo un error al registrar el usuario.",
					"Error",
					JOptionPane.ERROR_MESSAGE
				);
				return;
			}

			ViewStack.current().pop();
			StatusManager.showUpdate("Usuario registrado con éxito.");
		});
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