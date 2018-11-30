package bravo_correa_perez_quevedo_vivas;

import java.util.Iterator;
import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.sound.SoundFile;

public abstract class Mundo extends Thread {
	protected PApplet app;
	protected Jugador[] j;
	protected PImage[] fondo;
	protected PImage interfaz;
	protected LinkedList<Ovni> ovnis;
	protected LinkedList<Recogible> objetos;
	protected int contadorOvni;
	protected int contadorObj;
	protected int contadorTiempo;
	protected PFont mali;
	protected SoundFile mus;
	protected boolean vivo;
	protected boolean ganar;
	protected boolean matar;
	protected int contEclipse;
	protected int opEclipse;
	
	public Mundo(PApplet app) {
		this.app = app;
		j = new Jugador[2];
		j[0] = new Jugador(app, 1);
		j[0].start();
		j[1] = new Jugador(app, 2);
		j[1].start();
		vivo = true;
		ganar = false;
		matar = false;
		ovnis = new LinkedList<Ovni>();
		objetos = new LinkedList<Recogible>();
		fondo = new PImage[3];
		for (int i = 0; i < fondo.length; i++) {
			fondo[i] = app.loadImage("fondo"+i+".png");
		}
		contadorOvni = 0;
		contadorObj = 100;
		mali = app.loadFont("maliB_28.vlw");
		mus = new SoundFile(app, "musicaJuego.wav");
		mus.play();
		contadorTiempo = app.millis()+90000;
		contEclipse = 0;
		opEclipse = 0;
	}

	public void run() {
		while(vivo) {
			
			crearOvnis();
			synchronized(objetos) {
				if(contadorObj % 60 == 0) {
					int ran = (int)app.random(25);
					if(ran == 0 && contadorObj > 3600) {
						objetos.add(new Agujero(app));
					} else if(ran == 1) {
						Cometa c = new Cometa(app);
						c.getH().start();
						objetos.add(c);
					}else if(ran == 2 && contadorObj > 1800){
						objetos.add(new Eclipse(app));
					} else {
						objetos.add(new Estrella(app));
					}
					
				}
				validarObj();
				
			}
			if(contEclipse > app.millis() && opEclipse < 230) {
				opEclipse += 3;
				
			}else if(contEclipse < app.millis() && opEclipse >= 0) {
				opEclipse -= 3;
			}
			contadorOvni++;
			contadorObj++;
			
			try {
				sleep(16);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public abstract void pintar();
	
	public abstract boolean terminarJuego();
	
	public abstract void validarObj();
	
	public abstract void crearOvnis();
	
	public abstract void tecla();
	
	public void pararMus() {
		if(mus.isPlaying()) {
			mus.stop();
		}		
	}
	
	public LinkedList<Recogible> getObjetos() {
		return objetos;
	}
	
	public LinkedList<Ovni> getOvnis() {
		return ovnis;
	}
	
	public Jugador[] getJ() {
		return j;
	}
	
	public Jugador getJu() {
		return j[0];
	}
	
	public boolean getGanar() {
		return ganar;
	}
	public boolean getMatar() {
		return matar;
	}
	
	public void setMatar(boolean b) {
		 matar = b;
	}

	public int getContEclipse() {
		return contEclipse;
	}

	public void setContEclipse(int contEclipse) {
		this.contEclipse = contEclipse;
	}
	
	
}
