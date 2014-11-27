package com.arcadio.modelo;

/**
 * Created by alberto on 26/11/14.
 */
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ItemVariable implements Parcelable {


    private static List<ItemVariable> listaVariables = new ArrayList<ItemVariable>();
    private static int nextVariable = 0;
/*
    public synchronized static ItemVariable createInstance(String _name, String _value){
        if(nextVariable >= listaVariables.size()){
            listaVariables.add(new ItemVariable());
        }

        return listaVariables.get(nextVariable++).setData(_name, _value);
    }
*/
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
/*
    public ItemVariable(String _nombre, String _valor){
        setData(_nombre, _valor);
    }



    private ItemVariable setData(String _name, String _value){
        this.nombre=_name;
        this.valor=_value;
        this.tipo = null;
        return this;
    }
    */
    /**  */
    private String nombre = "";
    /**  */
    private String tipo = "";
    /**  */

    private boolean modificable = true;

    /**  */
    private double valor = Double.NaN;
    private double valorAnterior = Double.NaN; // permite saber si el nuevo valor es distinto del que contenía anteriormente
    /**  */
    private String valor_txt         = null;
    private String valorAnterior_txt = null; //permite saber si el nuevo valor es distinto del que contenía anteriormente
    /**  */
    private double valor_inicial = Double.NaN;
    /**  */
    private double valor_minimo = Double.NaN;
    /**  */
    private double valor_maximo = Double.NaN;
    /**  */
    private String unidad;  // ms, cm, etc...
    /**  */
    private double coeficiente1 = Double.NaN;
    /**  */
    private double coeficiente2 = Double.NaN;
    /**  */
    private double coeficiente3 = Double.NaN;


    private boolean es_valor_nulo = true;

    private long timestamp = -1;  // contiene el instante en que se ha actualizado su valor (puede ser el mismo que había)
    private long timestampModificado = -1; // contiene el instante en que se modificó el valor (
    private boolean valorModificado = true; // true si el nuevo valor es distinto del anterior
    // false si el nuevo y anterior valores son iguales
    /**
     *
     *
     */
    public ItemVariable(){
        this.setModificable(true);
        this.valorModificado = false;

    }

    /**
     *
     *
     */
    public ItemVariable(String que_nombre){
        this.nombre = que_nombre;


        this.setTipo("No Recibido");
        this.setModificable(true);
        this.valorModificado = false;
    }

    /**
     *
     *
     */
    public ItemVariable(String que_nombre, double que_valor){
        this.nombre = que_nombre;

        this.valorAnterior = this.valor;
        this.valor = que_valor;

        this.timestamp = System.currentTimeMillis();
        this.timestampModificado = this.timestamp;
        this.valorModificado = true;

        this.setTipo("No Recibido");
        this.setModificable(true);
    }

    /**
     *
     *
     */
    public ItemVariable(String que_nombre, String que_valor){
        this.nombre = que_nombre;
        this.valorAnterior_txt = this.valor_txt;
        this.valor_txt = que_valor;

        this.timestamp = System.currentTimeMillis();
        this.timestampModificado = this.timestamp;
        this.valorModificado = true;

        this.setTipo("No Recibido");
        this.setModificable(true);
    }

    /**
     *
     *
     */
    public ItemVariable(String que_nombre,
                        double que_valor,
                        String que_valor_txt,
                        double que_valor_inicial,
                        double que_valor_minimo,
                        double que_valor_maximo,
                        String que_unidad,
                        double que_coeficiente1,
                        double que_coeficiente2,
                        double que_coeficiente3){

        this.nombre = que_nombre;
        this.valorAnterior = this.valor;
        this.valor = que_valor;
        this.valorAnterior_txt = this.valor_txt;
        this.valor_txt=que_valor_txt;
        this.valor_inicial = que_valor_inicial;
        this.valor_minimo = que_valor_minimo;
        this.valor_maximo = que_valor_maximo;
        this.unidad = que_unidad;
        this.coeficiente1 = que_coeficiente1;
        this.coeficiente2 = que_coeficiente2;
        this.coeficiente3 = que_coeficiente3;

        es_valor_nulo = false;



        this.setTipo("No Recibido");
        this.setModificable(true);
        this.timestamp = System.currentTimeMillis();
        this.timestampModificado = this.timestamp;
        this.valorModificado = true;
    }

    /**
     *
     *
     */
    public String getNombre (){
        return nombre;
    }

    /**
     *
     *
     */
    public double getValor () {
        return valor;
    }

    /**
     *
     *
     */
    public void setValor (double v) {
        this.valorAnterior = this.valor;
        valor = v;
        es_valor_nulo = false;
        this.timestamp = System.currentTimeMillis();
        if (this.valorAnterior != this.valor){
            this.timestampModificado = this.timestamp;
            this.valorModificado = true;
        }else{
            this.valorModificado = false;
        }
    }

    /**
     *
     *
     */
    public String getValor_txt (){
        return valor_txt;
    }

    /**
     *
     *
     */
    public void setValor_txt (String valor){
        this.valorAnterior_txt = this.valor_txt;
        valor_txt = valor;
        es_valor_nulo = false;
        this.timestamp = System.currentTimeMillis();
        if ((this.valor_txt!= null) &&(this.valorAnterior_txt!=null)){
            if ( ! this.valor_txt.equals(this.valorAnterior_txt)){
                this.timestampModificado = this.timestamp;
                this.valorModificado = true;
            }else{
                this.valorModificado = false;
            }
        }else{

        }
    }



    /**
     *
     *
     */
    public double getValor_inicial (){
        return valor_inicial;
    }

    /**
     *
     *
     */
    public double getValor_minimo (){
        return valor_minimo;
    }

    /**
     *
     *
     */
    public double getValor_maximo (){
        return valor_maximo;
    }

    /**
     *
     *
     */
    public String getUnidad (){
        return unidad;
    }

    /**
     *
     *
     */
    public double getCoeficiente1 (){
        return coeficiente1;
    }

    /**
     *
     *
     */
    public double getCoeficiente2 (){
        return coeficiente2;
    }

    /**
     *
     *
     */
    public double getCoeficiente3 (){
        return coeficiente3;
    }

    /**
     *
     *
     */
    public String imprimirValoresVariable(){
        return nombre +"/"+ valor +"/"+ valor_txt +"/"+ valor_inicial +"/"+ valor_minimo +"/"+ valor_maximo +"/"+ unidad +"/"+ coeficiente1 +"/"+ coeficiente2 +"/"+ coeficiente3;
    }

    /**
     *
     *
     */
    public String toString(){
        return nombre;
    }


    public boolean isNulo (){
        return this.es_valor_nulo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isModificable() {
        return modificable;
    }

    public void setModificable(boolean modificable) {
        this.modificable = modificable;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public boolean isText(){
        boolean isText = false;
        if (this.valor_txt == null){
            isText = false;
        }else{
            isText = true;
        }
        return isText;
    }


    public boolean isNumeric(){
        boolean isNumeric = false;
        if (this.valor_txt == null){
            isNumeric = true;
        }else{
            isNumeric = false;
        }
        return isNumeric;
    }


    public long getTimestamp(){
        return this.timestamp;
    }

    public long getTimestampModificado(){
        return this.timestampModificado;
    }

    public boolean isValorModificado(){
        return this.valorModificado;
    }





    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(tipo);

        dest.writeDouble(valor);
        dest.writeDouble(valorAnterior);
        dest.writeString(valor_txt);
        dest.writeString(valorAnterior_txt);
        dest.writeDouble(valor_inicial);
        dest.writeDouble(valor_minimo);
        dest.writeDouble(valor_maximo);
        dest.writeString(unidad);
        dest.writeDouble(coeficiente1);
        dest.writeDouble(coeficiente2);
        dest.writeDouble(coeficiente3);
        dest.writeLong(timestamp);
        dest.writeLong(timestampModificado);

        boolean[] booleanArray = new boolean[3];
        booleanArray[0] = modificable;
        booleanArray[1] = es_valor_nulo;
        booleanArray[2] = valorModificado;
        dest.writeBooleanArray(booleanArray);

    }
    public ItemVariable readFromParcel(Parcel in) {
        this.nombre= in.readString();
        this.tipo= in.readString();
        this.valor=in.readDouble();
        this.valorAnterior=in.readDouble();
        this.valor_txt = in.readString();
        this.valorAnterior_txt = in.readString();
        this.valor_inicial= in.readDouble();
        this.valor_minimo = in.readDouble();
        this.valor_maximo = in.readDouble();
        this.unidad= in.readString();
        this.coeficiente1 = in.readDouble();
        this.coeficiente2 = in.readDouble();
        this.coeficiente3 = in.readDouble();
        this.timestamp = in.readLong();
        this.timestampModificado= in.readLong();
        boolean[] booleanArray = null;
        in.readBooleanArray(booleanArray);
        this.modificable=booleanArray[0];
        this.es_valor_nulo=booleanArray[1];
        this.valorModificado=booleanArray[2];
        return this;
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