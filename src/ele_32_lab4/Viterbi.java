package ele_32_lab4;

public class Viterbi {
	//private  String [][] matrix = new String[64][64];
	private String texto = "";
	private int NUM_STATES = 4;
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
			
			//Settamos o estado
			cod.setEstado(binario);
			
			//Obtemos o próximo estado se a entrada for o 0
			String transicao0 = cod.getSaida(0);
			int estado0 = cod.getEstado();
			//matrix[i][estado0] = transicao0;
			trelica[i] = new Estado();
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
	
	public void algoritmoViterbi(String input){
		texto = input;
		Estado[][] matrixVit = new Estado[NUM_STATES][1017];
		int[] custo = new int[NUM_STATES];
		int[] custoAnt = new int[NUM_STATES];
		
		for (int i=0; i<NUM_STATES; i++){
			for (int j=0; j<1017; j++){
				matrixVit[i][j]=new Estado();
				matrixVit[i][j].setId(j);
			}
			custo[i]=-1;
			custoAnt[i]=-1;
		}
		
		custo[0] = 0;
		int k = 0;
		int auxDist;
		int estado1, estado0;
		//int simbolo = Integer.parseInt(texto.substring(k, k+2),2);
		//String simbolo = texto.substring(k, k+2);
		String simbolo = texto.substring(k, k+3);
		boolean foiAlcancado[] = {false, false, false, false};
		foiAlcancado[0] = true;
		
		for(int i = 0; i < 1017;i++){
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
					matrixVit[estado0][i+1].addPrevState(trelica[j]);
					matrixVit[estado1][i+1].addPrevState(trelica[j]);
					
					
					System.out.println(trelica[j].getId()+" -> "+estado0);
					System.out.println(trelica[j].getId()+" -> "+estado1);
				}
				
				
				//Atualizando os caminhos
				//matrixVit[j][i].setTrans0(trelica[j].getTrans0(), estado0);
				//matrixVit[j][i].setTrans1(trelica[j].getTrans1(), estado1);
				//matrixVit[estado0][i+1].setTrans0(trelica[j].getTrans0(), j);
				//matrixVit[estado1][i+1].setTrans1(trelica[j].getTrans1(), j);
				
			}
			for(int j=0; j<NUM_STATES; j++){
				//Calculo da distancia mínima
				if(custo[j]!= -1){
					int min = 1000000, auxMin = 1000000, minState = -1;
					Estado auxState;
					//Iteramos sobre os estados anteriores que levam ao estado j na iteração i+1
					for(int m = 0; m < matrixVit[j][i+1].getNumberPrevSta(); m++){
						//Transicao com 0
						auxState = matrixVit[j][i+1].getPrevState(m);
						//Verficamos quais transiçoes foram utilizadas para chegar no mesmo
						if(auxState.getEstado0() == j){
							auxDist = calculateDist(simbolo, auxState.getTrans0()) + custoAnt[auxState.getId()];
							//auxMin = custo[auxState.getId()]+auxDist;
							
							if(m==0 || (auxDist < min) ){
								min = auxDist;
								minState = auxState.getId();
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
							}
						}
						
					}
					if(minState != -1){
						custo[j] = min;
						matrixVit[j][i+1].deleteAllPrev();
						matrixVit[j][i+1].addPrevState(trelica[minState]);
					}
				}
				
			}
			k = k+3;
			simbolo = texto.substring(k, k+3);
		}
		int min = 1000000;
		for(int i = 0; i < custo.length; i++){
			if (custo[i] < min){
				min = custo[i];
			}
		}
		System.out.println("custo minimo = "+ min);
					
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
