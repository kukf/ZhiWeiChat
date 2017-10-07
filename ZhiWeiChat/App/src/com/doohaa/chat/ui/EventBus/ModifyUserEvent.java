/*
* ModifyTagEvent.java $version 2016. 02. 17.
*
* Copyright 2016 LINE Corporation. All rights Reserved. 
* LINE Corporation PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*/
package com.doohaa.chat.ui.EventBus;

import com.doohaa.chat.api.dto.UserDto;

/**
 * @author sunshixiong
 */
public class ModifyUserEvent extends BaseEvent {

    private UserDto data;

    public ModifyUserEvent(EventType eventType, UserDto data) {
        super(eventType);
        this.data = data;
    }

    @Override
    public boolean match(Object obj) {
        return true;
    }

    public UserDto getData() {
        return data;
    }
}
