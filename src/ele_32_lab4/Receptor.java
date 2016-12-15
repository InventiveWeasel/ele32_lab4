package ele_32_lab4;

import java.io.IOException;
import java.math.BigInteger;



public class Receptor {
	
	private Reader reader;
	//private String texto = "000ef12f1f5357c7e1ee9361a2431cef1cef2728fdee49462f29361764f7b7ef2abf6a60a4d699651e3d3a927d073b28293621ed2ff3f0ae8117b3496a9ba318";
	private String texto ="";
	Scrambler scram = new Scrambler();
	private String textoDecodificado="";
	private Viterbi viterbi = new Viterbi();
	private String mensDecod="";
	private WriteOutput writer;
	
	
	public void decodificaString() throws IOException{
		reader = new Reader("entrada.txt");
		writer = new WriteOutput();
		texto = reader.getTexto();
		//texto = "e587f2dd9a038193917fc46a4659d5aff800b9704c68b6f4d467a2ba079a2d8777a7a4a421eb2f8b8f369b1ea38900cd554c61fd10dd3f87e80a40d01a3504cc2a19377c781b6a5a685708a27e2fc20a75efacd9bb5697bf34c42cd9978bda131476b8f510504c1589cb38a4b3106d294cb31a86705dc6f9dd7d3f2c36939260ebd3b84917eccc1fbdf98328e8cc8f4a3ae3b37c3254104f077a049e09d39912edbdab40236425a48cecf503f5549cc6603666d50db5c920de7ddc96378d21c78bda62d776be02a7f116e6b4ff5fcdf7ca2ae05b5700670530801c35262031b1b4cbafd2a5c6f11a5f51860393023dee2e8feb99bb4b7c915ceca6bb9d09";
		//Converte texto de hexa para bin
		String textoBin="";
		int i = 0;
		while(texto.charAt(i) == '0'){
			textoBin = textoBin + "0000";
			i++;
		}
		
		textoBin = textoBin + new BigInteger(texto,16).toString(2);
		
		String headerOriginal = new BigInteger("e587f2dd",16).toString(2);
		
		//Busca pelo header do quadro
		for(int k=0; k<textoBin.length() - 32;){
			String headerInt = textoBin.substring(k,k+32);
			
			
			//int xor = headerOriginal ^ headerInt;
			String headerXOR = xor(headerOriginal,headerInt);
			
			//Conta os erros relativos ao header
			int contaErros = 0;
			for(i=0; i < headerXOR.length(); i++){
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
				
				//bitsDesembaralhados = bitsDesembaralhados.substring(32,2032);
				
				//Decodificação utilizando o algoritmo de Viterbi
				//String xor = xor(texto, mensEnv);
				//int contaErros = 0;
				//for(int i=0; i < xor.length(); i++){
				//	if(xor.charAt(i)=='1')
				//		contaErros++;
				//}
				mensDecod = mensDecod + viterbi.algoritmoViterbi(bitsDesembaralhados.substring(32,2032));
				
				System.err.println(mensDecod.length());
				k += 2032;
			}
			else
				k++;
		}
		writer.write(mensDecod);
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
