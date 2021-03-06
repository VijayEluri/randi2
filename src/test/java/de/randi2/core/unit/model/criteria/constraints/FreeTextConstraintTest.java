package de.randi2.core.unit.model.criteria.constraints;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.randi2.model.criteria.constraints.FreeTextConstraint;
import de.randi2.testUtility.utility.AbstractDomainTest;
import de.randi2.unsorted.ConstraintViolatedException;


public class FreeTextConstraintTest extends	AbstractDomainTest<FreeTextConstraint> {

	private FreeTextConstraint constraint;
	private List<String> elements = new ArrayList<String>();
	
	public FreeTextConstraintTest(){
		super(FreeTextConstraint.class);
	}
	
	@Before
	public void setUp() throws ConstraintViolatedException{
		elements.add("value");
		constraint = new FreeTextConstraint(elements);
	}
	
	
	@Test
	public void testConstructorWithListEqualsNull(){
		try {
			constraint = new FreeTextConstraint(null);
			fail("the list of constraints should not be null");
		} catch (ConstraintViolatedException e) {}
	}
	
	@Test
	public void testConstructorWithEmptyList(){
		List<String> elements = new ArrayList<String>();
		try {
			constraint = new FreeTextConstraint(elements);
			fail("the list of constraints should be not empty");
		} catch (ConstraintViolatedException e) {}
	}
	
	
	@Test
	public void testConstructorWithListContainsMoreThanOneElement(){
		List<String> elements = new ArrayList<String>();
		elements.add("1");
		elements.add("2");
		try {
			constraint = new FreeTextConstraint(elements);
			fail("the list of constraints should not contain more than two elements");
		} catch (ConstraintViolatedException e) {}
	}
	
	
	@Test
	public void testConstructorWithListContainsOneNullElement(){
		List<String> elements = new ArrayList<String>();
		elements.add(null);
		try {
			constraint = new FreeTextConstraint(elements);
			fail("the list of constraints should not contain one element with the value null");
		} catch (ConstraintViolatedException e) {}
	}
	
	@Test
	public void testConstructorWithListContainsOneCorrectElement(){
		List<String> elements = new ArrayList<String>();
		elements.add("a");
		try {
			constraint = new FreeTextConstraint(elements);
			assertEquals("a", constraint.getExpectedValue());
		} catch (ConstraintViolatedException e) {
			fail("the list of constraints is ok");
		}
	}
	
	
	@Test
	public void testExpectedValue(){
		assertEquals(elements.get(0), constraint.getExpectedValue());
		constraint.setExpectedValue("Value2");
		assertEquals("Value2", constraint.getExpectedValue());
	}
	
	
	@Test
	public void testIsValueCorrect_WithCorrectValue(){
		try {
			constraint.isValueCorrect(elements.get(0));
		} catch (ConstraintViolatedException e) {
			fail("Value is correct");
		}
	}
	
	@Test
	public void testIsValueCorrect_WithIncorrectValue(){
		try {
			constraint.isValueCorrect("ValueXYZ");
			fail("Value is not correct");
		} catch (ConstraintViolatedException e) {		}
	}
	
	@Test
	public void testCheckValue(){
		assertTrue(constraint.checkValue(elements.get(0)));
		assertFalse(constraint.checkValue("ValueXYZ"));
		
	}
	
	@Test
	public void testUiName(){
		assertEquals(elements.get(0), constraint.getUIName());
	}
	
	
	@Test
	public void testEqualsAndHashCode_SameObjects() throws ConstraintViolatedException{
		FreeTextConstraint constraint1 = new FreeTextConstraint(elements);
		FreeTextConstraint constraint2 = constraint1;
		assertEquals(constraint1.hashCode(), constraint2.hashCode());
		assertTrue(constraint1.equals(constraint2));
	}
	
	@Test
	public void testEqualsAndHashCode_Null() throws ConstraintViolatedException{
		FreeTextConstraint constraint1 = new FreeTextConstraint(elements);
		assertFalse(constraint1.equals(null));
	}
	
	@Test
	public void testEqualsAndHashCode_DifferentClasses() throws ConstraintViolatedException{
		FreeTextConstraint constraint1 = new FreeTextConstraint(elements);
		String constraint2 = "";
		assertFalse(constraint1.equals(constraint2));
	}
	
	@Test
	public void testEqualsAndHashCode_ExpectedCorrectAndValueNull() throws ConstraintViolatedException{
		FreeTextConstraint constraint1 = new FreeTextConstraint(elements);
		FreeTextConstraint constraint2 = new FreeTextConstraint(elements);
		constraint1.setExpectedValue("a");
		constraint2.setExpectedValue(null);
		assertFalse(constraint1.equals(constraint2));
	}
	
	@Test
	public void testEqualsAndHashCode_ExpectedNullAndValueNot() throws ConstraintViolatedException{
		FreeTextConstraint constraint1 = new FreeTextConstraint(elements);
		FreeTextConstraint constraint2 = new FreeTextConstraint(elements);
		constraint1.setExpectedValue(null);
		constraint2.setExpectedValue("a");
		assertFalse(constraint1.equals(constraint2));
	}
	
	@Test
	public void testEqualsAndHashCode_ExpectedAndValueUnequal() throws ConstraintViolatedException{
		FreeTextConstraint constraint1 = new FreeTextConstraint(elements);
		FreeTextConstraint constraint2 = new FreeTextConstraint(elements);
		constraint1.setExpectedValue("a");
		constraint2.setExpectedValue("b");
		assertFalse(constraint1.equals(constraint2));
	}
	
	@Test
	public void testEqualsAndHashCode_ExpectedAndValueEqual() throws ConstraintViolatedException{
		FreeTextConstraint constraint1 = new FreeTextConstraint(elements);
		FreeTextConstraint constraint2 = new FreeTextConstraint(elements);
		String[] values = { stringUtil.getWithLength(254),
				stringUtil.getWithLength(2), "adsagsda dsf",
				stringUtil.getWithLength(132) };
		for (String s : values) {
			constraint1.setExpectedValue(s);
			constraint2.setExpectedValue(s);
			assertEquals(constraint1.hashCode(), constraint2.hashCode());
			assertTrue(constraint1.equals(constraint2));
		}
	}
		
	
	@Test
	public void testConstructor(){
		List<String> elements = new ArrayList<String>();
		try {
			constraint = new FreeTextConstraint(elements);
			fail("the list of constraints should be not empty");
		} catch (ConstraintViolatedException e) {}
		
		elements.add("Value1");
		try {
			constraint = new FreeTextConstraint(elements);
			assertTrue(constraint.getExpectedValue().equals("Value1"));
		} catch (ConstraintViolatedException e) {
			fail("the list of constraints is ok");
		}
		elements.add("Value2");
		try {
			constraint = new FreeTextConstraint(elements);
			fail("the list of constraints has more than two objects");
		} catch (ConstraintViolatedException e) {
		}
		try {
			constraint = new FreeTextConstraint(null);
			fail("the list of constraints is null");
		} catch (ConstraintViolatedException e) {
		}
		
	}
	
	
	
	@Test
	public void testIsValueCorrect(){
		try {
			constraint.isValueCorrect(elements.get(0));
		} catch (ConstraintViolatedException e) {
			fail("Value is correct");
		}
		try {
			constraint.isValueCorrect("ValueXYZ");
			fail("Value is not correct");
		} catch (ConstraintViolatedException e) {		}
	}
	
	@Test
	public void testExpectedValues(){
		assertTrue(constraint.getExpectedValue().equals(elements.get(0)));
		String test = "value123";
		constraint.setExpectedValue(test);
		assertTrue(constraint.getExpectedValue().equals(test));
	}
	
}
