package vm;

public class FuncMetaData {
	public String name;
	public int nargs;
	public int nlocals;
	public int address; // bytecode address

	public FuncMetaData(String name, int nargs, int nlocals, int address) {
		this.name = name;
		this.nargs = nargs;
		this.nlocals = nlocals;
		this.address = address;
	}
}
