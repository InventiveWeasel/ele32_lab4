package ele_32_lab4;

import java.util.ArrayList;

public class Estado {
	private String trans0 = "", trans1 = "";
	private ArrayList<Estado> tempEst = new ArrayList<Estado>();
	private int estado0, estado1;
	private int id;
	
	public void setId(int ID){
		id = ID;
	}
	
	public int getId(){
		return id;
	}
	
	public Estado getPrevState(int i){
		return tempEst.get(i);
	}
	
	public int getNumberPrevSta(){
		return tempEst.size();
	}
	public void addPrevState(Estado estado){
		tempEst.add(estado);
	}
	public void deleteAllPrev(){
		tempEst.clear();
	}
	public int getEstado0(){
		return estado0;
	}
	
	public int getEstado1(){
		return estado1;
	}
	
	public void setEstado0(int estado){
		estado0 = estado;
	}
	
	public void setEstado1(int estado){
		estado1 = estado;
	}
	public String getTrans0(){
		return trans0;
	}
	
	public String getTrans1(){
		return trans1;
	}
	
	public void setTrans0(String trans, int estado){
		trans0 = trans;
		estado0 = estado;
	}
	
	public void setTrans1(String trans, int estado){
		trans1 = trans;
		estado1 = estado;
	}
	
}
