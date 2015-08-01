package modelo;


public class Vetor2D {
	public double x,y;
	
	public Vetor2D(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public double length(){
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}
	
	public void normalize(){
		double len = length();
		this.x /= len;
		this.y /= len;
	}
	
	public double dot(Vetor2D v){
		return this.x * v.x + this.y * v.y;
	}
	
	public Vetor2D menos(Vetor2D v){
		return (new Vetor2D(this.x - v.x, this.y - v.y));
	}
	
	public void escreve(){
		System.out.println("("+this.x+","+this.y+")");
	}
}
