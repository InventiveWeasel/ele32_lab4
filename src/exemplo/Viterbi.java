package exemplo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

public class Viterbi {
	//private  String [][] matrix = new String[64][64];
	private String texto = "";
	private int NUM_STATES = 4;
	private int NUM_IT = 6;
	private Estado[] trelica = new Estado[NUM_STATES]; 
	
	public Viterbi(){
		setTrelica();
	}
	
	public void setString(String texto){
		this.texto = texto;
	}
	
	//Funcao para construir a trelica do decodificador
	private void setTrelica(){
		Codificador2 cod = new Codificador2();
		
		//Inicializamos a matriz
		/*for (int i=0; i<64; i++){
			for (int j=0; i<64; i++){
				matrix[i][j]="";
			}
		}
		*/
		
		for(int i=0; i<NUM_STATES; i++){
			//String de 6 bits que representa o estado do codificador
			String binario = Integer.toBinaryString(i);
			for(; binario.length() < 2;)
				binario = "0" + binario;
			/*String id = "";
			for(int j = binario.length(); j > 0; j--){
				id = id + binario.charAt(j-1);
			}
			*/
			//Settamos o estado
			cod.setEstado(binario);
			
			//Obtemos o próximo estado se a entrada for o 0
			String transicao0 = cod.getSaida(0);
			int estado0 = cod.getEstado();
			//matrix[i][estado0] = transicao0;
			trelica[i] = new Estado();
			///trelica[i].setId(Integer.parseInt(id,2));
			trelica[i].setId(i);
			trelica[i].setTrans0(transicao0, estado0);
	
			//Obtemos o próximo estado se a entrada for o 1
			cod.setEstado(binario);
			String transicao1 = cod.getSaida(1);
			int estado1 = cod.getEstado();
			//matrix[i][estado1] = transicao1;
			trelica[i].setTrans1(transicao1, estado1);
			
			System.out.println(i + " -> " + estado0 + ": " + "0/" + transicao0);
			System.out.println(i + " -> " + estado1 + ": " + "1/" + transicao1);
		}
		
	}
	
	public Estado[] getTrelica(){
		setTrelica();
		return trelica;
	}
	
	public String algoritmoViterbi(String input){
		texto = input;
		Estado[][] matrixVit = new Estado[NUM_STATES][1017];
		int[] custo = new int[NUM_STATES];
		int[] custoAnt = new int[NUM_STATES];
		
		for (int i=0; i<NUM_STATES; i++){
			for (int j=0; j<1017; j++){
				matrixVit[i][j]=new Estado();
				matrixVit[i][j].setId(i);
			}
			custo[i]=-1;
			custoAnt[i]=-1;
		}
		
		custo[0] = 0;
		int k = 0;
		int auxDist;
		int estado1, estado0;
		//int simbolo = Integer.parseInt(texto.substring(k, k+3),2);
		//String simbolo = texto.substring(k, k+2);
		String simbolo = texto.substring(k, k+3);
		
		//Inicializando o vetor
		boolean foiAlcancado[] = new boolean[NUM_STATES];
		for(int i = 0; i < foiAlcancado.length; i++)
			foiAlcancado[i] = false;
		foiAlcancado[0] = true;
		
		for(int i = 0; i < NUM_IT;i++){
			//Guardar valores antes de serem alterados
			guardarCustos(custo, custoAnt);
			System.out.println("Iteracao: "+i+1);
			for(int j=0; j<NUM_STATES; j++){
				if(custo[j] != -1)
					foiAlcancado[j] = true;
					
			}
			for(int j=0; j<NUM_STATES; j++){
				if(foiAlcancado[j]){
					estado0 = trelica[j].getEstado0();
					estado1 = trelica[j].getEstado1();
					//Inicializando os valores das novas transições acrescentadas com zero
					if(custo[estado0] == -1)
						custo[estado0] = 0;
					if(custo[estado1] == -1)
						custo[estado1] = 0;
					
					//Atualizando os caminhos
					if(i+1 < NUM_IT){
						matrixVit[estado0][i+1].addPrevState(trelica[j]);
						matrixVit[estado1][i+1].addPrevState(trelica[j]);
					}
					
					
					//System.out.println(trelica[j].getId()+" -> "+estado0);
					//System.out.println(trelica[j].getId()+" -> "+estado1);
				}
				
				
				//Atualizando os caminhos
				//matrixVit[j][i].setTrans0(trelica[j].getTrans0(), estado0);
				//matrixVit[j][i].setTrans1(trelica[j].getTrans1(), estado1);
				//matrixVit[estado0][i+1].setTrans0(trelica[j].getTrans0(), j);
				//matrixVit[estado1][i+1].setTrans1(trelica[j].getTrans1(), j);
				
			}
			for(int j=0; j < NUM_STATES; j++){
				//Calculo da distancia mínima
				if(custo[j]!= -1){
					int min = 1000000, auxMin = 1000000, minState = -1;
					int oneOrZero = 0;
					Estado auxState;
					//Iteramos sobre os estados anteriores que levam ao estado j na iteração i+1
					
					
					for(int m = 0; m < matrixVit[j][i+1].getNumberPrevSta(); m++){
						//Transicao com 0
						auxState = matrixVit[j][i+1].getPrevState(m);
						//Verficamos quais transiçoes foram utilizadas para chegar no mesmo estado j
						if(auxState.getEstado0() == j){
							auxDist = calculateDist(simbolo, auxState.getTrans0()) + custoAnt[auxState.getId()];
							//auxMin = custo[auxState.getId()]+auxDist;
							
							if(m==0 || (auxDist < min) ){
								min = auxDist;
								minState = auxState.getId();
								oneOrZero = 0;
							}
						}
						
						
						//Transicao com 1
						if(auxState.getEstado1() == j){
							//auxDist = calculateDist(simbolo, auxState.getTrans1());
							auxDist = calculateDist(simbolo, auxState.getTrans1()) + custoAnt[auxState.getId()];
							//auxMin = custo[auxState.getId()]+auxDist;
							if(auxDist < min){
								min = auxDist;
								minState = auxState.getId();
								oneOrZero = 1;
							}
						}
						
					}
					if(minState != -1){
						custo[j] = min;
						matrixVit[j][i+1].deleteAllPrev();
						matrixVit[j][i+1].addPrevState(trelica[minState]);
						matrixVit[j][i+1].setTransPrev(oneOrZero);
						System.out.println("prev state de "+j+" é: "+minState+". A transição é "+oneOrZero);
						System.out.println("prev state de "+j+" é: "+matrixVit[j][i+1].getPrevState(0).getId()+". A transição é "+oneOrZero);
					}
				}
				
			}
			k = k+3;
			if(k < texto.length())
				simbolo = texto.substring(k, k+3);
		}
		int min = 1000000;
		int minIndex = 0;
		for(int i = 0; i < custo.length; i++){
			if (custo[i] < min){
				min = custo[i];
				minIndex = i;
			}
		}
		System.out.println("custo minimo = "+ min);
		//Agora que temos os custos, devemos encontrar a mensagem enviada
		ArrayList<String> mensEnviada = new ArrayList<String>();
		ArrayList<String> mensSent = new ArrayList<String>();
		Estado auxState = null;
		for(int i = NUM_IT; i > 1; i--){
			auxState = matrixVit[minIndex][i-1].getPrevState(0);
			if(matrixVit[minIndex][i-1].getTransPrev() == 0){
				mensEnviada.add(auxState.getTrans0());
				mensSent.add("0");
			}
			if(matrixVit[minIndex][i-1].getTransPrev() == 1){
				mensEnviada.add(auxState.getTrans1());
				mensSent.add("1");
			}
			minIndex = auxState.getId();
		}
		
		Collections.reverse(mensEnviada);
		Collections.reverse(mensSent);
		
		
		String mens = "", mensEnv = "";
		for(int j = 0; j < mensSent.size(); j++)
			mens = mens + mensSent.get(j);
		for(int j = 0; j < mensEnviada.size(); j++)
			mensEnv = mensEnv+mensEnviada.get(j);
		
		
		System.out.println("A mensagem é:"+mensEnviada);
		System.out.println("Comparando:");
		System.out.println("A mensagem é:"+mensEnv);
		System.out.println("A mensagem é:"+mens.length()+"   "+mens);
		
		String original = "00000010000010000001100001000000101000011000001110001000000100100010100001011000011100011000001101101011000000010001110000010000011110000011001000000100010001001001001000001100001000000011100001110000110000100100100111011011000100100011110010100000101100010110010101000100100001000001011101111100101100010100001011100101111010010001011000010010010001001010000011110000110001100000010010001100101100110011010000000111100001001000110100111110001101111000100110001000000000110000000110000100110001011000001110110101000001010011011000101111100001010011110010001101100000100010100010000001100001010000010000000101110000100011100010101111110001110011010101001100100010100000001101000010010010100000010010001110000000111000010110111000001110010000010010001101101011101000101110001000101100011101010100000111100000111011101100001000000001011000111010110101000010011011001001010010000011110010100100000100001110001000111010000011100001111101111100001101001110001110001111100010010000101000001001000101110001101000010101011101100010111110010010001101100001010000111000001101010011101101011000000010001110110100100000000100010100100011110001000110101011101101110001000010010010110000010000000100000010000000001111001001110101010011000100000010000010101000011110111111100011111000100010001000111100110110011011101111000010000100001001011101101100011110111101000111101101000100110111011011110111001000110111110010011000011011011101010101000010101010000010111000000111000000100000010001011111100001111010001010011000100000000000111011101011010101011001011111010011001000001000010110100010011101000011100001010011000011101001111101011111000010101101010010111011010110001011111011111110011001110000010001011101100001010110001011000010000001001100111001101001010110101000000111000100001010101001000001001010110111100011000100010100011111011010111000100101010010101010000100010000000100000011101000000011000010001100000110010111110010100000000011101001100011011001011100011001000000101010001010011011000010101101010001010001101111010100110000011001011000000000100011";
		String xor2 = xor(mens, original.substring(0, mens.length()));
		int contaErros2 = 0;
		for(int i=0; i < xor2.length(); i++){
			if(xor2.charAt(i)=='1')
				contaErros2++;
		}
		System.out.println("Erros = "+contaErros2);
		System.out.println("Original: "+original);
		System.out.println("Recebida: "+mens);
		return mens;
	}
	
	private void guardarCustos(int[] custo, int[] custoAnt) {
		for(int i = 0; i < NUM_STATES; i++){
			custoAnt[i] = custo[i];
		}
		
	}

	private int calculateDist(String x, String y){
		String result = xor(x,y);
		int dist = 0;
		for(int i = 0; i < result.length(); i++){
			if(result.charAt(i)=='1'){
				dist++;
			}
		}
		return dist;
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
