package de.randi2.core.integration.modelDatabase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityTransaction;

import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.randi2.model.AbstractDomainObject;
import de.randi2.model.Login;
import de.randi2.model.Person;
import de.randi2.model.Trial;
import de.randi2.model.TrialSite;
import de.randi2.testUtility.utility.AbstractDomainDatabaseTest;

public class TrialSiteDatabaseTest extends
		AbstractDomainDatabaseTest<TrialSite> {

	private TrialSite validTrialSite;

	public TrialSiteDatabaseTest() {
		super(TrialSite.class);
	}

	@Before
	public void setUp() {
		super.setUp();
		validTrialSite = factory.getTrialSite();
		entityManager.persist(
				validTrialSite.getContactPerson());
	}

	@Test
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void testTrials() {
		persistTrialSite();
		
		List<Trial> tl = new ArrayList<Trial>();

		tl.add(factory.getTrial());
		tl.add(factory.getTrial());
		tl.add(factory.getTrial());
		
		for (Trial trial : tl) {
			persistTrial(trial);
		}
		
		TrialSite trialSite = entityManager
				.find(TrialSite.class, validTrialSite.getId());
		assertEquals(validTrialSite.getId(), trialSite.getId());

		assertEquals(tl.size(), trialSite.getTrials().size());

		List<Trial> trials = new ArrayList<Trial>();
		trials.add(new Trial());
		validTrialSite.setTrials(trials);
		assertEquals(trials, validTrialSite.getTrials());
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private void persistTrialSite(){
		entityManager.persist(validTrialSite);
		assertTrue(validTrialSite.getId() != AbstractDomainObject.NOT_YET_SAVED_ID);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private Login getPersistLogin(){
		Login login = factory.getLogin();
		entityManager.persist(login);
		return login;
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private void persistTrial(Trial trial){
		trial.addParticipatingSite(validTrialSite);
		trial.setLeadingSite(validTrialSite);
		Login login = getPersistLogin();
		trial.setSponsorInvestigator(login.getPerson());
		assertEquals(1, trial.getParticipatingSites().size());
		assertEquals(validTrialSite.getId(), ((AbstractDomainObject) trial
				.getParticipatingSites().toArray()[0]).getId());
		entityManager.persist(trial);
	}

	@Test
	@Transactional
	public void testContactPerson() {
		Person p = factory.getPerson();
		entityManager.persist(p);
		validTrialSite.setContactPerson(p);
		assertEquals(p.getSurname(), validTrialSite.getContactPerson()
				.getSurname());
		entityManager.persist(validTrialSite);
		assertTrue(validTrialSite.getId() != AbstractDomainObject.NOT_YET_SAVED_ID);
		assertTrue(p.getId() != AbstractDomainObject.NOT_YET_SAVED_ID);

		TrialSite c =  entityManager.find(
				TrialSite.class, validTrialSite.getId());
		assertEquals(p.getId(), c.getContactPerson().getId());
	}

	@Test
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void testMembers() {

		List<Person> members = new ArrayList<Person>();

		entityManager.persist(validTrialSite);

		for (int i = 0; i < 100; i++) {
			Person p = factory.getPerson();
			entityManager.persist(p);
			validTrialSite.getMembers().add(p);
			validTrialSite = entityManager.merge(validTrialSite);
			members.add(p);
		}
		entityManager.flush();

		TrialSite c = entityManager.find(
				TrialSite.class, validTrialSite.getId());
		assertEquals(validTrialSite.getId(), c.getId());
		entityManager.refresh(c);
		assertEquals(members.size(), c.getMembers().size());
	}

}
