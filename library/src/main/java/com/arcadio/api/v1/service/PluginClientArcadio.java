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
import com.arcadio.common.NameList;
import com.arcadio.common.VariablesList;
import com.arcadio.service.api.v1.listeners.OnClientStartedListener;

import java.util.List;
import java.util.Map;
/**
 * Created by Alberto Azuara García on 26/11/14.
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
    /**
     * EXPLICITAMENTE indico que quiero terminar la conexión. No tiene sentido
     * que haya reconexión.
     */

	public void disconnect(){

		try {
			if(remoteArcadio== null)
				Log.v("PluginClientArcadioLibrary-->", "Not connected with Arcadio Service");
			remoteArcadio.desconectar(sessionId, sessionKey);
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
    public void solicitarListaNombres(){
        try {
            remoteArcadio.solicitarListaNombres(sessionId, sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     * Se envía un telegrama que solicita la lista de nombres que posean el prefijo
     * que se indica.
     * Cuando la lista de nombres se muestra en forma de árbol, permite obtener los
     * nombres que deben de colgar de una rama dada (el prefijo).
     * Cuando llegue el telegram de respuesta se disparará el evento RECIBIDA_LISTA_NOMBRES.
     * @param _prefijo
     */
    public void solicitarListaNombresNivel(String _prefijo){
        try {
            remoteArcadio.solicitarListaNombresNivel(sessionId, sessionKey, _prefijo);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     * Este método permite obtener la lista de nombres públicos definidiso en
     * el sistema, pero debe invocarse después de:
     *      1. haber invocado anteriormetne el método "solicitarNombres()"
     *      2. haber llegado al estado EstadosEmcos.LISTA_RECIBIDA
     * Esto es así porque la lista de nombres puede ser MUY larga, y necesitar
     * un número indeterminado de telegramas para su recepción (y también un número
     * indeterminado de segundos según las condiciones de la comunicación).
     *
     * @return Devuelve la lista de los nombres conocidos por el sistema. Si no se
     * ha llamado previamente al método "solicitarNombres()" o no se ha esperado
     * al evento EstadosEmcos.LISTA_RECIBIDA, la lista recibida podrá estar incompleta.
     */
    public NameList getListaNombres(){
        NameList nameList = null;
        try {
            nameList = remoteArcadio.getListaNombres(sessionId,sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return nameList;
    }

    /**
     * Devuelve la lista de nombres cuyo tipo coincida con el solicitado.
     * IMPORTANTE: es un método síncrono. No sigue el modelo de programación habitual en
     * ConexionEmcos (asíncrono).
     * @param _tipo
     * @param _msTimeout
     * @return Lista de nombres cuyo tipo es el que se pasa como parámetro.
     */
    public List<String> solicitarListaNombresDeTipoBloqueo(String _tipo, int _msTimeout){
        List<String> nameList = null;
        try {
            nameList = remoteArcadio.solicitarListaNombresDeTipoBloqueo(sessionId, sessionKey, _tipo, _msTimeout);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return nameList;
    }

    List<String> solicitarListaNombresTipoHabilitadosDeTipoBloqueo(String tipo, int _msTimeout){
        List<String> nameList = null;
        try {
            nameList = remoteArcadio.solicitarListaNombresTipoHabilitadosDeTipoBloqueo(sessionId,sessionKey,tipo,_msTimeout);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return nameList;
    }

    /**
     *
     * @param _idPrimerNombre
     */
    void solicitarSubListaNombres(int _idPrimerNombre){
        try {
            remoteArcadio.solicitarSubListaNombres(sessionId,sessionKey,_idPrimerNombre);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     *
     */
    public void solicitarListaTipos(){
        try {
            remoteArcadio.solicitarListaTipos(sessionId, sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }
    /**
     *
     * @return
     */
    public List<String> getListaTipos(){
        List<String> typesList = null;
        try {
            remoteArcadio.getListaTipos(sessionId, sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return typesList;
    }

    /**
     *
     */
    public void solicitarListaClases(){
        try {
            remoteArcadio.solicitarListaClases(sessionId, sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }
    /**
     *
     */
    public void solicitarListaNombresConfigurables(){
        try {
            remoteArcadio.solicitarListaNombresConfigurables(sessionId, sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     *
     * @return
     */
    public NameList getListaNombresConfigurables(){
        NameList nameList = null;
        try {
            nameList = remoteArcadio.getListaNombresConfigurables(sessionId, sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return nameList;
    }

    /**
     *
     */
    public void solicitarListaNombresConfigurablesReservados(){
        try {
            remoteArcadio.solicitarListaNombresConfigurablesReservados(sessionId,sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     *
     * @return
     */
    public NameList getListaNombresConfigurablesReservados(){
        NameList nameList = null;
        try {
            nameList = remoteArcadio.getListaNombresConfigurablesReservados(sessionId, sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return nameList;
    }

    /**
     *
     * @param _nombreTipo
     */
    public void solicitarListaNombresTipos(String _nombreTipo){
        try {
            remoteArcadio.solicitarListaNombresTipos(sessionId, sessionKey, _nombreTipo);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     *
     * @return
     */
    public Map getListaNombresTipos(){
        Map listaNombresTipos = null;
        try {
            listaNombresTipos = remoteArcadio.getListaNombresTipos(sessionId, sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return listaNombresTipos;
    }

    /**
     *
     */
    public void lecturaPuntual(VariablesList _listaVariables){
        try {
            remoteArcadio.lecturaPuntual(sessionId, sessionKey, _listaVariables);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     * Igualico que lecturaPuntual, pero manda el telegrama ping, y registra
     * el instante en que se envió.
     * @param _listaVariables
     */
    void ping(VariablesList _listaVariables){
        try {
            remoteArcadio.ping(sessionId, sessionKey, _listaVariables);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     *
     * @param _listaVariables
     * @param _msTimeout
     * @return
     */
    VariablesList leerBloqueo(VariablesList _listaVariables, int _msTimeout){
        VariablesList variablesList = null;
        try {
            variablesList = remoteArcadio.leerBloqueo(sessionId, sessionKey, _listaVariables, _msTimeout);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return variablesList;
    }


    /**
     *
     * @param _prefijoCesta No debe existir ninguna otra cesta con el mismo prefijo. Tampoco debe
     * contener ningún carácter Cesta.BASKET_NAME_SEPARATOR . En cuaquiera de estos dos casos lanza una EmcosException.
     * @param _nombres
     */
    public void crearCesta(String _prefijoCesta, List<String> _nombres,
                    int event_time, int inhibit_time){
        try {
            remoteArcadio.crearCesta(sessionId,sessionKey, _prefijoCesta, _nombres, event_time, inhibit_time);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    public List<String> getCestas(){
        List<String> basketList = null;
        try {
            basketList = remoteArcadio.getCestas(sessionId, sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return basketList;
    }

    /**
     * Permite añadir un único nombre a la cesta indicada.
     * Puede usarse "insertarNombres" si se desea insertar un conjunto de ellos.
     * @param _prefijoCesta Nombre de la cesta a la que van a añadirse los nombres
     * @param _nombre Nombre que va a insertarse en la cesta
     */
    public void insertarNombre(String _prefijoCesta, String _nombre){
        try {
            remoteArcadio.insertarNombre(sessionId, sessionKey,_prefijoCesta, _nombre);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }
    /**
     * Permite añadir una lista de nombres a la cesta que se indica como primer parámetro.
     * @param _prefijoCesta Nombre de la cesta a la que van a añadirse los nombres
     * @param _nombres Colección de nombres que van a insertarse en la cesta
     */
    public void insertarNombres(String _prefijoCesta, List<String> _nombres){
        try {
            remoteArcadio.insertarNombres(sessionId, sessionKey, _prefijoCesta, _nombres);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     * Permite eliminar un nombre de la cesta que se indica como primer parámetro.
     * @param _prefijoCesta Nombre de la cesta de la que va a eliminarse el nombre.
     * @param _nombre Nombre que va a eliminarse de la cesta
     */
    public void eliminarNombre(String _prefijoCesta, String _nombre){
        try {
            remoteArcadio.eliminarNombre(sessionId, sessionKey, _prefijoCesta, _nombre);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     * Permite eliminar una lista de nombres de la cesta que se indica como primer parámetro.
     * @param _prefijoCesta Nombre de la cesta a la que van a añadirse los nombres
     * @param _nombres Colección de nombres que van a eliminarse de la cesta
     */
    public void eliminarNombres(String _prefijoCesta,List<String> _nombres){
        try {
            remoteArcadio.eliminarNombres(sessionId, sessionKey, _prefijoCesta, _nombres);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     * Permite modificar el periodo de refresco de la cesta indicada.
     * @param _prefijoCesta Nombre de la cesta a la que va a modificarse su periodo de refresco.
     * @param _nuevoPeriodoRefresco Nuevo periodo en ms.
     */
    public void setPeriodoCesta(String _prefijoCesta, int _nuevoPeriodoRefresco){
        try {
            remoteArcadio.setPeriodoCesta(sessionId, sessionKey, _prefijoCesta, _nuevoPeriodoRefresco);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    public int getPeriodoCesta(String _prefijoCesta){
        int basketPeriod = 0;
        try {
            remoteArcadio.getPeriodoCesta(sessionId,sessionKey, _prefijoCesta);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return  basketPeriod;
    }

    /**
     *  DEPRECATED. Ha sido sustituido por <b>eliminarCesta(_nc)</b>. Todos los otros métodos similares se llaman eliminarXXXX, y
     * este no tiene por qué ser una excepción.
     * Cualquier día de estos este método desaparecerá de la API.
     * @param _prefijoCesta Nombre de la cesta que deseamos eliminar.
     */
    public void borrarCesta(String _prefijoCesta){
        try {
            remoteArcadio.borrarCesta(sessionId, sessionKey, _prefijoCesta);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     *  Permite destruir una cesta existente.
     * @param _prefijoCesta Nombre de la cesta que deseamos eliminar.
     */
    public void eliminarCesta(String _prefijoCesta){
        try {
            remoteArcadio.eliminarCesta(sessionId, sessionKey, _prefijoCesta);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    public void setRetardoTelegramas(int _nuevoRetardoTelegramas){
        try {
            remoteArcadio.setRetardoTelegramas(sessionId, sessionKey, _nuevoRetardoTelegramas);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

   public boolean existeNombre(String _prefijoCesta, String _nombre){
       boolean exist = false;
       try {
           exist = remoteArcadio.existeNombre(sessionId, sessionKey, _prefijoCesta, _nombre);
       } catch (RemoteException e) {
           onclientstartedlistener.onClientStopped();
       }
       return exist;
   }

    /**
     * Devuelve el ItemVariable asociado al nombre que se pasa como parametro.
     * Contendra el último valor recibido en una cesta o en un telegrama leer, leer_bloqueo, ping.
     * @param _nombre
     * @return
     */
    public ItemVariable getVariable(String _nombre){
        ItemVariable itemVariable = null;
        try {
            remoteArcadio.getVariable(sessionId, sessionKey, _nombre);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return itemVariable;
    }

    public List<ItemVariable> getVariables(List<String> _nombres){
        List<ItemVariable> itemVariableList = null;
        try {
            itemVariableList = remoteArcadio.getVariables(sessionId, sessionKey, _nombres);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return itemVariableList;
    }

    /**
     *
     * @param _nombreVariable
     * @param _nuevoValor
     */
    public void modificarVariable(String _nombreVariable, double _nuevoValor){
        try {
            remoteArcadio.modificarVariable1(sessionId, sessionKey, _nombreVariable, _nuevoValor);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     *
     * @param _nombreVariable
     * @param _nuevoValor
     */
    public void modificarVariable(String _nombreVariable, String _nuevoValor){
        try {
            remoteArcadio.modificarVariable2(sessionId, sessionKey, _nombreVariable, _nuevoValor);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     *
     * @param _lv
     */
    public void modificarVariable(VariablesList _lv){
        try {
            remoteArcadio.modificarVariable3(sessionId,sessionKey, _lv);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }
    /**
     * ############   ????
     * @param _lv
     * @param first
     * @param dim
     */
    public void modificarVariable(List<ItemVariable> _lv, int first, int dim){
        try {
            remoteArcadio.modificarVariable4(sessionId, sessionKey, _lv, first, dim);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     *  ####COMPROBAR QUE FUNCIONAAAA. Ahora usa waitUltimoTelegrama
     *
     * @param _nombreVariable
     * @param _nuevoValor
     * @param _msTimeout
     */
    public void modificarVariableBloqueo(String _nombreVariable, double _nuevoValor,
                                         int _msTimeout){
        try {
            remoteArcadio.modificarVariableBloqueo1(sessionId, sessionKey, _nombreVariable,
                    _nuevoValor, _msTimeout);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     * ####COMPROBAR QUE FUNCIONAAAA. Ahora usa waitUltimoTelegrama
     *
     * @param _nombreVariable
     * @param _nuevoValor
     */
    public void modificarVariableBloqueo(String _nombreVariable, String _nuevoValor){
        try {
            remoteArcadio.modificarVariable2(sessionId, sessionKey, _nombreVariable, _nuevoValor);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     * ####COMPROBAR QUE FUNCIONAAAA. Ahora usa waitUltimoTelegrama
     *
     * @param _lv
     */
    void modificarVariableBloqueo(VariablesList _lv){
        try {
            remoteArcadio.modificarVariable3(sessionId, sessionKey, _lv);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }


        /* TODO ÁNGEL: Para qué???
        public void modificarVariableBloqueo(ArrayList<ItemVariable> _lv, int quantum,int elStruct,
        int _msTimeout,String downloaded)
        throws EmcosTimeoutException ;

        public void modificarVariableBloqueo(ArrayList<ItemVariable> _lv, int init,int quantum,
        int _msTimeout)
        throws EmcosTimeoutException;
        */

    /**
     * Permite conocer el último evento de estado recibido.
     * @return
     */
    public ParceableCosmeStates getEstado(){
        ParceableCosmeStates parceableCosmeStates = null;
        try {
            parceableCosmeStates = remoteArcadio.getEstado(sessionId, sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return parceableCosmeStates;
    }

    /**
     * Permite crear un nuevo muestreador.
     *
     * @param _nombreVariableAMuestrear
     * @param _nombreVariableDisparo
     * @param _numMuestras
     * @param _msMuestreo
     * @return
     */


    /**
     *  Arcadio Plus
     * El parámetro "_pathFichero" contiene la ruta relativa al fichero solicitado,
     * a partir del directorio del proyecto (por defecto: /usr/emcos/projects/xxx"
     *
     * @param _pathFichero
     */
    public void solicitarFicheroTexto(String _nombreInstancia, String _pathFichero){
        try {
            remoteArcadio.solicitarFicheroTexto(sessionId, sessionKey, _nombreInstancia, _pathFichero);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     * Arcadio Plus
     * El parámetro "_contenidoFicheroEMC" contiene el contenido del nuevo
     * fichero ".emc" que ha modificado un gestor de configuración.
     *
     * @param _contenidoFicheroEMC
     */
    public void enviarActualizacionFicheroEMC(String _contenidoFicheroEMC){
        try {
            remoteArcadio.enviarActualizacionFicheroEMC(sessionId, sessionKey, _contenidoFicheroEMC);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     * Envía el telegrama que permite conocer a qué clase pertenece la instancia cuyo nombre se
     * pasa como parámetro.
     */
    public String getTipoNombreBloqueo(String _nombre){
        String nameBlock = "";
        try {
           nameBlock=remoteArcadio.getTipoNombreBloqueo(sessionId, sessionKey, _nombre);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return nameBlock;
    }

    public void getTipoNombre(String _nombre){
        try {
            remoteArcadio.getTipoNombre(sessionId, sessionKey, _nombre);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     *
     * @return
     */
    public NameList getNombresExistentes(){
        NameList nameList = null;
        try {
            remoteArcadio.getNombresExistentes(sessionId, sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return nameList;
    }

    public boolean isDebugON(){
        boolean isDebugOn = false;
        try {
            isDebugOn = remoteArcadio.isDebugON(sessionId, sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return isDebugOn;
    }

    public void setDebug(boolean debug){
        try {
            remoteArcadio.setDebug(sessionId, sessionKey, debug);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }
    public int getMsRetardoTelegramas(){
        int msRetardoTelegrama =0;
        try {
            msRetardoTelegrama=remoteArcadio.getMsRetardoTelegramas(sessionId, sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return msRetardoTelegrama;
    }
    public void setMsRetardoTelegramas(int msRetardoTelegramas){
        try {
            remoteArcadio.setMsRetardoTelegramas(sessionId, sessionKey, msRetardoTelegramas);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }
    /**
     *Si está activado el CTP (Control de Telegramas Perdidos), devuelve el nÃºmero de telegramas
     *que han sido enviados, pero de los que aún no hemos recibido respuesta (son por lo tanto
     *candidatos a haberse perdido).
     */
    public int getNumTelegramasPendientesConfirmacion(){
        int numTelegrams=0;
        try {
            numTelegrams = remoteArcadio.getNumTelegramasPendientesConfirmacion(sessionId, sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return numTelegrams;
    }

    /**
     *Activa el CTP. La librería sigue la pista de los telegramas enviados, para saber
     *si han sido contestados o si se han perdido por el camino (en cuyo caso los
     *reenvía automáticamente).
     *En muchas configuraciones el uso de CTP no será necesario.
     */
    public void activarCTP(){
        try {
            remoteArcadio.activarCTP(sessionId, sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    public void desactivarCTP(){
        try {
            remoteArcadio.desactivarCTP(sessionId, sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     *El funcionamiento de CTP se basa en un thread que se despierta cada x milisegundos,
     *y comprueba si ha hecho timeout alguno de los telegramas marcados como
     *pendientes.
     *Este mÃ©todo permite fijar esa "x". Cada cuÃ¡nto tiempo CTP va a buscar telegramas
     *perdidos.
     */
    public void setPeriodoActivacionCTP(long _ms){
        try {
            remoteArcadio.setPeriodoActivacionCTP(sessionId, sessionKey, _ms);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     *
     */
    public void setTimeoutCTP(long _ms){
        try {
            remoteArcadio.setTimeoutCTP(sessionId, sessionKey, _ms);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     *  Arcadio Plus
     * ########### Replantear PLS
     * @param _nuevoNivelAcceso
     * @param _password
     */
    public void cambiarNivelAcceso(ParceableAccessLevels _nuevoNivelAcceso, String _password){
        try {
            remoteArcadio.cambiarNivelAcceso(sessionId, sessionKey, _nuevoNivelAcceso, _password);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     * Devuelve la longitud máxima (en caracteres) que podrá tener un telegrama.
     * Si enviamos un telegrama cuya longitud supere este valor, nos exponemos a
     * que se trunque y no sea correctamente procesado por el kernel.
     * Mensajero no enviará telegramas de tamaño superior a este valor.
     * @return
     */
    public int getMaxLengthTelegram(){
        int maxlenghtTelegram = 0;
        try {
            maxlenghtTelegram = remoteArcadio.getMaxLengthTelegram(sessionId, sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return maxlenghtTelegram;
    }

    /**
     * Devuelve el número de milisegundos empleados para procesar el último telegrama
     * recibido. Mide el tiempo usado por el método run de la clase "OyenteTelegrama",
     * desde que se sale del readLine, hasta que vuelve el bucle while para bloquearse
     * en la espera del siguiente telegrama.
     * Este tiempo incluye el utilizado por los métodos "notificarRefrescoVariables"
     * (y también los otros "notificarXXX" del cliente EmcosListener.
     *
     * @return the msTlgProcessing
     */
    public long getMsTlgProcessing(){
        long msTlgProcessing=0;
        try {
            msTlgProcessing = remoteArcadio.getMsTlgProcessing(sessionId, sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return msTlgProcessing;
    }

    /**
     *
     * @return Devuelve la cadena con la versión de arcadio.
     */
    public String getTxtVersion(){
        String txtVersion = "";
        try {
            txtVersion = remoteArcadio.getTxtVersion(sessionId, sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return txtVersion;
    }

    /**
     *
     * @return Devuelve sólamente el código de versión.
     */
    //
    public String getVersion(){
        String version ="";
        try {
            remoteArcadio.getVersion(sessionId, sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return version;
    }

    /**
     * Envía un telegrama que nos permitirá saber si los nombres que se pasan
     * como parametro son "int", "real" o "nonum".
     * El telegrama de respuesta disparará el evento "RECIBIDO_ISNUMERIC".
     *
     * @param _listaNombres
     */
    public void isNumeric(NameList _listaNombres){
        try {
            remoteArcadio.isNumeric(sessionId, sessionKey, _listaNombres);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     * ########  D  A  N  G  E  R  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     *
     * A fecha de hoy (1.17g 22-5-2008) este método no lo llama nadie.
     * Da mucho MIEDO!!!! Abre la puerta a inyectar cualquier veneno con total impunidad.
     *
     * @param _peticion
     */
    public void enviarTelegramaLibre (String _peticion){
        try {
            remoteArcadio.enviarTelegramaLibre(sessionId, sessionKey, _peticion);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     * Sólo se utiliza en el CTP. Candidato a desaparecer  //###########
     * @param _tlgAReenviar
     */
    public void reenviarTelegrama(String _tlgAReenviar){
        try {
            remoteArcadio.reenviarTelegrama(sessionId, sessionKey, _tlgAReenviar);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     * @return Devuelve el "num_peticion" del último telegrama que se haya enviado.
     * Útil para gestionar los telegramas con bloqueo
     */
    public int getNumUltimoTelegramaEnviado(){
        int lastNumTelegramSend=0;
        try {
            lastNumTelegramSend = remoteArcadio.getNumUltimoTelegramaRecibido(sessionId, sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return lastNumTelegramSend;
    }

    /**
     * @return Devuelve el "num_peticion" del último telegrama que haya recibido el OyenteTelegrama.
     * Útil para gestionar los telegramas con bloqueo
     */
    public int getNumUltimoTelegramaRecibido(){
        int lastTelegramReceibed=0;
        try {
            lastTelegramReceibed = remoteArcadio.getNumUltimoTelegramaRecibido(sessionId,
                    sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return lastTelegramReceibed;
    }

    /**
     * Permite que el thread desde el que invoquemos este método se bloquee hasta que hayamos
     * recibido el eco del último telegrama que se haya enviado.
     * Si esto no sucede en "_msTimeOut" ms, entonces se lanza una excepción.
     *
     * @param _msTimeOut
     */
    public void waitUltimoTelegrama(long _msTimeOut){
        try {
            remoteArcadio.waitUltimoTelegrama(sessionId, sessionKey, _msTimeOut);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     * ***Félix dice***: en mi opinión los usuarios no deberían tener control sobre estructuras
     * tan internas. Hay que ofrecerles una abstracción más digerible y universal.
     */
        /*
        public void cleanTlgs();

        /**
         * @return el número de nanosegundos transcurridos entre el envío y la recepción
         * del último telegrama de ping.
         */
    public long getPingTime(){
        long pingtime = 0;
        try {
            pingtime=remoteArcadio.getPingTime(sessionId, sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return pingtime;
    }

    /**
     * Permite modificar el periodo de envío de los telegramas "ping", siempre que
     * el watchdog esté habilitado (ver el método "setWatchdogEnabled()").
     * Si el periodo es 0 (o negativo), equivale a deshabilitar el watchdog.
     * @param _ms Valor en milisegundos.
     */
    public void setWatchdowgPeriod(int _ms){
        try {
            remoteArcadio.setWatchdowgPeriod(sessionId, sessionKey, _ms);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     * Permite habilitar/deshabilitar el watchdog. Este es un thread que envía periódicamente
     * un telegrama "ping" para generar tráfico entre arcadio y la pasarela, evitando que sus
     * sockets hagan timeout por falta de actividad.
     * El periodo de envío de esos telegramas "ping" puede modificarse con el método
     * "setWatchdgPeriod()", que por defecto es de 2 segundos.
     *
     * @param _enabled
     */
    public void setWatchdogEnabled (boolean _enabled){
        try {
            remoteArcadio.setWatchdogEnabled(sessionId, sessionKey, _enabled);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    /**
     * Permite conocer el número de ms que deben transcurrir sin que el socket reciba
     * nada, para que salte el timeout.
     * @return the socketTimeout
     */
    public int getSocketTimeout(){
        int socketTimeout = 0;
        try {
            socketTimeout = remoteArcadio.getSocketTimeout(sessionId, sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return socketTimeout;
    }

    /**
     * Permite modificar el número de ms que deben transcurrir para que salte el timeout
     * del socket cuando no se haya recibido nada en ese tiempo.
     * @param _socketTimeout the socketTimeout to set
     */
    public void setSocketTimeout(int _socketTimeout){
        try {
            remoteArcadio.setSocketTimeout(sessionId, sessionKey, _socketTimeout);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();;
        }
    }

    public boolean isConnected(){
        boolean isConected = false;
        try {
            isConected = remoteArcadio.isConnected(sessionId, sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return isConected;
    }

    /**
     * @return the reconexionActivada
     */
    public boolean isReconexionActivada(){
        boolean isReconexionActivada = false;
        try {
            isReconexionActivada = remoteArcadio.isReconexionActivada(sessionId, sessionKey);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
        return isReconexionActivada;
    }
    /**
     * @param _reconexionActivada the reconexionActivada to set
     */
    public void setReconexionActivada(boolean _reconexionActivada){
        try {
            remoteArcadio.setReconexionActivada(sessionId, sessionKey, _reconexionActivada);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    public void setTipoNombre(String tipoNombre){
        try {
            remoteArcadio.setTipoNombre(sessionId, sessionKey, tipoNombre);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

    public void setConnection(String input, String output){
        try {
            remoteArcadio.setConnection(sessionId, sessionKey, input, output);
        } catch (RemoteException e) {
            onclientstartedlistener.onClientStopped();
        }
    }

}
