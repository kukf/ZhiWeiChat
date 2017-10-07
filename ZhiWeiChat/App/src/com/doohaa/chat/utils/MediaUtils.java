package com.doohaa.chat.utils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Video.Thumbnails;
import android.util.Log;
import android.webkit.MimeTypeMap;

import jp.naver.android.commons.io.IOUtils;

public class MediaUtils {

    // media
    public static final String MEDIA_FOLDER = "tale";

    /**
     * Get a video id from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     */
    @SuppressLint("NewApi")
    public static long getVideoId(Context context, Uri uri) {

        // DocumentProvider
        if (UIUtils.hasKitkat() && DocumentsContract.isDocumentUri(context, uri)) {

            // content://com.android.providers.media.documents/document/video%3A37
            // ExternalStorageProvider or MediaProvider
            if (isExternalStorageDocument(uri) || isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Long.valueOf(split[1]);
            }

            // DownloadsProvider
            if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                return Long.valueOf(id);
            }

            return 0;
        }

        // content://media/external/video/media/37
        // MediaStore (and general)
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            try {
                return Long.valueOf(uri.toString().substring(uri.toString().lastIndexOf("/") + 1));
            } catch (NumberFormatException e) {
                Log.e(MediaUtils.class.getSimpleName(), e.getMessage());
                return getVideoIdFromContentUri(context, uri);
            }
        }

        // File TODO
        /*else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
		}*/

        return 0;
    }

    private static long getVideoIdFromContentUri(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        String[] projection = new String[]{MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA};
        Cursor cursor = contentResolver.query(uri, projection, null, null, null);

        if (cursor == null) {
        }
        cursor.moveToNext();
        return cursor.getLong(cursor.getColumnIndex(projection[0]));

    }

    public static Bitmap getVideoThumbnail(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }

        if ("file".equalsIgnoreCase(uri.getScheme())) {
            return MediaUtils.createVideoThumbnail(uri.getPath());
        }

        long videoId = MediaUtils.getVideoId(context, uri);

        if (videoId > 0) {
            String path = MediaUtils.getVideoThumbUrl(context, videoId);
            return BitmapFactory.decodeFile(path);

        } else {
            ParcelFileDescriptor parcelFileDescriptor = null;
            try {
                parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                IOUtils.close(parcelFileDescriptor);
            }
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            return BitmapFactory.decodeFileDescriptor(fileDescriptor);
        }
    }

    /**
     * Get a video thumbnail url from a uri
     */
    public static String getVideoThumbUrl(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        if ("file".equalsIgnoreCase(uri.getScheme())) {
            MediaUtils.createVideoThumbnail(uri.toString());
            return uri.toString();
        }

        long videoId = MediaUtils.getVideoId(context, uri);

        if (videoId > 0) {
            return MediaUtils.getVideoThumbUrl(context, videoId);
        }
        //TODO videoId 가 없으면 ...
        return null;
    }

    /**
     * Get a video thumbnail url from a video id.
     *
     * @param context The context.
     * @param videoId The video id.
     */
    public static String getVideoThumbUrl(Context context, long videoId) {

        if (context == null || videoId == 0) {
            Log.e(MediaUtils.class.getSimpleName(), "getVideoThumbUrl context == null || videoId == 0");
            return null;
        }

        Thumbnails.getThumbnail(context.getContentResolver(), videoId,
                Thumbnails.MINI_KIND, null);

        String[] thumb_projection = new String[]{Thumbnails._ID, Thumbnails.DATA};
        Uri thumbUri = Thumbnails.EXTERNAL_CONTENT_URI;
        Cursor videoThumbCursor = context.getContentResolver().query(thumbUri, thumb_projection,
                Thumbnails.VIDEO_ID + "=?", new String[]{String.valueOf(videoId)}, null);

        try {

            if (videoThumbCursor.moveToNext()) {
                return videoThumbCursor.getString(videoThumbCursor.getColumnIndex(Thumbnails.DATA));
            }

        } finally {
            if (videoThumbCursor != null)
                videoThumbCursor.close();
        }

        return null;
    }

    /**
     * Get a file path from a UriString. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     * <p/>
     * content://media.external/images/media/number
     * ->
     * file:///mnt/sdcard/.../imageName.format
     *
     * @param context   The context.
     * @param uriString The UriString to query.
     */
    public static String getPath(final Context context, final String uriString) {
        if (context == null || StringUtils.isEmpty(uriString)) {
            return null;
        }

        return getPath(context, Uri.parse(uriString));
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     * <p/>
     * content://media.external/images/media/number
     * ->
     * file:///mnt/sdcard/.../imageName.format
     *
     * @param context The context.
     * @param uri     The Uri to query.
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        // DocumentProvider
        if (UIUtils.hasKitkat() && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            String path = null;
            if (isGooglePhotosUri(uri)) {
                path = uri.getLastPathSegment();

            } else if (isNewGooglePhotosUri(uri)) {
                String pathUri = uri.getPath();

                int start = pathUri.indexOf("content");
                int end = pathUri.lastIndexOf("/ACTUAL");
                if (start > 0 && (end < pathUri.length() && start < end)) {
                    String newUri = pathUri.substring(start, end);
                    path = getDataColumn(context, Uri.parse(newUri), null, null);
                }
                if (path == null) {
                    path = getUrlWithAuthority(context, uri);
                }

            } else {
                path = getDataColumn(context, uri, null, null);
            }

            return path;
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        // return not null
        return StringUtils.isNotEmpty(uri.getPath()) ? uri.getPath() : null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = MediaColumns.DATA;
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static String getUrlWithAuthority(Context context, Uri uri) {
        if (uri.getAuthority() != null) {
            String mimeType = context.getContentResolver().getType(uri);
            if (StringUtils.isEmpty(mimeType)) {
                return null;
            }

            String extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
            if (mimeType.contains("image")) {
                return getImageUrlWithAuthority(context, uri, extension);

            } else if (mimeType.contains("video")) {
                String path = getVideoUriWithAuthority(context, uri, extension);
                if (isInvalidateVideo(context, path)) {
                    return path;
                }
            }
        }
        return null;
    }

    private static boolean isInvalidateVideo(Context context, String path) {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(context, Uri.fromFile(new File(path)));

            String hasVideo = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO);
            return "yes".equals(hasVideo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private static String getVideoUriWithAuthority(Context context, Uri uri, String extension) {
        InputStream is = null;
        FileOutputStream fileOutputStream = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);

                File file = File.createTempFile("temp", "video." + extension);

                fileOutputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
                return file.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.close(is);
                IOUtils.close(fileOutputStream);
            }
        }
        return null;
    }


    private static String getImageUrlWithAuthority(Context context, Uri uri, String extension) {
        InputStream is = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                if (bmp == null) {
                    return null;
                }
                return writeToTempImageAndGetPathUri(bmp, extension);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static String writeToTempImageAndGetPathUri(Bitmap inImage, String extension) {
        FileOutputStream fileOutputStream = null;
        try {

            File file = File.createTempFile("temp", "image." + extension);

            fileOutputStream = new FileOutputStream(file);

            inImage.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

            return file.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(fileOutputStream);
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isNewGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.contentprovider".equals(uri.getAuthority())
                || "com.google.android.apps.docs.storage.legacy".equals(uri.getAuthority());
    }

    public static boolean isGoogleDocs(Uri uri) {
        return "com.google.android.apps.photos.contentprovider".equals(uri.getAuthority());
    }

    public static int getOrientation(Uri uri) {
        return getOrientation(uri.getPath());
    }

    public static int getOrientation(String filePath) {

        try {

            final ExifInterface exifInterface = new ExifInterface(filePath);
            final int orientationExif = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            switch (orientationExif) {
                case ExifInterface.ORIENTATION_UNDEFINED:
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                    break;
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    break;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static Uri insertMediaImage(Context context, File file) {
        return insertMediaImage(context, file, MEDIA_FOLDER);
    }

    public static Uri insertMediaImage(Context context, File file, String path) {

        if (file == null) {
            return null;
        }
        ContentValues values = new ContentValues();
        values.put(Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(Media.MIME_TYPE, "image/jpeg");
        values.put(Media.SIZE, file.length());
        values.put(MediaColumns.DATA, file.getPath());
        return context.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
    }

    public static long getMediaFileSize(String fullPath) {
        return new File(fullPath).length();
    }

    public static long getMediaFileSize(String fullPath, Context context) {
        return new File(fullPath).length();
    }

    public static int getMediaFileDuration(Uri uri, Context context) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, uri);
        if (mediaPlayer == null) {
            return 0;
        }
        int duration = mediaPlayer.getDuration();
        mediaPlayer.release();
        return duration;
    }

    public static Bitmap createVideoThumbnail(String filePath) {
        return ThumbnailUtils.createVideoThumbnail(filePath, Thumbnails.MINI_KIND);
    }

    public static String getMimeType(Context context, String url) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }

        String path = getPath(context, url);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(getExtension(path));
    }

    public static String getExtension(String path) {
        if (StringUtils.isEmpty(path)) {
            return null;
        }

        int dotPos = path.lastIndexOf('.');
        if (0 <= dotPos) {
            return path.substring(dotPos + 1);
        }

        return null;
    }
}
