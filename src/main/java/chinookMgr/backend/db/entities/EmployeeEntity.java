package chinookMgr.backend.db.entities;

import chinookMgr.backend.Role;
import chinookMgr.backend.User;
import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.frontend.ViewStack;
import chinookMgr.frontend.components.TableInspector;
import chinookMgr.frontend.toolViews.EmployeeView;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static chinookMgr.backend.db.entities.EntityHelper.defaultSearch;

@SuppressWarnings({"EqualsReplaceableByObjectsCall", "JpaDataSourceORMInspection"})
@Entity
@jakarta.persistence.Table(name = "Employee", schema = "Chinook", catalog = "")
public class EmployeeEntity extends User {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@jakarta.persistence.Column(name = "EmployeeId", nullable = false)
	private int employeeId;

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	@Override
	public @NotNull Integer getId() {
		return employeeId;
	}

	@Basic
	@Column(name = "LastName", nullable = false, length = 20)
	private String lastName;

	@Override
	public @NotNull String getLastName() {
		return lastName;
	}

	@NotNull
	@Override
	public void setLastName(@NotNull String lastName) {
		this.lastName = lastName;
	}

	@Basic
	@Column(name = "FirstName", nullable = false, length = 20)
	private String firstName;

	@Override
	public @NotNull String getFirstName() {
		return firstName;
	}

	@NotNull
	@Override
	public void setFirstName(@NotNull String firstName) {
		this.firstName = firstName;
	}

	@Basic
	@Column(name = "Title", nullable = false)
	private int title;

	public int getTitle() {
		return title;
	}

	public void setTitle(int title) {
		this.title = title;
	}

	@Basic
	@Column(name = "ReportsTo", nullable = true)
	private Integer reportsTo;

	public Integer getReportsTo() {
		return reportsTo;
	}

	public void setReportsTo(Integer reportsTo) {
		this.reportsTo = reportsTo;
	}

	@Basic
	@Column(name = "BirthDate", nullable = true)
	private Timestamp birthDate;

	public Timestamp getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Timestamp birthDate) {
		this.birthDate = birthDate;
	}

	@Basic
	@Column(name = "HireDate", nullable = true)
	private Timestamp hireDate;

	public Timestamp getHireDate() {
		return hireDate;
	}

	public void setHireDate(Timestamp hireDate) {
		this.hireDate = hireDate;
	}

	@Basic
	@Column(name = "Address", nullable = true, length = 70)
	private String address;

	@Override
	public String getAddress() {
		return address;
	}

	@Override
	public void setAddress(String address) {
		this.address = address;
	}

	@Basic
	@Column(name = "City", nullable = true, length = 40)
	private String city;

	@Override
	public String getCity() {
		return city;
	}

	@Override
	public void setCity(String city) {
		this.city = city;
	}

	@Basic
	@Column(name = "State", nullable = true, length = 40)
	private String state;

	@Override
	public String getState() {
		return state;
	}

	@Override
	public void setState(String state) {
		this.state = state;
	}

	@Basic
	@Column(name = "Country", nullable = true, length = 40)
	private String country;

	@Override
	public String getCountry() {
		return country;
	}

	@Override
	public void setCountry(String country) {
		this.country = country;
	}

	@Basic
	@Column(name = "PostalCode", nullable = true, length = 10)
	private String postalCode;

	@Override
	public String getPostalCode() {
		return postalCode;
	}

	@Override
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	@Basic
	@Column(name = "Phone", nullable = true, length = 24)
	private String phone;

	@Override
	public String getPhone() {
		return phone;
	}

	@Override
	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Basic
	@Column(name = "Fax", nullable = true, length = 24)
	private String fax;

	@Override
	public String getFax() {
		return fax;
	}

	@Override
	public void setFax(String fax) {
		this.fax = fax;
	}

	@Basic
	@Column(name = "Email", nullable = true, length = 60)
	private String email;

	@Override
	public @NotNull String getEmail() {
		return email;
	}

	@Override
	public @NotNull List<Role> getRoles() {
		var title = TitleEntity.getById(this.title);
		if (title == null)
			return List.of();
		return Role.getRolesFromFlags(TitleEntity.getById(this.title).getRoles());
	}

	@Override
	public void setRoles(@NotNull List<Role> roles) {
		throw new UnsupportedOperationException("Employee roles are determined by their title");
	}

	@NotNull
	@Override
	public void setEmail(@NotNull String email) {
		this.email = email;
	}

	@Basic
	@Column(name = "Password", nullable = true)
	private byte[] password;

	@Override
	public byte[] getPassword() {
		return password;
	}

	@Override
	public void setPassword(byte[] password) {
		this.password = password;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		EmployeeEntity that = (EmployeeEntity)o;

		if (employeeId != that.employeeId) return false;
		if (title != that.title) return false;
		if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
		if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
		if (reportsTo != null ? !reportsTo.equals(that.reportsTo) : that.reportsTo != null) return false;
		if (birthDate != null ? !birthDate.equals(that.birthDate) : that.birthDate != null) return false;
		if (hireDate != null ? !hireDate.equals(that.hireDate) : that.hireDate != null) return false;
		if (address != null ? !address.equals(that.address) : that.address != null) return false;
		if (city != null ? !city.equals(that.city) : that.city != null) return false;
		if (state != null ? !state.equals(that.state) : that.state != null) return false;
		if (country != null ? !country.equals(that.country) : that.country != null) return false;
		if (postalCode != null ? !postalCode.equals(that.postalCode) : that.postalCode != null) return false;
		if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
		if (fax != null ? !fax.equals(that.fax) : that.fax != null) return false;
		if (email != null ? !email.equals(that.email) : that.email != null) return false;
		if (!Arrays.equals(password, that.password)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = employeeId;
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
		result = 31 * result + title;
		result = 31 * result + (reportsTo != null ? reportsTo.hashCode() : 0);
		result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
		result = 31 * result + (hireDate != null ? hireDate.hashCode() : 0);
		result = 31 * result + (address != null ? address.hashCode() : 0);
		result = 31 * result + (city != null ? city.hashCode() : 0);
		result = 31 * result + (state != null ? state.hashCode() : 0);
		result = 31 * result + (country != null ? country.hashCode() : 0);
		result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
		result = 31 * result + (phone != null ? phone.hashCode() : 0);
		result = 31 * result + (fax != null ? fax.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + Arrays.hashCode(password);
		return result;
	}


	public static EmployeeEntity getById(int id) {
		return EntityHelper.getById(EmployeeEntity.class, id);
	}

	public static TableInspector<EmployeeEntity> getTableInspector() {
		return new TableInspector<>(
			(session, search) -> session.createQuery("from EmployeeEntity where firstName like :search or lastName like :search", EmployeeEntity.class)
				.setParameter("search", defaultSearch(search)),
			(session, search) -> session.createQuery("select count(*) from EmployeeEntity where firstName like :search or lastName like :search", Long.class)
				.setParameter("search", defaultSearch(search)),

			User.getTableModel()
		).onNewButtonClick(() -> ViewStack.current().push(new EmployeeView()));
	}
}