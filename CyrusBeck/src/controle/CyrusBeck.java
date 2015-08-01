package controle;

import java.util.ArrayList;

import util.ConversorArray;
import util.TransformacoesGeometricas;
import modelo.Poligono;
import modelo.Reta;
import modelo.Vetor2D;

public class CyrusBeck {
	public Poligono poligono; 
	public Vetor2D[] poligonoOriginal;
	public int qtdePoligonos;
	public ArrayList<Poligono> poligonosSplitRec;
	
	public CyrusBeck(Poligono poligono){
		this.poligono = poligono;
		this.poligonoOriginal = new Vetor2D[this.poligono.vertices.size()];
		for (int i = 0; i<this.poligono.vertices.size(); i++){
			double x = this.poligono.vertices.get(i).x;
			double y = this.poligono.vertices.get(i).y;
			this.poligonoOriginal[i] = new Vetor2D(x,y);
		}
		this.poligonosSplitRec = new ArrayList<Poligono>();
		
	}
	
	public void checkSplitConcaveRec(Poligono p){
		Vetor2D[] aux = ConversorArray.objetoParaPrimitivo(p.vertices);
		System.out.println("###### avaliando o seguinte poligono");
		p.escrevevertices();
		for (int i = 0; i<p.vertices.size(); i++){
			double x = p.vertices.get(i).x;
			double y = p.vertices.get(i).y;
			this.poligonoOriginal[i] = new Vetor2D(x,y);
		}
		Poligono p1 = null, p2 = null;
		ArrayList<Vetor2D> v = p.vertices;
		int i = 0;
		boolean achouConcavo = false;
		if(p.vertices.size()>3){
			while (i<v.size()){
				//translada poligono para que Vi esteja em (0,0)
				double dX = v.get(i).x * -1;
				double dY = v.get(i).y * -1;
				p = TransformacoesGeometricas.translacao2D(p, dX, dY);
				//rotaciona poligono horario para que Vi+1 esteja sobre x
				int prox = i+1;
				if ((prox)==v.size()) prox = 0;
				double angulo = StrictMath.atan2(p.vertices.get(prox).y, p.vertices.get(prox).x);
				p = TransformacoesGeometricas.rotacao2D(p, -angulo);
				//verifica se o y do Vi+2 eh < 0, se sim split
				int prox2 = prox+1;
				if ((prox2)>=v.size()) prox2 = i+2-v.size();
				if (p.vertices.get(prox2).y < 0){
					achouConcavo = true;
					System.out.println("Iteracao i = "+i+" e Vertice concavo = ("+poligonoOriginal[prox].x+","+poligonoOriginal[prox].y+")");
				}
				if (achouConcavo){
					//a separacao se da da seguinte forma
					
					//primeiro poligono: Vi+1, Vi+2, .... ate o primeiro Vn que tenha y>=0
					ArrayList<Vetor2D> v1 = new ArrayList<Vetor2D>();
					//adiciona Vi+1 na lista
					double x = poligonoOriginal[prox].x;
					double y = poligonoOriginal[prox].y;
					v1.add(new Vetor2D(x, y));
					//adiciona Vi+2 na lista
					x = poligonoOriginal[prox2].x;
					y = poligonoOriginal[prox2].y;
					v1.add(new Vetor2D(x, y));
					//adiciona todos até encontrar o primeiro que tenha Y >=0
					int j = prox2+1;
					if (j>=v.size()){	
						j = 0;
					}
					while((v.get(j).y<0) && j != prox){
						double ax = poligonoOriginal[j].x;
						double ay = poligonoOriginal[j].y;
						v1.add(new Vetor2D(ax,ay));
						j++;
						if (j>=v.size()){	
							j = 0;
						}
					}
					x = poligonoOriginal[j].x;
					y = poligonoOriginal[j].y;
					v1.add(new Vetor2D(x,y));
					p1 = new Poligono(v1);
					
					//segundo poligono: Vi, Vi+1, primeiro Vn que tenha y>=0, ... ate ultimo V
					ArrayList<Vetor2D> v2 = new ArrayList<Vetor2D>();
					//adiciona Vi na lista
					x = poligonoOriginal[i].x;
					y = poligonoOriginal[i].y;
					v2.add(new Vetor2D(x, y));
					//adiciona Vi+1 na lista
					x = poligonoOriginal[prox].x;
					y = poligonoOriginal[prox].y;
					v2.add(new Vetor2D(x, y));
					//adiciona primeiro Vn com y>=0 na lista
					x = poligonoOriginal[j].x;
					y = poligonoOriginal[j].y;
					v2.add(new Vetor2D(x, y));
					if (j > i){
						//adiciona o restante dos vertices
						while (j<v.size()-1){
							j++;
							double ax = poligonoOriginal[j].x;
							double ay = poligonoOriginal[j].y;
							v2.add(new Vetor2D(ax,ay));
							
						}
						//roda de 0 a i-1
						j = 0;
						while(j<i){
							double ax = poligonoOriginal[j].x;
							double ay = poligonoOriginal[j].y;
							v2.add(new Vetor2D(ax,ay));
							j++;
						}
					}else{//j<i
						//roda de j a i-1
						while(j<i-1){
							j++;
							double ax = poligonoOriginal[j].x;
							double ay = poligonoOriginal[j].y;
							v2.add(new Vetor2D(ax,ay));
						}
					}
					p2 = new Poligono(v2);
					break;	
				}
			i++;	
			}
		}
		if (!achouConcavo){
			poligonosSplitRec.add(new Poligono (ConversorArray.primitivoParaObjeto(aux)));
			System.out.println("adicionei poligono analisado, pois nao houve nenhuma concavidade encontrada");
			
		}else{
			checkSplitConcaveRec(p1);
			checkSplitConcaveRec(p2);
		}
		
	}
	
	public Reta clip(Reta r, Poligono p){
		if(r != null){
			Vetor2D p1 = r.p1;
			Vetor2D p2 = r.p2;
			ArrayList<Vetor2D> normais = p.normaisInternas();
			
			double tmin,tmax,t;
		    tmin = 0;
		    tmax = 1;
		    double Dprod_inter, Wprod_inter;
		    boolean invalida = false;
		    
		    for (int i=0;i<p.vertices.size();i++){
		    	
		        Dprod_inter = p2.menos(p1).dot(normais.get(i));
		        Wprod_inter = p1.menos(p.vertices.get(i)).dot(normais.get(i));
		        
		        if(Dprod_inter!=0){
		            t = -(Wprod_inter)/Dprod_inter;
		            if(Dprod_inter>0){
		            	if(t>1){
		            		invalida = true;
		                }else{
		                    tmin = Math.max(t,tmin);
		                }
		            }else{
		                if(t<0){
		                	invalida = true;
		                }else{
		                    tmax = Math.min(t,tmax);
		                }
		            }
		        }else{
		            if(Wprod_inter<0){
	                    invalida = true;
		            }
		        }
		    }
		    if((tmin<tmax) && (!invalida)){
		    	Vetor2D inicio = r.parametrica(tmin);
		    	Vetor2D fim = r.parametrica(tmax);
		        r = new Reta(new Vetor2D(inicio.x,inicio.y),new Vetor2D(fim.x,fim.y));
		        r.visivel = true;
		    }else{
		    	r.visivel = false;
		    }
		}    
		return r;
	}
	
		
}
