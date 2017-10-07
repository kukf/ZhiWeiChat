package com.doohaa.chat.api.dto;

import java.util.List;

/**
 * Created by axiong on 2016/9/12.
 */
public class MemberMoneyAboutDto extends ApiResponse {
    private List<MemberMoneyAboutResultDto> data;

    public List<MemberMoneyAboutResultDto> getData() {
        return data;
    }

    public void setData(List<MemberMoneyAboutResultDto> data) {
        this.data = data;
    }
}
