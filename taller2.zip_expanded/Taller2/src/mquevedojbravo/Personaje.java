package mquevedojbravo;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public abstract class Personaje extends Thread {
	protected PApplet app;
	protected PVector pos;
	protected PVector vel;
	protected PVector ac;
	protected float fmax;
	protected float velmax;
	protected float ang;
	protected int estrellas;
	protected boolean vivo;
	protected PImage img;
	
	public Personaje(PApplet app) {
		this.app = app;
		ac = new PVector(0, 0);
		vel = new PVector(0, 0);
		estrellas = 0;
		vivo = true;
	}

	public abstract void pintar();
	
	public abstract void run(); 
	
	public void actualizar() {
		vel.add(ac);
		vel.limit(velmax);
		pos.add(vel);
		ac.mult(0);
	}
	
	
	public void perseguir(PVector obj) {
		PVector deseado = PVector.sub(obj, pos);
		deseado.normalize();
		deseado.mult(velmax);
		PVector direccion = PVector.sub(deseado, vel);
		direccion.limit(fmax);
		ac.add(direccion);
	}
	
	public void setVivo(boolean vivo) {
		this.vivo = vivo;
	}
}
