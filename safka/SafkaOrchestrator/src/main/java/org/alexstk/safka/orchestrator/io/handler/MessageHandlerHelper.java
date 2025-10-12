package org.alexstk.safka.orchestrator.io.handler;

import com.storozhuk.decoder.UserInfo;

public class MessageHandlerHelper {

    public static boolean isAdmin(UserInfo userInfo) {
        return userInfo != null && userInfo.permissions() != null && userInfo.permissions()
            .contains("ADMIN");
    }
}
