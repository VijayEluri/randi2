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
package de.randi2.model.exceptions;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;

@SuppressWarnings("serial")
public class ValidationException extends RuntimeException {

	private Set<ConstraintViolation<?>> invalids;
	private String[] messages;

	public ValidationException(Set<ConstraintViolation<?>> _invalids) {
		this.invalids = _invalids;
	}
	
	public String[] getMessages(){
		if(messages == null){
			messages = new String[invalids.size()];
			Iterator<ConstraintViolation<?>> it = invalids.iterator();
			for(int i = 0; i< messages.length; i++){
				messages[i] = it.next().getMessage();
			}
		}
		return messages.clone();
	}
	
	public Set<ConstraintViolation<?>> getInvalids(){
		return this.invalids;
	}

}
