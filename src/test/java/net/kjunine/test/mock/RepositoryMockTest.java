package net.kjunine.test.mock;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class RepositoryMockTest {

	@Test
	public void testFirst() {
		Repository repository = mock(Repository.class);
		Service service = new Service(repository);

		service.first(15);

		verify(repository).create("create");

		service.first(5);

		verify(repository).get();
	}

	@Test
	public void testSecond() {
		Repository repository = mock(Repository.class);

		when(repository.create("A")).thenReturn(false);
		when(repository.create("B")).thenReturn(true);

		Service service = new Service(repository);

		assertThat(service.second("A"), is("fail"));
		assertThat(service.second("B"), is("success"));
	}

}
