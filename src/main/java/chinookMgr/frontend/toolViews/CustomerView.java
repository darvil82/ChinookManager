package chinookMgr.frontend.toolViews;

import chinookMgr.backend.db.entities.CustomerEntity;
import chinookMgr.backend.db.entities.EmployeeEntity;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.Utils;
import com.toedter.calendar.JDateChooser;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class CustomerView extends ToolView {
	private JPanel mainPanel;
	private JPanel userViewPanel;
	private JButton btnBoss;
	private JTextField txtCompany;

	private final CustomerEntity currentCustomer;
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

		this.insertView(this.userViewPanel, this.userView);
		Utils.attachViewSelectorToButton(this.btnBoss, () -> this.supportEmployee, "soporte", EmployeeEntity.getTableInspector(), e -> this.supportEmployee = e, EmployeeView::new);
		this.txtCompany.setText(this.currentCustomer.getCompany());
	}

	@Override
	protected void buildForEntity() {
		super.buildForEntity();
	}

	@Override
	public @NotNull String getName() {
		if (this.currentCustomer == null)
			return "Nuevo empleado";

		return "Empleado (" + this.userView.getFullUsername() + ")";
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}
}