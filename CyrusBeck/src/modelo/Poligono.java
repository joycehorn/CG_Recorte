package modelo;

import java.util.ArrayList;
import java.util.Iterator;




public class Poligono {
	public ArrayList<Vetor2D> vertices;
	public boolean concavo;
	public ArrayList<Reta> retasVisiveis;
	
	public Poligono(ArrayList<Vetor2D> vertices){
		this.vertices = vertices;
		this.concavo = false;
		this.retasVisiveis = new ArrayList<Reta>();
	}
	
	public void escrevevertices(){
		Iterator<Vetor2D> it = this.vertices.iterator();
		System.out.println("--------------------------");
		while (it.hasNext()){
			Vetor2D v = it.next();
			System.out.println("Vertice ("+v.x+","+v.y+")");			
		}
		System.out.println("--------------------------");
	} 
	
	public Poligono clone(){
		ArrayList<Vetor2D> aux = new ArrayList<Vetor2D>();
		for (int i = 0; i<this.vertices.size(); i++){
			double x = this.vertices.get(i).x;
			double y = this.vertices.get(i).y;
			aux.add(new Vetor2D(x,y));
		}
		return new Poligono(aux);
	}
	
	public ArrayList<Vetor2D> normaisInternas() {
		ArrayList<Vetor2D> ni = new ArrayList<Vetor2D>();
		for (int j = 0; j < this.vertices.size(); j++){
			if(j==(this.vertices.size()-1)){
				double x = -1 * (this.vertices.get(0).y - this.vertices.get(j).y);
				double y = this.vertices.get(0).x - this.vertices.get(j).x;
				//System.out.println("adicionei ("+x+","+y+")");
				ni.add(new Vetor2D(x,y));
			}else{
				double x = -1 * (this.vertices.get(j+1).y - this.vertices.get(j).y);
				double y = this.vertices.get(j+1).x - this.vertices.get(j).x;
				//System.out.println("adicionei ("+x+","+y+")");
				ni.add(new Vetor2D(x,y));
			}
		}
	    return ni;
	}

	
}
