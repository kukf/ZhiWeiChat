package com.doohaa.chat.api;

import java.lang.reflect.Method;

import com.doohaa.chat.api.dto.ApiResponse;

public class ApiResultValidator {
    private static final String SUCCESS_STATUS_CODE = "0";
    public static final String UNKNOWN_ERROR_CODE = "-1";

    public static boolean isSuccess(Object response) {
        if (isInstanceOfApiResponse(response)) {
            ApiResponse apiResponse = (ApiResponse) response;
            if (apiResponse.isSuccess()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFail(Object response) {
        if (ApiResultValidator.isSuccess(response)) {
            return false;
        }
        return true;
    }

    public static String getStatusMessage(Object response) {
        if (isInstanceOfApiResponse(response)) {
            ApiResponse apiResponse = (ApiResponse) response;
            return apiResponse.getMessage();
        }
        return "";
    }

    public static String getStatusCode(Object response) {
        if (isInstanceOfApiResponse(response)) {
            ApiResponse apiResponse = (ApiResponse) response;
            return apiResponse.getCode();
        }
        return UNKNOWN_ERROR_CODE;
    }

    private static boolean isInstanceOfApiResponse(Object response) {
        return response != null && ApiResponse.class.isInstance(response);
    }

    public static boolean isInValid(Object response, Class<? extends ApiResponse> resultClazz) {

        // clazz ApiResponse 이면 returnValue 값이 없기때문에 null만 체크
        if (resultClazz == null || resultClazz == ApiResponse.class) {
            if (response == null) {
                return true;
            } else {
                return false;
            }
        }

        if (resultClazz.isInstance(response) == false) {
            return true;
        }
        Class<?> result = response.getClass();
        try {
            Method method = result.getDeclaredMethod("getResult");

            Object returnValue = method.invoke(response);
            if (returnValue == null) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public static boolean isValid(Object response, Class<? extends ApiResponse> clazz) {
        return isInValid(response, clazz) == false;
    }

}
