package com.arcadio.api.v1.service;

import android.os.Parcel;
import android.os.Parcelable;

import com.arcadio.CosmeError;

public class ParceableCosmeError extends CosmeError implements Parcelable{
	
	public ParceableCosmeError(CosmeError _error){
		super(_error.getVariable(), _error.getMsgError());
	}
	public ParceableCosmeError(Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.getVariable());
		dest.writeString(this.getMsgError());
	}
	public void readFromParcel(Parcel in) {
		setVariable(in.readString());
		setMsgError(in.readString());
	}
	public static final Parcelable.Creator<ParceableCosmeError> CREATOR
    = new Parcelable.Creator<ParceableCosmeError>() {
        public ParceableCosmeError createFromParcel(Parcel in) {
            return new ParceableCosmeError(in);
        }
 
        public ParceableCosmeError[] newArray(int size) {
            return new ParceableCosmeError[size];
        }
    };

}
