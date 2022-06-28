/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculadoraliga;

/**
 *
 * @author lukep
 */
public class Inicio {
    
    public static void main(String[] args){
        Calendario calendario = new Calendario(16, 12000);
        calendario.crearCalendario();
        calendario.inicializarClasificacion();
        calendario.inicializarEquipos();
        Thread hiloServ = new Thread(calendario);
        hiloServ.start();
        
        Clasificacion clasificacion = new Clasificacion();
        Usuario player = new Usuario("localhost", 12000,  clasificacion);
        Thread hiloJugador = new Thread(player);
        hiloJugador.start();
    }
}
