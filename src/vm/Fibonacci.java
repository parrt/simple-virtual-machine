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
	static int FIB_ADDRESS = 0;
	static int MAIN_ADDRESS = 28;
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
			CALL, FIB_ADDRESS, 1,   // 15
			LOAD, 0,				// 18
			ICONST, 1,				// 20
			ISUB,					// 22
			CALL, FIB_ADDRESS, 1,   // 23
			IADD,					// 26
			RET,					// 27
//.DEF MAIN: ARGS=0, LOCALS=0
// PRINT FIB(10)
			ICONST, 10,				// 28    <-- MAIN METHOD!
			CALL, FIB_ADDRESS, 1,   // 30
			PRINT,					// 33
			HALT					// 34
	};

	public static void main(String[] args) {
		VM vm = new VM(fibonacci, MAIN_ADDRESS, 0);
		vm.trace = true;
		vm.exec();
	}
}
