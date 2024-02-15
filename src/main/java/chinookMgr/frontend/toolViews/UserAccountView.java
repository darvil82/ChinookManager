package chinookMgr.frontend.toolViews;

import chinookMgr.backend.User;
import chinookMgr.backend.UserManager;
import chinookMgr.frontend.ToolView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class UserAccountView extends ToolView {
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
	private JPanel rolesContainer;
	private JPanel mainPanel;

	private final User<?> currentUser;


	public UserAccountView(User<?> user) {
		this.currentUser = user;
		this.buildForEntity();
	}

	@Override
	protected void buildForEntity() {
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
	}

	@Override
	protected void build() {
	}

	@Override
	public @NotNull String getName() {
		return "Usuario (" + this.txtName.getText() + " " + this.txtSurname.getText() + ")";
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}
}