package qbit.samples.hr;


import io.advantageous.boon.core.Sys;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a storage repo. Imagine this is talking to MongoDB or
 * Cassandra. Perhaps it is also indexing the department name via
 * SOLR. It does all of this and then returns when it is finished.
 * If this in turn called other services, it would use a Callback instead of
 * returning a boolean.
 */
public class DepartmentRepo {
	
	private final Map<Long, Department> departmentStorage = new HashMap<>();
	
	
	/**
	 * Add a department.
	 * @param department department we are adding.
	 * @return true if successfully stored the department
	 */
	public boolean addDepartment(final Department department) {
		
		System.out.printf("%s | DepartmentRepo.addDepartment(_) |-> %s\n", Instant.now(), department);
		Sys.sleep(5000); // simulate a slow operation taking 5 seconds
		departmentStorage.put(department.getId(), department);
		System.out.printf("%s | DepartmentRepo.addDepartment(_) |<- %s\n", Instant.now(), department);
		return true;
	}
	
}
