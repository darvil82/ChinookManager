package chinookMgr.frontend.toolViews.user;

import chinookMgr.backend.Role;
import chinookMgr.backend.User;
import chinookMgr.backend.UserManager;
import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.EmployeeEntity;
import chinookMgr.frontend.StatusManager;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.ViewStack;
import chinookMgr.shared.Utils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;

public class UserView<T extends User> extends ToolView {
	private JTextField txtName;
	private JTextField txtCountry;
	private JTextField txtSurname;
	private JTextField txtState;
	private JTextField txtCity;
	private JTextField txtPostal;
	private JTextField txtAddress;
	private JTextField txtPhone;
	private JTextField txtEmail;
	private JTextField txtFax;
	private JPanel mainPanel;
	private JList<Role> listRoles;
	private JButton btnChangePassword;

	private final T loadUser;
	private Byte[] newPassword;


	public UserView(T user) {
		this.loadUser = user;
		this.buildForEntity();
	}

	public UserView() {
		this.loadUser = null;
		this.buildForNew();
	}

	@Override
	protected void build() {
		super.build();
		this.listRoles.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		this.getValidator().register(this.txtName, e -> !e.getText().isBlank(), "El nombre no puede estar vacío");
		this.getValidator().register(this.txtSurname, e -> !e.getText().isBlank(), "El apellido no puede estar vacío");
		this.getValidator().register(this.txtEmail, e -> this.isEmailValid(e.getText()), "El email no es válido");
	}

	private boolean isEmailValid(String email) {
		return (this.loadUser != null || UserManager.isEmailAvailable(email)) && email.matches(".+@.+\\..+");
	}

	@Override
	protected void buildForNew() {
		super.buildForNew();
	}

	@Override
	protected void buildForEntity() {
		super.buildForEntity();
		this.txtName.setText(this.loadUser.getFirstName());
		this.txtSurname.setText(this.loadUser.getLastName());
		this.txtCountry.setText(this.loadUser.getCountry());
		this.txtState.setText(this.loadUser.getState());
		this.txtCity.setText(this.loadUser.getCity());
		this.txtPostal.setText(this.loadUser.getPostalCode());
		this.txtAddress.setText(this.loadUser.getAddress());
		this.txtPhone.setText(this.loadUser.getPhone());
		this.txtEmail.setText(this.loadUser.getEmail());
		this.txtFax.setText(this.loadUser.getFax());
		this.setRoles(this.loadUser.getRoles());

		this.getInputManager().register(() -> UserManager.getCurrentUser().hasPermission(Role.ADMIN) || UserManager.isCurrentUser(this.loadUser), this.btnChangePassword);
		this.btnChangePassword.setVisible(true);
		this.btnChangePassword.addActionListener(e -> {
			ViewStack.current().pushAwait(new ChangePasswordView(), newPassword -> {
				this.newPassword = newPassword;
			});
		});
	}


	@Override
	public @NotNull String getName() {
		return "Usuario (" + this.txtName.getText() + " " + this.txtSurname.getText() + ")";
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}

	public @NotNull String getFullUsername() {
		return this.txtName.getText() + " " + this.txtSurname.getText();
	}

	private void createUIComponents() {
		this.listRoles = new JList<>(Role.values());
	}

	public void setRolesEditable(boolean editable) {
		this.listRoles.setEnabled(editable);
	}

	public void setRoles(@NotNull List<Role> roles) {
		this.listRoles.setSelectedIndices(
			roles.stream()
				.mapToInt(Role::ordinal)
				.toArray()
		);
	}

	public void saveCurrent() {
		this.save(this.loadUser);

		HibernateUtil.withSession(s -> {
			s.merge(this.loadUser);
		});
	}

	public void save(@NotNull T user) {
		user.setFirstName(this.txtName.getText());
		user.setLastName(this.txtSurname.getText());
		user.setCountry(this.txtCountry.getText());
		user.setState(this.txtState.getText());
		user.setCity(this.txtCity.getText());
		user.setPostalCode(this.txtPostal.getText());
		user.setAddress(this.txtAddress.getText());
		user.setPhone(this.txtPhone.getText());
		user.setEmail(this.txtEmail.getText());
		user.setFax(this.txtFax.getText());

		if (this.newPassword != null) {
			byte[] primitivePassword = new byte[newPassword.length];
			for (int i = 0; i < newPassword.length; i++)
				primitivePassword[i] = newPassword[i];
			user.setPassword(Utils.toMD5(primitivePassword));
		}

		if (!(user instanceof EmployeeEntity))
			user.setRoles(this.listRoles.getSelectedValuesList());
	}
}