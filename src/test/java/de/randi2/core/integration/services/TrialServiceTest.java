package de.randi2.core.integration.services;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import de.randi2.model.Login;
import de.randi2.model.Role;
import de.randi2.model.TreatmentArm;
import de.randi2.model.Trial;
import de.randi2.model.TrialSite;
import de.randi2.model.TrialSubject;
import de.randi2.model.criteria.DichotomousCriterion;
import de.randi2.model.enumerations.TrialStatus;
import de.randi2.model.exceptions.TrialStateException;
import de.randi2.model.randomization.BlockRandomizationConfig;
import de.randi2.model.randomization.CompleteRandomizationConfig;
import de.randi2.model.randomization.ResponseAdaptiveRConfig;
import de.randi2.model.randomization.TruncatedBinomialDesignConfig;
import de.randi2.model.randomization.UrnDesignConfig;
import de.randi2.services.TrialService;
import de.randi2.services.TrialSiteService;
import de.randi2.services.UserService;

@Transactional
public class TrialServiceTest extends AbstractServiceTest{

	@Autowired private TrialService service;
	@Autowired private UserService userService;
	@Autowired private TrialSiteService trialSiteService;

	private Trial validTrial;
	
	
	@Override
	public void setUp() {
		super.setUp();
		authenticatAsPrincipalInvestigator();
		validTrial = factory.getTrial();
		validTrial.setSponsorInvestigator(user.getPerson());
		validTrial.setLeadingSite(trialSiteService.getTrialSiteFromPerson(user.getPerson()));
		validTrial.addParticipatingSite(trialSiteService.getTrialSiteFromPerson(user.getPerson()));
		validTrial.setStatus(TrialStatus.IN_PREPARATION);
		assertNotNull(validTrial.getLeadingSite());
	}
	
	
	@Test
	public void testCreate(){
		authenticatAsPrincipalInvestigator();
		service.create(validTrial);
		assertTrue(validTrial.getId()>0);
	}
	
	@Test
	public void testUpdate() throws IllegalArgumentException, TrialStateException{
		service.create(validTrial);
		assertTrue(validTrial.getId()>0);
		validTrial.setName("Trialname");
		service.update(validTrial);
		Trial dbTrial =  entityManager.find(Trial.class, validTrial.getId());
		assertNotNull(dbTrial);
		assertEquals(validTrial.getName(), dbTrial.getName());
		
	}
	
	@Test
	@Ignore
	public void testGetAll(){
		
		List<Trial> trials = new ArrayList<Trial>();
		service.create(validTrial);
		trials.add(validTrial);
		List<Trial> dbTrials = service.getAll();
		int trialsBefore = dbTrials.size();
		TrialSite site = trialSiteService.getTrialSiteFromPerson(user.getPerson());
		for(int i=0;i<10;i++){
			Trial trial = factory.getTrial();
			trial.setLeadingSite(site);
			trial.setSponsorInvestigator(user.getPerson());
			service.create(trial);
		}
		dbTrials = service.getAll();
		assertEquals(trialsBefore+10, dbTrials.size());
		
	}
	
	@Test
	public void testGetObject(){
		service.create(validTrial);
		assertTrue(validTrial.getId()>0);
		Trial dbTrial = service.getObject(validTrial.getId());
		assertNotNull(dbTrial);
		assertEquals(validTrial.getName(), dbTrial.getName());
		
	}
	
	@Test
	public void testRandomizeComplete() throws IllegalArgumentException, TrialStateException{
		TreatmentArm arm1 = new TreatmentArm();
		arm1.setPlannedSubjects(50);
		arm1.setName("arm1");
		arm1.setDescription("description");
		arm1.setTrial(validTrial);
		TreatmentArm arm2 = new TreatmentArm();
		arm2.setPlannedSubjects(50);
		arm2.setName("arm2");
		arm2.setDescription("description");		
		arm2.setTrial(validTrial);
		Set<TreatmentArm> arms = new HashSet<TreatmentArm>();
		arms.add(arm1);
		arms.add(arm2);
	
		service.create(validTrial);
		validTrial.setTreatmentArms(arms);
		validTrial.setRandomizationConfiguration(new CompleteRandomizationConfig());
		validTrial = service.update(validTrial);
		validTrial.setStatus(TrialStatus.ACTIVE);
		validTrial = service.update(validTrial);
		assertTrue(validTrial.getId()>0);
		assertEquals(2,validTrial.getTreatmentArms().size());
		authenticatAsInvestigator();
		for(int i=0;i<100;i++){
			TrialSubject subject = new TrialSubject();
			 subject.setIdentification("identification" + i);
			 subject.setTrialSite(validTrial.getLeadingSite());
			service.randomize(validTrial,subject );
		}
		
		Trial dbTrial = service.getObject(validTrial.getId());
		assertNotNull(dbTrial);
		assertEquals(validTrial.getName(), dbTrial.getName());
		assertEquals(2, dbTrial.getTreatmentArms().size());
		assertEquals(100, dbTrial.getSubjects().size());
	}
	
	@Test
	public void testRandomizeBlock() throws IllegalArgumentException, TrialStateException{
		int blocksize = 4;
		int randomizations = 100;
		TreatmentArm arm1 = new TreatmentArm();
		arm1.setPlannedSubjects(randomizations/2);
		arm1.setName("arm1");
		arm1.setDescription("description");
		arm1.setTrial(validTrial);
		TreatmentArm arm2 = new TreatmentArm();
		arm2.setPlannedSubjects(randomizations/2);
		arm2.setName("arm2");
		arm2.setDescription("description");
		arm2.setTrial(validTrial);
		Set<TreatmentArm> arms = new HashSet<TreatmentArm>();
		arms.add(arm1);
		arms.add(arm2);
	
		
		validTrial.setTreatmentArms(arms);
		BlockRandomizationConfig config =  new BlockRandomizationConfig();
		config.setMaximum(blocksize);
		config.setMinimum(blocksize);
		validTrial.setRandomizationConfiguration(config);
		validTrial.setStatus(TrialStatus.ACTIVE);
		service.create(validTrial);
		
		assertTrue(validTrial.getId()>0);
		assertEquals(2,validTrial.getTreatmentArms().size());
		assertTrue(validTrial.getRandomizationConfiguration().getId()>0);
		authenticatAsInvestigator();
		List<TreatmentArm> armsL = new ArrayList<TreatmentArm>(validTrial.getTreatmentArms());
		for(int i=0;i<randomizations;i++){
			TrialSubject subject = new TrialSubject();
			 subject.setIdentification("identification" + i);
			 subject.setTrialSite(validTrial.getLeadingSite());
			service.randomize(validTrial,subject );
			if((i%blocksize)==(blocksize-1)){
			assertEquals(armsL.get(0).getSubjects().size() ,armsL.get(1).getSubjects().size());
			}
			
			int diff=armsL.get(0).getSubjects().size() -armsL.get(1).getSubjects().size();
			assertTrue((blocksize/2)>=diff && (-1)*(blocksize/2)<=diff);
		}
		
		Trial dbTrial = service.getObject(validTrial.getId());
		assertNotNull(dbTrial);
		assertEquals(validTrial.getName(), dbTrial.getName());
		assertEquals(2, dbTrial.getTreatmentArms().size());
		List<TreatmentArm> armsDB = new ArrayList<TreatmentArm>(dbTrial.getTreatmentArms());
		assertEquals(randomizations, armsDB.get(0).getSubjects().size() + armsDB.get(1).getSubjects().size());
		assertEquals(randomizations/2, armsDB.get(0).getSubjects().size());
		assertEquals(randomizations/2, armsDB.get(1).getSubjects().size());
	}
	
	
	@Test
	public void testRandomizeTruncated() throws IllegalArgumentException, TrialStateException{
		TreatmentArm arm1 = new TreatmentArm();
		arm1.setPlannedSubjects(50);
		arm1.setName("arm1");
		arm1.setDescription("description");
		arm1.setTrial(validTrial);
		TreatmentArm arm2 = new TreatmentArm();
		arm2.setPlannedSubjects(50);
		arm2.setName("arm2");
		arm2.setDescription("description");
		arm2.setTrial(validTrial);
		Set<TreatmentArm> arms = new HashSet<TreatmentArm>();
		arms.add(arm1);
		arms.add(arm2);
		validTrial.setTreatmentArms(arms);
		validTrial.setRandomizationConfiguration(new TruncatedBinomialDesignConfig());
		validTrial.setStatus(TrialStatus.ACTIVE);
		service.create(validTrial);
		assertTrue(validTrial.getId()>0);
		assertEquals(2,validTrial.getTreatmentArms().size());
		authenticatAsInvestigator();
		for(int i=0;i<100;i++){
			TrialSubject subject = new TrialSubject();
			 subject.setIdentification("identification" + i);
			 subject.setTrialSite(validTrial.getLeadingSite());
			service.randomize(validTrial,subject );
		}
		
		Trial dbTrial = service.getObject(validTrial.getId());
		assertNotNull(dbTrial);
		assertEquals(validTrial.getName(), dbTrial.getName());
		assertEquals(2, dbTrial.getTreatmentArms().size());
		List<TreatmentArm> armsDB = new ArrayList<TreatmentArm>(dbTrial.getTreatmentArms());
		assertEquals(100, dbTrial.getSubjects().size());
		assertEquals(50, armsDB.get(0).getSubjects().size());
		assertEquals(50, armsDB.get(1).getSubjects().size());
	}
	
	@Test
	public void testUrnRandomization() throws IllegalArgumentException, TrialStateException{
		TreatmentArm arm1 = new TreatmentArm();
		arm1.setPlannedSubjects(50);
		arm1.setName("arm1");
		arm1.setDescription("description");
		arm1.setTrial(validTrial);
		TreatmentArm arm2 = new TreatmentArm();
		arm2.setPlannedSubjects(50);
		arm2.setName("arm2");
		arm2.setDescription("description");
		arm2.setTrial(validTrial);
		Set<TreatmentArm> arms = new HashSet<TreatmentArm>();
		arms.add(arm1);
		arms.add(arm2);
		validTrial.setTreatmentArms(arms);
		
		UrnDesignConfig conf = new UrnDesignConfig();
		conf.setInitializeCountBalls(4);
		conf.setCountReplacedBalls(2);
		validTrial.setRandomizationConfiguration(conf);
		validTrial.setStatus(TrialStatus.ACTIVE);
		
		service.create(validTrial);
		assertTrue(validTrial.getId()>0);
		assertEquals(2,validTrial.getTreatmentArms().size());
		assertTrue(validTrial.getRandomizationConfiguration().getId()>0);
		authenticatAsInvestigator();
		for(int i=0;i<100;i++){
			TrialSubject subject = new TrialSubject();
			 subject.setIdentification("identification" + i);
			 subject.setTrialSite(validTrial.getLeadingSite());
			service.randomize(validTrial,subject );
		}
		
		Trial dbTrial = service.getObject(validTrial.getId());
		assertNotNull(dbTrial);
		assertEquals(validTrial.getName(), dbTrial.getName());
		assertEquals(2, dbTrial.getTreatmentArms().size());
		assertEquals(100, dbTrial.getSubjects().size());
		assertTrue(dbTrial.getRandomizationConfiguration() instanceof UrnDesignConfig);
		assertTrue(((UrnDesignConfig)dbTrial.getRandomizationConfiguration()).getTempData() != null);
	}

	@Test
	public void testCreateAndRandomUrnDesign() throws IllegalArgumentException, TrialStateException{
		TreatmentArm arm1 = new TreatmentArm();
		arm1.setPlannedSubjects(50);
		arm1.setName("arm1");
		arm1.setDescription("description");
		arm1.setTrial(validTrial);
		TreatmentArm arm2 = new TreatmentArm();
		arm2.setPlannedSubjects(50);
		arm2.setName("arm2");
		arm2.setDescription("description");
		arm2.setTrial(validTrial);
		Set<TreatmentArm> arms = new HashSet<TreatmentArm>();
		arms.add(arm1);
		arms.add(arm2);
	
		validTrial.setTreatmentArms(arms);
		UrnDesignConfig conf = new UrnDesignConfig();
		conf.setInitializeCountBalls(4);
		conf.setCountReplacedBalls(2);
		validTrial.setRandomizationConfiguration(conf);
		validTrial.setStatus(TrialStatus.ACTIVE);
		
		service.create(validTrial);
		assertTrue(validTrial.getId()>0);
		assertEquals(2,validTrial.getTreatmentArms().size());
		authenticatAsInvestigator();
		
		for(int i=0;i<100;i++){
			TrialSubject subject = new TrialSubject();
			 subject.setIdentification("identification" + i);
			 subject.setTrialSite(validTrial.getLeadingSite());
			service.randomize(validTrial,subject );
		}
		entityManager.clear();
		Trial dbTrial = service.getObject(validTrial.getId());
		assertNotNull(dbTrial);
		assertEquals(validTrial.getName(), dbTrial.getName());
		assertEquals(2, dbTrial.getTreatmentArms().size());
		assertEquals(100, dbTrial.getSubjects().size());
		assertTrue(dbTrial.getRandomizationConfiguration() instanceof UrnDesignConfig);
		assertTrue(((UrnDesignConfig)dbTrial.getRandomizationConfiguration()).getTempData() != null);
	}
	
	@Test
	@Ignore
	@Transactional
	public void testGetSubjects() throws IllegalArgumentException, TrialStateException{
		/*
		 * Now creating another investigator
		 */
		//Login #1
		authenticatAsAdmin();
		
		TrialSite site = trialSiteService.getTrialSiteFromPerson(user.getPerson());
		Login l = factory.getLogin();
		String e = "i@getsubjectstest.com";
		l.setUsername(e);
		l.getPerson().setEmail(e);
		l.addRole(Role.ROLE_INVESTIGATOR);
		userService.create(l, site);
		//Login #2
		Login l2 = factory.getLogin();
		String e2 = "i2@getsubjectstest.com";
		l2.setUsername(e2);
		l2.getPerson().setEmail(e2);
		l2.addRole(Role.ROLE_INVESTIGATOR);
		userService.create(l2, site);
		/*
		 * First I need to create the trial and randomize some subjects.
		 */
		authenticatAsPrincipalInvestigator();
		Trial t = factory.getTrial();
		t.setLeadingSite(site);
		t.setSponsorInvestigator(user.getPerson());
		TreatmentArm arm1 = new TreatmentArm();
		arm1.setPlannedSubjects(25);
		arm1.setName("arm1");
		arm1.setTrial(t);
		TreatmentArm arm2 = new TreatmentArm();
		arm2.setPlannedSubjects(25);
		arm2.setName("arm2");
		arm2.setTrial(t);
		Set<TreatmentArm> arms = new HashSet<TreatmentArm>();
		arms.add(arm1);
		arms.add(arm2);
		t.addParticipatingSite(site);
		service.create(t);
		t.setTreatmentArms(arms);
		t.setRandomizationConfiguration(new CompleteRandomizationConfig());
		service.update(t);
		/*
		 * Checking if the trial saving and stuff went well. 
		 */
		assertTrue(t.getId()>0);
		assertEquals(2,t.getTreatmentArms().size());
		/*
		 * Randomizing the subjects using the service method.
		 */
		authenticatAsInvestigator();
		int expectedAmount = 50;
		for(int i=0;i<expectedAmount;i++){
			TrialSubject subject = new TrialSubject();
			subject.setIdentification("identification" + i);
			subject.setTrialSite(t.getLeadingSite());
			service.randomize(t,subject);
		}
		/*
		 * Trying to get the subjects for the admin user.
		 */
		List<TrialSubject> s = service.getSubjects(t,user);
		assertNotNull(s);
		assertEquals(expectedAmount, s.size());
		//TODO The user creation should be done here! (lplotni)
		/*
		 * Signing in the newly created user.
		 */
		AnonymousAuthenticationToken authToken = new AnonymousAuthenticationToken(
				e, l, new ArrayList<GrantedAuthority>(l.getAuthorities()));
		// Perform authentication
		SecurityContextHolder.getContext().setAuthentication(authToken);
		SecurityContextHolder.getContext().getAuthentication()
				.setAuthenticated(true);
		/*
		 * Randomizing another set of subjects
		 */
		int nextSet = 40;
		for(int i=0;i<nextSet;i++){
			TrialSubject subject = new TrialSubject();
			subject.setIdentification("anotherId" + i);
			subject.setTrialSite(t.getLeadingSite());
			service.randomize(t,subject);
		}
		/*
		 * Trying to get the second charge of the subjects.
		 */
		List<TrialSubject> s2 = service.getSubjects(t,l);
		assertNotNull(s2);
		assertEquals(nextSet, s2.size());
		/*
		 * Now testing some other scenarios. 
		 */
		List<TrialSubject> s3 = service.getSubjects(t, l2);
		assertNotNull(s3);
		assertEquals(0,s3.size());
	}
	
	@Test
	public void testCreateRandomAndAddResponseResponseAdaptiveRandomization() throws IllegalArgumentException, TrialStateException{
		TreatmentArm arm1 = new TreatmentArm();
		arm1.setPlannedSubjects(50);
		arm1.setName("arm1");
		arm1.setDescription("description");
		arm1.setTrial(validTrial);
		TreatmentArm arm2 = new TreatmentArm();
		arm2.setPlannedSubjects(50);
		arm2.setName("arm2");
		arm2.setDescription("description");
		arm2.setTrial(validTrial);
		TreatmentArm arm3 = new TreatmentArm();
		arm3.setPlannedSubjects(50);
		arm3.setName("arm3");
		arm3.setDescription("description");
		arm3.setTrial(validTrial);
		Set<TreatmentArm> arms = new HashSet<TreatmentArm>();
		arms.add(arm1);
		arms.add(arm2);
		arms.add(arm3);
	
		validTrial.setTreatmentArms(arms);
		ResponseAdaptiveRConfig conf = new ResponseAdaptiveRConfig();
		conf.setInitializeCountBallsResponseAdaptiveR(4);
		conf.setCountBallsResponseSuccess(8);
		conf.setCountBallsResponseFailure(4);
		validTrial.setRandomizationConfiguration(conf);
		
		DichotomousCriterion response = new DichotomousCriterion();
		response.setOption1("success");
		response.setOption2("failure");
		validTrial.setTreatmentResponse(response);
		
		validTrial.setStatus(TrialStatus.ACTIVE);
		
		service.create(validTrial);
		assertTrue(validTrial.getId()>0);
		assertEquals(3,validTrial.getTreatmentArms().size());
		authenticatAsInvestigator();
		
		for(int i=0;i<100;i++){
			 TrialSubject subject = new TrialSubject();
			 subject.setIdentification("identification" + i);
			 subject.setTrialSite(validTrial.getLeadingSite());
			 service.randomize(validTrial,subject );
		}
		
		entityManager.clear();
		Trial dbTrial = service.getObject(validTrial.getId());
		assertNotNull(dbTrial);
		assertEquals(validTrial.getName(), dbTrial.getName());
		assertEquals(3, dbTrial.getTreatmentArms().size());
		assertEquals(100, dbTrial.getSubjects().size());
		assertTrue(dbTrial.getRandomizationConfiguration() instanceof ResponseAdaptiveRConfig);
		assertTrue(dbTrial.getTreatmentResponse() instanceof DichotomousCriterion);
		assertTrue(((ResponseAdaptiveRConfig)dbTrial.getRandomizationConfiguration()).getTempData() != null);
	}
}
