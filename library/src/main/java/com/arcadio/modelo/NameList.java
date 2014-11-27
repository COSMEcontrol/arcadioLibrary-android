/*
 * ListaNombres.java
 *
 *
 */

package com.arcadio.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.*;

/**
* Class description goes here.
*
* @version 1.0 13 Feb 2005
* @author Alberto Azuara
*/
public class NameList implements Parcelable {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(nombres);
    }
    public NameList readFromParcel(Parcel in) {
        in.readMap(nombres, NameList.class.getClassLoader());
        return this;
    }
    public NameList(Parcel in) {
        readFromParcel(in);
    }
    public static final Parcelable.Creator<NameList> CREATOR
            = new Parcelable.Creator<NameList>() {
        public NameList createFromParcel(Parcel in) {
            return new NameList(in);
        }

        public NameList[] newArray(int size) {
            return new NameList[size];
        }
    };
    
    /**
     * contiene una lista de variables, ItemVariable
     */
    private SortedMap<String ,String> nombres = new TreeMap();   // <nombre, tipo>
    //private SortedMap <String, String> nombres  = Collections.synchronizedSortedMap(new TreeMap());

    // Estados en los que puede estar la lista
    static final public int LISTA_VACIA = 0;
    static final public int RECIBIENDO_LISTA = 1;
    static final public int LISTA_RECIBIDA = 2;

    // Indica si ya se han recibido TODOS los nombres de la lista.
    private int estado = LISTA_VACIA;
    /**
    *  
    * 
    */
    public NameList(){
	
    }

    /**
    *  
    * 
    */
    public void anadir(String _nombre) {      
        
	nombres.put(_nombre, "?");
    } 
    /**
    *  
    * 
    */
    public void anadir(String _nombre, String _tipo) {      
        
	nombres.put(_nombre, _tipo);
    } 


    public SortedMap<String ,String> getListaNombresTipos(){
        return this.nombres;
    }
    
    public Collection<String> getListaNombres(){
        return nombres.keySet();
    }
    

    public Collection<String> getListaTipos(){
        return nombres.values();
    }
    
    public void setTipo(String _variable, String _tipo){
        nombres.put(_variable,_tipo);
    }
        
    
    public int getNumNombres(){
        return nombres.size();
    }

    public String getTipo(String _nombreVariable) {
        return nombres.get(_nombreVariable);
    }

    /**
     * @return the estado
     */
    public int getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(int estado) {
        this.estado = estado;
    }

    public void reset(){
        this.nombres.clear();
    }



 }
