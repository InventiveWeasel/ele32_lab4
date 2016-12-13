package ele_32_lab4;

public class Viterbi {
	//private  String [][] matrix = new String[64][64];
	private Estado[] trelica = new Estado[64]; 
	private String texto = "";
	
	public Viterbi(){
		setTrelica();
	}
	
	public void setString(String texto){
		this.texto = texto;
	}
	
	//Funcao para construir a trelica do decodificador
	private void setTrelica(){
		Codificador cod = new Codificador();
		
		//Inicializamos a matriz
		/*for (int i=0; i<64; i++){
			for (int j=0; i<64; i++){
				matrix[i][j]="";
			}
		}
		*/
		
		for(int i=0; i<64; i++){
			//String de 6 bits que representa o estado do codificador
			String binario = Integer.toBinaryString(i);
			for(; binario.length() < 6;)
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
		Estado[][] matrixVit = new Estado[64][1017];
		int[] custo = new int[64];
		
		for (int i=0; i<64; i++){
			for (int j=0; j<64; j++){
				matrixVit[i][j]=new Estado();
				matrixVit[i][j].setId(j);
			}
			custo[i]=-1;
		}
		
		custo[0] = 0;
		int k = 0;
		int auxDist;
		int estado1, estado0;
		//int simbolo = Integer.parseInt(texto.substring(k, k+2),2);
		String simbolo = texto.substring(k, k+2);
		
		for(int i = 0; i < 1017;i++){
			System.out.println("Iteracao: "+i+1);
			for(int j=0; j<64; j++){
				if(custo[j] != -1){
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
			for(int j=0; j<64; j++){
				//Calculo da distancia mínima
				if(custo[j]!= -1){
					int min = 1000000, auxMin, minState = -1;
					Estado auxState;
					for(int m = 0; m < matrixVit[j][i+1].getNumberPrevSta(); m++){
						//Transicao com 0
						//auxDist = calculateDist(simbolo, trelica[j].getTrans0());
						auxState = matrixVit[j][i+1].getPrevState(m);
						if(auxState.getEstado0() == j){
							auxDist = calculateDist(simbolo, auxState.getTrans0());
							auxMin = custo[auxState.getId()]+auxDist;
							
							if(m==0 || (auxMin < min) ){
								min = auxMin;
								minState = auxState.getId();
							}
						}
						
						
						//Transicao com 1
						if(auxState.getEstado1() == j){
							auxDist = calculateDist(simbolo, auxState.getTrans1());
							auxMin = custo[auxState.getId()]+auxDist;
							if(auxMin < min){
								min = auxMin;
								minState = auxState.getId();
							}
						}
						if(minState != -1){
							custo[j] = custo[minState] + min;
							matrixVit[j][i+1].deleteAllPrev();
							matrixVit[j][i+1].addPrevState(trelica[minState]);
						}
					}
				}
				
			}
		}
		int min = 1000000;
		for(int i = 0; i < custo.length; i++){
			if (custo[i] < min){
				min = custo[i];
			}
		}
		System.out.println("custo minimo = "+ min);
					
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
