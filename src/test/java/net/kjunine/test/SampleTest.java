package net.kjunine.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class SampleTest {

	@BeforeClass
	public static void beforeClass() {
		System.out.println("Before class");
	}

	@AfterClass
	public static void afterClass() {
		System.out.println("After class");
	}

	@Before
	public void before() {
		System.out.println("Before");
	}

	@After
	public void after() {
		System.out.println("After");
	}

	@Test
	public void testAdd() {
		System.out.println("testAdd");

		int expected = 3;
		int actual = Sample.add(1, 2);

		assertEquals(expected, actual);
	}

	@Test
	public void useJunit() {
		System.out.println("useJunit");

		assertNull(null);
		assertNotNull(new Object());

		assertTrue(true);
		assertFalse(false);

		assertThat(1, is(1));
		assertThat(1, not(2));
		assertThat("abc", startsWith("ab"));

		assertThat("good", allOf(equalTo("good"), startsWith("go")));
		assertThat("good", not(allOf(equalTo("bad"), equalTo("good"))));
		assertThat("good", anyOf(equalTo("bad"), equalTo("good")));
		assertThat(new Object(), not(sameInstance(new Object())));
	}

	@Test
	@Ignore
	public void ignored() {
		System.out.println("ignored");
	}

}
