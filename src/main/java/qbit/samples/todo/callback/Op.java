package qbit.samples.todo.callback;

/**
 * @author vision8
 */
public class Op {
	
	private int requestExecTime;
	
	private int resultExecTime;
	
	public Op(int requestExecTime) {
		this.requestExecTime = requestExecTime;
	}
	
	public int requestExecTime() {
		return requestExecTime;
	}
	
	public void setRequestExecTime(int requestExecTime) {
		this.requestExecTime = requestExecTime;
	}
	
	public int resultExecTime() {
		return resultExecTime;
	}
	
	public void setResultExecTime(int resultExecTime) {
		this.resultExecTime = resultExecTime;
	}
	
	public boolean isComplete() {
		return resultExecTime >= requestExecTime;
	}
	
	@Override
	public String toString() {
		
		return String.format("Op{ requestExecTime=%d, resultExecTime=%d, isComplete=%b }",
				requestExecTime, resultExecTime, isComplete());
	}
	
}
