run: compile
	./MyAssembler $(asmFile)

compile: MyAssembler.cpp
	g++ -o MyAssembler MyAssembler.cpp
