package com.arcadio.modelo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * Esta clase contiene la información asociada a cada clase que el cliente haya solicitado crear a Arcadio.
 * Cuando haya que reestablecer la comunicación, podrá recrearse la cesta existente sin que el cliente
 * deba hacer absolutamente nada.
 * @author Alberto Azuara
 */
public class Basket {

    private String prefijoCesta;   // prefijo de la cesta (sin el sufijo aleatorio
    private String nombreCesta;    // compuesto por el prefijo más el sufijo aleatorio
    private Collection<String> listaNombres = new ArrayList();
    private int refresco;

    public Basket(String _prefijo, Collection<String> _listaNombres, int _refresco){
        this.prefijoCesta = _prefijo;
        //this.nombreCesta  = _nombreAleatorio;
        this.updateNombreAleatorio();

        this.listaNombres = _listaNombres;
        this.refresco = _refresco;
    }


    public Basket(String _prefijo, int _refresco){
        this.prefijoCesta = _prefijo;
        // this.nombreCesta  = _nombreAleatorio;
        this.updateNombreAleatorio();
        this.refresco = _refresco;
    }
    /**
     * @return the prefijo
     */
    public String getPrefijo() {
        return prefijoCesta;
    }

    /**
     * @return the nombre
     */
    public String getNombreCestaAleatorio() {
        return nombreCesta;
    }

    /**
     *
     */
    public void setNombreCestaAleatorio(String _nuevoNombreCompleto) {
        nombreCesta = _nuevoNombreCompleto;
    }

    /**
     * @return the listaNombres
     */
    public Collection<String> getListaNombres() {
        return listaNombres;
    }

    public void addNombre (String _nuevoNombre){
        listaNombres.add(_nuevoNombre);
    }


    public boolean  deleteNombre (String _nombre){
        return listaNombres.remove(_nombre);
    }

    /**
     * @return the refresco
     */
    public int getRefresco() {
        return refresco;
    }

    /**
     * @param refresco the refresco to set
     */
    public void setRefresco(int refresco) {
        this.refresco = refresco;
    }

    public void updateNombreAleatorio(){
        Random r = new Random();
        //cesta = cesta+"_"+Long.toString(Math.abs(r.nextLong()), 36);
        this.nombreCesta = this.prefijoCesta+"_"+Long.toString(Math.abs(r.nextInt()), 36);
    }


}