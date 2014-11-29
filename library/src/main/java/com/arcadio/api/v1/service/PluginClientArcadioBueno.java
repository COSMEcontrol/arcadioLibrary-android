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


public class PluginClientArcadioBueno {

	public static IPluginServiceArcadio remoteArcadio;
	private OnClientStartedListener onclientstartedlistener;
    public static final String MINIBLAS_PACKAGE_NAME = "com.miniblas.app";
	public static final String MINIBLAS_SERVICE_ARCADIO = "com.arcadio.api.v1.service.ConnectionArcadioService";
	//variables de session
	private int sessionId = 0;
	private String sessionKey = "";
	private Context context;


	public PluginClientArcadioBueno(Context context){
		this.context=context;
	}

	
	private ServiceConnection conexion = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.v("PluginClientArcadioLibrary-->", "Disconnected to service");
			remoteArcadio = null;
			onclientstartedlistener.onClientStopped();
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder _remoteArcadio) {
			Log.v("PluginClientArcadioLibrary-->", "Connected to service");
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
		msgIntent.setClassName(MINIBLAS_PACKAGE_NAME, MINIBLAS_SERVICE_ARCADIO);
		if(context.startService(msgIntent)==null)
			Log.v("PluginClientArcadioLibrary-->", "Failed to start service");
		else
			Log.v("PluginClientArcadioLibrary-->", "Iniciate service");
        Intent intent = new Intent();
        intent.setClassName(MINIBLAS_PACKAGE_NAME, MINIBLAS_SERVICE_ARCADIO);
		if(!context.bindService(intent, conexion, Context.BIND_AUTO_CREATE))
			Log.v("PluginClientArcadioLibrary-->", "ERROR Connecting to an application Arcadio service");
		else{
			Log.v("PluginClientArcadioLibrary-->", "Connecting to an application Arcadio service");
		}	
	}
	/**
	 * Conexion con servidor COSME remoto
	 */
	//requestId es el codigo que acabará devolviendo gotActivityResult
	public void connect(int connectionId, CosmeListener cosmeListener){
		if(remoteArcadio== null){
            Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
        }
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
					Log.v("PluginClientArcadioLibrary-->","Client identifier received API");
				}
				
				@Override
				public void onSessionError(String error) throws RemoteException {
					Log.v("PluginClientArcadioLibrary-->","Session Error: "+error);
					
				}
			}, new AdapterICosmeListener(cosmeListener));
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	public void disconnect(){
        /*
		try {
			if(remoteArcadio== null)
				Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
			remoteArcadio.disconnect(sessionId, sessionKey);
		} catch (RemoteException e) {
			onclientstartedlistener.onClientStopped();	
		}
		*/
	}
	public void stopService(){
		//desactiva la union-conexion con el stub del servicio de arcadio
		context.unbindService(conexion);
	}
	
	//variables


}
