package chinookMgr.frontend.toolViews;

import chinookMgr.backend.db.entities.CustomerEntity;
import chinookMgr.backend.db.entities.InvoiceEntity;
import chinookMgr.frontend.ToolView;
import chinookMgr.frontend.Utils;
import com.toedter.calendar.JDateChooser;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class InvoiceView extends ToolView {
	private JTextField txtCountry;
	private JTextField txtState;
	private JTextField txtCity;
	private JTextField txtPostal;
	private JTextField txtAddress;
	private JPanel datePanel;
	private JPanel invoiceLinesPanel;
	private JButton btnCustomer;
	private JPanel mainPanel;
	private JLabel lblTotal;

	private final InvoiceEntity currentInvoice;
	private CustomerEntity currentCustomer;
	private JDateChooser dateChooser;

	public InvoiceView(InvoiceEntity invoice) {
		this.currentInvoice = invoice;
		this.currentCustomer = CustomerEntity.getById(invoice.getCustomerId());
		this.buildForEntity();
	}

	@Override
	protected void build() {
		super.build();

		Utils.attachViewSelectorToButton(this.btnCustomer, () -> this.currentCustomer, "cliente", CustomerEntity.getTableInspector(), c -> this.currentCustomer = c, CustomerView::new);
		this.datePanel.add(this.dateChooser = new JDateChooser());
	}

	@Override
	protected void buildForEntity() {
		super.buildForEntity();

		this.txtCountry.setText(this.currentCustomer.getCountry());
		this.txtState.setText(this.currentCustomer.getState());
		this.txtCity.setText(this.currentCustomer.getCity());
		this.txtPostal.setText(this.currentCustomer.getPostalCode());
		this.txtAddress.setText(this.currentCustomer.getAddress());
		this.dateChooser.setDate(this.currentInvoice.getInvoiceDate());
		this.lblTotal.setText(this.currentInvoice.getTotal() + " €");
		this.insertView(this.invoiceLinesPanel, new GenericTableView<>("Productos", InvoiceEntity.getLinesTableInspector(this.currentInvoice)));
	}

	@Override
	public @NotNull String getName() {
		return "Factura Nº" + currentInvoice.getInvoiceId();
	}

	@Override
	public @NotNull JPanel getPanel() {
		return this.mainPanel;
	}
}