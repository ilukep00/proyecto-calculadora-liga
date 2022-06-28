/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculadoraliga;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author lukep
 */
public class Calendario implements Runnable {
    //numero total de equipos de la competicion
    //number of the teams in the competition
    private int numEquipos;
    
    //matriz para guardar el calendario
    //matrix to save the calendar
    private int calendario[][];
    
    //matriz para la clasificacion
    //matrix for the classification
    private int clasificacion[][];
    
    //matriz puntos
    //matrix of points
    private int puntos[][];
    
    //matriz partidos
    //matrix of matches
    private int partidosJugados[][];
    
    //matriz ganados
    //matrix of matches won
     private int partidosGanados[][];
     
    //matriz empatados
     //matrix of matches drawn
      private int partidosEmpatados[][];
      
    //matriz perdidos
      //matrix of matches lost
     private int partidosPerdidos[][];
     
    //matriz goles a favor
    //matrix of goals scored
      private int golesAFavor[][];
      
    //matriz goles en contra
    //matrix of goals let  
     private int golesEnContra[][];
    
    //Array de equipos
     //array of teams 
    private String equiposLiga[];
    
    //socket del servidor
    private ServerSocket serverSocket;
    
    //puerto del servidor
    private final int port;
    
    //constructor
    public Calendario(int numEquipos, int port) {
        this.numEquipos = numEquipos;
        //crear las matrices e inicializarlas
        //create the matrix and initialized
        this.calendario = new int[this.numEquipos][this.numEquipos-1];
        this.clasificacion = new int[this.numEquipos][8];
        this.partidosJugados = new int[this.numEquipos][this.numEquipos + this.numEquipos-2];
        inicializarMat(this.partidosJugados);
        this.puntos = new int[this.numEquipos][this.numEquipos + this.numEquipos-2];
        inicializarMat(this.puntos);
        this.partidosGanados = new int[this.numEquipos][this.numEquipos + this.numEquipos-2];
        inicializarMat(this.partidosGanados);
        this.partidosEmpatados = new int[this.numEquipos][this.numEquipos + this.numEquipos-2];
        inicializarMat(this.partidosEmpatados);
        this.partidosPerdidos = new int[this.numEquipos][this.numEquipos + this.numEquipos-2];
        inicializarMat(this.partidosPerdidos);
        this.golesAFavor = new int[this.numEquipos][this.numEquipos + this.numEquipos-2];
        inicializarMat(this.golesAFavor);
        this.golesEnContra = new int[this.numEquipos][this.numEquipos + this.numEquipos-2];
        inicializarMat(this.golesEnContra);
        this.port = port;
    }
    
    //algoritmo que crea el calendario mediante divide y venceras con la funcion calen
    //algorithm that create the calendar using divide and conquer with the calen function
    public void crearCalendario(){
        int filaIni = 0;
        int filaFin= numEquipos-1;
        int columnaIni = 0;
        int columnaFin = numEquipos-2;
        calen(filaIni, filaFin, columnaIni, columnaFin);
        
    }
    
    
    public void calen(int primeraF, int ultimaF, int primeraC, int ultimaC){
        //caso base
        if(primeraF == ultimaF -1 && primeraC == ultimaC){
            calendario[primeraF][primeraC] = ultimaF+1;
            calendario[ultimaF][ultimaC] = primeraF+1;
        }else{
            int mitadF = ((ultimaF + primeraF)+1)/2;
            int mitadC = (ultimaC + primeraC)/2;
            calen(primeraF, mitadF-1,primeraC, mitadC-1);
            calen(mitadF, ultimaF,primeraC, mitadC-1);
            mezclados(primeraF, mitadF, ultimaF, mitadC, ultimaC);
            
        }
    }
    
    public void mezclados(int primeraF, int filaMitad, int ultimaF, int primeraC, int ultimaC){
        int ini = primeraC;
        int numpos = ultimaC - primeraC + 1;
        for(int i = primeraF; i < filaMitad; i++){
            for(int j = 0; j < numpos; j++){
                if(ini + j <= ultimaC){
                    calendario[i][ini+j]= filaMitad+j+1;
                    calendario[filaMitad + j][ini+j] = i+1;
                }
                else if(ini + j > ultimaC){
                    int n = (ini + j) - (ultimaC +1)+primeraC;
                    calendario[i][n]= filaMitad+j+1;
                    calendario[filaMitad + j][n] = i+1;
                }
            }
            ini++;
        }
       
        
    }
    
    //funcion para inicializar las matrices
    //funcion used to initialize the matrix
     public void inicializarMat(int mat[][]){
        for (int i = 0; i < this.numEquipos; i++) {
            for (int j = 0; j < this.numEquipos + this.numEquipos-2; j++) {
                mat[i][j] = 0;
            }
            
        }
    }
    
     //funcion para inicializar la clasificacion
     //function used to initialize the classification
     
    public void inicializarClasificacion(){
        for(int i = 0; i < numEquipos; i ++){
            for(int j = 0; j < 8; j++){
                if(j == 0){
                    clasificacion[i][j] = i+1;
                }else{
                    clasificacion[i][j] = 0;
                }
            }
        }
    }
    
    //funcion para inicializar el array de los equipos
    //function used to initialize the array of teams
    public void inicializarEquipos(){

        equiposLiga = new String[16];
        equiposLiga[15] = "EL POZO MURCIA";
        equiposLiga[14] = "MANZANARES FS";
        equiposLiga[13] = "PALMA FUTSAL";
        equiposLiga[9] = "NOIA PORTUS APOSTOLI";
        equiposLiga[8] = "BARCELONA LASSA";
        equiposLiga[7] = "JAEN FS";
        equiposLiga[3] = "JIMBEE CARTAGENA";
        equiposLiga[2] = "CORDOBA PATRIMONIO";
        equiposLiga[1] = "LEVANTE UD FS";
        equiposLiga[0] = "XOTA FS";
        equiposLiga[4] = "INTER FS";
        equiposLiga[5] = "RIBERA NAVARRA FS";
        equiposLiga[6] = "UMA ANTEQUERA FS";
        equiposLiga[10] = "REAL BETIS FUTSAL";
        equiposLiga[11] = "V.A. VALDEPEÑAS";
        equiposLiga[12] = "INDUSTRIAS STA COLOMA";
        
        
    }
    
    //hilo
    //thread
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Started server on " + port);
            Socket clientSocket1 = serverSocket.accept();
            System.out.println("Accepted connection from " + clientSocket1.getInetAddress() + ":" + clientSocket1.getPort());
            boolean finish = false;
            while(finish == false){
                //acepto el cliente 1
                Comunicaciones comunicaciones = new Comunicaciones(clientSocket1);
                String msg = comunicaciones.receiveMsg();
                if("START".equals(msg)){
                     inicializarClasificacion();
                    inicializarMat(partidosJugados);
                    inicializarMat(puntos);
                    inicializarMat(partidosGanados);
                    inicializarMat(partidosEmpatados);
                    inicializarMat(partidosPerdidos);
                    inicializarMat(golesAFavor);
                    inicializarMat(golesEnContra);
                    comunicaciones.sendMsg("RESET");
                    comunicaciones.sendMsg("CLASIFICATION");
                    for(int i = 0; i < numEquipos; i++){
                        String line = "";
                        for(int j = 0; j < 8; j ++){
                            if(j == 0){
                                line = line + equiposLiga[clasificacion[i][j]-1] + " - ";
                            }else{
                                line = line + clasificacion[i][j] + " - ";
                            }
                        }
                        comunicaciones.sendMsg(line);
                    }
                    comunicaciones.sendMsg("JORNADA");
                    for(int i = 0; i < 16; i++){
                        if(calendario[i][0] > i+1){
                            //String line = equipos.get(i+1) + " - " + equipos.get(calendario[i][0]);
                            String line = equiposLiga[i] + " - " + equiposLiga[calendario[i][0]-1];
                            comunicaciones.sendMsg(line);
                        }
                    }
                }else if("FINISH".equals(msg)){
                     comunicaciones.sendMsg("FINISH");
                    try {
                        Thread.sleep(2000);
                        comunicaciones.socketClose();
                        finish = true;
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Calendario.class.getName()).log(Level.SEVERE, null, ex);
                    }
                     
                }else{
                    String parts[] = msg.split(" - ");
                    if("JORNADA".equals(parts[0])){
                        Integer jornada = Integer.parseInt(parts[1]);
                        enviarJornada(jornada-1, comunicaciones);
                    }else if("PARTIDO".equals(parts[0])){
                        try{
                            String equipo1[] = parts[1].split(" : ");
                            int numeroEquipo1 = numeroEquipo(equipo1[0]);
                            int golesEquipo1 = Integer.parseInt(equipo1[1]);
                            int puntuacionEquipo1 = 0;
                            //------
                            String equipo2[] = parts[2].split(" : ");
                            int numeroEquipo2 = numeroEquipo(equipo2[1]);
                            int golesEquipo2 = Integer.parseInt(equipo2[0]);
                            int puntuacionEquipo2 = 0;

                            if(golesEquipo1 > golesEquipo2){
                                puntuacionEquipo1 = 3;
                                puntuacionEquipo2 = 0;
                            }else if(golesEquipo1 < golesEquipo2){
                                puntuacionEquipo1 = 0;
                                puntuacionEquipo2 = 3;
                            }else if(golesEquipo1 == golesEquipo2){
                                puntuacionEquipo1 = 1;
                                puntuacionEquipo2 = 1;
                            }
                            String jornada[] = parts[3].split(" ");
                            int jornadita = Integer.parseInt(jornada[1]);

                            actualizarPosEquipo( puntuacionEquipo1,golesEquipo1, golesEquipo2, numeroEquipo1,jornadita-1);
                            actualizarPosEquipo( puntuacionEquipo2,golesEquipo2, golesEquipo1, numeroEquipo2,jornadita-1);
                            comunicaciones.sendMsg("CLASIFICATION");
                            for(int i = 0; i < numEquipos; i++){
                                String line = "";
                                for(int j = 0; j < 8; j ++){
                                    if(j == 0){
                                        line = line + equiposLiga[clasificacion[i][j]-1] + " - ";
                                    }else{
                                        line = line + clasificacion[i][j] + " - ";
                                    }
                                }
                                comunicaciones.sendMsg(line);
                            }
                        }catch(NumberFormatException e){
                            
                            System.out.println("Formato invalido");
                        } 
                   }
                }
                
                
             }
            
        } catch (IOException ex) {
            Logger.getLogger(Calendario.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Calendario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    public void actualizarPosEquipo(int puntuacion, int golesAF, int golesEnC, int numEquipo, int jornada){
        int linea = 0;
        int i = 0;
        boolean encontrado = false;
        while(encontrado == false && i < 16){

            if(clasificacion[i][0] == numEquipo){
                linea = i;
                encontrado = true;
            }else{
                i++;
            }
        }

        if(puntuacion == 3){
            puntos[numEquipo-1][jornada] = 3;
            partidosJugados[numEquipo-1][jornada] = 1;
            partidosGanados[numEquipo-1][jornada] = 1;
            partidosEmpatados[numEquipo-1][jornada] = 0;
            partidosPerdidos[numEquipo-1][jornada] = 0;
            golesAFavor[numEquipo-1][jornada] = golesAF;
            golesEnContra[numEquipo-1][jornada] = golesEnC;
            //clasificacion[linea][3] = clasificacion[linea][3] + 1;
        }else if(puntuacion == 1){
            puntos[numEquipo-1][jornada] = 1;
            partidosJugados[numEquipo-1][jornada] = 1;
            partidosGanados[numEquipo-1][jornada] = 0;
            partidosEmpatados[numEquipo-1][jornada] = 1;
            partidosPerdidos[numEquipo-1][jornada] = 0;
            golesAFavor[numEquipo-1][jornada] = golesAF;
            golesEnContra[numEquipo-1][jornada] = golesEnC;
            //clasificacion[linea][4] = clasificacion[linea][4] + 1;
        }else if(puntuacion == 0){
            puntos[numEquipo-1][jornada] = 0;
            partidosJugados[numEquipo-1][jornada] = 1;
            partidosGanados[numEquipo-1][jornada] = 0;
            partidosEmpatados[numEquipo-1][jornada] = 0;
            partidosPerdidos[numEquipo-1][jornada] = 1;
            golesAFavor[numEquipo-1][jornada] = golesAF;
            golesEnContra[numEquipo-1][jornada] = golesEnC;
            //clasificacion[linea][5] = clasificacion[linea][5] + 1;
        }
        clasificacion[linea][1] = sumaFila(numEquipo-1,puntos);
        clasificacion[linea][2] = sumaFila(numEquipo-1,partidosJugados);
        clasificacion[linea][3] = sumaFila(numEquipo-1,partidosGanados);
        clasificacion[linea][4] = sumaFila(numEquipo-1,partidosEmpatados);
        clasificacion[linea][5] = sumaFila(numEquipo-1,partidosPerdidos);
        clasificacion[linea][6] = sumaFila(numEquipo-1,golesAFavor);
        clasificacion[linea][7] = sumaFila(numEquipo-1,golesEnContra);
        if(puntuacion == 3){
            int j = linea;
            int[] aux = new int[8];
            while(j > 0 && clasificacion[j][1] > clasificacion[j-1][1]){
                aux = clasificacion[j-1];
                clasificacion[j-1] = clasificacion[j];
                clasificacion[j] = aux;
                j--;
            }
            if(j > 0){
                if(clasificacion[j][1] == clasificacion[j-1][1]){
                    while(j > 0 && clasificacion[j][1] == clasificacion[j-1][1] && clasificacion[j][6]-clasificacion[j][7] > clasificacion[j-1][6]-clasificacion[j-1][7] ){
                        aux = clasificacion[j-1];
                        clasificacion[j-1] = clasificacion[j];
                        clasificacion[j] = aux;
                        j--;
                    }
                    if(j > 0){
                        if(clasificacion[j][1] == clasificacion[j-1][1] && clasificacion[j][6]-clasificacion[j][7] == clasificacion[j-1][6]-clasificacion[j-1][7]){
                            while(j > 0 &&  clasificacion[j][1] == clasificacion[j-1][1] && clasificacion[j][6]-clasificacion[j][7] == clasificacion[j-1][6]-clasificacion[j-1][7]&& clasificacion[j][6] > clasificacion[j-1][6]){
                                 aux = clasificacion[j-1];
                                clasificacion[j-1] = clasificacion[j];
                                clasificacion[j] = aux;
                                j--;
                            }
                        }
                    }
                }
            }
                
        }else if(puntuacion == 0){
             int j = linea;
             int[] aux = new int[8];
              while(j < 15 && clasificacion[j][1] < clasificacion[j+1][1]){
                aux = clasificacion[j+1];
                clasificacion[j+1] = clasificacion[j];
                clasificacion[j] = aux;
                j++;
            }
            if(j < 15 && clasificacion[j][1] == clasificacion[j+1][1]){
                    while(j < 15 && clasificacion[j][1] == clasificacion[j+1][1] && clasificacion[j][6]-clasificacion[j][7] < clasificacion[j+1][6]-clasificacion[j+1][7] ){
                        aux = clasificacion[j+1];
                        clasificacion[j+1] = clasificacion[j];
                        clasificacion[j] = aux;
                        j++;
                    }
                    if(j < 15){
                        if(clasificacion[j][1] == clasificacion[j+1][1] &&clasificacion[j][6]-clasificacion[j][7] == clasificacion[j+1][6]-clasificacion[j+1][7]){
                            while(j < 15 &&  clasificacion[j][1] == clasificacion[j+1][1] && clasificacion[j][6]-clasificacion[j][7] == clasificacion[j+1][6]-clasificacion[j+1][7]&& clasificacion[j][6] < clasificacion[j+1][6]){
                                 aux = clasificacion[j+1];
                                clasificacion[j+1] = clasificacion[j];
                                clasificacion[j] = aux;
                                j++;
                            }
                        }
                    }
                }
            
        }else if(puntuacion == 1){
            int j = linea;
            if(j > 0 && clasificacion[j][1] >= clasificacion[j-1][1]){
                int[] aux = new int[8];
                while(j > 0 && clasificacion[j][1] > clasificacion[j-1][1]){
                    aux = clasificacion[j-1];
                    clasificacion[j-1] = clasificacion[j];
                    clasificacion[j] = aux;
                    j--;
                }
                if(j > 0){
                    if(clasificacion[j][1] == clasificacion[j-1][1]){
                        while(j > 0 && clasificacion[j][1] == clasificacion[j-1][1] && clasificacion[j][6]-clasificacion[j][7] > clasificacion[j-1][6]-clasificacion[j-1][7] ){
                            aux = clasificacion[j-1];
                            clasificacion[j-1] = clasificacion[j];
                            clasificacion[j] = aux;
                            j--;
                        }
                        if(j > 0){
                            if(clasificacion[j][1] == clasificacion[j-1][1] && clasificacion[j][6]-clasificacion[j][7] == clasificacion[j-1][6]-clasificacion[j-1][7]){
                                while(j > 0 &&  clasificacion[j][1] == clasificacion[j-1][1] && clasificacion[j][6]-clasificacion[j][7] == clasificacion[j-1][6]-clasificacion[j-1][7]&& clasificacion[j][6] > clasificacion[j-1][6]){
                                     aux = clasificacion[j-1];
                                    clasificacion[j-1] = clasificacion[j];
                                    clasificacion[j] = aux;
                                    j--;
                                }
                            }
                        }
                    }
                 }
            }else if(j < 15 && clasificacion[j][1] <= clasificacion[j+1][1]){
                 int[] aux = new int[8];
              while(j < 15 && clasificacion[j][1] < clasificacion[j+1][1]){
                aux = clasificacion[j+1];
                clasificacion[j+1] = clasificacion[j];
                clasificacion[j] = aux;
                j++;
            }
            if(j < 15 && clasificacion[j][1] == clasificacion[j+1][1]){
                    while(j < 15 && clasificacion[j][1] == clasificacion[j+1][1] && clasificacion[j][6]-clasificacion[j][7] < clasificacion[j+1][6]-clasificacion[j+1][7] ){
                        aux = clasificacion[j+1];
                        clasificacion[j+1] = clasificacion[j];
                        clasificacion[j] = aux;
                        j++;
                    }
                    if(j < 15){
                        if(clasificacion[j][1] == clasificacion[j+1][1] &&clasificacion[j][6]-clasificacion[j][7] == clasificacion[j+1][6]-clasificacion[j+1][7]){
                            while(j < 15 &&  clasificacion[j][1] == clasificacion[j+1][1] && clasificacion[j][6]-clasificacion[j][7] == clasificacion[j+1][6]-clasificacion[j+1][7]&& clasificacion[j][6] < clasificacion[j+1][6]){
                                 aux = clasificacion[j+1];
                                clasificacion[j+1] = clasificacion[j];
                                clasificacion[j] = aux;
                                j++;
                            }
                        }
                    }
                }
            }
        }
        
    }
    
    public int numeroEquipo(String nombre){
        int i = 0;
        boolean encontrado = false;
        int num = 0;
        while(i < 16 && encontrado == false){
            if(nombre.equals(equiposLiga[i])){
                num = i+1;
                encontrado = true;
            }
            i++;
        }
        return num;
    }
    
    public int sumaFila(int fila, int Mat[][]){
        int suma = 0;
        for(int i = 0; i < this.numEquipos + this.numEquipos-2; i++){
            suma = suma + Mat[fila][i];
        }
        return suma;
    }
    public void enviarJornada(int jornada, Comunicaciones comunicaciones){
        try {
            comunicaciones.sendMsg("JORNADA");
            if(jornada < 15){
                for(int i = 0; i < 16; i++){
                    if(jornada %2 == 0){
                        if(calendario[i][jornada] > i+1){
                                //String line = equipos.get(i+1) + " - " + equipos.get(calendario[i][jornada]);
                                String line = equiposLiga[i] + " - " + equiposLiga[calendario[i][jornada]-1];
                                comunicaciones.sendMsg(line);

                        }
                    }else{
                        if(calendario[i][jornada] < i+1){

                                String line = equiposLiga[i] + " - " + equiposLiga[calendario[i][jornada]-1];
                                comunicaciones.sendMsg(line);
                        }
                    }
                }
            }else{
                jornada = jornada -15;
                for(int i = 0; i < 16; i++){
                    if(jornada %2 == 0){
                        if(calendario[i][jornada] < i+1){
                                String line = equiposLiga[i] + " - " + equiposLiga[calendario[i][jornada]-1];
                                comunicaciones.sendMsg(line);

                        }
                    }else{
                        if(calendario[i][jornada] > i+1){

                               String line = equiposLiga[i] + " - " + equiposLiga[calendario[i][jornada]-1];
                                comunicaciones.sendMsg(line);
                        }
                    }
                }
                
            }       
        } catch (IOException ex) {
            Logger.getLogger(Calendario.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    
}
