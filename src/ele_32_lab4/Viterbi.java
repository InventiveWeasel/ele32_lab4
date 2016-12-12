package ele_32_lab4;

public class Viterbi {
	private  String [][] matrix = new String[64][64];
	private String texto = "";
	
	public void setString(String texto){
		this.texto = texto;
	}
	
	//Funcao para construir a trelica do decodificador
	private void setTrelica(){
		Codificador cod = new Codificador();
		
		//Inicializamos a matriz
		for (int i=0; i<64; i++){
			for (int j=0; i<64; i++){
				matrix[i][j]="";
			}
		}
		
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
			matrix[i][estado0] = transicao0;
	
			//Obtemos o próximo estado se a entrada for o 1
			cod.setEstado(binario);
			String transicao1 = cod.getSaida(1);
			int estado1 = cod.getEstado();
			matrix[i][estado1] = transicao1;
			
			System.out.println(i + " -> " + estado0 + ": " + "0/" + transicao0);
			System.out.println(i + " -> " + estado1 + ": " + "1/" + transicao1);
		}
		
	}
	
	public String[][] getTrelica(){
		setTrelica();
		return matrix;
	}
	
	public void algoritmoViterbi(){
		String[][] matrixVit = new String[64][1017];
		int[] custo = new int[64];
		
		for (int i=0; i<64; i++){
			for (int j=0; i<64; i++){
				matrixVit[i][j]="";
			}
			custo[i]=-1;
		}
		
		custo[0] = 0;
		int k = 0;
		int simbolo = Integer.parseInt(texto.substring(k, k+2));
		for(int j=0; j<64; j++){
			for (int i=0; i<64; i++){
				if(matrix[j][i] != ""){
					//
				}
					
			}
		}
		
				
	}
	
}
