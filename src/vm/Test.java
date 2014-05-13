package vm;

import static vm.Bytecode.BR;
import static vm.Bytecode.BRF;
import static vm.Bytecode.GLOAD;
import static vm.Bytecode.GSTORE;
import static vm.Bytecode.HALT;
import static vm.Bytecode.IADD;
import static vm.Bytecode.ICONST;
import static vm.Bytecode.ILT;
import static vm.Bytecode.PRINT;

public class Test {
	static int[] hello = {
		ICONST, 1,
		ICONST, 2,
		IADD,
		PRINT,
		HALT
	};

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

	public static void main(String[] args) {
		VM vm = new VM(hello, 0, 0);
		vm.trace = true;
		vm.exec();
		vm.dumpCodeMemory();

		vm = new VM(loop, 0, 2);
		vm.trace = true;
		vm.exec();
	}
}
