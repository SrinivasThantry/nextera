package nexterahome.core.servlets;

public class UserPojo {

	private String firstName;
	private String lastName;
	private long customerIdentifier;
	private String email;
	private boolean marketingOptIn;
	private String addressLine1;
	private String addressLine2;
	private String zip;
	private String state;
	private String city;
	private String coverageAddress;
	private boolean isMailingAddressSameasCoverageAddress;
	private String planName;
	private long marketingProgramId;
	private long deductible;
	private String promoCode;
	private String payload;
	private String phonenumbertype;
	private String phonenumber;
	
	

	public String getPhonenumbertype() {
		return phonenumbertype;
	}

	public void setPhonenumbertype(String phonenumbertype) {
		this.phonenumbertype = phonenumbertype;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public long getCustomerIdentifier() {
		return customerIdentifier;
	}

	public void setCustomerIdentifier(long customerIdentifier) {
		this.customerIdentifier = customerIdentifier;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean getMarketingOptIn() {
		return marketingOptIn;
	}

	public void setMarketingOptIn(boolean marketingOptIn) {
		this.marketingOptIn = marketingOptIn;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCoverageAddress() {
		return coverageAddress;
	}

	public void setCoverageAddress(String coverageAddress) {
		this.coverageAddress = coverageAddress;
	}

	public boolean getIsMailingAddressSameasCoverageAddress() {
		return isMailingAddressSameasCoverageAddress;
	}

	public void setMailingAddressSameasCoverageAddress(boolean isMailingAddressSameasCoverageAddress) {
		this.isMailingAddressSameasCoverageAddress = isMailingAddressSameasCoverageAddress;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public long getMarketingProgramId() {
		return marketingProgramId;
	}

	public void setMarketingProgramId(long marketingProgramId) {
		this.marketingProgramId = marketingProgramId;
	}

	public long getDeductible() {
		return deductible;
	}

	public void setDeductible(long deductible) {
		this.deductible = deductible;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	@Override
	public String toString() {
		return "UserPojo [firstName=" + firstName + ", lastName=" + lastName + ", customerIdentifier="
				+ customerIdentifier + ", email=" + email + ", marketingOptIn=" + marketingOptIn + ", addressLine1="
				+ addressLine1 + ", addressLine2=" + addressLine2 + ", zip=" + zip + ", state=" + state + ", city="
				+ city + ", coverageAddress=" + coverageAddress + ", isMailingAddressSameasCoverageAddress="
				+ isMailingAddressSameasCoverageAddress + ", planName=" + planName + ", marketingProgramId="
				+ marketingProgramId + ", deductible=" + deductible + ", promoCode=" + promoCode + ", payload="
				+ payload + "]";
	}

}
