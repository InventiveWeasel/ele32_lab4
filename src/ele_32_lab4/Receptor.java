package ele_32_lab4;

import java.math.BigInteger;


public class Receptor {
	private String texto = "e587f2dd9a038193917fc46a4659d5aff8d461c2cda88045426808c3194833b7d4b2414740496f2b1024bedf538cdf9e37697a12dbb4b307c16213bf7c6268aa16de39839d3811c57732e037534a569ece88e9ef4d063b22028d0fa205357b891917653e3e17f5dfb5ee5c177e14ea9d2aeabcdfedce2dc477405b2301e7e3a50f4736cca8e3ce5eb0774fa30e36e8f970e1570796584f7f86c8c11f28e90b6fd2c567dfe599b4b11b2893b6c855cae8ead817e8b3935b5def23bad543977482c2700c6da828b12d30575a805f7f43ce5b610525d4e7585148a3a767b940e4f1fba966880d30eb3d3dfd518bfc5cc5f38ee84ccbc69b2ed773f317be5b0f";
	Scrambler scram = new Scrambler();
	private String textoDecodificado="";
	private Viterbi viterbi = new Viterbi();
	
	
	public void decodificaString(){
		//Converte texto de hexa para bin
		String textoBin = new BigInteger(texto,16).toString(2);
		String headerOriginal = new BigInteger("e587f2dd",16).toString(2);
		
		//Busca pelo header do quadro
		for(int k=0; k<textoBin.length() - 32;){
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
				
				//Decodificação utilizando o algoritmo de Viterbi
				viterbi.algoritmoViterbi(bitsDesembaralhados);
				
				
				
				
				k += 2032;
			}
			else
				k++;
	
		}
	
		
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
