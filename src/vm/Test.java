package vm;

import static vm.Bytecode.BR;
import static vm.Bytecode.BRF;
import static vm.Bytecode.CALL;
import static vm.Bytecode.GLOAD;
import static vm.Bytecode.GSTORE;
import static vm.Bytecode.HALT;
import static vm.Bytecode.IADD;
import static vm.Bytecode.ICONST;
import static vm.Bytecode.ILT;
import static vm.Bytecode.IMUL;
import static vm.Bytecode.ISUB;
import static vm.Bytecode.LOAD;
import static vm.Bytecode.PRINT;
import static vm.Bytecode.RET;

public class Test {
	static int[] hello = {
		ICONST, 1,
		ICONST, 2,
		IADD,
		PRINT,
		HALT
	};

    /*
    iconst(1);
    iconst(2);
    iadd();
    print();
    halt();
     */

	static int[] loop = {
	// .GLOBALS 2; N, I
	// N = 10						ADDRESS
			ICONST, 10,				// 0
			GSTORE, 0,				// 2
	// I = 0
			ICONST, 0,				// 4
			GSTORE, 1,				// 6
	// WHILE I<N:
	// START (8):
			GLOAD, 1,				// 8
			GLOAD, 0,				// 10
			ILT,					// 12
			BRF, 24,				// 13
	//     I = I + 1
			GLOAD, 1,				// 15
			ICONST, 1,				// 17
			IADD,					// 19
			GSTORE, 1,				// 20
			BR, 8,					// 22
	// DONE (24):
	// PRINT "LOOPED "+N+" TIMES."
			HALT					// 24
	};

	static int FACTORIAL_INDEX = 0;
	static int FACTORIAL_ADDRESS = 0;
	static int MAIN_ADDRESS = 21;
	static int[] factorial = {
//.def factorial: ARGS=1, LOCALS=0	ADDRESS
//	IF N < 2 RETURN 1
			LOAD, 0,				// 0
			ICONST, 2,				// 2
			ILT,					// 4
			BRF, 10,				// 5
			ICONST, 1,				// 7
			RET,					// 9
//CONT:
//	RETURN N * FACT(N-1)
			LOAD, 0,				// 10
			LOAD, 0,				// 12
			ICONST, 1,				// 14
			ISUB,					// 16
			CALL, FACTORIAL_INDEX,	// 17
			IMUL,					// 19
			RET,					// 20
//.DEF MAIN: ARGS=0, LOCALS=0
// PRINT FACT(1)
			ICONST, 5,				// 21    <-- MAIN METHOD!
			CALL, FACTORIAL_INDEX,	// 23
			PRINT,					// 25
			HALT					// 26
	};

	static FuncMetaData[] metadata = {
		//.def factorial: ARGS=1, LOCALS=0	ADDRESS
		new FuncMetaData("factorial", 1, 0, FACTORIAL_ADDRESS)
	};

	public static void main(String[] args) {
		VM vm = new VM(factorial, MAIN_ADDRESS, 0, metadata);
		vm.trace = true;
		vm.exec();
	}
}
