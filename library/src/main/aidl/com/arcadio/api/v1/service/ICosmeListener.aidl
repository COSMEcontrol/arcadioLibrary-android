package com.arcadio.api.v1.service;
import java.util.List;
import com.arcadio.modelo.ItemVariable;
import com.arcadio.api.v1.service.ParceableCosmeError;
import com.arcadio.api.v1.service.ParceableEstadosCosme;

interface ICosmeListener {

	void notificarRefrescoVariables (String _nombreCesta,inout List<ItemVariable> _listaVariables);
	
	void notificarEstadoConexion (inout ParceableEstadosCosme _estado);
   
    void notificarError (inout ParceableCosmeError _error);
    
   // void notificarEvento(EstadosCosme _codEvento, Telegrama _tlg);
   void notificarListaNombres(inout List<ItemVariable> listaNombres);
   void notificarIsNumeric(inout ItemVariable variable);
   void notificarNomACesta(String nomCesta, String nomVariable);
   void notificarCestaCreada(String nomCesta);
   

}