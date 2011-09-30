/* 
 * (c) 2008- RANDI2 Core Development Team
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
package de.randi2.utility.logging;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import de.randi2.model.AbstractDomainObject;

@Entity
public class LogEntry {

	public enum ActionType {
		LOGIN, LOGOUT, CREATE, UPDATE, DELETE, RANDOMIZE, ADD_RESPONSE;
	};

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private long id;

	private GregorianCalendar time = new GregorianCalendar();

	private String username;

	@Enumerated(EnumType.STRING)
	private ActionType action;

	private Class<? extends AbstractDomainObject> clazz;

	private long identifier;

	@Lob
	private String value;

	@Lob
	private String uiName;

	@Transient
	private SimpleDateFormat formater = new SimpleDateFormat(
			"yyyy-MM-dd' 'HH:mm:ss");

	public GregorianCalendar getTime() {
		return time;
	}

	public void setTime(GregorianCalendar time) {
		this.time = time;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ActionType getAction() {
		return action;
	}

	/**
	 * @return
	 */
	public String getUiAction() {
		StringBuffer htmlString = new StringBuffer(
				"<p style=\"font-weight: bold; color:");
		switch (action) {
		case LOGIN:
			htmlString.append("#66CC00");
			break;
		case LOGOUT:
			htmlString.append("#666600");
			break;
		case CREATE:
			htmlString.append("#993366");
			break;
		case DELETE:
			htmlString.append("#990000");
			break;
		case UPDATE:
			htmlString.append("#CC3333");
			break;
		case RANDOMIZE:
			htmlString.append("#339999");
			break;
		case ADD_RESPONSE:
			htmlString.append("#003399");
			break;
		}
		return htmlString.append("\">").append(action.toString())
				.append("</p>").toString();
	}

	public void setAction(ActionType action) {
		this.action = action;
	}

	public Class<? extends AbstractDomainObject> getClazz() {
		return clazz;
	}

	public void setClazz(Class<? extends AbstractDomainObject> clazz) {
		this.clazz = clazz;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdentifier() {
		return identifier;
	}

	public void setIdentifier(long identifier) {
		this.identifier = identifier;
	}

	public String getUiName() {
		return uiName;
	}

	public void setUiName(String uiName) {
		this.uiName = uiName;
	}

	public String getTimeAsString() {
		return formater.format(time.getTime());
	}

	@Override
	public String toString() {
		if (clazz != null)
			return formater.format(time.getTime()) + " " + username + ": "
					+ action + " object type: " + clazz.getSimpleName()
					+ "(id= " + identifier + ") " + value;
		else
			return formater.format(time.getTime()) + " " + username + ": "
					+ action;
	}
}
