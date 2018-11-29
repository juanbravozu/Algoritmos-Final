package bravo_correa_perez_quevedo_vivas;

import java.util.Iterator;
import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PVector;

public class Cometa extends Recogible implements Runnable {

	private LinkedList<PVector> historia;
	private boolean vivo;
	private PVector objetivo;
	private Thread h;
	private int ran;
	private int vmax;
	private float fmax;
	private PVector vel, ac;
	private boolean borrar;
	
	public Cometa(PApplet app) {
		super(app);
		vivo = true;
		h = new Thread(this);
		img = app.loadImage("cometa.png");
		ran = (int) app.random(2);
		vmax = 5;
		fmax = 0.02f;
		if(ran == 1) {
			pos = new PVector(-20, 150);
			objetivo = new PVector(app.width+20, 150);
			vel = new PVector(1, -0.5f);
			vel.normalize();
			vel.mult(vmax);
		}else {
			pos = new PVector(app.width + 20, 485);
			objetivo = new PVector(-20, 485);
			vel = new PVector(-1, 0.5f);
			vel.normalize();
			vel.mult(vmax);
		}
		historia = new LinkedList<PVector>();
		historia.add(pos);
		ac = new PVector(0,0);
		
		
	}
	public void run() {
		while(vivo) {
			actualizar();
			perseguir(objetivo);
			try {
				h.sleep(16);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void pintar() {
		synchronized(historia) {
			Iterator<PVector> it = historia.iterator();
			int i = 0;
			while(it.hasNext()) {
					PVector p = it.next();
					int tam = 5 + i;
					int opacidad = (i*4);
					app.fill(214, 214, 214, opacidad);
					app.ellipse(p.x, p.y, tam, tam);
					i++;
			}
		}
		
		app.image(img, pos.x, pos.y);
	}
	
	public void actualizar() {
		vel.add(ac);
		vel.limit(vmax);
		pos.add(vel);
		ac.mult(0);
		PVector newP = new PVector(pos.x, pos.y);
		synchronized(historia) {
			historia.add(newP);
			if(historia.size() > 37) {
				historia.remove(0);
			}
		}
	}

	public void perseguir(PVector obj) {
		PVector deseado = PVector.sub(obj, pos);
		deseado.normalize();
		deseado.mult(vmax);
		PVector direccion = PVector.sub(deseado, vel);
		direccion.limit(fmax);
		ac.add(direccion);
	}
	
	public boolean borrar() {
		if(ran == 1 && pos.x > app.width+20) {
			return true;
		} else if(ran == 0 && pos.x < -20) {
			return true;
		} else {
			return false;
		}
	}
	
	public Thread getH() {
		return h;
	}
}
