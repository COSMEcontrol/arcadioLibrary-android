package com.arcadio.api.v1.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.arcadio.CosmeListener;
import com.arcadio.common.ItemVariable;
import com.arcadio.common.NamesList;
import com.arcadio.common.VariablesList;
import com.arcadio.service.api.v1.listeners.OnClientStartedListener;

import java.util.List;
import java.util.Map;
/**
 * Created by Alberto Azuara García on 05/12/14.
 */

public class PluginClientArcadio {
	
	public static IPluginServiceArcadio remoteArcadio;
	private OnClientStartedListener onclientstartedlistener;
    public static final String MINIBLAS_PACKAGE_NAME = "com.miniblas.app";
	public static final String MINIBLAS_SERVICE_ARCADIO = "com.arcadio.api.v1.service.ConnectionArcadioService";
	//variables de session
	private int sessionId = 0;
	private String sessionKey = "";
	private Context context;
	
	
	public PluginClientArcadio(Context context){
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
			remoteArcadio.connect1(connectionId, new ISessionStartedListener.Stub() {
                @Override
                public IBinder asBinder() {
                    return this;
                }

                @Override
                public void onSessionStarted(int _sessionId, String _sessionKey)
                        throws RemoteException {
                    sessionId = _sessionId;
                    sessionKey = _sessionKey;
                    Log.v("PluginClientArcadioLibrary-->", "Client identifier received API");
                }

                @Override
                public void onSessionError(String error) throws RemoteException {
                    Log.v("PluginClientArcadioLibrary-->", "Session Error: " + error);

                }
            }, new AdapterICosmeListener(cosmeListener));
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}
    /**
     * EXPLICITAMENTE indico que quiero terminar la conexión. No tiene sentido
     * que haya reconexión.
     */

	public void disconnect(){

		try {
			if(remoteArcadio== null)
				Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
			remoteArcadio.disconnect(sessionId, sessionKey);
		} catch (RemoteException e) {
			onclientstartedlistener.onClientStopped();	
		}

	}
	public void stopService(){
		//desactiva la union-conexion con el stub del servicio de arcadio
		context.unbindService(conexion);
	}
	
	//variables

    //
    //   A P I    P Ú B L I C A
    //
    //   Métodos a utilizar desde quienes implementen EmcosListener, según sus
    //   necesidades...
    //


    /**
     * Permite a un cliente obtener la lista de nombres existentes (públicos)
     * en el sistema. Puede ser un proceso largo que necesite del intercambio
     * de varios telegramas, por lo que se recomienda lanzarlo en una conexión
     * dedicada sólo a este propósito y ejecutada en un thread independiente
     * para no interferir con el resto de la aplicación.
     */
    public void addNameToBag(String _bagName, String _name){
        try {
            if(remoteArcadio== null) {
                Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
            }else {
                remoteArcadio.addNameToBag(sessionId, sessionKey, _bagName, _name);
            }
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }
    public void blockingRead1(String _name, int _timeout){
        try {
            if(remoteArcadio== null) {
                Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
            }else {
                remoteArcadio.blockingRead1(sessionId, sessionKey, _name, _timeout);
            }
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }

    }
    public void blockingRead2(String _name, int _timeout){
        try {
            if(remoteArcadio== null) {
                Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
            }else {
                remoteArcadio.blockingRead2(sessionId, sessionKey, _name, _timeout);
            }
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }

    }
    public void blockingWrite(String _name, double _value, int _timeout){
        try {
            if(remoteArcadio== null) {
                Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
            }else {
                remoteArcadio.blockingWrite(sessionId, sessionKey, _name, _value, _timeout);
            }
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }

    }

    public void connect2(ISessionStartedListener sessionListener, ICosmeListener _iCosmeListener, String _password, String _host, int _port){
        try {
            if(remoteArcadio== null) {
                Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
            }else {
                remoteArcadio.connect2(sessionListener, _iCosmeListener, _password, _host, _port);
            }
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }

    }
    public void createBag(String _bagName){
        try {
            if(remoteArcadio== null) {
                Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
            }else {
                remoteArcadio.createBag(sessionId, sessionKey, _bagName);
            }
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }

    }
    public void deleteBag(String _bagName){
        try {
            if(remoteArcadio== null) {
                Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
            }else {
                remoteArcadio.deleteBag(sessionId, sessionKey, _bagName);
            }
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }

    }
    public int getBagPeriod(String _bagName){
        try {
            if(remoteArcadio== null) {
                Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
            }else {
                return remoteArcadio.getBagPeriod(sessionId, sessionKey, _bagName);
            }
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return 0;
    }
    public List<String> getBags(){
        try {
            if(remoteArcadio== null) {
                Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
            }else {
                return remoteArcadio.getBags(sessionId, sessionKey);
            }
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return null;
    }
    public long getPingLatencyMs(){
        try {
            if(remoteArcadio== null) {
                Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
            }else {
                return remoteArcadio.getPingLatencyMs(sessionId, sessionKey);
            }
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return 0;
    }
    public ItemVariable getVariable(String _name){
        try {
            if(remoteArcadio== null) {
                Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
            }else {
                return remoteArcadio.getVariable(sessionId, sessionKey, _name);
            }
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return null;
    }
    public List<ItemVariable> getVariables(List<String> _names){
        try {
            if(remoteArcadio== null) {
                Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
            }else {
                remoteArcadio.getVariables(sessionId, sessionKey, _names);
            }
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return null;
    }
    public String getVersion(){
        try {
            if(remoteArcadio== null) {
                Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
            }else {
                return remoteArcadio.getVersion(sessionId, sessionKey);
            }
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return null;
    }
    public boolean isConnected(){
        try {
            if(remoteArcadio== null) {
                Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
            }else {
                return remoteArcadio.isConnected(sessionId, sessionKey);
            }
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return false;
    }
    public void removeNameFromBag(String _bagName, String _name){
        try {
            if(remoteArcadio== null) {
                Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
            }else {
                remoteArcadio.removeNameFromBag(sessionId, sessionKey, _bagName, _name);
            }
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }

    }
    public void setBagPeriod(String _bagName, int _ms){
        try {
            if(remoteArcadio== null) {
                Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
            }else {
                remoteArcadio.setBagPeriod(sessionId, sessionKey, _bagName, _ms);
            }
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }

    }
    public void setPingPeriod(int _ms){
        try {
            if(remoteArcadio== null) {
                Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
            }else {
                remoteArcadio.setPingPeriod(sessionId, sessionKey, _ms);
            }
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }

    }
    public void singleRead(VariablesList _vars){
        try {
            if(remoteArcadio== null) {
                Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
            }else {
                remoteArcadio.singleRead(sessionId, sessionKey, _vars);
            }
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }

    }
    public void waitForLastTelegram(int _msTimeout){
        try {
            if(remoteArcadio== null) {
                Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
            }else {
                remoteArcadio.waitForLastTelegram(sessionId, sessionKey, _msTimeout);
            }
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }

    }
    public void writeVariable1(String _name, double _value){
        try {
            if(remoteArcadio== null) {
                Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
            }else {
                remoteArcadio.writeVariable1(sessionId, sessionKey,_name, _value);
            }
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }

    }
    public void writeVariable2(String _name, String _value){
        try {
            if(remoteArcadio== null) {
                Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
            }else {
                remoteArcadio.writeVariable2(sessionId, sessionKey, _name, _value);
            }
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }

    }
    public void writeVariables3(VariablesList _names){
        try {
            if(remoteArcadio== null) {
                Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
            }else {
                remoteArcadio.writeVariables3(sessionId, sessionKey, _names);
            }
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }

    }
    //CosmeconnectorPlus
    public NamesList getNamesList(){
        try {
            if(remoteArcadio== null) {
                Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
            }else {
                return remoteArcadio.getNamesList();
            }
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return null;
    }
    public void requestNamesList(){
        try {
            if(remoteArcadio== null) {
                Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
            }else {
                remoteArcadio.requestNamesList();
            }
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

}
