package org.kudrenko.telegram.otto.events;

import org.drinkless.td.libcore.telegram.TdApi;

public class UpdateFileEvent extends AbsEvent {
    public TdApi.FileLocal file;

    public UpdateFileEvent(TdApi.FileLocal file) {
        this.file = file;
    }
}
