package net.kjunine.test.mock;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class MockitoUsageTest {

	@Test
	@SuppressWarnings("unchecked")
	public void testVerify() {
		List<String> mock = mock(List.class);

		mock.add("one");
		mock.clear();

		verify(mock).add("one");
		verify(mock).clear();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testStub() {
		List<String> mock = mock(List.class);

		when(mock.get(0)).thenReturn("first");
		when(mock.get(1)).thenThrow(new RuntimeException());

		assertThat(mock.get(0), is("first"));
		try {
			mock.get(1);
			fail();
		} catch (RuntimeException e) {
		}
		assertThat(mock.get(2), nullValue());

		verify(mock).get(0);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testArgumentMatcher() {
		List<String> mock = mock(List.class);

		when(mock.get(anyInt())).thenReturn("element");
		assertThat(mock.get(999), is("element"));
		verify(mock).get(anyInt());

		mock.add(111, "string");
		verify(mock).add(anyInt(), eq("string"));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testVerifyTimes() {
		List<String> mock = mock(List.class);

		mock.add("once");
		mock.add("twice");
		mock.add("twice");
		mock.add("three times");
		mock.add("three times");
		mock.add("three times");

		verify(mock).add("once");
		verify(mock, times(2)).add("twice");
		verify(mock, times(3)).add("three times");
		verify(mock, never()).add("never happened");

		verify(mock, atLeastOnce()).add("twice");
		verify(mock, atLeast(2)).add("three times");
		verify(mock, atMost(4)).add("three times");
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testStubVoid() {
		List<String> mock = mock(List.class);

		doThrow(new RuntimeException()).when(mock).clear();

		try {
			mock.clear();
			fail();
		} catch (RuntimeException e) {
		}
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testVerifyInOrder() {
		List<String> firstMock = mock(List.class);
		List<String> secondMock = mock(List.class);

		firstMock.add("was called first");
		secondMock.add("was called second");

		InOrder inOrder = inOrder(firstMock, secondMock);
		inOrder.verify(firstMock).add("was called first");
		inOrder.verify(secondMock).add("was called second");
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testVerifyZeroInteractions() {
		List<String> mock = mock(List.class);
		List<String> otherMock = mock(List.class);

		mock.add("one");
		verify(mock).add("one");

		verifyZeroInteractions(otherMock);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testVerifyNoMoreInteractions() {
		List<String> mock = mock(List.class);

		mock.add("one");
		mock.add("two");
		verify(mock).add("one");

		try {
			verifyNoMoreInteractions(mock);
			fail();
		} catch (AssertionError e) {
		}

		verify(mock).add("two");
		verifyNoMoreInteractions(mock);
	}

	@Mock
	private List<String> annotatedMock;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testMockAnnotation() {
		annotatedMock.add("one");
		verify(annotatedMock).add("one");
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testIterativeStub() {
		List<String> mock = mock(List.class);

		when(mock.get(anyInt())).thenReturn("one", "two", "three");

		assertThat(mock.get(anyInt()), is("one"));
		assertThat(mock.get(anyInt()), is("two"));
		assertThat(mock.get(anyInt()), is("three"));

		when(mock.get(anyInt())).thenThrow(new RuntimeException()).thenReturn(
				"four");

		try {
			mock.get(anyInt());
			fail();
		} catch (RuntimeException e) {
		}
		assertThat(mock.get(anyInt()), is("four"));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testThenAnswer() {
		List<String> mock = mock(List.class);

		// Why do I need this?
		when(mock.get(anyInt())).thenAnswer(new Answer<String>() {
			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return "[" + args[0] + "]";
			}
		});

		assertThat(mock.get(0), is("[0]"));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testDoSomething() {
		List<String> mock = mock(List.class);

		doThrow(new RuntimeException()).when(mock).clear();
		try {
			mock.clear();
			fail();
		} catch (RuntimeException e) {
		}

		// Why do I need this?
		doAnswer(new Answer<String>() {
			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				return invocation.getMock().toString();
			}
		}).when(mock).get(anyInt());
		assertThat(mock.get(100), is(mock.toString()));

		doNothing().doThrow(new RuntimeException()).when(mock).clear();
		mock.clear();
		try {
			mock.clear();
			fail();
		} catch (RuntimeException e) {
		}

		doReturn("return").when(mock).get(anyInt());
		assertThat(mock.get(100), is("return"));
	}

	@Test
	public void testSpy() {
		List<String> list = new ArrayList<String>();
		List<String> spy = spy(list);

		when(spy.size()).thenReturn(100);

		spy.add("one");
		spy.add("two");

		assertThat(spy.get(0), is("one"));
		assertThat(spy.size(), is(100));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testRealPartialMocks() {
		List<String> mock = mock(ArrayList.class);

		when(mock.size()).thenCallRealMethod();

		assertThat(mock.size(), is(0));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testReset() {
		List<String> mock = mock(List.class);

		mock.add("one");

		reset(mock);

		verifyZeroInteractions(mock);
	}

}
