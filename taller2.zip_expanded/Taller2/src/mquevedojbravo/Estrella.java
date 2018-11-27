package mquevedojbravo;

import processing.core.PApplet;
import processing.core.PVector;

public class Estrella extends Recogible {

	private float ang;
	
	public Estrella(PApplet app) {
		super(app);
		img = app.loadImage("estrella.png");
		pos = new PVector(app.random(100 ,app.width-100), app.random(50, app.height-50));
	}

	public void pintar() {
		app.pushMatrix();
		app.translate(pos.x, pos.y);
		app.rotate(ang);
		app.image(img, 0, 0);
		app.popMatrix();
		ang += app.PI/450;
	}

}
