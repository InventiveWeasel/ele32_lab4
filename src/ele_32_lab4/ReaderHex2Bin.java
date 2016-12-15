package ele_32_lab4;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

public class ReaderHex2Bin {
	private BufferedReader input;
	private int bitCounter;
	private int db[];
	private int byteRec;
	private String texto = "";
	int counter = 0;
	
	public ReaderHex2Bin(String file) throws IOException{
		bitCounter = 0;
		db = new int[8];
		byteRec = 0;
		
		try {
			input = new BufferedReader(new FileReader(file));
			String line;
			while((line = input.readLine()) != null)
				texto = texto + line;
			input.close();
			texto = new BigInteger(texto,16).toString(2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public String getTexto(){
		return texto;
	}
	public int getBit(){
		/*
		try {
			byteRec = input.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(byteRec == -1){
			return -1;
		}
		
		return byteRec-48;
		*/
		byteRec = texto.charAt(counter)-48;
		counter++;
		return byteRec;
		
		/*
		if(bitCounter%8 == 0){
			db = new int[8];  //Reseta byte recebido com zeros
			try {
				byteRec = input.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			bitCounter = 0;
			if(byteRec == -1){
				return -1;
			}
			String aux = Integer.toBinaryString(byteRec);
			for(int i = aux.length()-1, j = 7; i >= 0; i--, j--){
				//System.out.println("i = "+ Integer.toString(i));
				//System.out.println("j = "+ Integer.toString(j));
				db[j] = aux.charAt(i) - 48; //48 é o 0 em ASCII;
			}
			
		}
		
		bitCounter++;
		return db[bitCounter-1];
		*/
	}

	public boolean endOfFile() {
		if(counter == texto.length()){
			return true;
		}
		return false;
	}
	
}
