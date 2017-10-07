package com.doohaa.chat.ui.common;

import com.doohaa.chat.R;

public enum OCCommonPopupConstants {
    COMMON_ONE_BUTTON(-1, R.string.dl_ok, -1),
    COMMON_TWO_BUTTON(-1, R.string.dl_ok, R.string.dl_cancel),
    NEED_REAL_INFO(R.string.need_real_info, R.string.ok, R.string.cancel),
    //	COMMENT_DELETE(R.string.article_comment_delete_message, R.string.ok, R.string.cancel),
    //	LOGIN(R.string.login_popup_message, R.string.ok, R.string.cancel),
    //	FAIL_FB_SIGN_UP_NAME(R.string.popup_fail_fb_signup_name, R.string.ok, R.string.cancel),
    //	FAIL_LINE_SIGN_UP_NAME(R.string.popup_fail_line_signup_name, R.string.ok, R.string.cancel),
    //	SIGN_UP_EMPTY_NAME(R.string.popup_empty_name, R.string.ok, -1),
    //	WRITE_CANCEL_CONFIRM(R.string.popup_write_discard_confirm_message, R.string.popup_yes, R.string.popup_no),
    //	WRITE_EDIT_CANCEL_CONFIRM(R.string.popup_write_edit_discard_confirm_message, R.string.popup_yes, R.string.popup_no),
    //	WRITE_ONLY_HASH_TAG(-1, R.string.popup_ok, R.string.popup_no_thanks),
    //	ARTICLE_UPLOAD_FAILED(R.string.popup_article_upload_failed, R.string.popup_try_again, R.string.popup_discard),
    //	ARTICLE_UPLOAD_COMPLETED(R.string.popup_article_upload_completed, R.string.ok, R.string.cancel),
    //	FAIL_PROFILE_UPLOAD(R.string.popup_fail_profile_upload, R.string.ok, R.string.cancel),
    //	LOGOUT(R.string.popup_logout_confirm, R.string.popup_yes, R.string.popup_no),
    //	HSAHTAG_FAVORITE(R.string.tag_favorite_success, R.string.ok, -1),
    //	MODIFY_TAG_CANCEL_CONFIRM(R.string.popup_modify_tag_discard_confirm_message, R.string.popup_yes, R.string.popup_no),
    //	MODIFY_TAG_CONFIRM(R.string.popup_modify_tag_confirm_message, R.string.popup_yes, R.string.popup_no),
    //	COVER_IMAGE_TOO_SMALL(R.string.cover_image_too_small, R.string.ok, -1),
    //	TO_DEFAULT_COVER_IMAGE(R.string.popup_to_default_cover_image, R.string.ok, R.string.cancel),
    //	HASHTAG_UNDER_REVIEW(R.string.popup_hashtag_under_review, R.string.ok, -1),
    //	CBT_EXPIRED(R.string.cbt_expired, R.string.ok, R.string.cancel);
    ;
    private final int message;
    private final int positiveBtn;
    private final int negativeBtn;

    OCCommonPopupConstants(int message, int positiveBtn, int negativeBtn) {
        this.message = message;
        this.positiveBtn = positiveBtn;
        this.negativeBtn = negativeBtn;
    }

    public int getMessage() {
        return message;
    }

    public int getPositiveBtn() {
        return positiveBtn;
    }

    public int getNegativeBtn() {
        return negativeBtn;
    }

}
