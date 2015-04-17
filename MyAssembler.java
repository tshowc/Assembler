import java.util.*;
import java.io.*;

public class MyAssembler{
	public static void main(String[] argv) {
		//Load new file
		if(argv.length > 0){	
			//Array for 16bit Binary Instruction
			char [] binArray = new char[16];
			String binValue;
			File newFile = new File(argv[0]);
			try(Scanner fileScan = new Scanner(newFile);){
				String line = "";
				String hackFile = argv[0].substring(0, argv[0].indexOf(".asm")) + ".hack";
				System.out.println(hackFile);
				File outFile = new File(hackFile);
				FileOutputStream streamOut = new FileOutputStream(outFile);
				BufferedWriter fileBuff = new BufferedWriter(new OutputStreamWriter(streamOut));	
				while(fileScan.hasNextLine()){
					line = fileScan.nextLine();
				//	System.out.println(line.charAt(0));
			//		System.out.println(line);
					if ((line.length() > 0) && (line.charAt(0) == '@')){//A-INSTRUCTION
				//		System.out.println("A-INSTRUCTION");
					//      Initialize index[0] to 0
						binArray[0] = '0';
						String subLine = line.substring(1);
			//			System.out.println(subLine);
						binValue = decToBin(subLine);
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
					}
					else if((line.length() > 0) && ((line.indexOf('D')!=-1) || (line.indexOf('A')!=-1) || (line.indexOf('M')!=-1)
						|| (line.indexOf('=')!=-1) || (line.indexOf(';')!=-1)) && (line.indexOf('/') == -1)){//C-INSTRUCTION
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
			
		Comp = compDict(subComp);
		Dest = destDict(noDest);
		
		//JUMP LOOGIC
		switch(subJump){
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
		break;
		default: System.out.println("ERROR IN JUMP");
		}

		total = Comp + Dest + Jump;
		return total;

		


	}


	public static String compDict(String lineComp){
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
		

		switch(lineComp){
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
		
	
	
	
