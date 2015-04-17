run: compile
	java MyAssembler $(asmfile)
compile: MyAssembler.java
	javac MyAssembler.java
