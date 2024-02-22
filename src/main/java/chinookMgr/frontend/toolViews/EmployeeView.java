package chinookMgr.frontend.toolViews;

import chinookMgr.backend.db.entities.EmployeeEntity;
import chinookMgr.frontend.ToolView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class EmployeeView extends ToolView {
	private JPanel mainPanel;
	private JPanel userViewPanel;
	private JButton btnTitle;
	private JPanel hireDatePanel;
	private JButton btnBoss;

	private final EmployeeEntity currentEmployee;
	private final UserView userView;

	public EmployeeView(EmployeeEntity employee) {
		this.currentEmployee = employee;
		this.userView = new UserView(employee);
		this.buildForEntity();
	}

	public EmployeeView() {
		this.currentEmployee = new EmployeeEntity();
		this.userView = new UserView();
		this.buildForEntity();
	}

	@Override
	protected void build() {
		super.build();

		this.insertView(this.userViewPanel, this.userView);
	}

	@Override
	protected void buildForEntity() {
		super.buildForEntity();

	}

	@Override
	public @NotNull String getName() {
		return "Empleado";
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}
}