package qbit.samples.todo.callback;


import io.advantageous.qbit.reactive.Callback;

/**
 * Async interface for ExecuteOpAsync.
 *
 * @author vision8
 */
public interface ExecuteOpAsync {
	
	/** Run the ExecuteOp's execute(op) in an asynchronous way. */
	void execute(final Callback<Boolean> callback, Op op);
	
}
