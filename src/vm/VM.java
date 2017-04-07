package vm;

import java.util.ArrayList;
import java.util.List;

import static vm.Bytecode.BR;
import static vm.Bytecode.BRF;
import static vm.Bytecode.BRT;
import static vm.Bytecode.CALL;
import static vm.Bytecode.GLOAD;
import static vm.Bytecode.GSTORE;
import static vm.Bytecode.HALT;
import static vm.Bytecode.IADD;
import static vm.Bytecode.ICONST;
import static vm.Bytecode.IEQ;
import static vm.Bytecode.ILT;
import static vm.Bytecode.IMUL;
import static vm.Bytecode.ISUB;
import static vm.Bytecode.LOAD;
import static vm.Bytecode.POP;
import static vm.Bytecode.PRINT;
import static vm.Bytecode.RET;
import static vm.Bytecode.STORE;

/** A simple stack-based interpreter */
public class VM {
	public static final int DEFAULT_STACK_SIZE = 1000;
	public static final int DEFAULT_CALL_STACK_SIZE = 1000;
	public static final int FALSE = 0;
	public static final int TRUE = 1;

	// registers
	int ip;             // instruction pointer register
	int sp = -1;  		// stack pointer register
	int callsp = -1;    // call stack pointer register

	int startip = 0;	// where execution begins

	// memory
	int[] code;         // word-addressable code memory but still bytecodes.
	int[] globals;      // global variable space
	int[] stack;		// Operand stack, grows upwards
	Context[] callstack;// call stack, grows upwards

	/** Metadata about the functions allows us to refer to functions by
	 * 	their index in this table. It makes code generation easier for
	 * 	the bytecode compiler because it doesn't have to resolve
	 *  addresses for forward references. It can generate simply
	 *  "CALL i" where i is the index of the function. Later, the
	 *  compiler can store the function address in the metadata table
	 *  when the code is generated for that function.
	 */
	FuncMetaData[] metadata;

	public boolean trace = false;

	public VM(int[] code, int startip, int nglobals, FuncMetaData[] metadata) {
		this.code = code;
		this.startip = startip;
		globals = new int[nglobals];
		stack = new int[DEFAULT_STACK_SIZE];
		callstack = new Context[DEFAULT_CALL_STACK_SIZE];
		this.metadata = metadata;
	}

	public void exec() {
		ip = startip;
		cpu();
	}

	/** Simulate the fetch-decode execute cycle */
	protected void cpu() {
		int opcode = code[ip];
		int a,b,addr,regnum;
		while (opcode!= HALT && ip < code.length) {
			if ( trace ) System.err.printf("%-35s", disInstr());
			ip++; //jump to next instruction or to operand
			switch (opcode) {
				case IADD:
					b = stack[sp--];   			// 2nd opnd at top of stack
					a = stack[sp--]; 			// 1st opnd 1 below top
					stack[++sp] = a + b;      	// push result
					break;
				case ISUB:
					b = stack[sp--];
					a = stack[sp--];
					stack[++sp] = a - b;
					break;
				case IMUL:
					b = stack[sp--];
					a = stack[sp--];
					stack[++sp] = a * b;
					break;
				case ILT :
					b = stack[sp--];
					a = stack[sp--];
					stack[++sp] = (a < b) ? TRUE : FALSE;
					break;
				case IEQ :
					b = stack[sp--];
					a = stack[sp--];
					stack[++sp] = (a == b) ? TRUE : FALSE;
					break;
				case BR :
					ip = code[ip++];
					break;
				case BRT :
					addr = code[ip++];
					if ( stack[sp--]==TRUE ) ip = addr;
					break;
				case BRF :
					addr = code[ip++];
					if ( stack[sp--]==FALSE ) ip = addr;
					break;
				case ICONST:
					stack[++sp] = code[ip++]; // push operand
					break;
				case LOAD : // load local or arg
					regnum = code[ip++];
					stack[++sp] = callstack[callsp].locals[regnum];
					break;
				case GLOAD :// load from global memory
					addr = code[ip++];
					stack[++sp] = globals[addr];
					break;
				case STORE :
					regnum = code[ip++];
					callstack[callsp].locals[regnum] = stack[sp--];
					break;
				case GSTORE :
					addr = code[ip++];
					globals[addr] = stack[sp--];
					break;
				case PRINT :
					System.out.println(stack[sp--]);
					break;
				case POP:
					--sp;
					break;
				case CALL :
					// expects all args on stack
					int findex = code[ip++];			// index of target function
					int nargs = metadata[findex].nargs;	// how many args got pushed
					int nlocals = metadata[findex].nlocals;
					callstack[++callsp] = new Context(ip,nargs+nlocals);
					// copy args into new context
					for (int i=0; i<nargs; i++) {
						callstack[callsp].locals[i] = stack[sp-i];
					}
					sp -= nargs;
					ip = metadata[findex].address;		// jump to function
					break;
				case RET:
					ip = callstack[callsp--].returnip;
					break;
				default :
					throw new Error("invalid opcode: "+opcode+" at ip="+(ip-1));
			}
			if ( trace ) System.err.println(stackString());
			opcode = code[ip];
		}
		if ( trace ) System.err.printf("%-35s", disInstr());
		if ( trace ) System.err.println(stackString());
		if ( trace ) dumpDataMemory();
	}

	protected String stackString() {
		StringBuilder buf = new StringBuilder();
		buf.append("stack=[");
		for (int i = 0; i <= sp; i++) {
			int o = stack[i];
			buf.append(" ");
			buf.append(o);
		}
		buf.append(" ]");
		return buf.toString();
	}

	protected String disInstr() {
		int opcode = code[ip];
		String opName = Bytecode.instructions[opcode].name;
		StringBuilder buf = new StringBuilder();
		buf.append(String.format("%04d:\t%-11s", ip, opName));
		int nargs = Bytecode.instructions[opcode].n;
		if ( opcode==CALL ) {
			buf.append(metadata[code[ip+1]].name);
		}
		else if ( nargs>0 ) {
			List<String> operands = new ArrayList<String>();
			for (int i=ip+1; i<=ip+nargs; i++) {
				operands.add(String.valueOf(code[i]));
			}
			for (int i = 0; i<operands.size(); i++) {
				String s = operands.get(i);
				if ( i>0 ) buf.append(", ");
				buf.append(s);
			}
		}
		return buf.toString();
	}

	protected void dumpDataMemory() {
		System.err.println("Data memory:");
		int addr = 0;
		for (int o : globals) {
			System.err.printf("%04d: %s\n", addr, o);
			addr++;
		}
		System.err.println();
	}
}
