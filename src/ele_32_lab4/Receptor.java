package ele_32_lab4;

import java.math.BigInteger;


public class Receptor {
	private String texto = "";
	Scrambler scram = new Scrambler();
	private String textoDecodificado="";
	
	public void decodificaString(){
		//Converte texto de hexa para bin
		String textoBin = new BigInteger(texto,16).toString(2);
		int headerOriginal = Integer.parseInt("e587f2dd",16);
		
		//Busca pelo header do quadro
		for(int k=0; k<texto.length() - 32;){
			int headerInt = Integer.parseInt(textoBin.substring(k,k+32),2);
			int xor = headerOriginal ^ headerInt;
			String headerXOR = Integer.toBinaryString(xor);
			
			//Conta os erros relativos ao header
			int contaErros = 0;
			for(int i=0; i < headerXOR.length(); i++){
				if(headerXOR.charAt(i)=='1')
					contaErros++;
				if(contaErros > 4)
					break;
			}
			
			//Caso seja tolerável a quantidade de erros, encontramos o header
			//e decodificamos o bloco
			if(contaErros <= 4){
				//Desambaralhar bits
				String seqBits = textoBin.substring(k,k+2032);
				String bitsDesembaralhados="";

				scram.reset();
				for(int j=0; j<seqBits.length();j++){
					int bitdesemb = (scram.getBit() + Character.getNumericValue(seqBits.charAt(j)))%2;
					bitsDesembaralhados += Integer.toString(bitdesemb);
				}
				
				//Bits desembaralhados
				
				
				
				
				k += 2032;
			}
			else
				k++;
	
		}
	
		
	}
	

}
