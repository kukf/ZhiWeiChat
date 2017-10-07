package com.doohaa.chat.ui.AddContact;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.doohaa.chat.api.dto.ContactInfo;

import java.util.ArrayList;

/**
 * Created by sunshixiong on 5/23/16.
 */
public class ContactHelper {
    private static final String TAG = "ContactHelper";
    private Context context;
    private QueryFinishListener queryFinishListener;

    public interface QueryFinishListener {
        void onQueryFinished(ArrayList<ContactInfo> contactList);
    }

    public ContactHelper(Context context) {
        this.context = context;
    }

    public void queryContactList() {
        new QueryTask(queryFinishListener).execute();
    }

    private ArrayList<ContactInfo> getPhoneNumberFromMobile() {
        ArrayList<ContactInfo> contactList = new ArrayList<>();
        try {
            Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);
            //moveToNext方法返回的是一个boolean类型的数据
            while (cursor.moveToNext()) {
                //读取通讯录的姓名
                String name = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                //读取通讯录的号码
                String number = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                ContactInfo contactInfo = new ContactInfo(name, number);
                contactList.add(contactInfo);
            }
        } catch (Exception e) {
            Log.e(TAG, "get contactList fail! ==> " + e.getMessage());
        }
        return contactList;
    }

    private class QueryTask extends AsyncTask<Void, Void, ArrayList<ContactInfo>> {
        private QueryFinishListener listener;

        QueryTask(QueryFinishListener listener) {
            this.listener = listener;
        }


        @Override
        protected ArrayList<ContactInfo> doInBackground(Void... params) {
            return getPhoneNumberFromMobile();
        }

        @Override
        protected void onPostExecute(ArrayList<ContactInfo> contactList) {
            if (listener != null) {
                listener.onQueryFinished(contactList);
            }
        }
    }

    public void setQueryFinishListener(QueryFinishListener queryFinishListener) {
        this.queryFinishListener = queryFinishListener;
    }
}
