package com.arcadio.api.v1.service;
import com.arcadio.api.v1.service.ISessionStartedListener;
import com.arcadio.api.v1.service.ICosmeListener;
import java.util.List;
interface IPluginServiceArcadio {

    // AIDLs can pass the following basic primitives:
    // int, long, boolean, float, double and String.
    // Anything else will have to be parcelable.

    void connect(int id,ISessionStartedListener sessionListener, ICosmeListener cosmeListener);
    void disconnect(int sessionId, String sessionKey);
    
	//variables
	void leerVariable(int sessionId, String sessionKey,String _nombreVariable);
	void leerListaVariables(int sessionId, String sessionKey,inout List<String> _listaVariables);
	void solicitarVariables(int sessionId, String sessionKey);
	void modificarValorVarible(int sessionId, String sessionKey,String _nombreVariable, double _valor);
	void isNumeric(int sessionId, String sessionKey,String _nombresVariables);
	
	//basket
	void crearCesta(int sessionId, String sessionKey,String _nombreCesta, int _periodoRefresco);
	void eliminarCesta(int sessionId, String sessionKey,String _nombreCesta);
	void modificarPeriodoCesta(int sessionId, String sessionKey,String _nombreCesta,int nuevoRefrescoMs);
	void introducirVariableACesta(int sessionId, String sessionKey,String _nombreCesta, String _nombreVariable);
	void introducirVariablesACesta(int sessionId, String sessionKey,String _nombreCesta, inout List<String> _listaVariables);
	void eliminarVariableDeCesta(int sessionId, String sessionKey,String _nombreCesta,String _nombreVariable);
	void eliminarVariablesDeCesta(int sessionId, String sessionKey,String _nombreCesta, inout List<String> _listaVariables);
	void solicitarListaVariablesCesta(int sessionId, String sessionKey,String _nombreCesta);
	void solicitarListaCestas(int sessionId, String sessionKey);
	
	void ping(int sessionId, String sessionKey,String _nombreVariable);
	void pingListaVariables(int sessionId, String sessionKey,inout List<String> _listaVariables);
	void tipoNombre(int sessionId, String sessionKey,String _nombreVariable);
	void pedirTipos(int sessionId, String sessionKey);
	void obtenerMaxTamBufferCosme(int sessionId, String sessionKey);
}