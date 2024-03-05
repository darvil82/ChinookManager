package chinookMgr.frontend.toolViews;

import chinookMgr.backend.Role;
import chinookMgr.backend.Saveable;
import chinookMgr.backend.User;
import chinookMgr.backend.UserManager;
import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.frontend.ToolView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class UserView extends ToolView implements Saveable {
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

	private User currentUser;


	public UserView(User user) {
		this.currentUser = user;
		this.buildForEntity();
	}

	public UserView() {
		this.currentUser = null;
		this.buildForNew();
	}

	@Override
	protected void build() {
		super.build();
		this.listRoles.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		this.getValidator().register(this.txtName, e -> !e.getText().isBlank(), "El nombre no puede estar vacío");
		this.getValidator().register(this.txtSurname, e -> !e.getText().isBlank(), "El apellido no puede estar vacío");
		this.getValidator().register(this.txtEmail, e -> e.getText().matches(".+@.+\\..+"), "El email no es válido");

		this.getInputManager().register(Role.MANAGE_ROLES, this.listRoles);
	}

	@Override
	protected void buildForEntity() {
		super.buildForEntity();
		this.txtName.setText(this.currentUser.getFirstName());
		this.txtSurname.setText(this.currentUser.getLastName());
		this.txtCountry.setText(this.currentUser.getCountry());
		this.txtState.setText(this.currentUser.getState());
		this.txtCity.setText(this.currentUser.getCity());
		this.txtPostal.setText(this.currentUser.getPostalCode());
		this.txtAddress.setText(this.currentUser.getAddress());
		this.txtPhone.setText(this.currentUser.getPhone());
		this.txtEmail.setText(this.currentUser.getEmail());
		this.txtFax.setText(this.currentUser.getFax());
		this.listRoles.setSelectedIndices(
			this.currentUser.getRoles().stream()
				.mapToInt(Role::ordinal)
				.toArray()
		);
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

	@Override
	public void save() {
		this.currentUser.setFirstName(this.txtName.getText());
		this.currentUser.setLastName(this.txtSurname.getText());
		this.currentUser.setCountry(this.txtCountry.getText());
		this.currentUser.setState(this.txtState.getText());
		this.currentUser.setCity(this.txtCity.getText());
		this.currentUser.setPostalCode(this.txtPostal.getText());
		this.currentUser.setAddress(this.txtAddress.getText());
		this.currentUser.setPhone(this.txtPhone.getText());
		this.currentUser.setEmail(this.txtEmail.getText());
		this.currentUser.setFax(this.txtFax.getText());
		this.currentUser.setRoles(this.listRoles.getSelectedValuesList());

		HibernateUtil.withSession(s -> {
			s.merge(this.currentUser);
		});
	}
}