package com.doohaa.chat.ui.EventBus;

import java.util.EnumSet;

public abstract class BaseEvent {
    private final EventType eventType;

    public BaseEvent(EventType eventType) {
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }


    public abstract boolean match(Object obj);

    public boolean matchEventType(EnumSet<EventType> eventTypes) {
        return eventTypes.contains(eventType);
    }

}
