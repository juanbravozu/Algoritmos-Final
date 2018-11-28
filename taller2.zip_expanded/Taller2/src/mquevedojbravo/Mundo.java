package mquevedojbravo;

import java.util.Iterator;
import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.sound.SoundFile;

public class Mundo extends Thread {

	private PApplet app;
	private Jugador j;
	private PImage fondo;
	private PImage interfaz;
	private LinkedList<Ovni> ovnis;
	private LinkedList<Recogible> objetos;
	private int contadorOvni;
	private int contadorObj;
	private int contadorTiempo;
	private PFont mali;
	private SoundFile mus;
	private boolean vivo;
	private boolean ganar;
	private boolean matar;
	
	public Mundo(PApplet app) {
		this.app = app;
		j = new Jugador(app, 2);
		j.start();
		vivo = true;
		ganar = false;
		matar = false;
		ovnis = new LinkedList<Ovni>();
		Ovni o = new Ovni(app, this);
		o.start();
		ovnis.add(o);
		objetos = new LinkedList<Recogible>();
		fondo = app.loadImage("fondo1.png");
		interfaz = app.loadImage("Interfaz1.png");
		contadorOvni = 0;
		contadorObj = 100;
		mali = app.loadFont("maliB_28.vlw");
		mus = new SoundFile(app, "musicaJuego.wav");
		mus.play();
		contadorTiempo = app.millis()+90000;
	}
	
	public void pintar() {
		app.image(fondo, app.width/2, app.height/2);
		j.pintar();
		//Pintar Ovnis
		synchronized(ovnis) {
			Iterator<Ovni> it = ovnis.iterator();
			while(it.hasNext()) {
				Ovni o = it.next();
				o.pintar();
			}
		}
		synchronized(objetos) {
			Iterator<Recogible> ite = objetos.iterator();
			while(ite.hasNext()) {
				Recogible r = ite.next();
				r.pintar();
			}
		}
		app.image(interfaz, app.width/2, app.height/2);
		//Texto Interfaz
		app.textAlign(app.CORNER, app.CENTER);
		app.textFont(mali);
		app.fill(255);
		app.text(j.getEstrellas(), 1150.81f, 556.21f);
		app.text(j.getAgujero(),  1150.81f, 595.71f);
		app.text(j.getCometa(),  1150.81f, 632.5f);
		//Tiempo
		int seg = (contadorTiempo - app.millis())/1000;
		int min = seg/60;
		seg -= min*60;
		if(seg > 9) {
			app.text(min + ":" + seg, 946.62f, 635.13f);
		} else {
			app.text(min + ":0" + seg, 946.62f, 635.13f);
		}
		
		
	}
	
	public void run() {
		while(vivo) {
			synchronized(ovnis) {
				//Crear Ovnis
				if(contadorOvni % 180 == 0) {
					Ovni o = new Ovni(app, this);
					o.start();
					ovnis.add(o);
				}
			}

			synchronized(objetos) {
				if(contadorObj % 60 == 0) {
					int ran = (int)app.random(20);
					if(ran == 0) {
						objetos.add(new Agujero(app));
					} else if(ran == 1) {
						Cometa c = new Cometa(app);
						c.getH().start();
						objetos.add(c);
					}else {
						objetos.add(new Estrella(app));
					}
					
				}
				
				Iterator<Recogible> it = objetos.iterator();
				while(it.hasNext()) {
					Recogible o = it.next();
					if(j.validarObj(o)) {
						it.remove();
					}
					
					if(o instanceof Cometa && ((Cometa)o).borrar()) {
						it.remove();
					}
				}
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
	
	public boolean terminarJuego() {
		if(contadorTiempo-app.millis() <= 0) {
			mus.stop();
			Ovni o = null;
			Iterator<Ovni> it = ovnis.iterator();
			if(it.hasNext()) {
				o = it.next();
			}
			while(it.hasNext()) {
				Ovni obj = it.next();
				
				if(o.getEstrellas()-obj.getEstrellas() <= 0) {
					o = obj;
				}
			}
			if(o.getEstrellas() - j.getEstrellas() <= 0) {
				ganar = true;
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	public void pararMus() {
		if(mus.isPlaying()) {
			mus.stop();
		}		
	}
	
	public void tecla() {
		if(app.key == '2' && j.usarAgujero()) {
			for(Ovni o : ovnis) {
				o.setVivo(false);
			}
			ovnis.clear();
		}
		
		if(app.key == '1') {
			j.usarCometa();
		}
	}
	
	//Referencias a las variables y objetos a partir de aquí
	
	public LinkedList<Recogible> getObjetos() {
		return objetos;
	}
	
	public LinkedList<Ovni> getOvnis() {
		return ovnis;
	}
	
	public Jugador getJ() {
		return j;
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
	
}