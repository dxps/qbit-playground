package qbit.samples.hr;

import java.util.ArrayList;
import java.util.List;

public class Department {
	
	private final long id;
	private final String name;
	private final List<Employee> employeeList;
	
	public Department(long id, String name, List<Employee> employeeList) {
		this.id = id;
		this.name = name;
		this.employeeList = employeeList;
	}
	
	public void addEmployee(Employee employee) {
		employeeList.add(employee);
	}
	
	public List<Employee> getEmployeeList() {
		return new ArrayList<>(employeeList);
	}
	
	public long getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "Department{ id=" + id +
				", name='" + name + '\'' +
				", employeeList=" + employeeList +
				'}';
	}
	
}
