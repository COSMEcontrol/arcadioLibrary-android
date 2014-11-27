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
* @author Javier Perojo
*/
public class TypeList implements Parcelable {


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(tipos);
    }
    public TypeList readFromParcel(Parcel in) {
        in.readMap(tipos, TypeList.class.getClassLoader());
        return this;
    }
    public TypeList(Parcel in) {
        readFromParcel(in);
    }
    public static final Parcelable.Creator<TypeList> CREATOR
            = new Parcelable.Creator<TypeList>() {
        public TypeList createFromParcel(Parcel in) {
            return new TypeList(in);
        }

        public TypeList[] newArray(int size) {
            return new TypeList[size];
        }
    };



    private SortedMap<String, NameList> tipos = new TreeMap();
   //private SortedMap <String, ListaNombres> tipos  = Collections.synchronizedSortedMap(new TreeMap());
    
    /**
    *  
    * 
    */
    public TypeList(){
	
    }

    /**
    *  
    * 
    */
    public void anadirTipo(String _nombreTipo) {      
        NameList nombres = new NameList();
	tipos.put(_nombreTipo, nombres);
    } 

    public void anadirNombreDeTipo (String _tipo, String _nombre){
        NameList nombres = tipos.get(_tipo);
        nombres.anadir(_nombre);
        tipos.put(_tipo, nombres);
    }
    
    /**
    *  
    * Dado un tipo, devuelve aquellos nombres que sean de ese tipo.
    */
    public Collection<String> getNombresDeTipo(String _tipo) { 
        NameList nombres = tipos.get(_tipo);
        if (nombres!=null){
            return nombres.getListaNombres();
        }else{
            return null;
        }
        
    } 
    
    
    public Collection<String> getListaTipos(){
        //Iterator<String> it = tipos.keySet().iterator();
        //return it;
        
        return tipos.keySet();
    }

    public void reset(){
        this.tipos.clear();
    }
    
 }
