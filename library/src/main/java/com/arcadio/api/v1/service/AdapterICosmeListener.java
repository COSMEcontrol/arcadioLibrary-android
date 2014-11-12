package com.arcadio.api.v1.service;

import android.os.IBinder;
import android.os.RemoteException;

import com.arcadio.CosmeListener;
import com.arcadio.modelo.ItemVariable;

import java.util.ArrayList;
import java.util.List;

public class AdapterICosmeListener extends ICosmeListener.Stub {
	
	CosmeListener cosmeListener;
	
	public AdapterICosmeListener(CosmeListener cosmeListener){
		this.cosmeListener = cosmeListener;
	}



	@Override
	public void notificarRefrescoVariables(String _nombreCesta,
			List<ItemVariable> _listaVariables) throws RemoteException {
		ArrayList<ItemVariable> listavariables = new ArrayList<ItemVariable>();
		listavariables.addAll(_listaVariables);
		cosmeListener.notificarRefrescoVariables(_nombreCesta, listavariables);
		
	}

	@Override
	public void notificarEstadoConexion(ParceableEstadosCosme _estado)
			throws RemoteException {
		cosmeListener.notificarEstadoConexion(_estado.getEstado());	
	}

	@Override
	public void notificarError(ParceableCosmeError _error) throws RemoteException {
		cosmeListener.notificarError(_error);
		
	}

	@Override
	public void notificarListaNombres(List<ItemVariable> listaNombres)
			throws RemoteException {
		ArrayList<ItemVariable> lista = new ArrayList<ItemVariable>();
		lista.addAll(listaNombres);
		cosmeListener.notificarListaNombres(lista);
		
	}

	@Override
	public void notificarIsNumeric(ItemVariable variable)
			throws RemoteException {
		cosmeListener.notificarIsNumeric(variable);
		
	}



	@Override
	public IBinder asBinder() {
		return this;
	}



	@Override
	public void notificarNomACesta(String nomCesta, String nomVariable)
			throws RemoteException {
//		cosmeListener.notificarNomACesta(new Cesta(nomCesta), new ItemVariable(nomVariable));
	}



	@Override
	public void notificarCestaCreada(String nomCesta) throws RemoteException {
//		cosmeListener.notificarCestaCreada(new Cesta(nomCesta));
	}

}
