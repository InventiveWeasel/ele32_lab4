package ele_32_lab4;

import java.io.IOException;
import java.math.BigInteger;



public class Receptor {
	
	private ReaderHex2Bin reader;
	private Reader readerOriginal;
	//private String texto = "000ef12f1f5357c7e1ee9361a2431cef1cef2728fdee49462f29361764f7b7ef2abf6a60a4d699651e3d3a927d073b28293621ed2ff3f0ae8117b3496a9ba318";
	private String texto ="";
	Scrambler scram = new Scrambler();
	private String textoDecodificado="";
	private Viterbi viterbi = new Viterbi();
	private String mensDecod="";
	private WriteOutput writer;
	private long totalTime = 0;
	
	
	public void decodificaString() throws IOException{
		reader = new ReaderHex2Bin("Augusto_Murilo.txt");
		readerOriginal = new Reader("theWarInTheAir.txt");
		String original = readerOriginal.getTexto();
		writer = new WriteOutput();
		texto = reader.getTexto();
		//texto = "e587f2dd9a038193917fc46a4659d5aff800b9704c68b6f4d467a2ba079a2d8777a7a4a421eb2f8b8f369b1ea38900cd554c61fd10dd3f87e80a40d01a3504cc2a19377c781b6a5a685708a27e2fc20a75efacd9bb5697bf34c42cd9978bda131476b8f510504c1589cb38a4b3106d294cb31a86705dc6f9dd7d3f2c36939260ebd3b84917eccc1fbdf98328e8cc8f4a3ae3b37c3254104f077a049e09d39912edbdab40236425a48cecf503f5549cc6603666d50db5c920de7ddc96378d21c78bda62d776be02a7f116e6b4ff5fcdf7ca2ae05b5700670530801c35262031b1b4cbafd2a5c6f11a5f51860393023dee2e8feb99bb4b7c915ceca6bb9d09";
		//Converte texto de hexa para bin
		String textoBin=texto;
		/*
		int i = 0;
		while(texto.charAt(i) == '0'){
			textoBin = textoBin + "0000";
			i++;
		}
		String sub = "";
		String aux = "";
		for(int k = 0; k < texto.length(); k++){
			System.out.println(k+" / "+texto.length());
			sub = texto.substring(k, k+1);
			
			switch(sub){
			case "0":
				aux = "0000";
				break;
			case "1":
				aux = "0001";
				break;
			case "2":
				aux = "0010";
				break;
			case "3":
				aux = "0011";
				break;
			case "4":
				aux = "0100";
				break;
			case "5":
				aux = "0101";
				break;
			case "6":
				aux = "0110";
				break;
			case "7":
				aux = "0111";
				break;
			case "8":
				aux = "1000";
				break;
			case "9":
				aux = "1001";
				break;
			case "a":
				aux = "1010";
				break;
			case "b":
				aux = "1011";
				break;
			case "c":
				aux = "1100";
				break;
			case "d":
				aux = "1101";
				break;
			case "e":
				aux = "1110";
				break;
			case "f":
				aux = "1111";
				break;
			}
			textoBin = textoBin+aux;
		}
		//textoBin = textoBin + new BigInteger(texto,16).toString(2);
		 */
		
		//System.out.println("Texto Processado");
		
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
				
				long startTime = System.currentTimeMillis();
				
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
				long stopTime = System.currentTimeMillis();
			    long elapsedTime = stopTime - startTime;
			    totalTime = totalTime+elapsedTime;
			      //System.out.println(elapsedTime);
				
				//System.err.println(mensDecod.length());
				
				k += 2032;
			}
			else
				k++;
		}
		//float erro = calculaErro(mensDecod, original);
		float erro = (float) viterbi.getCusto()/original.length();
		float mediaTempo = totalTime/(mensDecod.length()/1000);
		System.out.println("Erro: "+erro);
		System.out.println("Media de Tempo: "+ mediaTempo);
		writer.write(mensDecod);
	}


	private float calculaErro(String x, String y) {
		String xor = xor(x,y);
		int contaErros = 0;
		for(int i=0; i < xor.length(); i++){
			if(xor.charAt(i)=='1')
				contaErros++;
		}
		
		return (float) contaErros/x.length();
	}


	private String xor(String x, String y) {
		String result = "";
		int aux1, aux2;
		for(int i = 0; i<x.length();i++){
			aux1 = Character.getNumericValue(x.charAt(i));
			aux2 = Character.getNumericValue(y.charAt(i));
			result = result+(aux1^aux2);
		}
		return result;
	}
	

}
