/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculadoraliga;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;

/**
 *
 * @author lukep
 */
public class Usuario implements Runnable {
    //direccion del servidor del usuaurio
    private String hostAddr;
    //puerto del servidor del usuario
    private int port;
     // esta conectado no
    private boolean conectado = false;
    //interfaz del jugador(tablero)
    private Clasificacion clasificacion; 
     //socket con el servidor del juego
    private Socket socket;
    //comunicaciones del juego
    private Comunicaciones comunicationsJ;

    public Usuario(String hostAddr, int port, Clasificacion clasificacion) {
        try {
            this.hostAddr = hostAddr;
            this.port = port;
            this.clasificacion = clasificacion;
            this.clasificacion.setVisible(true);
            //creo el socket del servidor del juego
            this.socket = new Socket(hostAddr,port);
            this.comunicationsJ = new Comunicaciones(socket);
            clasificacion.setComunications(comunicationsJ);
        } catch (IOException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            boolean finish = false;
            while(finish == false){
                JButton[][] array = clasificacion.getMatBotones();
                JButton[][] array2 = clasificacion.getMatPartidos();
                String mensaje = comunicationsJ.receiveMsg();
                if("RESET".equals(mensaje)){
                    clasificacion.getjTextField17().setText("JORNADA 1");
                    clasificacion.inicializarJornadas();
                    clasificacion.getjTextField1().setText("");
                    clasificacion.getjTextField2().setText("");
                    clasificacion.getjTextField3().setText("");
                    clasificacion.getjTextField4().setText("");
                    clasificacion.getjTextField5().setText("");
                    clasificacion.getjTextField6().setText("");
                    clasificacion.getjTextField7().setText("");
                    clasificacion.getjTextField8().setText("");
                    clasificacion.getjTextField9().setText("");
                    clasificacion.getjTextField10().setText("");
                    clasificacion.getjTextField11().setText("");
                    clasificacion.getjTextField12().setText("");
                    clasificacion.getjTextField13().setText("");
                    clasificacion.getjTextField14().setText("");
                    clasificacion.getjTextField15().setText("");
                    clasificacion.getjTextField16().setText("");
                }else if("FINISH".equals(mensaje)){
                    comunicationsJ.socketClose();
                    finish = true;
                }
                else if("CLASIFICATION".equals(mensaje)){
                    int i = 0;
                    while(i < 16){
                        String msg = comunicationsJ.receiveMsg();
                        //System.out.println(msg);

                        String[] parts = msg.split(" - ");
                        //System.out.println(parts.length);
                        for(int j = 0; j < 8; j++){
                            array[i][j].setText(parts[j]);
                        }
                        i++;
                    }
                }else if("JORNADA".equals(mensaje)){
                   int i = 0;
                    while(i < 8){
                        String msg = comunicationsJ.receiveMsg();
                        //System.out.println(msg);

                        String[] parts = msg.split(" - ");
                        //System.out.println(parts.length);
                        for(int j = 0; j < 2; j++){
                            array2[i][j].setText(parts[j]);
                        }
                        i++;
                    }
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
