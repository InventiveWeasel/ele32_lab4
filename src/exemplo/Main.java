package exemplo;

public class Main {
	

	public static void main(String[] args){
		//Receptor rec = new Receptor();
		//rec.decodificaString();
		Viterbi vit = new Viterbi();
		vit.algoritmoViterbi("000101110000000");
	}
	
}
