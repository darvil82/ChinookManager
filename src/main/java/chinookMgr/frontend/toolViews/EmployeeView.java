package chinookMgr.frontend.toolViews;

import chinookMgr.backend.Saveable;
import chinookMgr.backend.db.entities.EmployeeEntity;
import chinookMgr.backend.db.entities.TitleEntity;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.Utils;
import chinookMgr.frontend.components.SaveOption;
import chinookMgr.frontend.components.TableComboBox;
import com.toedter.calendar.JDateChooser;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class EmployeeView extends ToolView implements Saveable {
	private JPanel mainPanel;
	private JPanel userViewPanel;
	private JPanel hireDatePanel;
	private JButton btnBoss;
	private JPanel titleContainer;
	private JPanel birthDatePanel;
	private JPanel savePanel;

	private EmployeeEntity currentEmployee;
	private final UserView userView;

	private EmployeeEntity selectedBoss;
	private JDateChooser hireDateChooser;
	private JDateChooser birthDateChooser;
	private TableComboBox<TitleEntity> titleCombo;


	public EmployeeView(EmployeeEntity employee) {
		this.currentEmployee = employee;
		this.userView = new UserView(employee);
		this.selectedBoss = this.currentEmployee.getReportsTo() == null ? null : EmployeeEntity.getById(this.currentEmployee.getReportsTo());
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

		this.getValidator().register(this.userView.getValidator());
		this.insertView(this.savePanel, new SaveOption<>(this));
		this.insertView(this.userViewPanel, this.userView);
		this.titleContainer.add(this.titleCombo = new TableComboBox<>(TitleEntity.class, TitleEntity::getName));
		this.hireDatePanel.add(this.hireDateChooser = new JDateChooser());
		this.birthDatePanel.add(this.birthDateChooser = new JDateChooser());
		Utils.attachViewSelectorToButton(this.btnBoss, () -> this.selectedBoss, "superior", EmployeeEntity.getTableInspector(), e -> this.selectedBoss = e, EmployeeView::new);

		this.getValidator().register(this.btnBoss, e -> this.selectedBoss.getEmployeeId() != this.currentEmployee.getEmployeeId(), "El empleado no puede ser su propio superior");
	}

	@Override
	protected void buildForEntity() {
		super.buildForEntity();
		this.titleCombo.setSelectedEntity(TitleEntity.getById(this.currentEmployee.getTitle()));
		this.hireDateChooser.setDate(this.currentEmployee.getHireDate());
		this.birthDateChooser.setDate(this.currentEmployee.getBirthDate());
	}

	@Override
	public @NotNull String getName() {
		if (this.currentEmployee == null)
			return "Nuevo empleado";

		return "Empleado (" + this.userView.getFullUsername() + ")";
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}

	@Override
	public void save() {
		this.userView.save();
	}
}