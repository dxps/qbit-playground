package qbit.samples.todo.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic simulation of a slow operation.
 *
 * @author vision8
 */
public class ExecuteOp {
	
	private Logger logger = LoggerFactory.getLogger(ExecuteOp.class);
	
	/** Execute the operation. */
	public boolean execute(Op op) {
		
		int resultExecTime = 0;
		logger.debug(">>> execute > {} sec", op.requestExecTime());
		
		while (resultExecTime < op.requestExecTime()) {
			try {
				Thread.sleep(1000);
				resultExecTime++;
				op.setResultExecTime(resultExecTime);
			} catch (InterruptedException e) {
			}
		}
		
		logger.debug("<<< execute > {} sec", op.requestExecTime());
		
		return op.isComplete();
	}
	
}
