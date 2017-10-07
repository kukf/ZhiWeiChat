package com.doohaa.chat.ui.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;

public class BaseFragment extends Fragment {

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void showProgress() {
		showProgress(true, false, null);
	}

	public void showProgress(String tag) {
		showProgress(true, false, tag);
	}

	public void showProgress(boolean cancelable, boolean touchable, String tag) {
		Activity activity = getActivity();
		if (activity instanceof BaseActivity) {
			((BaseActivity) activity).showProgressDialog(cancelable, touchable, tag);
		}
	}

	public void hideProgress() {
		Activity activity = getActivity();
		if (activity instanceof BaseActivity) {
			((BaseActivity) activity).hideProgressDialog();
		}
	}

	public void hideProgress(boolean immediately) {
		Activity activity = getActivity();
		if (activity instanceof BaseActivity) {
			((BaseActivity) activity).hideProgressDialog(immediately);
		}
	}

	public void showErrorToast(VolleyError error) {
		Activity activity = getActivity();
		if (activity instanceof BaseActivity) {
			((BaseActivity) activity).showErrorToast(error);
		}
	}

	public boolean isFinishingDestroyed() {
		return getActivity() == null || this.isDetached() || ((BaseActivity) getActivity()).isFinishingDestroyed();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		hideProgress();
	}
}
