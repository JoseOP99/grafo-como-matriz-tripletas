package practica3.modelo;

import java.util.ArrayList;
import java.util.List;

public class MatrizEnTripletas {
    
    //Listado de tripletas
    private final List<Tripleta> tripletas = new ArrayList<>();
    
    //Metodo constructor de matriz en tripletas 
    public MatrizEnTripletas(Tripleta tripleta) {
        tripletas.add(tripleta);  
    }

    //Metodos getter y setter
    public void setTripleta(Tripleta t, int i) {
        tripletas.add(i, t);
        tripletas.get(0).setValor(tripletas.get(0).getValor() + 1);
    }

    public void setNumeroTripletas(int i) {
        tripletas.get(0).setValor(i);
    }

    public int getFilas() {
        return tripletas.get(0).getFila();
    }

    public int getColumnas() {
        return tripletas.get(0).getColumna();
    }

    public int getNumeroTripletas() {
        return tripletas.get(0).getValor();
    }

    public Tripleta getTripleta(int i) {
        return tripletas.get(i);
    }

    //Inserta una nueva tripleta a la lista de tripletas cumpliendo su respectivo orden por filas y columnas
    public void insertaTripleta(Tripleta tripleta) {
        //Aqui verificamos que no se registren valores iguales a 0 o los valores de la diagonal principal
        if (tripleta.getValor() != 0 && tripleta.getFila()!=tripleta.getColumna()) {
            tripletas.add(tripleta);
            int i, j, datos;
            Tripleta t, tx;
            tx = getTripleta(0);
            datos = tx.getValor();
            i = 1;
            t = getTripleta(i);
            while (i <= datos && t.getFila() < tripleta.getFila()) {
                i++;
                t = getTripleta(i);
            }
            while (i <= datos && t.getFila() == tripleta.getFila() && t.getColumna() < tripleta.getColumna()) {
                i++;
                t = getTripleta(i);
            }
            datos++;
            j = datos - 1;
            while (j >= i) {
                tripletas.set(j + 1, tripletas.get(j));
                j--;
            }
            //Insertamos la tripleta en la posicion correspondiente y aumentamos el numero de tripletas
            tripletas.set(i, tripleta);
            setNumeroTripletas(datos);
        }
    }

    
}
