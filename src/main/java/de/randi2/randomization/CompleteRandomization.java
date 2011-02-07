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
package de.randi2.randomization;

import de.randi2.model.TreatmentArm;
import de.randi2.model.Trial;
import de.randi2.model.TrialSubject;
import de.randi2.model.randomization.CompleteRandomizationConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author jthoenes
 */
public class CompleteRandomization extends RandomizationAlgorithm<CompleteRandomizationConfig> {

	public CompleteRandomization(Trial _trial) {
		super(_trial);
	}

	public CompleteRandomization(Trial _trial, long seed) {
		super(_trial, seed);
	}


	@Override
	protected TreatmentArm doRadomize(TrialSubject subject, Random random) {
		List<TreatmentArm> arms = new ArrayList<TreatmentArm>(trial.getTreatmentArms());
		return arms.get(random.nextInt(arms.size()));
	}
}
