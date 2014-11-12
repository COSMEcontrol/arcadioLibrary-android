package com.arcadio.api.v1.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.arcadio.CosmeListener;
import com.arcadio.service.api.v1.listeners.OnClientStartedListener;

import java.util.List;



public class PluginClientArcadio {
	
	public static IPluginServiceArcadio remoteArcadio;
	//listener que notifica el estado de conexion con el servicio
	private OnClientStartedListener onclientstartedlistener;
	public static final String NAME_SERVICE_ARCADIO = "com.arcadio.api.v1.service.ArcadioService";
	//variables de session
	private int sessionId = 0;
	private String sessionKey = "";
	private Context context;
	
	
	public PluginClientArcadio(Context context){
		this.context=context;
	}
	
	//hacer singelton ??? pensar
	
	private ServiceConnection conexion = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.v("onServiceDisconnected", "onServiceDisconnected");
			remoteArcadio = null;
			onclientstartedlistener.onClientStopped();
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder _remoteArcadio) {
			Log.v("onserviceconected", "onserviceconected");
			remoteArcadio =  IPluginServiceArcadio.Stub.asInterface(_remoteArcadio);
			//generar llamada a onClientStarted
			onclientstartedlistener.onClientStarted();
			
		}
	};
	/**
	 * Conexion con servicio android Arcadio.
	 * @param onclientstartedlistener ( OnClientStartedListener ) 
	 */
	public void startService(OnClientStartedListener onclientstartedlistener){
		//iniciar la unicion-conexion stub con el servicio de arcadio
		this.onclientstartedlistener=onclientstartedlistener;
		Intent msgIntent = new Intent();
		msgIntent.setClassName("com.miniblas.app", "com.arcadio.api.v1.service.ConnectionArcadioService");
		if(context.startService(msgIntent)==null)
			Log.v("no start con el servicio", "no start con el servicio");
		else
			Log.v("servicio iniciado", "servicio iniciado");      
        Intent intent = new Intent();
        intent.setClassName("com.miniblas.app", "com.arcadio.api.v1.service.ConnectionArcadioService");
		if(!context.bindService(intent, conexion, Context.BIND_AUTO_CREATE))
			Log.v("no bind con el servicio", "no bind con el servicio");
		else{
			Log.v("iniciado bind con el servicio", "iniciado bind con el servicio");
		}	
	}
	/**
	 * Conexion con servidor COSME remoto
	 */
	//requestId es el codigo que acabará devolviendo gotActivityResult
	public void connect(int connectionId, CosmeListener cosmeListener){
		if(remoteArcadio== null)
			Log.v("nulo", "nulo");
		try {
			
			remoteArcadio.connect(connectionId, new ISessionStartedListener.Stub() {		
				@Override
				public IBinder asBinder() {
					return this ;
				}
				
				@Override
				public void onSessionStarted(int _sessionId, String _sessionKey)
						throws RemoteException {
					sessionId = _sessionId;
					sessionKey = _sessionKey;
					Log.v("PluginClientArcadio->> Recibido identificador cliente API", _sessionId+" y "+ _sessionKey);
				}
				
				@Override
				public void onSessionError(String error) throws RemoteException {
					Log.v("PluginClientArcadio->> Error onssessionError",error);
					
				}
			}, new AdapterICosmeListener(cosmeListener));
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}
	/*
	 * Previously established Arcadio session to the foreground.
	 * Arcadio no corta la conexion
	 */
//	private void attach(){
//		
//	}

	
	public void disconnect(){
		//desconectar del servidor COSME
		try {
			if(remoteArcadio== null)
				Log.v("nulo", "nulo");
			remoteArcadio.disconnect(sessionId, sessionKey);
		} catch (RemoteException e) {
			//generar error
			Log.v("nulo", "nulo");
			onclientstartedlistener.onClientStopped();	
		}
	}
	public void stopService(){
		//desactiva la union-conexion con el stub del servicio de arcadio
		context.unbindService(conexion);
	}
	
	//variables
	public void leerVariable(String _nombreVariable){
		try {
			remoteArcadio.leerVariable(sessionId, sessionKey, _nombreVariable);
		} catch (RemoteException e) {
			onclientstartedlistener.onClientStopped();	
		}
	}
	public void leerListaVariables(List<String> _listaVariables){
		try {
			remoteArcadio.leerListaVariables(sessionId, sessionKey, _listaVariables);
		} catch (RemoteException e) {
			onclientstartedlistener.onClientStopped();	
		}
	}
	public void solicitarVariables(){
		try {
			remoteArcadio.solicitarVariables(sessionId, sessionKey);
		} catch (RemoteException e) {
			onclientstartedlistener.onClientStopped();	
		}
	}
	public void modificarValorVarible(String _nombreVariable, double _valor){
		try {
			remoteArcadio.modificarValorVarible(sessionId, sessionKey, _nombreVariable, _valor);
		} catch (RemoteException e) {
			onclientstartedlistener.onClientStopped();	
		}
	}
	public void isNumeric(String _nombresVariables){
		try {
			remoteArcadio.isNumeric(sessionId, sessionKey, _nombresVariables);
		} catch (RemoteException e) {
			onclientstartedlistener.onClientStopped();	
		}
	}
		
	//basket
	public void crearCesta(String _nombreCesta, int _periodoRefresco){
		try {
			remoteArcadio.crearCesta(sessionId, sessionKey, _nombreCesta, _periodoRefresco);
		} catch (RemoteException e) {
			onclientstartedlistener.onClientStopped();	
		}
	}
	public void eliminarCesta(String _nombreCesta){
		try {
			remoteArcadio.eliminarCesta(sessionId, sessionKey, _nombreCesta);
		} catch (RemoteException e) {
			onclientstartedlistener.onClientStopped();	
		}
	}
	public void modificarPeriodoCesta(String _nombreCesta,int nuevoRefrescoMs){
		try {
			remoteArcadio.modificarPeriodoCesta(sessionId, sessionKey, _nombreCesta, nuevoRefrescoMs);
		} catch (RemoteException e) {
			onclientstartedlistener.onClientStopped();	
		}
	}
	public void introducirVariableACesta(String _nombreCesta, String _nombreVariable){
		try {
			remoteArcadio.introducirVariableACesta(sessionId, sessionKey, _nombreCesta, _nombreVariable);
		} catch (RemoteException e) {
			onclientstartedlistener.onClientStopped();	
		}
	}
	public void introducirVariablesACesta(String _nombreCesta,List<String> _listaVariables){
		try {
			remoteArcadio.introducirVariablesACesta(sessionId, sessionKey, _nombreCesta, _listaVariables);
		} catch (RemoteException e) {
			onclientstartedlistener.onClientStopped();	
		}
	}
	public void eliminarVariableDeCesta(String _nombreCesta,String _nombreVariable){
		try {
			remoteArcadio.eliminarVariableDeCesta(sessionId, sessionKey, _nombreCesta, _nombreVariable);
		} catch (RemoteException e) {
			onclientstartedlistener.onClientStopped();	
		}
	}
	public void eliminarVariablesDeCesta(String _nombreCesta, List<String> _listaVariables){
		try {
			remoteArcadio.eliminarVariablesDeCesta(sessionId, sessionKey, _nombreCesta, _listaVariables);
		} catch (RemoteException e) {
			onclientstartedlistener.onClientStopped();	
		}
	}
	public void solicitarListaVariablesCesta(String _nombreCesta){
		try {
			remoteArcadio.solicitarListaVariablesCesta(sessionId, sessionKey, _nombreCesta);
		} catch (RemoteException e) {
			onclientstartedlistener.onClientStopped();	
		}
	}
	public void solicitarListaCestas(){
		try {
			remoteArcadio.solicitarListaCestas(sessionId, sessionKey);
		} catch (RemoteException e) {
			onclientstartedlistener.onClientStopped();	
		}
	}
		
	public void ping(String _nombreVariable){
		try {
			remoteArcadio.ping(sessionId, sessionKey, _nombreVariable);
		} catch (RemoteException e) {
			onclientstartedlistener.onClientStopped();	
		}
	}
	public void pingListaVariables(List<String> _listaVariables){
		try {
			remoteArcadio.pingListaVariables(sessionId, sessionKey, _listaVariables);
		} catch (RemoteException e) {
			onclientstartedlistener.onClientStopped();	
		}
	}
	public void tipoNombre(String _nombreVariable){
		try {
			remoteArcadio.tipoNombre(sessionId, sessionKey, _nombreVariable);
		} catch (RemoteException e) {
			onclientstartedlistener.onClientStopped();	
		}
	}
	public void pedirTipos(){
		try {
			remoteArcadio.pedirTipos(sessionId, sessionKey);
		} catch (RemoteException e) {
			onclientstartedlistener.onClientStopped();	
		}
	}
	public void obtenerMaxTamBufferCosme(){
		try {
			remoteArcadio.obtenerMaxTamBufferCosme(sessionId, sessionKey);
		} catch (RemoteException e) {
			onclientstartedlistener.onClientStopped();	
		}
	}

}
