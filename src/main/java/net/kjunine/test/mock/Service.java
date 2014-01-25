package net.kjunine.test.mock;

public class Service {

	private Repository repository;

	public Service(Repository repository) {
		this.repository = repository;
	}

	public void first(int num) {
		if (num > 10) {
			repository.create("create");
		} else {
			repository.get();
		}
	}

	public String second(String s) {
		boolean success = repository.create(s);

		return success ? "success" : "fail";
	}

}
