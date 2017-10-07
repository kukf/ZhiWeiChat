package com.doohaa.chat.api;

import android.content.Context;

import com.android.volley.Response;
import com.doohaa.chat.api.dto.UserDto;
import com.doohaa.chat.utils.Validator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LittleBear on 2016/5/17.
 */
public class ApiHelper extends BaseApi {

    /**
     * 发送短信验证码
     *
     * @param context
     * @param phoneNo
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     */
    public static <T> void seCode(Context context, String phoneNo, Class<T> clazz, Response.Listener<T> listener,
                                  Response.ErrorListener errorListener) {
        String url = "publics/member/sendCode.do";
        Map params = new HashMap();
        params.put("phone", phoneNo);
        url = buildUrl(url, params);
        getResponse(context, url, clazz, listener, errorListener);
    }

    /**
     * 注册
     *
     * @param context
     * @param code
     * @param phoneNo
     * @param password
     * @param inviter
     * @param name
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     */
    public static <T> void register(Context context, String code, String phoneNo, String password, String inviter, String name, Class<T> clazz, Response.Listener<T> listener,
                                    Response.ErrorListener errorListener) {
        String url = "publics/member/register.do";
        Map params = new HashMap();
        if (Validator.isNotEmpty(code)) {
            params.put("code", code);
        }

        if (Validator.isNotEmpty(phoneNo)) {
            params.put("phone", phoneNo);
        }

        if (Validator.isNotEmpty(password)) {
            params.put("password", password);
        }
        if (Validator.isNotEmpty(inviter)) {
            params.put("inviter", inviter);
        }

        if (Validator.isNotEmpty(name)) {
            params.put("name", name);
        }
        url = buildUrl(url, params);
        getResponse(context, url, clazz, listener, errorListener);
    }

    /**
     * 登录
     *
     * @param context
     * @param phone
     * @param password
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     */
    public static <T> void login(Context context, String phone, String password, Class<T> clazz, Response.Listener<T> listener,
                                 Response.ErrorListener errorListener) {
        String url = "publics/member/login.do";
        Map params = new HashMap();
        if (Validator.isNotEmpty(phone)) {
            params.put("phone", phone);
        }

        if (Validator.isNotEmpty(password)) {
            params.put("password", password);
        }
        url = buildUrl(url, params);
        getResponse(context, url, clazz, listener, errorListener);
    }


    /**
     * 修改密码
     *
     * @param context
     * @param oldPassword
     * @param newPassword
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     */
    public static <T> void changePassword(Context context, String oldPassword, String newPassword, Class<T> clazz, Response.Listener<T> listener,
                                          Response.ErrorListener errorListener) {
        String url = "publics/member/password/change.do";
        Map params = new HashMap();
        if (Validator.isNotEmpty(oldPassword)) {
            params.put("oldPassword", oldPassword);
        }

        if (Validator.isNotEmpty(newPassword)) {
            params.put("newPassword", newPassword);
        }
        url = buildUrl(url, params);
        getResponse(context, url, clazz, listener, errorListener);
    }

    /**
     * 修改密码
     *
     * @param context
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     */
    public static <T> void logout(Context context, Class<T> clazz, Response.Listener<T> listener,
                                  Response.ErrorListener errorListener) {
        String url = "public/member/logout.do";
        getResponse(context, url, clazz, listener, errorListener);
    }


    /**
     * 获取用户信息
     *
     * @param context
     * @param userId
     * @param inviter
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     */
    public static <T> void getUserProfile(Context context, String userId, String inviter, Class<T> clazz, Response.Listener<T> listener,
                                          Response.ErrorListener errorListener) {
        String url = "publics/member/profile/query.do";
        Map params = new HashMap();
        if (Validator.isNotEmpty(userId)) {
            params.put("id", userId);
        }

        if (Validator.isNotEmpty(inviter)) {
            params.put("inviter", inviter);
        }
        url = buildUrl(url, params);
        getResponse(context, url, clazz, listener, errorListener);
    }

    /**
     * 申请添加好友
     *
     * @param context
     * @param userId
     * @param message
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     */
    public static <T> void applyFriend(Context context, String userId, String message, Class<T> clazz, Response.Listener<T> listener,
                                       Response.ErrorListener errorListener) {
        String url = "publics/friend/apply.do";
        Map params = new HashMap();
        if (Validator.isNotEmpty(userId)) {
            params.put("friendId", userId);
        }

        if (Validator.isNotEmpty(message)) {
            params.put("message", message);
        }
        url = buildUrl(url, params);
        getResponse(context, url, clazz, listener, errorListener);
    }


    /**
     * 修改用户信息
     *
     * @param context
     * @param userDto
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     */
    public static <T> void modifyUserInfo(Context context, UserDto userDto, Class<T> clazz, Response.Listener<T> listener,
                                          Response.ErrorListener errorListener) {
        String url = "publics/member/profile/update.do";

        Map params = new HashMap();
        if (Validator.isNotEmpty(userDto)) {
            Gson gson = new GsonBuilder().create();
            params.put("memberProfileJson", gson.toJson(userDto));
        }
        url = buildUrl(url, params);
        getResponse(context, url, clazz, listener, errorListener);
    }


    /**
     * 接受/拒绝好友邀请
     *
     * @param context
     * @param memberFriendId
     * @param agree
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>            agree:  1接受，2拒绝
     */
    public static <T> void agreeFriend(Context context, String memberFriendId, int agree, Class<T> clazz, Response.Listener<T> listener,
                                       Response.ErrorListener errorListener) {
        String url = "publics/friend/apply/agree.do";
        Map params = new HashMap();
        if (Validator.isNotEmpty(memberFriendId)) {
            params.put("memberFriendId", memberFriendId);
        }
        params.put("flag", agree);
        url = buildUrl(url, params);
        getResponse(context, url, clazz, listener, errorListener);
    }

    /**
     * 获取产品列表
     *
     * @param context
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     */
    public static <T> void getProducts(Context context, Class<T> clazz, Response.Listener<T> listener,
                                       Response.ErrorListener errorListener) {
        String url = "publics/product/all/query.do";
        getResponse(context, url, clazz, listener, errorListener);
    }

    /**
     * 获取所有产品买卖权限
     *
     * @param context
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     */
    public static <T> void getProduceAble(Context context, Class<T> clazz, Response.Listener<T> listener,
                                          Response.ErrorListener errorListener) {
        String url = "publics/product/deal/able.do";
        getResponse(context, url, clazz, listener, errorListener);
    }


    /**
     * 获取好友列表
     *
     * @param context
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     */
    public static <T> void friendList(Context context, Class<T> clazz, Response.Listener<T> listener,
                                      Response.ErrorListener errorListener) {
        String url = "publics/friend/query.do";
        getResponse(context, url, clazz, listener, errorListener);
    }

    /**
     * 获取交易历史
     *
     * @param context
     * @param count
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     */
    public static <T> void dealHistory(Context context, int count, Class<T> clazz, Response.Listener<T> listener,
                                       Response.ErrorListener errorListener) {
        String url = "publics/product/deal/his.do";
        Map params = new HashMap();
        if (count > 0) {
            params.put("rowCount", count);
        }
        url = buildUrl(url, params);
        getResponse(context, url, clazz, listener, errorListener);
    }

    /**
     * 买入商品
     *
     * @param context
     * @param productId
     * @param count
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     */
    public static <T> void buyProduct(Context context, long productId, String count, Class<T> clazz, Response.Listener<T> listener,
                                      Response.ErrorListener errorListener) {
        String url = "publics/product/deal/buy.do";
        Map params = new HashMap();
        params.put("productId", productId);
        params.put("quantity", count);
        url = buildUrl(url, params);
        getResponse(context, url, clazz, listener, errorListener);
    }

    /**
     * 卖出商品
     *
     * @param context
     * @param productId
     * @param count
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     */
    public static <T> void sellProduct(Context context, long productId, String count, Class<T> clazz, Response.Listener<T> listener,
                                       Response.ErrorListener errorListener) {
        String url = "publics/product/deal/sell.do";
        Map params = new HashMap();
        params.put("productId", productId);
        params.put("quantity", count);
        url = buildUrl(url, params);
        getResponse(context, url, clazz, listener, errorListener);
    }

    /**
     * 获取公告
     *
     * @param context
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     */
    public static <T> void bulletinList(Context context, Class<T> clazz, Response.Listener<T> listener,
                                        Response.ErrorListener errorListener) {
        String url = "publics/message/query/list.do";
        getResponse(context, url, clazz, listener, errorListener);
    }

    /**
     * 获取公告
     *
     * @param context
     * @param id
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     */
    public static <T> void getBulletin(Context context, int id, Class<T> clazz, Response.Listener<T> listener,
                                       Response.ErrorListener errorListener) {
        String url = "publics/message/query/id.do";
        Map params = new HashMap();
        params.put("id", id);
        url = buildUrl(url, params);
        getResponse(context, url, clazz, listener, errorListener);
    }

    /**
     * 获取用户持有的产品
     *
     * @param context
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     */
    public static <T> void getMemberProducts(Context context, Class<T> clazz, Response.Listener<T> listener,
                                             Response.ErrorListener errorListener) {
        String url = "publics/member/product.do";
        getResponse(context, url, clazz, listener, errorListener);
    }

    /**
     * 删除好友
     *
     * @param context
     * @param friendId
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     */
    public static <T> void deleteFriend(Context context, String friendId, Class<T> clazz, Response.Listener<T> listener,
                                        Response.ErrorListener errorListener) {
        String url = "publics/friend/delete.do";
        Map params = new HashMap();
        params.put("friendId", friendId);
        url = buildUrl(url, params);
        getResponse(context, url, clazz, listener, errorListener);
    }

    /**
     * 批量查询好友
     *
     * @param context
     * @param memberids
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     */
    public static <T> void profileList(Context context, String memberids, Class<T> clazz, Response.Listener<T> listener,
                                       Response.ErrorListener errorListener) {
        String url = "publics/member/profile/query/list.do";
        Map params = new HashMap();
        params.put("memberIdList", memberids);
        url = buildUrl(url, params);
        getResponse(context, url, clazz, listener, errorListener);
    }

    /**
     * 获取图片上传的token
     *
     * @param context
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     */
    public static <T> void getUploadToken(Context context, Class<T> clazz, Response.Listener<T> listener,
                                          Response.ErrorListener errorListener) {
        String url = "/publics/system/upload/token/get.do";
        getResponse(context, url, clazz, listener, errorListener);
    }

    /**
     * 上传图片信息
     *
     * @param context
     * @param keyName
     * @param name
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     */
    public static <T> void upload(Context context, String keyName, String name, Class<T> clazz, Response.Listener<T> listener,
                                  Response.ErrorListener errorListener) {
        String url = "publics/system/upload/property.do";
        Map params = new HashMap();
        params.put("keyName", keyName);
        params.put("name", name);
        url = buildUrl(url, params);
        ;
        getResponse(context, url, clazz, listener, errorListener);
    }

    /**
     * 检测是否需要升级
     *
     * @param context
     * @param version
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     */
    public static <T> void checkVersion(Context context, String version, Class<T> clazz, Response.Listener<T> listener,
                                        Response.ErrorListener errorListener) {
        String url = "public/property/update/query.do";
        Map params = new HashMap();
        params.put("level", version);
        url = buildUrl(url, params);
        getResponse(context, url, clazz, listener, errorListener);
    }

    /**
     * 充值
     *
     * @param context
     * @param money
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     */
    public static <T> void recharge(Context context, double money, Class<T> clazz, Response.Listener<T> listener,
                                    Response.ErrorListener errorListener) {
        String url = "publics/pay/alipay.do";
        Map params = new HashMap();
        params.put("money", money);
        url = buildUrl(url, params);
        getResponse(context, url, clazz, listener, errorListener);
    }

    /**
     * 查询充值提现历史
     *
     * @param context
     * @param payFlag
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     */
    public static <T> void searchRechageWithdrawals(Context context, int payFlag, Class<T> clazz, Response.Listener<T> listener,
                                                    Response.ErrorListener errorListener) {
        String url = "publics/pay/history.do";
        if (payFlag > 0) {
            Map params = new HashMap();
            params.put("payFlag", payFlag);
            url = buildUrl(url, params);
        }
        getResponse(context, url, clazz, listener, errorListener);
    }

    /**
     * 申请提现
     *
     * @param context
     * @param money
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     */
    public static <T> void withdrawals(Context context, double money, Class<T> clazz, Response.Listener<T> listener,
                                       Response.ErrorListener errorListener) {
        String url = "publics/pay/withdraw.do";
        Map params = new HashMap();
        params.put("money", money);
        url = buildUrl(url, params);
        getResponse(context, url, clazz, listener, errorListener);
    }

    /**
     * 模糊搜索社区接口
     *
     * @param context
     * @param name
     * @param startNum
     * @param clazz
     * @param listener
     * @param errorListener
     * @param <T>
     */
    public static <T> void query(Context context, String name, int numPerPage, int startNum, Class<T> clazz, Response.Listener<T> listener,
                                 Response.ErrorListener errorListener) {
        String url = "publics/group/query.do";
        Map params = new HashMap();
        params.put("name", name);
        params.put("startNum", startNum);
        params.put("numPerPage", numPerPage);
        url = buildUrl(url, params);
        getResponse(context, url, clazz, listener, errorListener);
    }
}
