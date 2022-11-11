package com.ksa.telegram.orangecomplexbot.component;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface IBotMessenger {
     abstract boolean isAllow();
     abstract boolean isExclusive();
     abstract SendMessage execute();
    public abstract boolean isAdmin();

}
