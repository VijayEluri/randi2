package de.randi2.model.randomization;

import lombok.Data;
import de.randi2.randomization.Minimization;
import de.randi2.randomization.RandomizationAlgorithm;

@Data
public class MinimizationConfig extends AbstractRandomizationConfig {

	
	private int p;
	
	@Override
	public RandomizationAlgorithm<? extends AbstractRandomizationConfig> createAlgorithm() {
		return new Minimization(super.getTrial());
	}

}
