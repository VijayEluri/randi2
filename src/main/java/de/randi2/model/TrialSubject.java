/* 
 * (c) 2008-2009 RANDI2 Core Development Team
 * 
 * This file is part of RANDI2.
 * 
 * RANDI2 is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * RANDI2 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * RANDI2. If not, see <http://www.gnu.org/licenses/>.
 */
package de.randi2.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import de.randi2.unsorted.ContraintViolatedException;

/**
 * The Class TrialSubject.
 */
@Entity

/**
 * Sets the properties.
 * 
 * @param properties
 *            the new properties
 */
@Data
@EqualsAndHashCode(callSuper=true)
@ToString(exclude={"arm"})
public class TrialSubject extends AbstractDomainObject {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4469807155833123516L;

	/** The identification. */
	@NotNull
	@NotEmpty
	@Length(max = MAX_VARCHAR_LENGTH)
	private String identification;

	/** The rand number. */
	@NotNull
	@NotEmpty
	@Length(max = MAX_VARCHAR_LENGTH)
	private String randNumber;
	
	/** The counter. */
	private int counter;
	
	/**
	 * Gets the counter.
	 * 
	 * @return the counter
	 */
	public int getCounter() {
		return counter;
	}
	
	/**
	 * Sets the counter.
	 * 
	 * @param counter
	 *            the new counter
	 */
	public void setCounter(int counter) {
		this.counter = counter;
	}

	/** The trial site. */
	@ManyToOne
	private TrialSite trialSite;
	
	/** The investigator. */
	@ManyToOne
	private Person investigator;

	/** The arm. */
	@NotNull
	@ManyToOne(cascade = CascadeType.ALL)
	private TreatmentArm arm;

	/** The properties. */
	@OneToMany(cascade = CascadeType.PERSIST)
	private Set<SubjectProperty<?>> properties = new HashSet<SubjectProperty<?>>();


	/**
	 * Gets the stratum.<br />
	 * Generate the stratum identification string for the actual trial subject.
	 * [criterion_id]_[constraint_id];[criterion_id]_[constraint_id];...
	 * 
	 * @return the stratum
	 */
	@SuppressWarnings("unchecked")
	@Transient
	public String getStratum() {
		List<String> stratum = new ArrayList<String>();
		for (SubjectProperty p : properties) {
			try {
				stratum.add(p.getCriterion().getId() + "_" + p.getStratum());
			} catch (ContraintViolatedException e) {
				e.printStackTrace();
			}
		}
		Collections.sort(stratum);
		StringBuffer result = new StringBuffer();
		for (String l : stratum) {
			result.append(l + ";");
		}
		return result.toString();
	}

	/* (non-Javadoc)
	 * @see de.randi2.model.AbstractDomainObject#getUIName()
	 */
	@Override
	public String getUIName() {
		return identification;
	}
	
	/**
	 * Gets the properties ui string.
	 * 
	 * @return the properties ui string
	 */
	@Deprecated
	public String getPropertiesUIString(){
		StringBuilder stringB = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		for(SubjectProperty<?> p : getProperties()){
			stringB.append(p.getCriterion().getName()).append(": ");
			if(GregorianCalendar.class.isInstance(p.getValue()))
				stringB.append(sdf.format(((GregorianCalendar)p.getValue()).getTime()));
			else
				stringB.append(p.getValue().toString());
			stringB.append("|");
		}
		return stringB.toString();
	}
}
