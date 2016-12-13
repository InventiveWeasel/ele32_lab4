package ele_32_lab4;

public class Codificador2 {
	int[] estados = new int[2];
	int saida1 = 0;
	int saida2 = 0;
	int saida0 = 0;
	int contador = 0;
	
	public Scrambler scram;
	
	public Codificador2(){
		resetEstados();
		scram = new Scrambler();
	}
	
	public void resetEstados(){
		estados[1] = 0;
		estados[0] = 0;
	}
	
	public void setEstado(String estado){
		estados[1] = Character.getNumericValue(estado.charAt(0));
		estados[0] = Character.getNumericValue(estado.charAt(1));
	}
	
	public int getEstado(){	
		String estadosString = Integer.toString(estados[1]) + Integer.toString(estados[0]);
		return Integer.parseInt(estadosString, 2);
	}
	
	public String getSaida(int entrada){
		contador++;
		saida0 = entrada;
		saida1 = ((entrada + estados[1] + estados[0])%2 + estados[1])%2;
		//saida1 = (saida1 + scram.getBit())%2;
		saida2 = ((entrada + estados[1] + estados[0])%2 + estados[0])%2;
		//saida2 = (saida2 + scram.getBit())%2;
		
		if(contador == 1000){
			resetEstados();
			contador = 0;
		}
		else{
			int aux = (entrada + estados[1] + estados[0])%2;
			estados[1] = estados[0];
			estados[0] = aux;
		}
		
		return Integer.toString(saida0) + Integer.toString(saida1) + Integer.toString(saida2);
	}
}
