package visao;


import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_LINE_LOOP;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.sound.midi.ControllerEventListener;
import javax.sound.midi.ShortMessage;
import javax.swing.JFrame;



import modelo.Poligono;
import modelo.Reta;
import modelo.Vetor2D;

import com.jogamp.opengl.util.FPSAnimator;


import controle.CyrusBeck;

public class Renderer extends KeyAdapter implements GLEventListener,
		ControllerEventListener {

	public int width, height;
	public static GL2 gl;
	public static GLU glu = new GLU();
	private static ArrayList<Poligono> poligonos;
	private static ArrayList<Reta> retas;
	private static ArrayList<Vetor2D> janela;
	private static Reta r;
	private static CyrusBeck cb;

	public void keyPressed(KeyEvent evento) {
		int keyCode = evento.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		case KeyEvent.VK_S:
			System.out.println("COMANDO SPLIT");
			cb.checkSplitConcaveRec(cb.poligono);
			ArrayList<Poligono> aux= cb.poligonosSplitRec;
			if (!aux.isEmpty()) poligonos = aux;
			System.out.println("SPLITEI EM "+poligonos.size());
			
			System.out.println("####POLIGONOS SPLITADOS####");
			Iterator<Poligono> it = poligonos.iterator();
			while (it.hasNext()){
				Poligono polAux = it.next();
				polAux.escrevevertices();
			}
			
			break;
		case KeyEvent.VK_C:
			System.out.println("COMANDO CLIP");
			for(int j = 0; j<poligonos.size(); j++){
				if (retas != null){	
					for(int i = 0; i< retas.size(); i++){
						Reta aux2 = cb.clip(retas.get(i),poligonos.get(j));
						if (aux2!= null && aux2.visivel){
							poligonos.get(j).retasVisiveis.add(aux2);
						}
					}
					
				}else{
					System.out.println("JA CLIPOU");
				}
			}
			retas = null;
			break;
		}
	}

	public void init(GLAutoDrawable drawable) {
	      GL2 gl = drawable.getGL().getGL2();
	      glu = new GLU();                      
	      gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	      gl.glClearDepth(1.0f);      
	      gl.glEnable(GL_DEPTH_TEST); 
	      gl.glDepthFunc(GL_LEQUAL);  
	      gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); 
	      gl.glShadeModel(GL_SMOOTH); 
	      poligonos = new ArrayList<Poligono>();
	      retas = new ArrayList<Reta>();
	      janela = new ArrayList<Vetor2D>();
	      criaJanelas();		
	  }
	 
	  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
	      GL2 gl = drawable.getGL().getGL2();
	      if (height == 0) height = 1;
	      float aspect = (float)width / height;
	      gl.glViewport(0, 0, width, height);
	      gl.glMatrixMode(GL_PROJECTION);
	      gl.glLoadIdentity();
	      glu.gluPerspective(45.0, aspect, 0.1, 1000.0);
	      gl.glMatrixMode(GL_MODELVIEW);
	      gl.glLoadIdentity();
	   }
	 
	   public void display(GLAutoDrawable drawable) {
	      GL2 gl = drawable.getGL().getGL2();
	      gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	      gl.glLoadIdentity();
	      gl.glTranslatef(0.0f, -5.0f, -20.0f);
	      
	      for (int i = 0; i< poligonos.size(); i++){
		      gl.glBegin(GL_LINE_LOOP);
		      gl.glColor3d(1, 0, 0);
		      	Iterator<Vetor2D> it = poligonos.get(i).vertices.iterator();
		      	while(it.hasNext()){
		      		Vetor2D v = it.next();
		      		gl.glVertex3d(v.x, v.y, 0);
		      	}
		      gl.glEnd();
	      }
	      if (retas!=null){
		      for (int i = 0; i< retas.size(); i++){
		    	  r = retas.get(i);
			      if (r != null){
				      gl.glBegin(GL.GL_LINES);
				      gl.glColor3d(1, 1, 1);
				      	gl.glVertex3d(r.p1.x, r.p1.y, 0);
				      	gl.glVertex3d(r.p2.x, r.p2.y, 0);
				      gl.glEnd();
			      }	
		      }
	      }	
    	  for(int j = 0; j<poligonos.size(); j++){
				for(int i = 0; i< poligonos.get(j).retasVisiveis.size(); i++){
					Reta aux2 = poligonos.get(j).retasVisiveis.get(i);
					if (aux2!=null){
						gl.glBegin(GL.GL_LINES);
					      gl.glColor3d(0, 1, 0);
					      	gl.glVertex3d(aux2.p1.x, aux2.p1.y, 0);
					      	gl.glVertex3d(aux2.p2.x, aux2.p2.y, 0);
					      gl.glEnd();
					}
				}
    	  }
	   }

	public static void main(String args[]) {
		GLCanvas canvas = new GLCanvas();
		Renderer renderer = new Renderer();
		JFrame janela = new JFrame("Teste");
		janela.getContentPane().add(canvas);
		janela.setExtendedState(Frame.MAXIMIZED_BOTH);
		janela.setUndecorated(true);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvas.addGLEventListener(renderer);
		canvas.addKeyListener(renderer);
		FPSAnimator anim = new FPSAnimator(canvas, 30, true);
		janela.setLocationRelativeTo(null);
		janela.setVisible(true);
		canvas.requestFocus();
		anim.start();
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void controlChange(ShortMessage event) {
	}

	public void dispose(GLAutoDrawable drawable) {
	}

	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
	}
	
	public void criaJanelas(){

		//duas concavidades
		/*janela.add(new Vetor2D(0,0));
		janela.add(new Vetor2D(2,1));
		janela.add(new Vetor2D(5,0));
		janela.add(new Vetor2D(5,5));
		janela.add(new Vetor2D(0,5));
		janela.add(new Vetor2D(2,3));*/
		
		//concavidade leste
	/*	janela.add(new Vetor2D(0,0));
		janela.add(new Vetor2D(5,0));
		janela.add(new Vetor2D(4,3));
		janela.add(new Vetor2D(5,5));
		janela.add(new Vetor2D(0,5));*/
		
		//concavidade oeste 
		/*janela.add(new Vetor2D(0,0));
		janela.add(new Vetor2D(5,0));
		janela.add(new Vetor2D(5,5));
		janela.add(new Vetor2D(0,5));
		janela.add(new Vetor2D(4,3));*/
		
		//concavidade norte 
		/*janela.add(new Vetor2D(0,0));
		janela.add(new Vetor2D(5,0));
		janela.add(new Vetor2D(5,5));
		janela.add(new Vetor2D(4,3));
		janela.add(new Vetor2D(0,5));*/
		
		//concavidade sul
		/*janela.add(new Vetor2D(0,0));
		janela.add(new Vetor2D(2,4));
		janela.add(new Vetor2D(5,0));
		janela.add(new Vetor2D(5,5));
		janela.add(new Vetor2D(0,5));*/
		
		//convexa
	    /*janela.add(new Vetor2D(0,0));
  		janela.add(new Vetor2D(5,0));
  		janela.add(new Vetor2D(5,5));
  		janela.add(new Vetor2D(0,5));*/
	    
		//multiplas concavidades todas as direcoes
		janela.add(new Vetor2D(0,2));
		janela.add(new Vetor2D(2,0));
		janela.add(new Vetor2D(5,0));
		janela.add(new Vetor2D(4,3));
		janela.add(new Vetor2D(2,4));
		janela.add(new Vetor2D(4,6));
		janela.add(new Vetor2D(1,6));
		janela.add(new Vetor2D(2,7));
		janela.add(new Vetor2D(1,8));
		janela.add(new Vetor2D(0,10));
		janela.add(new Vetor2D(-1,8));
		janela.add(new Vetor2D(-2,9));
		janela.add(new Vetor2D(-3,7));
		janela.add(new Vetor2D(-1,7));
		janela.add(new Vetor2D(-4,6));
		janela.add(new Vetor2D(-2,4));
		janela.add(new Vetor2D(-4,2));
		janela.add(new Vetor2D(-2,0));
		
		geraRetas();
	      		
	    cb = new CyrusBeck(new Poligono(janela));
		poligonos.add(new Poligono(janela));
	}
	
	public void geraRetas(){
		retas.add(new Reta(new Vetor2D(-6,2), new Vetor2D(6,0)));
		retas.add(new Reta(new Vetor2D(-1,-2), new Vetor2D(6,6)));
		retas.add(new Reta(new Vetor2D(6,5), new Vetor2D(-6,5)));
		retas.add(new Reta(new Vetor2D(10,-1), new Vetor2D(-10,-1)));
		retas.add(new Reta(new Vetor2D(-2,10), new Vetor2D(-2,-2)));
		retas.add(new Reta(new Vetor2D(3,-2), new Vetor2D(2,8)));
		retas.add(new Reta(new Vetor2D(4,4), new Vetor2D(6,2)));
		retas.add(new Reta(new Vetor2D(-1,2), new Vetor2D(4,7)));
		retas.add(new Reta(new Vetor2D(2,1), new Vetor2D(-4,7)));
		retas.add(new Reta(new Vetor2D(1,-3), new Vetor2D(1,7)));
		retas.add(new Reta(new Vetor2D(0,-5), new Vetor2D(0,12)));
	}
}
