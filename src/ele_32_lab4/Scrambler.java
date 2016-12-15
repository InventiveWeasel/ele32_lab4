package ele_32_lab4;

public class Scrambler {
	private int[] states;
	private int[] auxStates;
	private int contador;
	
	public Scrambler(){
		states = new int[8];
		auxStates = new int[8];
		contador = 0;
		for(int i = 0; i < states.length; i++)
			states[i] = 1;
	}
	
	public int getBit(){
		if(contador == 2000){
			reset();
		}
		System.arraycopy(states, 0, auxStates, 0, states.length);
		states[0] = (auxStates[7]+auxStates[4]+auxStates[2]+auxStates[0])%2;
		for(int i = 1; i < states.length ; i++){
			states[i] = auxStates[i-1];
		}
		
		contador++;
		return auxStates[7];
	}
	
	public void reset(){
		for(int i = 0; i < states.length; i++)
			states[i] = 1;
	}
	
}
