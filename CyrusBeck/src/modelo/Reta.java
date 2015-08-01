package modelo;

public class Reta {
	public Vetor2D p1,p2;
	public boolean visivel;
	
	public Reta(Vetor2D p1, Vetor2D p2){
		this.p1 = p1;
		this.p2 = p2;
		
	}
	
	public Vetor2D parametrica(double t){
		double x = this.p1.x+(this.p2.x-this.p1.x)*t;
		double y = this.p1.y+(this.p2.y-this.p1.y)*t;
		return new Vetor2D(x,y);
		
	}
	
	public Vetor2D vetorDirecao(){
		return new Vetor2D(this.p2.x-this.p1.x,this.p2.y-this.p1.y);
	}
	
	public void escreve(){
		System.out.println("Reta:");
		this.p1.escreve();
		this.p2.escreve();
	}
	
}
