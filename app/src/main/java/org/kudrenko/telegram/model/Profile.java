package org.kudrenko.telegram.model;

import org.drinkless.td.libcore.telegram.TdApi;

public class Profile {
    public String name;
    public String phone;
    public TdApi.File file;

    public Profile(String name, String phone, TdApi.File file) {
        this.name = name;
        this.phone = phone;
        this.file = file;
    }
}
