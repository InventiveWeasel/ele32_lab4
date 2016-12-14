package ele_32_lab4;

import java.math.BigInteger;


public class Receptor {
	private String texto = "000ef12f1f5357c7e1ee9361a2431cef1cef2728fdee49462f29361764f7b7ef2abf6a60a4d699651e3d3a927d073b28293621ed2ff3f0ae8117b3496a9ba318";
	///Scrambler scram = new Scrambler();
	private String textoDecodificado="";
	private Viterbi viterbi = new Viterbi();
	
	
	public void decodificaString(){
		//Converte texto de hexa para bin
		String textoBin = new BigInteger(texto,16).toString(2);
		textoBin = "000000000000" + textoBin;
		///String headerOriginal = new BigInteger("e587f2dd",16).toString(2);
		
		//Busca pelo header do quadro
		/*for(int k=0; k<textoBin.length() - 32;){
			String headerInt = textoBin.substring(k,k+32);
			
			//int xor = headerOriginal ^ headerInt;
			String headerXOR = xor(headerOriginal,headerInt);
			
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
				*/
				//bitsDesembaralhados = bitsDesembaralhados.substring(32,2032);
				
				//Decodificação utilizando o algoritmo de Viterbi
				String mensDecod = viterbi.algoritmoViterbi(textoBin);
				//String xor = xor(texto, mensEnv);
				//int contaErros = 0;
				//for(int i=0; i < xor.length(); i++){
				//	if(xor.charAt(i)=='1')
				//		contaErros++;
				//}
				///k += 2032;
			///}
			///else
				///k++;
	
		///}
	
		
	}


	private String xor(String x, String y) {
		String result = "";
		int aux1, aux2;
		for(int i = 0; i<32;i++){
			aux1 = Character.getNumericValue(x.charAt(i));
			aux2 = Character.getNumericValue(y.charAt(i));
			result = result+(aux1^aux2);
		}
		return result;
	}
	

}
