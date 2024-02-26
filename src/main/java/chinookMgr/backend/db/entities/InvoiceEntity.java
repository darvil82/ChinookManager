package chinookMgr.backend.db.entities;

import chinookMgr.frontend.components.TableInspector;
import chinookMgr.frontend.toolViews.TrackView;
import chinookMgr.shared.ListTableModel;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static chinookMgr.backend.db.entities.EntityHelper.defaultSearch;

@Entity
@jakarta.persistence.Table(name = "Invoice", schema = "Chinook", catalog = "")
public class InvoiceEntity {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@jakarta.persistence.Column(name = "InvoiceId", nullable = false)
	private int invoiceId;

	public int getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	@Basic
	@Column(name = "CustomerId", nullable = false)
	private int customerId;

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	@Basic
	@Column(name = "InvoiceDate", nullable = false)
	private Timestamp invoiceDate;

	public Timestamp getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Timestamp invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	@Basic
	@Column(name = "BillingAddress", nullable = true, length = 70)
	private String billingAddress;

	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	@Basic
	@Column(name = "BillingCity", nullable = true, length = 40)
	private String billingCity;

	public String getBillingCity() {
		return billingCity;
	}

	public void setBillingCity(String billingCity) {
		this.billingCity = billingCity;
	}

	@Basic
	@Column(name = "BillingState", nullable = true, length = 40)
	private String billingState;

	public String getBillingState() {
		return billingState;
	}

	public void setBillingState(String billingState) {
		this.billingState = billingState;
	}

	@Basic
	@Column(name = "BillingCountry", nullable = true, length = 40)
	private String billingCountry;

	public String getBillingCountry() {
		return billingCountry;
	}

	public void setBillingCountry(String billingCountry) {
		this.billingCountry = billingCountry;
	}

	@Basic
	@Column(name = "BillingPostalCode", nullable = true, length = 10)
	private String billingPostalCode;

	public String getBillingPostalCode() {
		return billingPostalCode;
	}

	public void setBillingPostalCode(String billingPostalCode) {
		this.billingPostalCode = billingPostalCode;
	}

	@Basic
	@Column(name = "Total", nullable = false, precision = 2)
	private BigDecimal total;

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		InvoiceEntity that = (InvoiceEntity)o;

		if (invoiceId != that.invoiceId) return false;
		if (customerId != that.customerId) return false;
		if (invoiceDate != null ? !invoiceDate.equals(that.invoiceDate) : that.invoiceDate != null) return false;
		if (billingAddress != null ? !billingAddress.equals(that.billingAddress) : that.billingAddress != null)
			return false;
		if (billingCity != null ? !billingCity.equals(that.billingCity) : that.billingCity != null) return false;
		if (billingState != null ? !billingState.equals(that.billingState) : that.billingState != null) return false;
		if (billingCountry != null ? !billingCountry.equals(that.billingCountry) : that.billingCountry != null)
			return false;
		if (billingPostalCode != null ? !billingPostalCode.equals(that.billingPostalCode) : that.billingPostalCode != null)
			return false;
		if (total != null ? !total.equals(that.total) : that.total != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = invoiceId;
		result = 31 * result + customerId;
		result = 31 * result + (invoiceDate != null ? invoiceDate.hashCode() : 0);
		result = 31 * result + (billingAddress != null ? billingAddress.hashCode() : 0);
		result = 31 * result + (billingCity != null ? billingCity.hashCode() : 0);
		result = 31 * result + (billingState != null ? billingState.hashCode() : 0);
		result = 31 * result + (billingCountry != null ? billingCountry.hashCode() : 0);
		result = 31 * result + (billingPostalCode != null ? billingPostalCode.hashCode() : 0);
		result = 31 * result + (total != null ? total.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Factura Nº" + this.invoiceId;
	}

	public static TableInspector<InvoiceEntity> getTableInspector() {
		return new TableInspector<>(
			(session, s) ->
				session.createQuery(
					"select i from InvoiceEntity i join CustomerEntity c on i.customerId = c.customerId where c.company like :search or c.firstName like :search",
					InvoiceEntity.class
				)
					.setParameter("search", defaultSearch(s)),

			(session, s) ->
				session.createQuery(
					"select count(i) from InvoiceEntity i join CustomerEntity c on i.customerId = c.customerId where c.company like :search or c.firstName like :search",
					Long.class
				)
					.setParameter("search", defaultSearch(s)),

			new ListTableModel<>(List.of("Número", "Empresa", "Cliente", "Total"), (ent, col) -> switch (col) {
				case 0 -> String.valueOf(ent.getInvoiceId());
				case 1 -> CustomerEntity.getById(ent.getCustomerId()).getCompany();
				case 2 -> CustomerEntity.getById(ent.getCustomerId()).getFullName();
				case 3 -> ent.getTotal() + " €";
				default -> null;
			})
		);
	}

	public static TableInspector<InvoiceLineEntity> getLinesTableInspector(@NotNull InvoiceEntity invoice) {
		return new TableInspector<>(
			(session, s) ->
				session.createQuery(
					"select il from InvoiceLineEntity il join TrackEntity t on il.trackId = t.trackId where il.invoiceId = :invoiceId and t.name like :search",
					InvoiceLineEntity.class
				)
					.setParameter("search", defaultSearch(s))
					.setParameter("invoiceId", invoice.getInvoiceId()),

			(session, s) ->
				session.createQuery(
					"select count(il) from InvoiceLineEntity il join TrackEntity t on il.trackId = t.trackId where il.invoiceId = :invoiceId and t.name like :search",
					Long.class
				)
					.setParameter("search", defaultSearch(s))
					.setParameter("invoiceId", invoice.getInvoiceId()),

			new ListTableModel<>(List.of("Producto", "Precio", "Cantidad", "Total"), (ent, col) -> switch (col) {
				case 0 -> TrackEntity.getById(ent.getTrackId()).getName();
				case 1 -> ent.getUnitPrice() + " €";
				case 2 -> String.valueOf(ent.getQuantity());
				case 3 -> (ent.getUnitPrice().doubleValue() * ent.getQuantity()) + " €";
				default -> null;
			})
		).openViewOnRowClick(line -> new TrackView(TrackEntity.getById(line.getTrackId())));
	}
}