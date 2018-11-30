package bravo_correa_perez_quevedo_vivas;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.sound.SoundFile;

public class Logica {

	private PApplet app;
	private Mundo m;
	private int pantalla;
	private PImage[] menus;
	private SoundFile soundMenu;
	private int estrellas;
	private int estrellas1;
	private int estrellas2;
	private int contOvnis;
	private int contOvnis1;
	private int contOvnis2;
	private int cometas;
	private int cometas1;
	private int cometas2;
	private int agujeros;
	private int agujeros1;
	private int agujeros2;
	private PFont mali;
	
	/**
	 * Pantalla 0 - Menu Inicio
	 * Pantalla 1 - Juego
	 * Pantalla 2 - Inst1
	 * Pantalla 3 - Inst2
	 * Pantalla 4 - Inst3
	 * Pantalla 5 - Inst4
	 * Pantalla 6 - Ganaste
	 * Pantalla 7 - Perdiste
	 * Pantalla 8 - Seleccion modo
	 * Pantalla 9 - Gana Jugador 1
	 * Pantalla 10 - Gana jugador 2
	 */
	
	public Logica(PApplet app) {
		this.app = app;
		m = null;
		pantalla = 0;
		mali = app.loadFont("maliB_47.vlw");
		menus = new PImage[23];
		for(int i = 0; i < menus.length; i++) {
			menus[i] = app.loadImage("menu" + i + ".png");
		}
		
		soundMenu = new SoundFile(app, "musicaMenu.wav");
		soundMenu.loop();
	}

	public void pintar() {
		
		switch(pantalla) {
		case 0:
			interaccionMenu();
			break;
		
		case 1:
			if(m.getState().name().equals("NEW")) {
				m.start();
			}
			m.pintar();
			if(soundMenu.isPlaying()) {
				soundMenu.stop();
			}
			if(m.terminarJuego()) {
				if(m instanceof MundoCooperativo) {
					estrellas = ((MundoCooperativo)m).getEstrellasTotal();
					contOvnis = ((MundoCooperativo)m).getContadorOvnis();
					agujeros = m.getJ()[0].getAgujerosTotal()+m.getJ()[1].getAgujerosTotal();
					cometas = m.getJ()[0].getCometasTotal()+m.getJ()[1].getCometasTotal();
					if(m.getGanar()) {
						pantalla = 6;
					}else {
						pantalla = 7;
					}
				} else {
					estrellas1 = m.getJ()[0].getEstrellas();
					contOvnis1 = m.getJ()[0].getContOvnis();
					agujeros1 = m.getJ()[0].getAgujerosTotal();
					cometas1 = m.getJ()[0].getCometasTotal();
					
					estrellas2 = m.getJ()[1].getEstrellas();
					contOvnis2 = m.getJ()[1].getContOvnis();
					agujeros2 = m.getJ()[1].getAgujerosTotal();
					cometas2 = m.getJ()[1].getCometasTotal();
					if(m.getGanar()) {
						pantalla = 9;
					} else {
						pantalla = 10;
					}
				}
				m.pararMus();
				m = null;
				soundMenu.loop();
			}
			if(m != null && m.getMatar()) {
				m.pararMus();
				if(m instanceof MundoCooperativo) {
					pantalla = 7;
					estrellas = m.getJu().getEstrellasTotal();
					contOvnis = m.getJu().getContOvnis();
					agujeros = m.getJu().getAgujerosTotal();
					cometas = m.getJu().getCometasTotal();
				} else {
					estrellas1 = m.getJ()[0].getEstrellas();
					contOvnis1 = m.getJ()[0].getContOvnis();
					agujeros1 = m.getJ()[0].getAgujerosTotal();
					cometas1 = m.getJ()[0].getCometasTotal();
					
					estrellas2 = m.getJ()[1].getEstrellas();
					contOvnis2 = m.getJ()[1].getContOvnis();
					agujeros2 = m.getJ()[1].getAgujerosTotal();
					cometas2 = m.getJ()[1].getCometasTotal();
					if(estrellas1 > estrellas2) {
						pantalla = 9;
					} else {
						pantalla = 10;
					}
				}
				m = null;
				soundMenu.loop();
			}
			
			break;
			
		case 6:
			ganaste();
			app.textFont(mali);
			app.text(estrellas, 452.18f, 330f);
			app.text(contOvnis, 818.3f, 330f);
			app.text(agujeros, 818.3f, 450.92f);
			app.text(cometas, 452.18f, 450.92f);
			break;
			
		case 7:
			perdiste();
			app.textFont(mali);
			app.text(estrellas, 452.18f, 330f);
			app.text(contOvnis, 818.3f, 330f);
			app.text(agujeros, 818.3f, 450.92f);
			app.text(cometas, 452.18f, 450.92f);
			break;
			
		case 8:
			interaccionSeleccion();
			break;
			
		case 9:
			ganaJugadorUno();
			app.text(estrellas1, 352, 276);
			app.text(contOvnis1, 352f, 355);
			app.text(agujeros1, 352f, 434);
			app.text(cometas1, 352f, 512);
			
			app.text(estrellas2, 942, 276);
			app.text(contOvnis2, 942, 355);
			app.text(agujeros2, 942, 434);
			app.text(cometas2, 942, 512);
			break;
			
		case 10:
			ganaJugadorDos();
			app.text(estrellas1, 352, 276);
			app.text(contOvnis1, 352f, 355);
			app.text(agujeros1, 352f, 434);
			app.text(cometas1, 352f, 512);
			
			app.text(estrellas2, 942, 276);
			app.text(contOvnis2, 942, 355);
			app.text(agujeros2, 942, 434);
			app.text(cometas2, 942, 512);
			break;
		}
		interInstru();
		app.stroke(255,100);
		app.noFill();
		app.ellipse(app.mouseX, app.mouseY, 10, 10);
	}
	
	public void interaccionMenu() {
		int x = app.mouseX;
		int y = app.mouseY;
		float posX = app.width/2;
		float posY = app.height/2;
		if(x > posX - 72.82f && x < posX + 72.67f && y > posY + 86.75f && y < posY + 145.16f){
			app.image(menus[1], posX, posY);
		} else if(x > posX - 158.29f && x < posX + 161.94f && y > posY + 161.94f && y < posY + 207.66f){
			app.image(menus[2], posX, posY);
		} else if(x > posX - 56.32f && x < posX + 56.55f && y > posY + 236.77f && y < posY + 283.98f) {
			app.image(menus[3], posX, posY);
		}else {
			app.image(menus[0], posX, posY);
		}
	}
	
	public void interaccionSeleccion() {
		int x = app.mouseX;
		int y = app.mouseY;
		float posX = app.width/2;
		float posY = app.height/2;
		if(x > 130.64f && y > 293.47f && x < 550.62f && y < 500.89f){
			app.image(menus[17], posX, posY);
		} else if(x > 649.88f && y > 293.47f && x < 1069.86f && y < 500.89f) {
			app.image(menus[18], posX, posY);
		} else {
			app.image(menus[16], posX, posY);
		}
	}
	
	public void ganaJugadorUno() {
		int x = app.mouseX;
		int y = app.mouseY;
		float posX = app.width/2;
		float posY = app.height/2;
		if(x > 455 && x < 749 && y > 555 && y < 623) {
			app.image(menus[20], posX, posY);
		}else {
			app.image(menus[19], posX, posY);
		}
	}
	
	public void ganaJugadorDos() {
		int x = app.mouseX;
		int y = app.mouseY;
		float posX = app.width/2;
		float posY = app.height/2;
		if(x > 455 && x < 749 && y > 555 && y < 623) {
			app.image(menus[22], posX, posY);
		}else {
			app.image(menus[21], posX, posY);
		}
	}
	
	public void interInstru() {
		int x = app.mouseX;
		int y = app.mouseY;
		float posX = app.width/2;
		float posY = app.height/2;
		switch(pantalla) {
		case 2:	
		if(x > posX - 105.41f && x < posX + 108.13f && y > posY + 219.98f && y < posY + 271.36f) {
			app.image(menus[5], posX, posY);
		}else {
			app.image(menus[4], posX, posY);
		}
			
			break;
		
		case 3:
			if(x > posX - 105.41f && x < posX + 108.13f && y > posY + 219.98f && y < posY + 271.36f) {
				app.image(menus[7], posX, posY);
			}else {
				app.image(menus[6], posX, posY);
			}
			break;
			
		case 4:
			if(x > posX - 105.41f && x < posX + 108.13f && y > posY + 219.98f && y < posY + 271.36f) {
				app.image(menus[9], posX, posY);
			}else {
				app.image(menus[8], posX, posY);
			}
			break;
			
		case 5:			
			if(x > posX - 69.16f && x < posX + 70.97f && y > posY + 218.9f && y < posY + 272.66f) {
				app.image(menus[11], posX, posY);
			}else {
				app.image(menus[10], posX, posY);
			}
			break;
			
		}
	}
	
	public void perdiste(){
		int x = app.mouseX;
		int y = app.mouseY;
		float posX = app.width/2;
		float posY = app.height/2;
		if(x > posX - 105.41f && x < posX + 108.13f && y > posY + 219.98f && y < posY + 271.36f) {
			app.image(menus[13], posX, posY);
		}else {
			app.image(menus[12], posX, posY);
		}
	}
	
	public void ganaste() {
		int x = app.mouseX;
		int y = app.mouseY;
		float posX = app.width/2;
		float posY = app.height/2;
		if(x > posX - 105.41f && x < posX + 108.13f && y > posY + 219.98f && y < posY + 271.36f) {
			app.image(menus[15], posX, posY);
		}else {
			app.image(menus[14], posX, posY);
		}
	}
	
	public void clic() {
		int x = app.mouseX;
		int y = app.mouseY;
		float posX = app.width/2;
		float posY = app.height/2;
		switch(pantalla) {
		case 0:
			if(x > posX - 72.82f && x < posX + 72.67f && y > posY + 86.75f && y < posY + 145.16f){
				pantalla = 8;
			} else if(x > posX - 158.29f && x < posX + 161.94f && y > posY + 161.94f && y < posY + 207.66f){
				pantalla = 2;
			} else if(x > posX - 56.32f && x < posX + 56.55f && y > posY + 236.77f && y < posY + 283.98f) {
				app.exit();
			}
			break;
		
		case 1:
			break;
			
		case 2:
			if(x > posX - 105.41f && x < posX + 108.13f && y > posY + 219.98f && y < posY + 271.36f) {
				pantalla = 3;
			}
			break;
			
		case 3:
			if(x > posX - 105.41f && x < posX + 108.13f && y > posY + 219.98f && y < posY + 271.36f) {
				pantalla = 4;
			}
			break;
			
		case 4:
			if(x > posX - 105.41f && x < posX + 108.13f && y > posY + 219.98f && y < posY + 271.36f) {
				pantalla = 5;
			}
			break;
			
		case 5:			
			if(x > posX - 69.16f && x < posX + 70.97f && y > posY + 218.9f && y < posY + 272.66f) {
				pantalla = 8;
			}
			break;
			
		case 6:
			if(x > posX - 105.41f && x < posX + 108.13f && y > posY + 219.98f && y < posY + 271.36f) {
				pantalla = 0;
			}
			break;
			
		case 7:
			if(x > posX - 105.41f && x < posX + 108.13f && y > posY + 219.98f && y < posY + 271.36f) {
				pantalla = 0;
			}
			break;
			
		case 8:
			if(x > 130.64f && y > 293.47f && x < 550.62f && y < 500.89f){
				m = new MundoVersus(app);
				pantalla = 1;
			} else if(x > 649.88f && y > 293.47f && x < 1069.86f && y < 500.89f) {
				m = new MundoCooperativo(app);
				pantalla = 1;
			}
			break;
			
		case 9:
			if(x > 455 && x < 749 && y > 555 && y < 623) {
				pantalla = 0;
			}
			break;
			
		case 10:
			if(x > 455 && x < 749 && y > 555 && y < 623) {
				pantalla = 0;
			}
			break;
		}
	}
	
	public void tecla() {
		if(m != null) {
			m.tecla();
		}
		
	}
}