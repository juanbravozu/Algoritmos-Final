package mquevedojbravo;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public abstract class Recogible{

	protected PApplet app;
	protected PImage img;
	protected PVector pos;
	
	public Recogible(PApplet app) {
		this.app = app;
	}
	public abstract void pintar();
	
	public PVector getPos() {
		return pos;
	}
}
