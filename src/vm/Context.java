package vm;

/** To call, push one of these and pop to return */
public class Context {
	int returnip;
	int[] locals; // args + locals, indexed from 0

	public Context(int returnip, int nlocals) {
		this.returnip = returnip;
		locals = new int[nlocals];
	}
}
