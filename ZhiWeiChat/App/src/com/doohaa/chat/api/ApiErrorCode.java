package com.doohaa.chat.api;

public interface ApiErrorCode {
    String EXPIRED_TOKEN = "703";
    String FORCE_UPDATE = "970";

    /**
     * obs error code
     */
    interface ObsResponseCode {
        String SUCCESS = "200";
        String TRANSCODING_PROGRESS = "202";
        String TRANSCODING_FAIL = "204";
        String BAD_REQUEST = "400";
        String UNAUTHORIZED = "401";
        String FORBIDDEN = "403";
        String NOT_FOUND = "404";
        String UNSUPPORTED_MEDIA_TYPE = "415";
        String INTERNAL_SERVER_ERROR = "500";
        String SERVICE_UNAVAILABLE = "503";
    }
}
