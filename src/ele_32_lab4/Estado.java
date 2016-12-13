package ele_32_lab4;

public class Estado {
	private String trans0 = "", trans1 = "";
	private int estado0, estado1;
	
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
	
	public void setTrans0(String trans){
		trans0 = trans;
	}
	
	public void setTrans1(String trans){
		trans1 = trans;
	}
	
}
