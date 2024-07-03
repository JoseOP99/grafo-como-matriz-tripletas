package practica3;

import practica3.modelo.MatrizEnTripletas;
import practica3.modelo.Tripleta;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class GrafoComoMatrizEnTripletas {

    private MatrizEnTripletas grafoEnTripletas;
    private Tripleta tripleta;
    private int vertices;

    //Leemos el archivo y los cargamos a una matriz en tripletas
    public void cargarArchivo() {
        try {
            //Accedemos al archivo, al libro y la hoja
            FileInputStream archivo = new FileInputStream("Matrizdetiempos1.xlsx");
            XSSFWorkbook libro;
            XSSFSheet hoja;
            libro = new XSSFWorkbook(archivo);
            hoja = libro.getSheetAt(0);
            //Creamos iteradores para movernos por las filas
            Iterator<Row> iteradorFila = hoja.iterator();
            Row fila;
            //Avanzamos el iterador 
            iteradorFila.next();
            //Creamos la tripleta 0 y creamos la matriz en tripletas que representara al grafo
            vertices = hoja.getPhysicalNumberOfRows() - 1;
            tripleta = new Tripleta(vertices, vertices, 0);
            grafoEnTripletas = new MatrizEnTripletas(tripleta);
            //Comenzamos el recorrido
            while (iteradorFila.hasNext()) {
                fila = iteradorFila.next();
                //Creamos iteradores para movernos por las celdas
                Iterator<Cell> iteradorCelda = fila.cellIterator();
                Cell celda;
                iteradorCelda.next();
                while (iteradorCelda.hasNext()) {
                    celda = iteradorCelda.next();
                    //Creamos una nueva tripleta y la insertamos al grafo como matriz en tripletas
                    tripleta = new Tripleta(celda.getRowIndex(), celda.getColumnIndex(), (int) celda.getNumericCellValue());
                    grafoEnTripletas.insertaTripleta(tripleta);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println(e);
        }
    }

    //Creamos un vector del tamaño de los vertices en el que se van a almacenar los grados de salida de los vertices
    public int[] gradoSaliente() {
        int[] gradoS = new int[grafoEnTripletas.getFilas()];
        //Leemos las tripletas y aumentamos el vector de la posicion corresponndiente al numero de la fila de esa tripleta
        for (int i = 1; i <= grafoEnTripletas.getNumeroTripletas(); i++) {
            gradoS[grafoEnTripletas.getTripleta(i).getFila() - 1]++;
        }
        return gradoS;
    }

    //Imprimime los grados de salida de un grafo, que corresponden a las bases que no pueden enviar mensajes
    public void mostrarGradoSaliente(int[] gradoS) {
        int contador = 0;
        String cadena = "Bases que no pueden enviar mensajes\n";
        for (int i = 0; i < gradoS.length; i++) {
            if (gradoS[i] == 0) {
                cadena = cadena + " Base " + (i + 1) + " - ";
                contador++;
            }
        }
        cadena = cadena.substring(0, cadena.length() - 2);
        if (contador > 0) {
            JOptionPane.showMessageDialog(null, cadena);
        } else {
            JOptionPane.showMessageDialog(null, "Todas las bases pueden enviar mensajes", "Bases", JOptionPane.WARNING_MESSAGE);
        }
    }

    //Creamos un vector del tamaño de los vertices en el que se van a almacenar los grados de entrada de los vertices
    public int[] gradoEntrante() {
        //Leemos las tripletas y aumentamos el vector de la posicion corresponndiente al numero de la columna de esa tripleta
        int[] gradoE = new int[grafoEnTripletas.getFilas()];
        for (int i = 1; i <= grafoEnTripletas.getNumeroTripletas(); i++) {
            gradoE[grafoEnTripletas.getTripleta(i).getColumna() - 1]++;
        }
        return gradoE;
    }

    //Creamos un vector del tamaño de los vertices en el que se van a almacenar los grados totales de los vertices
    public int[] gradoTotal(int[] gradoEntrada, int[] gradoSalida) {
        //Recibe como parametro los grados de entrada y salida del grafo y los suma
        int[] gradoT = new int[grafoEnTripletas.getFilas()];
        for (int i = 1; i <= gradoT.length; i++) {
            gradoT[i - 1] = gradoEntrada[i - 1] + gradoSalida[i - 1];
        }
        return gradoT;
    }

    //Imprimime los grados totales de un grafo, que corresponden a las bases que estan aisladas o no tienen conexion de ningun tipo con otra base
    public void mostrarGradoTotal(int[] gradoT) {
        int contador = 0;
        String cadena = "Bases que no pueden enviar ni recibir mensajes (aisladas)\n";
        for (int i = 0; i < gradoT.length; i++) {
            if (gradoT[i] == 0) {
                cadena = cadena + " Base " + (i + 1) + " - ";
                contador++;
            }
        }
        cadena = cadena.substring(0, cadena.length() - 2);
        if (contador > 0) {
            JOptionPane.showMessageDialog(null, cadena);
        } else {
            JOptionPane.showMessageDialog(null, "No existen bases aisladas", "Bases", JOptionPane.WARNING_MESSAGE);
        }
    }

    //Metodo para buscar el menor costo y la ruta de una base A origen a una base B destino
    public void dijkstra(int origen, int destino, boolean mostrar) {
        //crea vectores donde almacenara el costo, la ruta y si ya se le busco el menor costo a ese vertice
        int[] menorCosto = new int[vertices];
        boolean[] verticeYaProcesado = new boolean[vertices];
        int[][] recorrido = new int[vertices][vertices];
        //El recorrido se comienza por el origen para cada vertice
        for (int i = 0; i < vertices; i++) {
            recorrido[i][0] = origen + 1;
        }
        //Se llena el vector de menor costo y el vector ya procesado 
        for (int i = 0; i < vertices; i++) {
            menorCosto[i] = Integer.MAX_VALUE;
            verticeYaProcesado[i] = false;
        }
        //de un vertice a si mismo el costo es 0
        menorCosto[origen] = 0;
        //busca la ruta menos costosa para cada vertice
        for (int contador = 0; contador < vertices - 1; contador++) {
            //Toma el vertice con menor costo del vector ya procesados
            int menorD = menorCostoV(menorCosto, verticeYaProcesado);
            verticeYaProcesado[menorD] = true;
            //Se recorre y se actualizan los costos
            for (int v = 0; v < vertices; v++) {
                if (!verticeYaProcesado[v] && menorCosto[menorD] != Integer.MAX_VALUE) {
                    //Se recorren y se comparan las tripletas
                    for (int w = 1; w <= grafoEnTripletas.getNumeroTripletas(); w++) {
                        if (grafoEnTripletas.getTripleta(w).getFila() == menorD + 1 && grafoEnTripletas.getTripleta(w).getColumna() == v + 1) {
                            if (grafoEnTripletas.getTripleta(w).getValor() > 0 && grafoEnTripletas.getTripleta(w).getValor() + menorCosto[menorD] < menorCosto[v]) {
                                //Se asigna un nuevo costo menor al anterior
                                menorCosto[v] = menorCosto[menorD] + grafoEnTripletas.getTripleta(w).getValor();
                                //Almacenamos las rutas o los recorridos
                                //se almacena el recorrido del vertice de menor costo encontrado
                                for (int i = 0; i < vertices; i++) {
                                    if (recorrido[v][i] == 0) {
                                        for (int j = 0; j < vertices; j++) {
                                            if (recorrido[menorD][i] != 0) {
                                                recorrido[v][i] = recorrido[menorD][i];
                                            }
                                        }
                                    }
                                }
                                //se almacena el nuevo vertice enconrado adyacente al vertice de menor costo
                                for (int i = 0; i < vertices; i++) {
                                    //Se controla agrega el nuevo recorrido
                                    if (recorrido[v][i] == 0 || grafoEnTripletas.getTripleta(w).getFila() == recorrido[v][i]) {
                                        recorrido[v][i + 1] = grafoEnTripletas.getTripleta(w).getColumna();
                                        break;
                                    }
                                    //Se controla los recorridos que se deben remplazar
                                    if (recorrido[v][i] == 0 || grafoEnTripletas.getTripleta(w).getColumna() == recorrido[v][i]) {
                                        if (recorrido[v][i - 1] == origen + 1) {
                                            recorrido[v][i] = grafoEnTripletas.getTripleta(w).getFila();
                                            recorrido[v][i + 1] = grafoEnTripletas.getTripleta(w).getColumna();
                                            break;
                                        } else {
                                            recorrido[v][i - 1] = grafoEnTripletas.getTripleta(w).getFila();
                                            recorrido[v][i] = grafoEnTripletas.getTripleta(w).getColumna();
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //mostramos los resultados encontrados 
        if (mostrar) {
            //mostramos el costo
            mostrarC(menorCosto, origen, destino);
        } else {
            //mostramos la ruta o recorrido realizasdo
            mostrarR(menorCosto, origen, destino, recorrido);
        }
    }

    //Metodo que retorna la posicion del vertice con menor costo
    private int menorCostoV(int[] costos, boolean[] verticeYaProcesado) {
        int menor = Integer.MAX_VALUE;
        int posicion = 0;
        for (int v = 0; v < vertices; v++) {
            if (verticeYaProcesado[v] == false && costos[v] <= menor) {
                menor = costos[v];
                posicion = v;
            }
        }
        return posicion;
    }

    //Mostrar la ruta de ir de una base A a una base B
    private void mostrarR(int[] menorCosto, int origen, int destino, int[][] recorridos) {
        if (menorCosto[destino] != Integer.MAX_VALUE && menorCosto[destino] != 0) {
            String cadena = "La ruta para ir de la base " + (origen + 1) + " hasta la base " + (destino + 1) + " es: \n";
            for (int i = 0; i < recorridos.length; i++) {
                if (recorridos[destino][i] != 0) {
                    cadena = cadena + "Base " + recorridos[destino][i] + "  >>  ";
                }
            }
            cadena = cadena.substring(0, cadena.length() - 4);
            JOptionPane.showMessageDialog(null, cadena);
        } else {
            JOptionPane.showMessageDialog(null, "No se puede enviar mensajes a la base: " + (destino + 1), "No encontrado", JOptionPane.WARNING_MESSAGE);
        }/*
        System.out.println("\nruta menor costos\n");
        for (int x = 0; x < recorridos.length; x++) {
            System.out.print(x + 1 + "\t - |");
            for (int y = 0; y < recorridos[x].length; y++) {
                System.out.print(recorridos[x][y]);
                if (y != recorridos[x].length - 1) {
                    System.out.print("\t");
                }
            }
            System.out.println("|");
        }*/
    }

    //mostrar el costo de ir de una base A a una base B 
    private void mostrarC(int[] menorCosto, int origen, int destino) {
        if (menorCosto[destino] != Integer.MAX_VALUE && menorCosto[destino] != 0) {
            String cadena = "El tiempo que tarda de ir de la base " + (origen + 1) + " hasta la base " + (destino + 1) + " es: " + menorCosto[destino];
            JOptionPane.showMessageDialog(null, cadena);
        } else {
            JOptionPane.showMessageDialog(null, "No se puede enviar mensajes a la base: " + (destino + 1), "No encontrado", JOptionPane.WARNING_MESSAGE);
        }
        /*System.out.println("\nCostos\n");
        for (int i = 0; i < vertices; i++) {
            System.out.println((i + 1) + " -> " + menorCosto[i]);
        }*/
    }

    public void mostrarGrupos() {
        String cadena = "";
        ArrayList<String> grupos1 = new ArrayList<>();
        int[] visitado = new int[vertices];
        grupos1 = bfs(1, grupos1, visitado);
        for (int i = 0; i < grupos1.size(); i++) {
            cadena = cadena + "\n\nGrupo " + (i + 1) + "\n" + grupos1.get(i);
        }
        JOptionPane.showMessageDialog(null, cadena);
    }

    public ArrayList<String> bfs(int v, ArrayList<String> grupos, int[] visitado) {
        String cadena;
        Queue cola = new ArrayDeque();
        visitado[v - 1] = 1;
        cadena = "Base " + v + " - ";
        cola.add(v);
        while (!cola.isEmpty()) {
            v = (int) cola.poll();
            for (int w = 1; w <= grafoEnTripletas.getNumeroTripletas(); w++) {
                if (grafoEnTripletas.getTripleta(w).getFila() == v) {
                    if (visitado[grafoEnTripletas.getTripleta(w).getColumna() - 1] == 0) {
                        visitado[grafoEnTripletas.getTripleta(w).getColumna() - 1] = 1;
                        cadena = cadena + "Base " + grafoEnTripletas.getTripleta(w).getColumna() + " - ";
                        cola.add(grafoEnTripletas.getTripleta(w).getColumna());
                    }
                }
            }
        }
        cadena = cadena.substring(0, cadena.length() - 2);
        grupos.add(cadena);
        for (int i = 0; i < visitado.length; i++) {
            if (visitado[i] == 0) {
                bfs(i + 1, grupos, visitado);
            }
        }
        return grupos;
    }
    
    public String recorridoCamion(int [][] matrizAdy, int valorInicialX,int []necesidadEstaciones){
        String recorrido="";
        Tripleta t=new Tripleta(matrizAdy.length, matrizAdy.length, 0);
        MatrizEnTripletas adyEnTripletas=new MatrizEnTripletas(tripleta);
        
        
        return recorrido;
    }

}
