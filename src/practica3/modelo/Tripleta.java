package practica3.modelo;

public class Tripleta {
    //Atributos de la clase Tripleta
    private int valor;
    private int fila;
    private int columna;
    
    //Metodo o constructor para Tripleta
    public Tripleta(int fila, int columna, int valor) {
        this.fila = fila;
        this.columna = columna;
        this.valor = valor;
    }
    //Metodos getter y setter
    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

}
