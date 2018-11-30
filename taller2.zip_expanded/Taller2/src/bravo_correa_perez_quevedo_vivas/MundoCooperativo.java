package bravo_correa_perez_quevedo_vivas;

import java.util.Iterator;
import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.sound.SoundFile;

public class MundoCooperativo extends Mundo {

	private int estrellas;
	private int estrellasTotal;
	private int contadorOvnis;
	
	public MundoCooperativo(PApplet app) {
		super(app);
		estrellas = 0;
		Ovni o = new Ovni(app, this, 0, 0);
		o.start();
		ovnis.add(o);
		interfaz =app.loadImage("interfaz0.png");
	}
	
	public void pintar() {
		if(contadorOvni < 1800) {
			app.image(fondo[0], app.width/2, app.height/2);
		}else if( contadorOvni < 3600) {
			app.image(fondo[1], app.width/2, app.height/2);
		}else {
			app.image(fondo[2], app.width/2, app.height/2);
		}
		
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
		app.fill(0,opEclipse);
		app.rect(0, 0, app.width, app.height);
		app.noFill();
		app.strokeWeight(3);
		app.stroke(27, 103, 144);
		app.rect(303.5f,597.5f, 173, 35, 10);
		app.fill(27, 103, 144);
		if(j[0].getContEfecto()-app.millis() >= 0) {
			app.rect(303.5f,597.5f, app.map(j[0].getContEfecto()-app.millis(), 0, 5000, 0, 173), 35, 10);
		}
		app.noFill();
		app.rect(573.5f,597.5f, 173, 35, 10);
		app.fill(27, 103, 144);
		if(contEclipse-app.millis() >= 0) {
			app.rect(573.5f,597.5f, app.map(contEclipse-app.millis(), 0, 5000, 0, 173), 35, 10);
		}
		//Texto Interfaz
		app.textAlign(app.CORNER, app.CENTER);
		app.textFont(mali);
		app.fill(255);
		app.text(estrellas, 1150.81f, 556.21f);
		app.text(j[0].getAgujero()+j[1].getAgujero(),  1150.81f, 595.71f);
		app.text(j[0].getCometa()+j[1].getCometa(),  1150.81f, 632.5f);
		
		//Tiempo
		int seg = (contadorTiempo - app.millis())/1000;
		int min = seg/60;
		seg -= min*60;
		if(seg > 9) {
			app.text(min + ":" + seg, 1084.8f, 50);
		} else {
			app.text(min + ":0" + seg, 1084.8f, 50);
		}
		
		if(contadorOvni < 1800) {
			app.text("Nivel 1", 115, 625);
		}else if( contadorOvni < 3600) {
			app.text("Nivel 2", 115, 625);
		}else {
			app.text("Nivel 3", 115, 625);
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
			estrellas = j[0].usarCometa(estrellas);
			estrellas = j[1].usarCometa(estrellas + 5);
		}
	}
	
	public void crearOvnis() {
		synchronized(ovnis) {
			//Crear Ovnis
			if(contadorOvni % 180 == 0) {
				if(contadorOvni < 1800) {
					Ovni o = new Ovni(app, this, 0, 0);
					o.start();
					ovnis.add(o);
				}else if(contadorOvni >= 1800 && contadorOvni < 3600) {
					Ovni o = new Ovni(app, this, 1, 0);
					o.start();
					ovnis.add(o);
				} else {
					Ovni o = new Ovni(app, this, 2, 0);
					o.start();
					ovnis.add(o);
				}
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
			if(o.getEstrellas() - estrellas <= 0) {
				ganar = true;
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	public void validarObj() {
		Iterator<Recogible> it = objetos.iterator();
		while(it.hasNext()) {
			Recogible o = it.next();
			if(j[0].validarObj(o) || j[1].validarObj(o)) {
				if(o instanceof Estrella) {
					estrellas++;
				}else if(o instanceof Eclipse) {
					contEclipse = app.millis()+5000;
				}
				it.remove();
				
			}
			
			if(o instanceof Cometa && ((Cometa)o).borrar()) {
				it.remove();
			}
		}
	}
	
	//Referencias a las variables y objetos a partir de aquí
	
	public LinkedList<Recogible> getObjetos() {
		return objetos;
	}
	
	public int getEstrellas() {
		return estrellas;
	}
	
	public void setEstrellas(int e) {
		estrellas = e;
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

	public int getEstrellasTotal() {
		return estrellasTotal;
	}

	public void setEstrellasTotal(int estrellasTotal) {
		this.estrellasTotal = estrellasTotal;
	}

	public int getContadorOvnis() {
		return contadorOvnis;
	}

	public void setContadorOvnis(int contadorOvnis) {
		this.contadorOvnis = contadorOvnis;
	}

	
}
	
