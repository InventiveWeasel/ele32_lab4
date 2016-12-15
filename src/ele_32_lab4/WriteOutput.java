package ele_32_lab4;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class WriteOutput {
	public static void converHex(String out) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter("out.txt", "UTF-8");
		int k = 0;
		String outHex = "";
		//while(k + 4 <= out.length()){
			//int aux = Integer.parseInt(out.substring(k, k+4), 2);
			//outHex = outHex + Integer.toString(aux,16);
			//k = k+4;
		//}
		writer.print(new BigInteger(out,2).toString(16));
		writer.close();
	}
	
	public void write(String mens) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter("out.txt", "UTF-8");
		writer.print(mens);
		writer.close();
	}
	
}
