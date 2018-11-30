package bravo_correa_perez_quevedo_vivas;

import processing.core.PApplet;
import processing.core.PVector;

public class Eclipse extends Recogible {

	public Eclipse(PApplet app) {
		super(app);
		img = app.loadImage("eclipse.png");
		pos = new PVector(app.random(100 ,app.width-100), app.random(50, app.height-50));
	}

	public void pintar() {
		app.image(img, pos.x, pos.y);
	}

}
