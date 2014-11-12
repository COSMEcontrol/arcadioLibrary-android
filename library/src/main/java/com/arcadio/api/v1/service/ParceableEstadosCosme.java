package com.arcadio.api.v1.service;

import android.os.Parcel;
import android.os.Parcelable;

import com.arcadio.EstadosCosme;

	
	public class ParceableEstadosCosme implements Parcelable{
		
		private EstadosCosme estado = EstadosCosme.DESCONECTADO;
		
		
		public EstadosCosme getEstado(){
			return estado;
		}
		public ParceableEstadosCosme(Parcel in){
			readFromParcel(in);
		}
		public ParceableEstadosCosme(EstadosCosme _estado){
			this.estado=_estado;
		}
	    
	    public int describeContents() {
			return 0;
		}
	    public void readFromParcel(Parcel in) {
	        String entrada = in.readString();
	        estado = EstadosCosme.valueOf(entrada);
	    }
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(estado.toString());	
		}
		public static final Parcelable.Creator<ParceableEstadosCosme> CREATOR
	    = new Parcelable.Creator<ParceableEstadosCosme>() {
	        public ParceableEstadosCosme createFromParcel(Parcel in) {
	            return new ParceableEstadosCosme(in);
	        }
	 
	        public ParceableEstadosCosme[] newArray(int size) {
	            return new ParceableEstadosCosme[size];
	        }
	    };
	}
	

