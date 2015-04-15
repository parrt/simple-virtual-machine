package vm;

/** To call, push one of these and pop to return */
public class Context {
	Context invokingContext;	// parent in the stack or "caller"
	FuncMetaData metadata;		// info about function we're executing
	int returnip;
	int[] locals; // args + locals, indexed from 0

	public Context(Context invokingContext, int returnip, FuncMetaData metadata) {
		this.invokingContext = invokingContext;
		this.returnip = returnip;
		this.metadata = metadata;
		locals = new int[metadata.nargs+metadata.nlocals];
	}
}
