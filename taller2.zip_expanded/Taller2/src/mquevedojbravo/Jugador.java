package mquevedojbravo;

import java.util.Iterator;
import java.util.LinkedList;
import de.voidplus.leapmotion.*;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Jugador extends Personaje{

	private LinkedList<PVector> historia;
	private int cometa;
	private int agujeros;
	private int agujerosTotal;
	private int estrellasTotal;
	private int cometasTotal;
	private int contOvnis;
	private int contEfecto;
	private int tipoJugador;
	private boolean cometaMas;
	private boolean cometaMenos;
	private LeapMotion leap;
	private PVector obj;
	
	public Jugador(PApplet app, int tipo) {
		super(app);
		tipoJugador = tipo;
		pos = new PVector(app.width/2, app.height/2);
		historia = new LinkedList<PVector>();
		historia.add(pos);
		cometaMas = false;
		cometaMenos = false;
		if(tipo == 1) {
			img = app.loadImage("nave.png");
		} else {
			img = app.loadImage("nave2.png");
			leap = new LeapMotion(app);
		}		
		velmax = 7f;
		fmax = 0.3f;
		contOvnis = 0;
		agujeros = 0;
		agujerosTotal = 0;
		estrellasTotal = 0;
		cometasTotal = 0;
		cometa = 0;
		contEfecto = 0;	
	}

	public void run() {
		while(vivo) {
			actualizar();
			
			if(tipoJugador == 1) {
				obj = new PVector(app.mouseX, app.mouseY);
			} else {
				if(leap.getHands().size() > 0) {
					Hand mano = leap.getHands().get(0);
					obj = mano.getPosition();
				} else {
					obj = pos;
				}
			}
			perseguir(obj);
			efectoCometa();
			ang = vel.heading() + app.PI/2;
			try {					
				sleep(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void pintar() {	
		app.noStroke();
		synchronized(historia) {
			Iterator<PVector> it = historia.iterator();
			int i = 0;
			while(it.hasNext()) {
					PVector p = it.next();
					int tam = 5 + (int)(i/2);
					int opacidad = 30+(i*4);
					if(tipoJugador == 1) {
						app.fill(0, 193, 208, opacidad);
					} else {
						app.fill(163, 6, 30, opacidad);
					}
					app.ellipse(p.x, p.y, tam, tam);
					i++;
			}
		}
		app.pushMatrix();
		app.translate(pos.x, pos.y);
		app.rotate(ang);
		app.image(img, 0, 0);
		app.popMatrix();
		
		if(tipoJugador == 2) {
			app.stroke(255, 200, 200,100);
			app.noFill();
			app.ellipse(obj.x, obj.y, 10, 10);
		}
	}
	
	public void actualizar() {
		vel.add(ac);
		vel.limit(velmax);
		pos.add(vel);
		ac.mult(0);
		PVector newP = new PVector(pos.x, pos.y);
		synchronized(historia) {
			historia.add(newP);
			if(historia.size() >  35) {
				historia.remove(0);
			}
		}
	}
	
	public void perseguir(PVector obj) {
		PVector deseado = PVector.sub(obj, pos);
		
		float dist = deseado.mag();
		deseado.normalize();
		if(dist < 100) {
			float mag = app.map(dist, 0, 100, 0, velmax);
			deseado.mult(mag);
		} else {
			deseado.mult(velmax);
		}
		PVector direccion = PVector.sub(deseado, vel);
		direccion.limit(fmax);
		ac.add(direccion);
	}
	
	public boolean validarObj(Recogible o) {
		if(app.dist(pos.x, pos.y, o.getPos().x, o.getPos().y) < 28) {
			if(o instanceof Estrella) {
				estrellas++;
				estrellasTotal++;
				return true;
			} else if(o instanceof Agujero){
				agujeros++;
				agujerosTotal++;
				return true;
			} else if(o instanceof Cometa) {
				cometa++;
				cometasTotal++;
				return true;
			} else {
				return false;
			}
		}else {
			return false;
		 }
	}
	
	public void usarCometa() {
		if(estrellas >= 5 && cometa > 0) {
			cometaMas = true;
			estrellas -= 5;
			contEfecto = app.millis() + 5000;
			cometa--;
		}
	}
	
	public boolean usarAgujero() {
		if(agujeros > 0 && estrellas >= 15) {
			agujeros--;
			estrellas -= 15;
			return true;
		}else {
			return false;
		}
	}
	
	public void efectoCometa() {
		if(!cometaMenos && !cometaMas) {
			velmax = 7f;
			fmax = 0.3f;
		}
		if(cometaMas) {
			velmax = 10;
			fmax = 0.4f;
		} else if(cometaMenos) {
			velmax = 4.5f;
			fmax = 0.2f;
		}
		if(contEfecto - app.millis() < 0) {
			cometaMas = false;
			cometaMenos = false;
		}
	}
	
	//Referencias a las variables y objetos desde aquí
	
	public PVector getPos() {
		return pos;
	}
	
	public int getCometasTotal() {
		return cometasTotal;
	}
	
	public void setContEfecto(int e) {
		contEfecto = e;
	}
	
	public void setVel(float vel) {
		velmax = vel;
	}
	
	public int getEstrellas() {
		return estrellas;
	}
	
	public void setEstrella(int e) {
		estrellas = e;
	}

	public int getContOvnis() {
		return contOvnis;
	}

	public void setContOvnis(int contOvnis) {
		this.contOvnis = contOvnis;
	}
	
	public int getAgujero() {
		return agujeros;
	}
	
	public void setAgujero(int agujeros) {
		this.agujeros = agujeros;
	}

	public int getCometa() {
		return cometa;
	}

	public void setCometa(int cometa) {
		this.cometa = cometa;
	}

	public int getAgujerosTotal() {
		return agujerosTotal;
	}	
	
	public int getEstrellasTotal() {
		return estrellasTotal;
	}
	
	public void setEstrellasTotal(int e) {
		estrellasTotal = e;
	}


	public void setCometaMenos(boolean cometaMenos) {
		this.cometaMenos = cometaMenos;
	}

	public void setCometaMas(boolean cometaMas) {
		this.cometaMas = cometaMas;
	}
	
	
}
