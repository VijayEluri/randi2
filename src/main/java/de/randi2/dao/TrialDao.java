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
package de.randi2.dao;

import java.util.List;

import de.randi2.model.Login;
import de.randi2.model.Trial;
import de.randi2.model.TrialSite;
import de.randi2.model.TrialSubject;
import de.randi2.services.TrialService;

/**
 * The Interface TrialDao.
 */
public interface TrialDao extends AbstractDao<Trial>{
	
	/**
	 * @see TrialService
	 * @param trial
	 * @param investigator
	 * @return
	 */
	public List<TrialSubject> getSubjects(Trial trial, Login investigator);
	
	

	/**
	 * @see TrialService
	 * @param trial site
	 * @return
	 */
	public List<Trial> getAll(TrialSite trialsite);
	
}
