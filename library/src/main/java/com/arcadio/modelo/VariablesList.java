/*
 * ListaVariables.java
 *
 * Created on 26 de Junio de 2006
 *
 * Clase diseñada para contener las variables asociadas a una cesta.
 *
 * Cuando el "OyenteTelegramas" recibe un telegrama con el contenido de una cesta crea una instancia de esta 
 * clase (a partir de la información contenida en el telegrama) y se la pasa a la ventana principal que se 
 * encargue de mostrar el interfaz de usuario.
 * Esa ventana de interfaz de usuario podrá interrogar a esta instancia (métodos "obtVariable (queNombre)", 
 * "obtValorVariable(queNombre)" u "obtValorTextoVariable(queNombre)") por cada uno de los nombres de variables que le interese.
 * 
 */

package com.arcadio.modelo;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collection;
import java.util.HashMap;


/**
* Class description goes here.
*
* @version 1.0 13 Feb 2005
* @author Alberto Azuara
*/
public class VariablesList implements Parcelable {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(vars);
    }
    public VariablesList readFromParcel(Parcel in) {
        in.readMap(vars, VariablesList.class.getClassLoader());
        return this;
    }
    public VariablesList(Parcel in) {
        readFromParcel(in);
    }
    public static final Parcelable.Creator<VariablesList> CREATOR
            = new Parcelable.Creator<VariablesList>() {
        public VariablesList createFromParcel(Parcel in) {
            return new VariablesList(in);
        }

        public VariablesList[] newArray(int size) {
            return new VariablesList[size];
        }
    };


    /**
     * contiene una lista de variables, ItemVariable
     */
    private HashMap<String, ItemVariable> vars;

    /**
    *  
    * 
    */
    public VariablesList(){
	vars = new HashMap();
    }

    /**
    *  
    * 
    */
    public void anadir(String nombre) {      
        //System.out.print(nombre);
        ItemVariable v = new ItemVariable (nombre);
	vars.put(nombre, v);
    } 

    /**
    *  
    * 
    */
    public void anadir(String nombre, double valor) {        
        ItemVariable v = new ItemVariable (nombre, valor);
	vars.put (nombre, v);
    } 
    
    /**
    *  
    * 
    */
    public void anadir(String nombre, String valor) {        
        ItemVariable v = new ItemVariable (nombre, valor);
	vars.put (nombre, v);
    } 

    /**
    *  
    * 
    */
    public void anadir(ItemVariable v) {        
	vars.put (v.getNombre(), v);
    } 

    /**
    *  
    * 
    */
    public HashMap getValores() {        
        return vars;
    } 
    
    public ItemVariable getVariable (String _nombreVariable){
        ItemVariable v = (ItemVariable) vars.get(_nombreVariable);
        
        return v;
    }
    
    
    public double getValorVariable (String _nombreVariable){
        double dd = 0.0;
        ItemVariable v = (ItemVariable) vars.get(_nombreVariable);
        if (v!=null){
            dd = v.getValor();
        }
        return dd;
    }
    
    
    
    public String getValorTextoVariable (String _nombreVariable){
        String txt = null;
        ItemVariable v = (ItemVariable) vars.get(_nombreVariable);
        if (v!=null){
            txt = v.getValor_txt();
        }
        return txt;
    }
    
    
    
    public Collection<ItemVariable> getLista(){
        return vars.values();
    }
    
    
    public boolean existe (String _nombreVariable){
        return vars.containsKey(_nombreVariable);
    }

    public void eliminar (String _nombre){
        this.vars.remove(_nombre);
    }

    /**
     * Erases all the ItemVariables it could contain.
     * After executing this method, the instances contains 0 ItemVariables.
     */
    public void clear(){
        this.vars.clear();
    }
 }
