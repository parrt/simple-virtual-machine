package vm;

import static vm.Bytecode.BRF;
import static vm.Bytecode.CALL;
import static vm.Bytecode.HALT;
import static vm.Bytecode.IADD;
import static vm.Bytecode.ICONST;
import static vm.Bytecode.ILT;
import static vm.Bytecode.ISUB;
import static vm.Bytecode.LOAD;
import static vm.Bytecode.PRINT;
import static vm.Bytecode.RET;

public class Fibonacci {
	/*
	def fib(n):
	    if n < 2: return n
	    return fib(n-2) + fib(n-1)
	 */
	static int FIBONACCI_INDEX = 0;
	static int FIBONACCI_ADDRESS = 0;
	static int MAIN_ADDRESS = 26;
	static int[] fibonacci = {
//.def FIB: ARGS=1, LOCALS=0	ADDRESS
//	IF N < 2 RETURN N
			LOAD, 0,				// 0
			ICONST, 2,				// 2
			ILT,					// 4
			BRF, 10,				// 5
			LOAD, 0,				// 7
			RET,					// 9
//CONT:
//	RETURN FIB(N-2) + FIB(N-1)
			LOAD, 0,				// 10
			ICONST, 2,				// 12
			ISUB,					// 14
			CALL, FIBONACCI_INDEX,	// 15
			LOAD, 0,				// 17
			ICONST, 1,				// 19
			ISUB,					// 21
			CALL, FIBONACCI_INDEX,	// 22
			IADD,					// 24
			RET,					// 25
//.DEF MAIN: ARGS=0, LOCALS=0
// PRINT FACT(1)
			ICONST, 10,				// 26    <-- MAIN METHOD!
			CALL, FIBONACCI_INDEX,	// 28
			PRINT,					// 30
			HALT					// 31
	};
	static FuncMetaData[] fibonacci_metadata = {
		//.def fibonacci: ARGS=1, LOCALS=0	ADDRESS
		new FuncMetaData("fibonacci", 1, 0, FIBONACCI_ADDRESS),
		new FuncMetaData("main", 0, 0, MAIN_ADDRESS)
	};

	public static void main(String[] args) {
		VM vm = new VM(fibonacci, MAIN_ADDRESS, 0, fibonacci_metadata);
		vm.trace = true;
		vm.exec();
	}
}
