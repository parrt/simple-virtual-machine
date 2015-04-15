simple-virtual-machine
======================

A simple VM for a talk on building VMs in Java. See [video](https://www.youtube.com/watch?v=OjaAToVkoTw) and [slides](http://www.slideshare.net/parrt/how-to-build-a-virtual-machine).

There are multiple branches:

* [master](https://github.com/parrt/simple-virtual-machine). Basic instructions only (no function calls).
* [add-functions](https://github.com/parrt/simple-virtual-machine/tree/add-functions). Includes CALL/RET instructions, runs factorial test function.
* [split-stack](https://github.com/parrt/simple-virtual-machine/tree/split-stack). Split into operand stack and function call stack.
* [func-meta-info](https://github.com/parrt/simple-virtual-machine/tree/func-meta-info).  CALL bytecode instruction takes an index into a metadata table for functions rather than an address and the number of arguments. This makes it much easier for bytecode compiler to generate code because it doesn't need to worry about forward references. This branch also properly allocates space for local variables.
* [shatter-stack](https://github.com/parrt/simple-virtual-machine/tree/shatter-stack). Broke apart the `Context[]` stack into a linked-list with `invokingContext` as parent pointer to caller. added call stack for trace.

See also a [C version derived from split-stack](https://github.com/parrt/simple-virtual-machine-C). Parts derived from [codyebberson's C implementation](https://github.com/codyebberson/vm).
