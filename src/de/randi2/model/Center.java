package de.randi2.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;


@Entity
public class Center extends AbstractDomainObject{

	public final static int MAX_LENGTH_POSTCODE = 10;
	
	private String name = "";
	
	private String street = "";
	private String postcode = "";
	private String city = "";
	private String password = "";
	
	@OneToOne
	private Person contactPerson = null;
	
	@OneToMany(mappedBy="center")
	private List<Person> members = null;
	
	//@Transient
	//private List<Trial> trials = new ArrayList<Trial>();

	@NotEmpty
	@Length(max=MAX_VARCHAR_LENGTH)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(name == null){
			name = "";
		}
		this.name = name;
	}

	@Length(max=MAX_VARCHAR_LENGTH)
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		if(street == null){
			street = "";
		}
		this.street = street;
	}

	@Length(max=MAX_LENGTH_POSTCODE)
	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		if(postcode == null){
			postcode = "";
		}
		this.postcode = postcode;
	}

	@Length(max=MAX_VARCHAR_LENGTH)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		if(city == null){
			city = "";
		}
		this.city = city;
	}

	//TODO Please annotate this filed. (lplotni)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Person getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(Person contactPerson) {
		this.contactPerson = contactPerson;
	}

	public List<Person> getMembers() {
		return members;
	}

	public void setMembers(List<Person> members) {
		this.members = members;
	}

	
	
	
}
