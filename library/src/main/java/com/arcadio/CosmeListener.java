/*
* CosmeListener.java
*
* Created on 27 de junio de 2006, 18:23
*
* To change this template, choose Tools | Template Manager
* and open the template in the editor.
*/
 
package com.arcadio;

import com.arcadio.modelo.Basket;
import com.arcadio.modelo.CosmeError;
import com.arcadio.modelo.ItemVariable;

import java.util.ArrayList;
 
 
/**
*
* @author fserna
*/
public interface CosmeListener {
   
    public void notificarRefrescoVariables (String _nombreCesta, ArrayList<ItemVariable> _listaVariables);
   
    public void notificarEstadoConexion (CosmeStates _estado);
   
    public void notificarError (CosmeError _error);
    
   // public void notificarEvento(EstadosCosme _codEvento, Telegrama _tlg);
    public void notificarListaNombres(ArrayList<ItemVariable> listaNombres);
    public void notificarIsNumeric(ItemVariable variable);
    public void notificarCestaCreada(Basket basket);
    public void notificarNomACesta(Basket basket, ItemVariable variable);

}