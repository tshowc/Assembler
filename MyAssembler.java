import java.util.*;
import java.io.*;

public class MyAssembler{


	static HashMap <String, String> memMap = new HashMap <String, String>();
	static	int mem = 16;
	static int PCROM = 0;
	static String prevLine = "";
	public static void main(String[] argv) {
		//Load new file
		if(argv.length > 0){	
			//Array for 16bit Binary Instruction
			char [] binArray = new char[16];
			String binValue;
			File newFile = new File(argv[0]);
			try(Scanner bananaScan = new Scanner(newFile);){
				String baLine = "";
				String subLine = "";
				while(bananaScan.hasNextLine()){
					baLine = bananaScan.nextLine().trim();
					if ((baLine.length() > 0) && (baLine.charAt(0)=='(')){
//							System.out.println("IN BANANA STUFF");
							subLine = baLine.substring(1, baLine.length()-1);
							//Check if subLine is a reserved word
							memMap.put(subLine, Integer.toString(PCROM));
					}
			//			if (prevLine.indexOf(';') != -1){
			//				subLine = baLine.substring(1, baLine.length()-1);
			//				//Check if subLine is a reserved word
			//				memMap.put(subLine, Integer.toString(PCROM));
			//			}
			//			else{
			//				subLine = baLine.substring(1, baLine.length()-1);
			//				memMap.put(subLine, Integer.toString(PCROM));
			//			}
			//		}i
					baLine = baLine.trim();
					if((baLine.length() > 0) && (baLine.charAt(0) != '/')&& (baLine.charAt(0) != '(')){
//						System.out.println("IN BANANA STUFF");
						PCROM++;
						prevLine = baLine;
					}
				}	
			}catch(IOException x){
				System.err.format("IOException: %s%n", x);
			}
			try(Scanner fileScan = new Scanner(newFile);){
				String line = "";
				String hackFile = argv[0].substring(0, argv[0].indexOf(".asm")) + ".hack";
			//	System.out.println(hackFile);
				File outFile = new File(hackFile);
				FileOutputStream streamOut = new FileOutputStream(outFile);
				BufferedWriter fileBuff = new BufferedWriter(new OutputStreamWriter(streamOut));	
				while(fileScan.hasNextLine()){
					line = fileScan.nextLine();
					line = line.trim();
					if(line.indexOf('/') != -1){
					line = line.substring(0,line.indexOf('/'));}
				//	System.out.println(line.charAt(0));
			//		System.out.println(line);
					if ((line.length() > 0) && ((line.charAt(0) == '@') || (line.charAt(0)=='('))){//A-INSTRUCTION
				//		System.out.println("A-INSTRUCTION");
					//      Initialize index[0] to 0
						if (line.charAt(0)=='('){
						}
						else{
						binArray[0] = '0';
						String subLine = line.substring(1);
						//Check if subLine is a reserved word
						String numLine = aDict(subLine);
									
			//			System.out.println(subLine);
						binValue = decToBin(numLine);
						int count = 0;
						int index = 1;
						while(count < binValue.length()){
							binArray[index] = binValue.charAt(count);
							index++;
							count++;
						}
			//			System.out.print("A-INSTRUCTION: ");
					 	for(int i=0; i < 16; i++){
							System.out.print(binArray[i]);
							fileBuff.write(binArray[i]);	
						}
						fileBuff.newLine();
						System.out.println();
					}}
					else if((line.length() > 0) && ((line.indexOf('D')!=-1) || (line.indexOf('A')!=-1) || (line.indexOf('M')!=-1)
						|| (line.indexOf('=')!=-1) || (line.indexOf(';')!=-1)) && (line.charAt(0) != '/') && (line.indexOf('(') == -1)){//C-INSTRUCTION
					//	System.out.println("C-INSTRUCTION YEA");
						//Initalize index[0..2] to 0
						for(int i=0; i < 3; i++){
							binArray[i] = '1';
						}
						if (line.indexOf('=') > -1){
						//Assignment Instruction
						//	System.out.println("Assign:");
							binValue = assignInst(line);
							int count = 0;
							int index = 3;
							while(count < binValue.length()){
								binArray[index] = binValue.charAt(count);
								index++;
								count++;
							}
							for(int i=0; i<16; i++){
								System.out.print(binArray[i]);
								fileBuff.write(binArray[i]);	
							}
							fileBuff.newLine();
							System.out.println();
						}
						else if(line.indexOf(';') > -1){
						//Jump Instruction
						//	System.out.println("Jumping:");
							binValue = jumpInst(line);
							int count = 0;
							int index = 3;
							while (count < binValue.length()){
								binArray[index] = binValue.charAt(count);
								index++;
								count++;
							}
							for(int i=0; i<16; i++){
								System.out.print(binArray[i]);
								fileBuff.write(binArray[i]);	
							}
							fileBuff.newLine();
							System.out.println();
						}
					}
					
				}fileBuff.close();
			
			}catch(IOException x){
				System.err.format("IOException: %s%n", x);
			}
		}
		else{
		System.out.println("No file argument");
		}
	}


	public static String aDict(String aLine){
		String numALine = "";
		String R = "";

		switch(aLine.trim()){
		case "R0":
		case "SP": numALine = "0";
		break;
		case "R1":
		case "LCL": numALine = "1";
		break;
		case "R2":
		case "ARG": numALine = "2";
		break;
		case "R3":
		case "THIS": numALine = "3";
		break;
		case "R4":
		case "THAT": numALine = "4";
		break;
		case "R5": numALine = "5";
		break;
		case "R6": numALine = "6";
		break;
		case "R7": numALine = "7";
		break;
		case "R8": numALine = "8";
		break;
		case "R9": numALine = "9";
		break;
		case "R10": numALine = "10";
		break;
		case "R11": numALine = "11";
		break;
		case "R12": numALine = "12";
		break;
		case "R13": numALine = "13";
		break;
		case "R14": numALine = "14";
		break;
		case "R15": numALine = "15";
		break;
		case "SCREEN": numALine = "16384";
		break;
		case "KBD": numALine = "24576";
		break;
		default: numALine = "";
		}
	

	//	System.out.println(aLine);
	//	System.out.println(numALine);

	
		if(numALine == ""){
			if (Character.isLetter(aLine.charAt(0))){
				//HASHMAP PALOOZA
	
//				System.out.println(aLine);
				if (memMap.containsKey(aLine)){
							
					numALine = memMap.get(aLine);
				}
				else{
				
					memMap.put(aLine, Integer.toString(mem));
					mem++;
					numALine = memMap.get(aLine);
				}
			}
			else{
				numALine = aLine;
			}
		}

		

//		System.out.println(numALine);
		return numALine; 
	


	}

	//Convert Decimal to Binary
	public static String decToBin(String subDec){
		String binaryString = "";
		int subInt = Integer.parseInt(subDec);
		int bP;
		double binPow;
		double power = 14;
		double two = 2;
		while(!(power < 0)){
			binPow = Math.pow(two, power);
			bP = (int) binPow;
			if(subInt >= bP){
			//	System.out.println("IN 1");
				subInt = subInt - bP;
				binaryString = binaryString.concat("1");
				power--;
			}
			else{
			//	System.out.println("IN 0");
				binaryString = binaryString.concat("0");
				power--;
			}
		}
	//	String message = "BINARY STRING = " + binaryString;
	//	System.out.println(message);
		return binaryString;
	}	


	public static String assignInst(String newLine){
		newLine = newLine.trim();
		int indexEqual = newLine.indexOf('=');
		String subDest = newLine.substring(0, indexEqual);
		String subComp = newLine.substring(indexEqual+1);
		String binDest = ""; //Binary for Dest
		String Comp = "";//Binary for a and Comp
		String remLine = ""; //remaining Binary Code
		String Jump = "000";
		

		Comp = compDict(subComp);
		String messageComp = "COMP IS: " + Comp;
//		System.out.println(messageComp);


		binDest = destDict(subDest);

		String messageDest = "DEST IS: " + binDest;
//		System.out.println(messageDest);


		remLine = Comp + binDest + Jump;
		String messageLine = "REMAINING LINE IS: " + remLine;
//		System.out.println(messageLine);
		return remLine;
	
	}

	public static String jumpInst(String jumpLine){
		int indexSemi = jumpLine.indexOf(';');
		String subComp = jumpLine.substring(0, indexSemi);
		String subJump = jumpLine.substring(indexSemi+1);
		String Comp = "";
		String noDest = "";
		String Dest = "";
		String Jump = "";
		String total = "";
		jumpLine = jumpLine.trim();	
		Comp = compDict(subComp);
		Dest = destDict(noDest);
		
	//	System.out.println(subJump);
	//	System.out.println(subJump.length());
		//JUMP LOOGIC
		switch(subJump.trim()){
		case "JGT": Jump = "001";
		break;
		case "JEQ": Jump = "010";
		break;
		case "JGE": Jump = "011";
		break;
		case "JLT": Jump = "100";
		break;
		case "JNE": Jump = "101";
		break;
		case "JLE": Jump = "110";
		break;
		case "JMP": Jump = "111";
		//	System.out.println("IN JMP");
		break;
		default: System.out.println("ERROR IN JUMP");
		}

		total = Comp + Dest + Jump;
		return total;

		


	}


	public static String compDict(String lineComp){
		lineComp = lineComp.trim();
		String binComp = "";
		String oneA = "";
	
		if(lineComp.indexOf('A') != -1){
			oneA = "0";
		}
		else if(lineComp.indexOf('M') != -1){
			oneA = "1";
		}
		else{
			oneA = "0";
		}
		
	//	System.out.println(lineComp);
	//	System.out.println(lineComp.length());
		switch(lineComp.trim()){
		case "0": binComp = "101010"; 
		break;
		case "1": binComp = "111111";
		break;
		case "-1": binComp = "111010";
		break;
		case "D": binComp = "001100";
		break;
		case ("A"):
		case ("M"): binComp = "110000";
		break;
		case "!D": binComp = "001101";
		break;
		case ("!A"):
		case ("!M"): binComp = "110001";
		break;
		case "-D": binComp = "001111";
		break;
		case ("-A"):
		case ("-M"): binComp = "110011";
		break;
		case "D+1": binComp = "011111";
		break;
		case ("A+1"):
		case ("M+1"): binComp = "110111";
		break;
		case "D-1": binComp = "001110";
		break;
		case ("A-1"):
		case ("M-1"): binComp = "110010";
		break;
		case ("D+A"):
		case ("D+M"): binComp = "000010";
		break;
		case ("D-A"):
		case ("D-M"): binComp = "010011";
		break;
		case ("A-D"):
		case ("M-D"): binComp = "000111";
		break;
		case ("D&A"):
		case ("D&M"): binComp = "000000";
		break;
		case ("D|A"):
		case ("D|M"): binComp = "010101";
		break;
		default: System.out.println("ERROR");
		}

		String Comp = oneA + binComp;
		return Comp;
	}

	public static String destDict(String destLine){
		String binDest = "";		


		if (destLine.indexOf("A") != -1){
			binDest = binDest.concat("1");
		}
		else{
			binDest = binDest.concat("0");
		}
		if (destLine.indexOf("D") != -1){
			binDest = binDest.concat("1");
		}
		else{
			binDest = binDest.concat("0");
		}
		if (destLine.indexOf("M") != -1){
			binDest = binDest.concat("1");
		}
		else{
			binDest = binDest.concat("0");
		}

		return binDest;
	}

}
		
	
	
	
