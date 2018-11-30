package bravo_correa_perez_quevedo_vivas;

import java.util.Iterator;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;

public class Ovni extends Personaje{

	private PApplet app;
	private Mundo m; 
	private Jugador[] j;
	private PImage ovni;
	private float ang;
	private PFont mali;
	private boolean entrar;
	private boolean huir;
	private int atacar;
	private int modo;
	
	public Ovni(PApplet app, Mundo m, int nivel, int modo) {
		super(app);
		this.m = m;
		this.app = app;
		this.modo = modo;
		j = m.getJ();
		vel.set(j[0].getPos());
		mali = app.loadFont("maliB_18.vlw");
		
		int random = (int)app.random(4);
		float x, y;
		if(random == 0) {
			x = app.random(-200,-50);
			y = app.random(app.height);
		} else if(random == 1) {
			x = app.random(app.width+50, app.width+200);
			y = app.random(app.height);
		} else if(random == 2) {
			x = app.random(app.width);
			y = app.random(-200,-50);
		} else {
			x = app.random(app.width);
			y = app.random(app.height+50, app.height+200);
		}

		pos = new PVector(x, y);
		ovni = app.loadImage("ovni"+nivel+".png");
		
		velmax = 5 + nivel;
		fmax = 0.2f + (nivel/10);
		ang = 0;
		entrar = false;
		huir = false;
		atacar = 0;
		
	}

	public void run() {
		while(vivo) {
			try {
				int i;
				if(app.dist(j[0].getPos().x, j[0].getPos().y, pos.x, pos.y) < app.dist(pos.x, pos.y, j[1].getPos().x, j[1].getPos().y)) {
					i = 0;
				} else {
					i = 1;
				}
					if(estrellas > j[i].getEstrellas()) {
						velmax = 5;
						fmax = 0.2f;
						perseguirJugador(j[i]);
					} else if(estrellas < j[i].getEstrellas()){					
						if(app.dist(pos.x, pos.y, j[i].getPos().x, j[i].getPos().y) < 125) {
							velmax = 10;
							fmax = 0.4f;
							huir(j[i].getPos(), j[i]);
							huir = true;
						} else {
							velmax = 5;
							fmax = 0.2f;
							buscarObj();
						}
					} else {
						buscarObj();
					}
				mover();
				actualizar();
				
				atacar++;
				ang += app.PI/50;
				sleep(16);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void pintar() {
		app.pushMatrix();
		app.translate(pos.x, pos.y);
		app.fill(255);
		app.textFont(mali);
		app.text(estrellas, 35, 15);
		app.rotate(ang);
		app.image(ovni, 0, 0);
		app.popMatrix();
	}
	
	public void buscarObj() {
		Recogible o = null;
		synchronized(m.getObjetos()) {
			Iterator<Recogible> it = m.getObjetos().iterator();
			if(it.hasNext()) {
				o = it.next();
			}
			while(it.hasNext()) {
				Recogible obj = it.next();
				if(app.dist(pos.x,pos.y, obj.getPos().x, obj.getPos().y) < app.dist(pos.x, pos.y, o.getPos().x, o.getPos().y)) {
					o = obj;
				}
			}
			if(o != null) {
				perseguir(o.getPos());
				if(app.dist(pos.x, pos.y, o.getPos().x, o.getPos().y) < 25) {
					if(o instanceof Estrella) {
						estrellas++;
					} else  {
						for(int i = 0; i < 2; i++) {
							if(o instanceof Agujero) {
								if(modo == 0) {
									MundoCooperativo mc = ((MundoCooperativo)m);
									if(mc.getEstrellas() - 15 >= 0 ) {
										mc.setEstrellas(mc.getEstrellas()-15);
									} else {
										mc.setEstrellas(0);
									}
								} else {
									if(j[i].getEstrellas() - 15 >= 0 ) {
										j[i].setEstrella(j[i].getEstrellas()-15);
									} else {
										j[i].setEstrella(0);
									}
								}
							} else if(o instanceof Cometa) {
								j[i].setCometaMenos(true);
								j[i].setContEfecto(app.millis()+5000);
							}
						}
					}
					m.getObjetos().remove(o);
				}
			}
		}
	}
	
	public void huir(PVector obj, Jugador j) {
		PVector objetivo = new PVector();
		objetivo.set(obj);
		objetivo.set(-objetivo.x, -objetivo.y);
		PVector deseado = PVector.add(objetivo, pos);
		deseado.normalize();
		deseado.mult(velmax);
		PVector direccion = PVector.sub(deseado, vel);
		direccion.limit(fmax);
		ac.add(direccion);
		
		synchronized(m.getOvnis()) {
		if(app.dist(j.getPos().x, j.getPos().y, pos.x, pos.y) < 45) {
			j.setEstrella(j.getEstrellas()+2);
			j.setEstrellasTotal(j.getEstrellasTotal()+2);
			j.setContOvnis(j.getContOvnis()+1);
			vivo = false;
			m.getOvnis().remove(this);
			}
		}
	}
	
	public void perseguirJugador(Jugador j) {
		perseguir(j.getPos());
		if(app.dist(j.getPos().x, j.getPos().y, pos.x, pos.y) < 45 && atacar > 60) {
			if(modo == 0) {
				MundoCooperativo mc = ((MundoCooperativo)m);
				if(mc.getEstrellas() > 0) {
					if(mc.getEstrellas()-5 >= 0) {
						mc.setEstrellas(mc.getEstrellas()-5);					
					} else {
						mc.setEstrellas(0);
					}
				} else {
					m.setMatar(true);
				}
			} else {
				if(j.getEstrellas() > 0) {
					if(j.getEstrellas()-5 >= 0) {
						j.setEstrella(j.getEstrellas()-5);					
					} else {
						j.setEstrella(0);
					}
				} else {
					m.setMatar(true);
				}
			}
			estrellas += 2;
			atacar = 0;
		}
	}
	
	public void mover() {
		if(pos.x > 60 && pos.x < 1140 && pos.y > 60 && pos.y < 615) {
			entrar = true;
		}
		if(entrar && !huir) {
			if(pos.x < 50 || pos.x > 1150) {
				PVector newvel = new PVector(-vel.x, vel.y);
				vel.set(newvel);
			}
			
			if(pos.y < 50 || pos.y > 625) {
				PVector newvel = new PVector(vel.x, -vel.y);
				vel.set(newvel);
			}
			huir = false;
		}
	}
	
	//Referencias a variables
	
	public int getEstrellas() {
		return estrellas;
	}
}
