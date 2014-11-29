package com.arcadio.api.v1.service;
import com.arcadio.api.v1.service.ISessionStartedListener;
import com.arcadio.api.v1.service.ICosmeListener;
import java.util.List;
import com.arcadio.modelo.NameList;
import com.arcadio.modelo.VariablesList;
import com.arcadio.modelo.ItemVariable;
import java.util.Map;
import com.arcadio.api.v1.service.ParceableAccessLevels;
import com.arcadio.api.v1.service.ParceableCosmeStates;
interface IPluginServiceArcadio {

    // AIDLs can pass the following basic primitives:
    // int, long, boolean, float, double and String.
    // Anything else will have to be parcelable.

    void connect(int id,ISessionStartedListener sessionListener, ICosmeListener cosmeListener);
    



	 //
        //   A P I    P Ú B L I C A
        //
        //   Métodos a utilizar desde quienes implementen EmcosListener, según sus
        //   necesidades...
        //
        /**
         * EXPLICITAMENTE indico que quiero terminar la conexión. No tiene sentido
         * que haya reconexión.
         */
     void desconectar(int sessionId, String sessionKey);

        /**
         * Permite a un cliente obtener la lista de nombres existentes (públicos)
         * en el sistema. Puede ser un proceso largo que necesite del intercambio
         * de varios telegramas, por lo que se recomienda lanzarlo en una conexión
         * dedicada sólo a este propósito y ejecutada en un thread independiente
         * para no interferir con el resto de la aplicación.
         */
     void solicitarListaNombres(int sessionId, String sessionKey);

        /**
         * Se envía un telegrama que solicita la lista de nombres que posean el prefijo
         * que se indica.
         * Cuando la lista de nombres se muestra en forma de árbol, permite obtener los
         * nombres que deben de colgar de una rama dada (el prefijo).
         * Cuando llegue el telegram de respuesta se disparará el evento RECIBIDA_LISTA_NOMBRES.
         * @param _prefijo
         */
     void solicitarListaNombresNivel(int sessionId, String sessionKey,String _prefijo);

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
     NameList getListaNombres(int sessionId, String sessionKey);

        /**
         * Devuelve la lista de nombres cuyo tipo coincida con el solicitado.
         * IMPORTANTE: es un método síncrono. No sigue el modelo de programación habitual en
         * ConexionEmcos (asíncrono).
         * @param _tipo
         * @param _msTimeout
         * @return Lista de nombres cuyo tipo es el que se pasa como parámetro.
         */
     List<String> solicitarListaNombresDeTipoBloqueo(int sessionId, String sessionKey,String _tipo, int _msTimeout);

     List<String> solicitarListaNombresTipoHabilitadosDeTipoBloqueo(int sessionId, String sessionKey,String tipo, int _msTimeout);

        /**
         *
         * @param _idPrimerNombre
         */
     void solicitarSubListaNombres(int sessionId, String sessionKey,int _idPrimerNombre);

        /**
         *
         */
     void solicitarListaTipos(int sessionId, String sessionKey);
        /**
         *
         * @return
         */
     List<String> getListaTipos(int sessionId, String sessionKey);

        /**
         *
         */
     void solicitarListaClases(int sessionId, String sessionKey);
        /**
         *
         */
     void solicitarListaNombresConfigurables(int sessionId, String sessionKey);

        /**
         *
         * @return
         */
     NameList getListaNombresConfigurables(int sessionId, String sessionKey);

        /**
         *
         */
     void solicitarListaNombresConfigurablesReservados(int sessionId, String sessionKey);

        /**
         *
         * @return
         */
     NameList getListaNombresConfigurablesReservados(int sessionId, String sessionKey);

        /**
         *
         * @param _nombreTipo
         */
     void solicitarListaNombresTipos(int sessionId, String sessionKey,String _nombreTipo);

        /**
         *
         * @return
         */
     Map getListaNombresTipos(int sessionId, String sessionKey);

        /**
         *
         * @param _listaVariables
         */
     void lecturaPuntual(int sessionId, String sessionKey,inout VariablesList _listaVariables);

        /**
         * Igualico que lecturaPuntual, pero manda el telegrama ping, y registra
         * el instante en que se envió.
         * @param _listaVariables
         */
     void ping(int sessionId, String sessionKey,inout VariablesList _listaVariables);

        /**
         *
         * @param _listaVariables
         * @param _msTimeout
         * @return
         * @throws EmcosTimeoutException
         */
     VariablesList leerBloqueo(int sessionId, String sessionKey,inout VariablesList _listaVariables, int _msTimeout);


        /**
         *
         * @param _prefijoCesta No debe existir ninguna otra cesta con el mismo prefijo. Tampoco debe
         * contener ningún carácter Cesta.BASKET_NAME_SEPARATOR . En cuaquiera de estos dos casos lanza una EmcosException.
         * @param _nombres
         * @param _event_time
         * @throws EmcosException
         */
     void crearCesta(int sessionId, String sessionKey,String _prefijoCesta, inout List<String> _nombres,
                               int event_time, int inhibit_time);

     List<String> getCestas(int sessionId, String sessionKey);

        /**
         * Permite añadir un único nombre a la cesta indicada.
         * Puede usarse "insertarNombres" si se desea insertar un conjunto de ellos.
         * @param _nombreCesta Nombre de la cesta a la que van a añadirse los nombres
         * @param _nombre Nombre que va a insertarse en la cesta
         */
     void insertarNombre(int sessionId, String sessionKey,String _prefijoCesta, String _nombre);
        /**
         * Permite añadir una lista de nombres a la cesta que se indica como primer parámetro.
         * @param _nombreCesta Nombre de la cesta a la que van a añadirse los nombres
         * @param _nombres Colección de nombres que van a insertarse en la cesta
         */
     void insertarNombres(int sessionId, String sessionKey,String _prefijoCesta, inout List<String> _nombres);

        /**
         * Permite eliminar un nombre de la cesta que se indica como primer parámetro.
         * @param _nombreCesta Nombre de la cesta de la que va a eliminarse el nombre.
         * @param _nombre Nombre que va a eliminarse de la cesta
         */
     void eliminarNombre(int sessionId, String sessionKey,String _prefijoCesta, String _nombre);

        /**
         * Permite eliminar una lista de nombres de la cesta que se indica como primer parámetro.
         * @param _nombreCesta Nombre de la cesta a la que van a añadirse los nombres
         * @param _nombres Colección de nombres que van a eliminarse de la cesta
         */
     void eliminarNombres(int sessionId, String sessionKey,String _prefijoCesta,inout List<String> _nombres);

        /**
         * Permite modificar el periodo de refresco de la cesta indicada.
         * @param _nombreCesta Nombre de la cesta a la que va a modificarse su periodo de refresco.
         * @param _nuevoPeriodoRefresco Nuevo periodo en ms.
         */
     void setPeriodoCesta(int sessionId, String sessionKey,String _prefijoCesta, int _nuevoPeriodoRefresco);

     int getPeriodoCesta(int sessionId, String sessionKey,String _prefijoCesta);

        /**
         *  >>>> DEPRECATED. Ha sido sustituido por <b>eliminarCesta(_nc)</b>. Todos los otros métodos similares se llaman eliminarXXXX, y
         * este no tiene por qué ser una excepción.
         * Cualquier día de estos este método desaparecerá de la API.
         * @param _nombreCesta Nombre de la cesta que deseamos eliminar.
         */
     void borrarCesta(int sessionId, String sessionKey,String _prefijoCesta);

        /**
         *  Permite destruir una cesta existente.
         * @param _nombreCesta Nombre de la cesta que deseamos eliminar.
         */
     void eliminarCesta(int sessionId, String sessionKey,String _prefijoCesta);

     void setRetardoTelegramas(int sessionId, String sessionKey,int _nuevoRetardoTelegramas);

     boolean existeNombre(int sessionId, String sessionKey,String _prefijoCesta, String _nombre);

        /**
         * Devuelve el ItemVariable asociado al nombre que se pasa como parametro.
         * Contendra el último valor recibido en una cesta o en un telegrama leer, leer_bloqueo, ping.
         * @param _nombre
         * @return
         */
     ItemVariable getVariable(int sessionId, String sessionKey,String _nombre);

     List<ItemVariable> getVariables(int sessionId, String sessionKey,inout List<String> _nombres);

        /**
         *
         * @param _nombreVariable
         * @param _nuevoValor
         */
     void modificarVariable1(int sessionId, String sessionKey,String _nombreVariable, double _nuevoValor);

        /**
         *
         * @param _nombreVariable
         * @param _nuevoValor
         */
     void modificarVariable2(int sessionId, String sessionKey,String _nombreVariable, String _nuevoValor);

        /**
         *
         * @param _lv
         */
     void modificarVariable3(int sessionId, String sessionKey,inout VariablesList _lv);
        /**
         * >>>>############   ????
         * @param _lv
         * @param first
         * @param dim
         */
     void modificarVariable4(int sessionId, String sessionKey,inout List<ItemVariable> _lv, int first, int dim);

        /**
         * >>>> ####COMPROBAR QUE FUNCIONAAAA. Ahora usa waitUltimoTelegrama
         *
         * @param _nombreVariable
         * @param _nuevoValor
         * @param _msTimeout
         * @throws EmcosTimeoutException
         */
     void modificarVariableBloqueo1(int sessionId, String sessionKey,String _nombreVariable,
                                                          double _nuevoValor,
                                                          int _msTimeout);

        /**
         * >>>>> ####COMPROBAR QUE FUNCIONAAAA. Ahora usa waitUltimoTelegrama
         *
         * @param _nombreVariable
         * @param _nuevoValor
         * @param _msTimeout
         * @throws EmcosTimeoutException
         */
     void modificarVariableBloqueo2(int sessionId, String sessionKey,String _nombreVariable,
                                                          String _nuevoValor,
                                                          int _msTimeout);

        /**
         * >>>>> ####COMPROBAR QUE FUNCIONAAAA. Ahora usa waitUltimoTelegrama
         *
         * @param _lv
         * @param _msTimeout
         * @throws EmcosTimeoutException
         */
     void modificarVariableBloqueo3(int sessionId, String sessionKey,inout VariablesList _lv, int _msTimeout);


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
     ParceableCosmeStates getEstado(int sessionId, String sessionKey);

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
         * >> Arcadio Plus
         * El parámetro "_pathFichero" contiene la ruta relativa al fichero solicitado,
         * a partir del directorio del proyecto (por defecto: /usr/emcos/projects/xxx"
         *
         * @param _pathFichero
         */
     void solicitarFicheroTexto(int sessionId, String sessionKey,String _nombreInstancia, String _pathFichero);

        /**
         * >>> Arcadio Plus
         * El parámetro "_contenidoFicheroEMC" contiene el contenido del nuevo
         * fichero ".emc" que ha modificado un gestor de configuración.
         *
         * @param _contenidoFicheroEMC
         */
     void enviarActualizacionFicheroEMC(int sessionId, String sessionKey,String _contenidoFicheroEMC);

        /**
         * Envía el telegrama que permite conocer a qué clase pertenece la instancia cuyo nombre se
         * pasa como parámetro.
         */
      String getTipoNombreBloqueo(int sessionId, String sessionKey,String _nombre);

     void getTipoNombre(int sessionId, String sessionKey,String _nombre);

        /**
         *
         * @return
         */
     NameList getNombresExistentes(int sessionId, String sessionKey);

     boolean isDebugON(int sessionId, String sessionKey);

     void setDebug(int sessionId, String sessionKey,boolean debug);
     int getMsRetardoTelegramas(int sessionId, String sessionKey);
     void setMsRetardoTelegramas(int sessionId, String sessionKey,int msRetardoTelegramas);
        /**
         *Si está activado el CTP (Control de Telegramas Perdidos), devuelve el nÃºmero de telegramas
         *que han sido enviados, pero de los que aún no hemos recibido respuesta (son por lo tanto
         *candidatos a haberse perdido).
         */
     int getNumTelegramasPendientesConfirmacion(int sessionId, String sessionKey);

        /**
         *Activa el CTP. La librería sigue la pista de los telegramas enviados, para saber
         *si han sido contestados o si se han perdido por el camino (en cuyo caso los
         *reenvía automáticamente).
         *En muchas configuraciones el uso de CTP no será necesario.
         */
     void activarCTP(int sessionId, String sessionKey);

     void desactivarCTP(int sessionId, String sessionKey);

        /**
         *El funcionamiento de CTP se basa en un thread que se despierta cada x milisegundos,
         *y comprueba si ha hecho timeout alguno de los telegramas marcados como
         *pendientes.
         *Este mÃ©todo permite fijar esa "x". Cada cuÃ¡nto tiempo CTP va a buscar telegramas
         *perdidos.
         */
     void setPeriodoActivacionCTP(int sessionId, String sessionKey,long _ms);

        /**
         *
         */
     void setTimeoutCTP(int sessionId, String sessionKey,long _ms);

        /**
         * >> Arcadio Plus
         * ########### Replantear PLS
         * @param _nuevoNivelAcceso
         * @param _password
         */
     void cambiarNivelAcceso(int sessionId, String sessionKey, inout ParceableAccessLevels _nuevoNivelAcceso, String _password);

        /**
         * Devuelve la longitud máxima (en caracteres) que podrá tener un telegrama.
         * Si enviamos un telegrama cuya longitud supere este valor, nos exponemos a
         * que se trunque y no sea correctamente procesado por el kernel.
         * Mensajero no enviará telegramas de tamaño superior a este valor.
         * @return
         */
     int getMaxLengthTelegram(int sessionId, String sessionKey);

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
     long getMsTlgProcessing(int sessionId, String sessionKey);

        /**
         *
         * @return Devuelve la cadena con la versión de arcadio.
         */
     String getTxtVersion(int sessionId, String sessionKey);

        /**
         *
         * @return Devuelve sólamente el código de versión.
         */
        //
     String getVersion(int sessionId, String sessionKey);

        /**
         * Envía un telegrama que nos permitirá saber si los nombres que se pasan
         * como parametro son "int", "real" o "nonum".
         * El telegrama de respuesta disparará el evento "RECIBIDO_ISNUMERIC".
         *
         * @param _listaNombres
         */
     void isNumeric(int sessionId, String sessionKey,inout NameList _listaNombres);

        /**
         * //########  D  A  N  G  E  R  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         *
         * A fecha de hoy (1.17g 22-5-2008) este método no lo llama nadie.
         * Da mucho MIEDO!!!! Abre la puerta a inyectar cualquier veneno con total impunidad.
         *
         * @param _peticion
         */
     void enviarTelegramaLibre (int sessionId, String sessionKey,String _peticion);

        /**
         * Sólo se utiliza en el CTP. Candidato a desaparecer  //###########
         * @param _tlgAReenviar
         */
     void reenviarTelegrama(int sessionId, String sessionKey,String _tlgAReenviar);

        /**
         * @return Devuelve el "num_peticion" del último telegrama que se haya enviado.
         * Útil para gestionar los telegramas con bloqueo
         */
     int getNumUltimoTelegramaEnviado(int sessionId, String sessionKey);

        /**
         * @return Devuelve el "num_peticion" del último telegrama que haya recibido el OyenteTelegrama.
         * Útil para gestionar los telegramas con bloqueo
         */
     int getNumUltimoTelegramaRecibido(int sessionId, String sessionKey);

        /**
         * Permite que el thread desde el que invoquemos este método se bloquee hasta que hayamos
         * recibido el eco del último telegrama que se haya enviado.
         * Si esto no sucede en "_msTimeOut" ms, entonces se lanza una excepción.
         *
         * @param _msTimeOut
         * @throws EmcosTimeoutException
         */
     void waitUltimoTelegrama(int sessionId, String sessionKey,long _msTimeOut);

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
     long getPingTime(int sessionId, String sessionKey);

        /**
         * Permite modificar el periodo de envío de los telegramas "ping", siempre que
         * el watchdog esté habilitado (ver el método "setWatchdogEnabled()").
         * Si el periodo es 0 (o negativo), equivale a deshabilitar el watchdog.
         * @param _ms Valor en milisegundos.
         */
     void setWatchdowgPeriod(int sessionId, String sessionKey,int _ms);

        /**
         * Permite habilitar/deshabilitar el watchdog. Este es un thread que envía periódicamente
         * un telegrama "ping" para generar tráfico entre arcadio y la pasarela, evitando que sus
         * sockets hagan timeout por falta de actividad.
         * El periodo de envío de esos telegramas "ping" puede modificarse con el método
         * "setWatchdgPeriod()", que por defecto es de 2 segundos.
         *
         * @param _enabled
         */
     void setWatchdogEnabled (int sessionId, String sessionKey,boolean _enabled);

        /**
         * Permite conocer el número de ms que deben transcurrir sin que el socket reciba
         * nada, para que salte el timeout.
         * @return the socketTimeout
         */
     int getSocketTimeout(int sessionId, String sessionKey);

        /**
         * Permite modificar el número de ms que deben transcurrir para que salte el timeout
         * del socket cuando no se haya recibido nada en ese tiempo.
         * @param socketTimeout the socketTimeout to set
         */
     void setSocketTimeout(int sessionId, String sessionKey,int _socketTimeout);

     boolean isConnected(int sessionId, String sessionKey);

        /**
         * @return the reconexionActivada
         */
     boolean isReconexionActivada(int sessionId, String sessionKey);
        /**
         * @param reconexionActivada the reconexionActivada to set
         */
     void setReconexionActivada(int sessionId, String sessionKey,boolean _reconexionActivada);

     void setTipoNombre(int sessionId, String sessionKey,String tipoNombre);

     void setConnection(int sessionId, String sessionKey,String input, String output);
}