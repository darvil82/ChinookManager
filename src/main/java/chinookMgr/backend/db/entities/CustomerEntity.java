package chinookMgr.backend.db.entities;

import jakarta.persistence.*;

@Entity
@jakarta.persistence.Table(name = "Customer", schema = "Chinook", catalog = "")
public class CustomerEntity {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@jakarta.persistence.Column(name = "CustomerId", nullable = false)
	private int customerId;

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	@Basic
	@Column(name = "FirstName", nullable = false, length = 40)
	private String firstName;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Basic
	@Column(name = "LastName", nullable = false, length = 20)
	private String lastName;

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Basic
	@Column(name = "Company", nullable = true, length = 80)
	private String company;

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Basic
	@Column(name = "Address", nullable = true, length = 70)
	private String address;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Basic
	@Column(name = "City", nullable = true, length = 40)
	private String city;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Basic
	@Column(name = "State", nullable = true, length = 40)
	private String state;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Basic
	@Column(name = "Country", nullable = true, length = 40)
	private String country;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Basic
	@Column(name = "PostalCode", nullable = true, length = 10)
	private String postalCode;

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	@Basic
	@Column(name = "Phone", nullable = true, length = 24)
	private String phone;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Basic
	@Column(name = "Fax", nullable = true, length = 24)
	private String fax;

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Basic
	@Column(name = "Email", nullable = false, length = 60)
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Basic
	@Column(name = "SupportRepId", nullable = true)
	private Integer supportRepId;

	public Integer getSupportRepId() {
		return supportRepId;
	}

	public void setSupportRepId(Integer supportRepId) {
		this.supportRepId = supportRepId;
	}

	@Basic
	@Column(name = "Password", nullable = true, length = 32)
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Basic
	@Column(name = "Roles", nullable = false)
	private byte roles;

	public byte getRoles() {
		return roles;
	}

	public void setRoles(byte roles) {
		this.roles = roles;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CustomerEntity that = (CustomerEntity)o;

		if (customerId != that.customerId) return false;
		if (roles != that.roles) return false;
		if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
		if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
		if (company != null ? !company.equals(that.company) : that.company != null) return false;
		if (address != null ? !address.equals(that.address) : that.address != null) return false;
		if (city != null ? !city.equals(that.city) : that.city != null) return false;
		if (state != null ? !state.equals(that.state) : that.state != null) return false;
		if (country != null ? !country.equals(that.country) : that.country != null) return false;
		if (postalCode != null ? !postalCode.equals(that.postalCode) : that.postalCode != null) return false;
		if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
		if (fax != null ? !fax.equals(that.fax) : that.fax != null) return false;
		if (email != null ? !email.equals(that.email) : that.email != null) return false;
		if (supportRepId != null ? !supportRepId.equals(that.supportRepId) : that.supportRepId != null) return false;
		if (password != null ? !password.equals(that.password) : that.password != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = customerId;
		result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (company != null ? company.hashCode() : 0);
		result = 31 * result + (address != null ? address.hashCode() : 0);
		result = 31 * result + (city != null ? city.hashCode() : 0);
		result = 31 * result + (state != null ? state.hashCode() : 0);
		result = 31 * result + (country != null ? country.hashCode() : 0);
		result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
		result = 31 * result + (phone != null ? phone.hashCode() : 0);
		result = 31 * result + (fax != null ? fax.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (supportRepId != null ? supportRepId.hashCode() : 0);
		result = 31 * result + (password != null ? password.hashCode() : 0);
		result = 31 * result + (int)roles;
		return result;
	}
}