package org.kudrenko.telegram.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Country implements Parcelable {
    public String name;
    public int code;

    public Country(String name, int code) {
        this.name = name;
        this.code = code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.code);
    }

    private Country(Parcel in) {
        this.name = in.readString();
        this.code = in.readInt();
    }

    public static final Parcelable.Creator<Country> CREATOR = new Parcelable.Creator<Country>() {
        public Country createFromParcel(Parcel source) {
            return new Country(source);
        }

        public Country[] newArray(int size) {
            return new Country[size];
        }
    };
}
