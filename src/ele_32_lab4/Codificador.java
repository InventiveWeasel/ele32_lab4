package ele_32_lab4;

public class Codificador{
	
	int[] estados = new int[6];
	int saida1 = 0;
	int saida2 = 0;
	int contador = 0;
	
	public Scrambler scram;
	
	public Codificador(){
		resetEstados();
		scram = new Scrambler();
	}
	
	public void resetEstados(){
		estados[5] = 0;
		estados[4] = 0;
		estados[3] = 0;
		estados[2] = 0;
		estados[1] = 0;
		estados[0] = 0;
	}
	
	public void setEstado(String estado){
		estados[5] = Character.getNumericValue(estado.charAt(5));
		estados[4] = Character.getNumericValue(estado.charAt(4));
		estados[3] = Character.getNumericValue(estado.charAt(3));
		estados[2] = Character.getNumericValue(estado.charAt(2));
		estados[1] = Character.getNumericValue(estado.charAt(1));
		estados[0] = Character.getNumericValue(estado.charAt(0));
	}
	
	public int getEstado(){	
		String estadosString = Integer.toString(estados[5]) + Integer.toString(estados[4]) + Integer.toString(estados[3]) + Integer.toString(estados[2]) + Integer.toString(estados[1]) + Integer.toString(estados[0]);
		return Integer.parseInt(estadosString, 2);
	}
	
	public String getSaida(int entrada){
		contador++;
		saida1 = (((((entrada + estados[0])%2) + estados[1])%2 + estados[2])%2 + estados[5])%2;
		saida1 = (saida1 + scram.getBit())%2;
		saida2 = (((((entrada + estados[1])%2) + estados[2])%2 + estados[4])%2 + estados[5])%2; 
		saida2 = (saida2 + scram.getBit())%2;
		
		if(contador == 1000){
			resetEstados();
			contador = 0;
		}
		else{
			estados[5] = estados[4];
			estados[4] = estados[3];
			estados[3] = estados[2];
			estados[2] = estados[1];
			estados[1] = estados[0];
			estados[0] = entrada;
		}
		
		return Integer.toString(saida1) + Integer.toString(saida2);
	}
}
