package util;


import java.util.Iterator;

import modelo.Poligono;
import modelo.Vetor2D;

public class TransformacoesGeometricas {
	
	public static Poligono translacao2D(Poligono p, double dX, double dY){
		Iterator<Vetor2D> it = p.vertices.iterator();
		while (it.hasNext()){
			Vetor2D v = it.next();
			v.x+=dX;
			v.y+=dY;
		}
		return p;
	}
	
	public static Poligono rotacao2D(Poligono p, double angulo){
		Iterator<Vetor2D> it = p.vertices.iterator();
		while (it.hasNext()){
			Vetor2D v = it.next();
			double novoX = (v.x * StrictMath.cos(angulo)) - (v.y * StrictMath.sin(angulo));
			double novoY = (v.x * StrictMath.sin(angulo)) + (v.y * StrictMath.cos(angulo));		
			v.x = novoX;
			v.y = novoY;
		}
		return p;
	}
}
