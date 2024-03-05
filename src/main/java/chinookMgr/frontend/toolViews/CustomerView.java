package chinookMgr.frontend.toolViews;

import chinookMgr.backend.Role;
import chinookMgr.backend.Saveable;
import chinookMgr.backend.UserManager;
import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.CustomerEntity;
import chinookMgr.backend.db.entities.EmployeeEntity;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.Utils;
import chinookMgr.frontend.components.SaveOption;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class CustomerView extends ToolView implements Saveable {
	private JPanel mainPanel;
	private JPanel userViewPanel;
	private JButton btnBoss;
	private JTextField txtCompany;
	private JPanel savePanel;

	private CustomerEntity currentCustomer;
	private final UserView userView;

	private EmployeeEntity supportEmployee;


	public CustomerView(CustomerEntity customer) {
		this.currentCustomer = customer;
		this.userView = new UserView(customer);
		this.supportEmployee = this.currentCustomer.getSupportRepId() == null ? null : EmployeeEntity.getById(this.currentCustomer.getSupportRepId());
		this.buildForEntity();
	}

	public CustomerView() {
		this.currentCustomer = new CustomerEntity();
		this.userView = new UserView();
		this.buildForEntity();
	}

	@Override
	protected void build() {
		super.build();

		this.getValidator().register(this.userView.getValidator());
		this.getInputManager().register(this.userView.getInputManager());
		this.insertView(this.userViewPanel, this.userView);
		this.insertView(this.savePanel, new SaveOption<>(this, Role.MANAGE_CUSTOMERS));
		Utils.attachViewSelectorToButton(this.btnBoss, () -> this.supportEmployee, "soporte", EmployeeEntity.getTableInspector(), e -> this.supportEmployee = e, EmployeeView::new);
	}

	@Override
	protected void buildForEntity() {
		super.buildForEntity();
		this.txtCompany.setText(this.currentCustomer.getCompany());
	}

	@Override
	public @NotNull String getName() {
		if (this.currentCustomer == null)
			return "Nuevo cliente";

		return "Cliente (" + this.userView.getFullUsername() + ")";
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}

	@Override
	public void save() {
		this.userView.save();
		this.currentCustomer.setCompany(this.txtCompany.getText());
		this.currentCustomer.setSupportRepId(this.supportEmployee.getEmployeeId());

		HibernateUtil.withSession(s -> {
			s.merge(this.currentCustomer);
		});

		if (UserManager.isCurrentUser(this.currentCustomer))
			UserManager.fireUserEntityUpdate();
	}
}