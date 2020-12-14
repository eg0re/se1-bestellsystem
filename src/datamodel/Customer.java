package datamodel;

public class Customer {
	final String id;
	private String firstName;
	private String lastName;
	private String contact;

	protected Customer(String id, String name, String contact) {
		if( id == null) {
			this.id = null;
		} else {
			this.id = id;
		}
		
		if ( name == null) {
			this.lastName = "";
		} else {
			this.lastName = name;
		}

		if ( contact == null) {
			this.contact = "";
		} else {
			this.contact = contact;
		}

		
		this.firstName = "";
		
	}

	public String getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstname) {
		if( firstname == null) {
			this.firstName = "";
		} else {
			this.firstName = firstname;
		}
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastname) {
		if( lastname == null) {
			this.lastName = "";
		} else {
			this.lastName = lastname;
		}
	}
	
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		if( contact == null) {
			this.contact = "";
		} else {
			this.contact = contact;
		}
	}
}
