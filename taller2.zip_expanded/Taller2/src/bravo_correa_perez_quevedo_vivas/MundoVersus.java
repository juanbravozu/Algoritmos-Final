package bravo_correa_perez_quevedo_vivas;

import java.util.Iterator;
import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.sound.SoundFile;

public class MundoVersus extends Mundo {
	
	private PApplet app;
	private Jugador[] j;
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
	
	public MundoVersus(PApplet app) {
		super(app);
	}
	
	public void pintar() {
		app.image(fondo, app.width/2, app.height/2);
		j[0].pintar();
		j[1].pintar();
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
		app.text(j[0].getEstrellas(), 1150.81f, 556.21f);
		app.text(j[0].getAgujero(),  1150.81f, 595.71f);
		app.text(j[0].getCometa(),  1150.81f, 632.5f);
	
		
		app.fill(255, 200, 200);
		app.text(j[1].getEstrellas(), 1180.81f, 556.21f);
		app.text(j[1].getAgujero(),  1180.81f, 595.71f);
		app.text(j[1].getCometa(),  1180.81f, 632.5f);
		app.fill(255);
		
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
			if(o.getEstrellas() - j[0].getEstrellas() <= 0) {
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
		if(app.keyCode == app.RIGHT) {
			System.out.println(j[1].getEstrellas());
		}
		if(app.key == '2' && (j[0].usarAgujero() || j[1].usarAgujero())) {
			for(Ovni o : ovnis) {
				o.setVivo(false);
			}
			ovnis.clear();
		}
		
		if(app.key == '1') {
			j[0].usarCometa();
			j[1].usarCometa();
		}
	}
	
	//Referencias a las variables y objetos a partir de aquí
	
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
}
	



