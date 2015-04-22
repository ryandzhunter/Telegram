package org.kudrenko.telegram.otto.events;

import org.drinkless.td.libcore.telegram.TdApi;

public class AuthStateUpdateEvent extends AbsEvent {
    public TdApi.AuthState state;

    public AuthStateUpdateEvent(TdApi.AuthState state) {
        this.state = state;
    }
}
