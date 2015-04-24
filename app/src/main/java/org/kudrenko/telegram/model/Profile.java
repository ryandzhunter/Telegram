package org.kudrenko.telegram.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.drinkless.td.libcore.telegram.TdApi;

public class Profile implements Parcelable {
    public String name;
    public String phone;
    public String icon;

    public Profile(String name, String phone, TdApi.File file) {
        this.name = name;
        this.phone = phone;
        if (file.getConstructor() == TdApi.FileLocal.CONSTRUCTOR)
            icon = ((TdApi.FileLocal) file).path;
    }

    public Profile() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeString(this.icon);
    }

    private Profile(Parcel in) {
        this.name = in.readString();
        this.phone = in.readString();
        this.icon = in.readString();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        public Profile createFromParcel(Parcel source) {
            return new Profile(source);
        }

        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };
}
