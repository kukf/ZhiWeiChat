package com.doohaa.chat.utils;
public class GlobalVariables {
	public final static int CANCELED = -1;// 拍照
	public final static int PICK_PHOTO = 0;// 选择图片
	public final static int TAKE_PICTURE = 1;// 拍照
	public final static int CROP_PHOTO = 2;// 选择图片
	// 消息类型
	public final static int MSG_TEXT = 1;// 文本
	public final static int MSG_IMAGE = 2;// 图片
	public final static int MSG_AUDIO = 3;// 语音
	public final static int AVATAR_USER = 4;// avatar
	public final static int APK = 5;// apk升级目录
	public final static int TEMP = 6;// 临时文件目录
	public final static int HTML = 7;// 压缩文件夹
	public final static int REQUEST = 10;
	public final static String ROOT_PATH = android.os.Environment.getExternalStorageDirectory() + "/com.doohaa.chat/";// 根目录
	public final static String IMAGE_FLODER = "/image/";// 图片文件夹
	public final static String AUDIO_FOLDER = "/audio/";// 语音文件夹
	public final static String AVATAR_FOLDER = ROOT_PATH + "avatar/";// 联系人头像文件夹
	public final static String HTML_FOLDER = ROOT_PATH + "html/";// 网页文件夹
	public final static String LOG_FOLDER = ROOT_PATH + "log/";// 日志文件夹
	public final static String APK_PATH = ROOT_PATH + "apk/";// 系统安装包路径
	public final static String TEMP_PATH = ROOT_PATH + "temp/";// 临时目录
	// 图片限定尺寸
	public final static int MAX_IMAGE_SIZE = 600;// 图片最大边不能超过600
	public final static int IMAGE_SHOW_SIZE = 90;// 图片展示的尺寸
	public final static String USERNAME = "username";// 用户名
	public final static String NAME = "name";// 用户名
	public static String ACCOUNT = "";
}
