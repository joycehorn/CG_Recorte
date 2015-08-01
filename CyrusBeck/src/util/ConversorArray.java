package util;

import java.util.ArrayList;

import modelo.Vetor2D;

public class ConversorArray {
	public static ArrayList<Vetor2D> primitivoParaObjeto (Vetor2D[] primitivo){
		ArrayList<Vetor2D> objeto = new ArrayList<Vetor2D>(primitivo.length);
		for (int i = 0; i<primitivo.length; i++){
			double x = primitivo[i].x;
			double y = primitivo[i].y;
			objeto.add(i, new Vetor2D(x,y));
		}
		return objeto;
	}
	
	public static Vetor2D[] objetoParaPrimitivo(ArrayList<Vetor2D> objeto){
		Vetor2D[] primitivo = new Vetor2D[objeto.size()];
		for (int i = 0; i<objeto.size(); i++){
			double x = objeto.get(i).x;
			double y = objeto.get(i).y;
			primitivo[i] = new Vetor2D(x,y);
		}
		return primitivo;
	}
}
