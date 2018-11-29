package bravo_correa_perez_quevedo_vivas;

import processing.core.PApplet;

public class Main extends PApplet {
	
	private Logica log;
	
	public static void main(String[] args) {
		PApplet.main("bravo_correa_perez_quevedo_vivas.Main");
	}
	
	public void settings() {
		size(1200, 675);
	}
	
	public void setup() {
		log = new Logica(this);
		imageMode(CENTER);
		noStroke();
		noCursor();
		strokeWeight(3);
	}
	
	public void draw() {
		log.pintar();
	}
	
	public void keyPressed() {
		log.tecla();
	}
	
	public void mousePressed() {
		log.clic();
	}
}

