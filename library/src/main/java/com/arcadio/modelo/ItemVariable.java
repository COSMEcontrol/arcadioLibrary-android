package com.arcadio.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ItemVariable implements Parcelable{
	
	private static List<ItemVariable> listaVariables = new ArrayList<ItemVariable>();
	private static int nextVariable = 0;
	
	public synchronized static ItemVariable createInstance(String _name, String _value){
		if(nextVariable >= listaVariables.size()){
			listaVariables.add(new ItemVariable());
		}
		
		return listaVariables.get(nextVariable++).setData(_name, _value);		
	}
	
	public synchronized static ItemVariable createInstanceFromParcel(Parcel in){
		if(nextVariable >= listaVariables.size()){
			listaVariables.add(new ItemVariable());
		}
		
		return listaVariables.get(nextVariable++).readFromParcel(in);
	}
	
	public synchronized static void clearInstanceList(){
		System.out.println("Total ItemVariable: " + nextVariable);
		nextVariable = 0;
	}
	
	private String nombre;
	private String tipo;
	
	private String valor = "0";
	
	public ItemVariable(){
	}
	public ItemVariable(String _nombre, String _valor){
		setData(_nombre, _valor);
	}

	
	
	private ItemVariable setData(String _name, String _value){
		this.nombre=_name;
		this.valor=_value;
		this.tipo = null;
		return this;
	}
	
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public ItemVariable(String _nombre){
		this.nombre = _nombre;
	}
//	public ItemVariable(Parcel in) {
//		readFromParcel(in);
//	}
	


	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(nombre);
		dest.writeString(tipo);
		dest.writeString(valor);
//		dest.writeString(minimo);	
	}
	public ItemVariable readFromParcel(Parcel in) {
	    this.nombre=in.readString();
	    this.tipo=in.readString();
	    this.valor=in.readString();
	    return this;
//	    this.minimo=in.readString();
	}
	public static final Parcelable.Creator<ItemVariable> CREATOR
    = new Parcelable.Creator<ItemVariable>() {
        public ItemVariable createFromParcel(Parcel in) {
            return createInstanceFromParcel(in);
        }
 
        public ItemVariable[] newArray(int size) {
            return new ItemVariable[size];
        }
    };
}
