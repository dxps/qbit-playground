package qbit.samples.todo.callback;


import io.advantageous.qbit.reactive.Callback;

/**
 * Interface for asynchronous calls to ExecuteOp.<br/><br/>
 * This is created by ExecuteOp's service queue, using:<br>
 *   <code>execOpServiceQueue.createProxy(ExecuteOpAsync.class)</code>
 *
 * @author vision8
 */
public interface ExecuteOpAsync {
	
	/** Run the ExecuteOp's execute(op) in an asynchronous way. */
	void execute(final Callback<Boolean> callback, Op op);
	
}
