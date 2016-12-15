package ele_32_lab4;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

public class Viterbi {
	private String texto = "";
	private int NUM_STATES = 64;
	private int NUM_IT = 1001;
	private Estado[] trelica = new Estado[NUM_STATES]; 
	private int custoTotal = 0;
	
	public Viterbi(){
		setTrelica();
	}
	
	public void setString(String texto){
		this.texto = texto;
	}
	
	//Funcao para construir a trelica do decodificador
	private void setTrelica(){
		CodificadorOriginal cod = new CodificadorOriginal();
		
		for(int i=0; i<NUM_STATES; i++){
			
			//String de 6 bits que representa o estado do codificador
			String binario = Integer.toBinaryString(i);
			for(; binario.length() < 6;)
				binario = "0" + binario;
			
			//for(int j = binario.length(); j < 0; j--){
			//	id = id + binario.charAt(j-1)
			//}
			//Settamos o estado
			cod.setEstado(binario);
			
			//Obtemos o próximo estado se a entrada for o 0
			String transicao0 = cod.getSaida(0);
			int estado0 = cod.getEstado();
			
			
			trelica[i] = new Estado();
			trelica[i].setId(i);
			trelica[i].setTrans0(transicao0, estado0);
	
			//Obtemos o próximo estado se a entrada for o 1
			cod.setEstado(binario);
			String transicao1 = cod.getSaida(1);
			int estado1 = cod.getEstado();
			trelica[i].setTrans1(transicao1, estado1);
			
			//System.out.println(i + " -> " + estado0 + ": " + "0/" + transicao0);
			//System.out.println(i + " -> " + estado1 + ": " + "1/" + transicao1);
		}
		
		for(int i=0; i<NUM_STATES; i++){
			int j = +32;
			//System.out.println(i + " -> " + trelica[i].getEstado0() + ": " + "0/" + trelica[i].getTrans0()+"\t"+j + " -> " + trelica[j].getEstado0() + ": " + "0/" + trelica[j].getTrans0());
			//System.out.println(i + " -> " + trelica[i].getEstado1() + ": " + "1/" + trelica[i].getTrans1()+"\t"+j + " -> " + trelica[j].getEstado1() + ": " + "1/" + trelica[j].getTrans1());
		}
		
		
	}
	
	public Estado[] getTrelica(){
		setTrelica();
		return trelica;
	}
	
	public String algoritmoViterbi(String input){
		texto = input;
		Estado[][] matrixVit = new Estado[NUM_STATES][1018];
		int[] custo = new int[NUM_STATES];
		int[] custoAnt = new int[NUM_STATES];
		
		//Inicialização
		for (int i=0; i<NUM_STATES; i++){
			for (int j=0; j<NUM_IT; j++){
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
		
		String simbolo = texto.substring(k, k+2);
		
		//Inicializando "foiAlcancado" o vetor com false's
		//Um estado foi alcancado se existe um caminho que chega até ele
		boolean foiAlcancado[] = new boolean[NUM_STATES];
		for(int i = 0; i < foiAlcancado.length; i++)
			foiAlcancado[i] = false;
		foiAlcancado[0] = true;
		
		for(int i = 0; i < NUM_IT-1;i++){
			//Guardar valores antes de serem alterados
			guardarCustos(custo, custoAnt);
			
			//System.out.println("Iteracao: "+i);
			String custoaux = "";
			for(int j=0; j<NUM_STATES; j++){
				custoaux = custoaux+custo[j] +" ";
				if(custo[j] != -1)
					foiAlcancado[j] = true;
					
			}
			//System.out.println("Iteraçao "+i);
			//System.out.println(custoaux);
			//System.out.println(simbolo);
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
			for(int j=0; j<NUM_STATES; j++){
				//Calculo da distancia mínima
				if(custo[j]!= -1){
					int min = 1000000, auxMin = 1000000, minState = -1;
					int oneOrZero = 0;
					Estado auxState;
					//Iteramos sobre os estados anteriores que levam ao estado j na iteração i+1
					
					int x;
					try{
						x = matrixVit[j][i+1].getNumberPrevSta();
					}catch(NullPointerException e){
						System.out.println("ex");
					}
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
						int h = i+1;
						//System.out.println("prev state de "+j+" é: "+minState+". A transição é "+oneOrZero);
						//System.out.println("prev state de "+j+" é: "+matrixVit[j][i+1].getPrevState(0).getId()+". A transição é "+oneOrZero);
					}
				}
				
			}
			k = k+2;
			if(k < texto.length())
				simbolo = texto.substring(k, k+2);
		}
		int min = 1000000;
		int minIndex = 0;
		int [] custosMin = new int[64];
		int x = 0;
		for(int i = 0; i < custo.length; i++){
			if (custo[i] < min){
				min = custo[i];
				minIndex = i;
			}
		}
		for(int i = 0; i < custo.length; i++){
			if(custo[i] == min){
				custosMin[x] = i;
				x++;
			}
		}
		custoTotal = custoTotal + min;
		//System.out.println("custo minimo = "+ min);
		//Agora que temos os custos, devemos encontrar a mensagem enviada
		ArrayList<String> mensEnviada = new ArrayList<String>();
		ArrayList<String> mensSent = new ArrayList<String>();
		Estado auxState = null;
		/*
		for(int i = NUM_IT-1; i > 0; i--){
			auxState = matrixVit[minIndex][i].getPrevState(0);
			if(matrixVit[minIndex][i].getTransPrev() == 0){
				mensEnviada.add(auxState.getTrans0());
				mensSent.add("0");
			}
			if(matrixVit[minIndex][i].getTransPrev() == 1){
				mensEnviada.add(auxState.getTrans1());
				mensSent.add("1");
			}
			minIndex = auxState.getId();
		}
		*/
		//for(int j = 0; j < x; j++){
			//minIndex = custosMin[j];
		String mens = "";
			for(int i = NUM_IT-1; i > 0; i--){
				int o = i-1;
				//System.out.println("Iteração: "+o);
				
				auxState = matrixVit[minIndex][i].getPrevState(0);
				if(matrixVit[minIndex][i].getTransPrev() == 0){
					mensEnviada.add(auxState.getTrans0());
					mensSent.add("0");
				}
				if(matrixVit[minIndex][i].getTransPrev() == 1){
					mensEnviada.add(auxState.getTrans1());
					mensSent.add("1");
				}
				//System.out.println(mensSent.get(NUM_IT-1-i) + " -> "+ mensEnviada.get(NUM_IT-1-i));
				minIndex = auxState.getId();
			}
			Collections.reverse(mensSent);
			for(int y = 0; y < mensSent.size(); y++)
				mens = mens + mensSent.get(y);
			//System.out.println("mens min "+j+": "+ mens);
			mensSent.clear();
			
		Collections.reverse(mensEnviada);
		///Collections.reverse(mensSent);
		
		
		String mensEnv = "";
		//for(int j = 0; j < mensSent.size(); j++)
			///mens = mens + mensSent.get(j);
		for(int j = 0; j < mensEnviada.size(); j++)
			mensEnv = mensEnv+mensEnviada.get(j);
		
		
		//mensEnv = new BigInteger(mensEnv,2).toString(16);
		String texto = "e587f2dd9a038193917fc46a4659d5aff8d461c2cda88045426808c3194833b7d4b2414740496f2b1024bedf538cdf9e37697a12dbb4b307c16213bf7c6268aa16de39839d3811c57732e037534a569ece88e9ef4d063b22028d0fa205357b891917653e3e17f5dfb5ee5c177e14ea9d2aeabcdfedce2dc477405b2301e7e3a50f4736cca8e3ce5eb0774fa30e36e8f970e1570796584f7f86c8c11f28e90b6fd2c567dfe599b4b11b2893b6c855cae8ead817e8b3935b5def23bad543977482c2700c6da828b12d30575a805f7f43ce5b610525d4e7585148a3a767b940e4f1fba966880d30eb3d3dfd518bfc5cc5f38ee84ccbc69b2ed773f317be5b0f";
		//String texto = "4fcec99aee587f2dd9a038193917fc46a4659d5aff8d4627ebd7c5baa509912e9eca97b024188b53d727f403a5da2dba764c0c8860df363affcf1aa9deca94e4b99593a77cd1950514b427e10958a3c5233926b5de4dce2ab3c9f016d317c1e75dab3519eae40fff8a3bcb881a2ea5b582d1eb9be5c79da4055b6734c8ac1f7f538466c60e143d06b9f7dc06a21834e595e82e2115231d1934a0b4da44dfb1e37b577da4355a4ec66be7f2f16f98798091132dbf1491407e9a4555dbd090b389f67ef45444b5183d1fd171500be02f88701c9544806f243c1972b4fa0200a4acf2d663caa6cc1a1d81d398744986ad814a216c903e6cb950c16aae75c96be587f2dd7e8548ce0f6fce706529336076a2180c4f7cce9a213d57db6ecf42ac4cdede74e55f68a1e591d864e8368e212ab792858bdc1772173c777948e7fb630ee52f25e52442ffca533aa140bbb1cb418b2d566e370a3c02d0f93e69dafec0ab633bede6d74ab5800794a03b0c59137a3f493de3d896ccdf3a7d521f4f3876643fc9e0b22c78ddc9ad78b427df";
		//String texto = "495800f9ace22e587f0cd9a23819391ffc07a4659d5afc8d462fe3c7c5baa50d93269e4a97f024188b53c7a7f403a5da6dba664c1c88605f363affcf1aa9feca9464999593a77cd1958554b427e9495833d5233926f1df4dce2ab3c9e016f316c1635eab3409eae40fef9a3ac3889a2e8594c251eb9b45c79da4055b6735c8ec1f7f540466c60e143d07b1b75806a258346d9ce02e2155231d1934b0b6d244dfb1ab7b5f7da43d424ec66b67faf16f18798091132dbf1491407e9a0455dbd090b388f67ae4544495181d0fd571502be02f88701c9544802fb53c1972bcbe0600a6acf2d673ca26cd0a1c81d390744982ad814a216c903e64b150c162ae75896bc587f2dd706c45a9336056a218084f7cce9a213d57db4ecfc2ae4edede74e55f6ca5e591d864e8348c212ab792858bdc17f2553c777d40e7fb630ce56f25e52402ffca533aa048bbb1cba10b2d566e270a3c03d8f13e497afec08b6b3bede4d74ab5801796a27b2c5d137a3f493de2d8d6ccdf3a7d521f4f3836643fc9e0923c78ddc9ad58b427df744d8a2c758f65b3e35f37d2c11e9e66cce2527434b798c0271d21a8305d92b060d7eae87154a8203c42a0d9b68e5d2b1036818d898f7f5392a1250192579474cbe95314eeeb00";
		texto = new BigInteger(texto,16).toString(2);
		//texto = texto.substring(32,2032);
		/*
		String xor = xor(texto, mensEnv);
		int contaErros = 0;
		for(int i=0; i < xor.length(); i++){
			if(xor.charAt(i)=='1')
				contaErros++;
		}
		*/
		//System.out.println("A mensagem é:"+mensEnviada);
		//System.out.println("Comparando:");
		//System.out.println("A original é:"+texto);
		//System.out.println("A mensagem é:"+mensEnv);
		///System.out.println("O numero de erros é: "+contaErros);
		//System.out.println("A mensagem é:"+mens.length()+"   "+mens);
		
		//String original = input;
		//String xor2 = xor(mens, original.substring(0, mens.length()));
		//int contaErros2 = 0;
		//for(int i=0; i < xor2.length(); i++){
		//	if(xor2.charAt(i)=='1')
		//		contaErros2++;
		//}
		//System.out.println("Erros = "+contaErros2);
		//System.out.println("Original: "+original);
		//System.out.println("Recebida: "+mens);
		//System.out.println("Decodificada: "+mens);
		return mens;
	}
	
	public int getCusto(){
		return custoTotal;
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