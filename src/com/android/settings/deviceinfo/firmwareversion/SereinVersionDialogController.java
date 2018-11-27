/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.settings.deviceinfo.firmwareversion;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemProperties;
import android.os.Handler;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import com.android.settings.Utils;
import com.android.settings.R;

public class SereinVersionDialogController implements View.OnClickListener {
    @VisibleForTesting
    private static final int SEREIN_VERSION_VALUE_ID = R.id.serein_version;
    private static final String SEREIN_VERSION = "serein_version";
    private static final String SEREIN_PROP = "ro.serein.codename";
    private final Context mContext;

    String[] keks;
    private final int TAPS_FOR_EASTER = 11;
    private Toast mTapToast;
    private int mEasterCountdown = TAPS_FOR_EASTER;
    private final FirmwareVersionDialogFragment mDialog;

    public SereinVersionDialogController(FirmwareVersionDialogFragment dialog) {
        mDialog = dialog;
        mContext = dialog.getContext();
    }

     public void initialize() {
         // Create an ArrayAdapter that will contain all list items
         ArrayAdapter<String> adapter;
         keks = mContext.getResources().getStringArray(R.array.kek_tap);

         mDialog.setText(SEREIN_VERSION_VALUE_ID, SystemProperties.get(SEREIN_PROP,
                mContext.getResources().getString(R.string.device_info_default)));
         registerClickListeners();

    }

    @Override
    public void onClick(View v) {
         if (mEasterCountdown -1 > 0) {
             if (mTapToast != null) {
                 mTapToast.cancel();
                 mTapToast = null;
             }
             toasts();
             mEasterCountdown--;
             if (mEasterCountdown -1 == 0) {
                 mEasterCountdown++;
                 showeasteregg();
             }
        }
    }

    private void registerClickListeners() {
        mDialog.registerClickListener(SEREIN_VERSION_VALUE_ID, this /* listener */);
    }

     public void showeasteregg() {
         if (mTapToast != null) {
              mTapToast.cancel();
         }
         mTapToast.makeText(mContext, "Long live the goddess!",
               mTapToast.LENGTH_LONG).show();
         String url = "https://i.imgur.com/VM6KAao.jpg";
         Intent i = new Intent(Intent.ACTION_VIEW);
         i.setData(Uri.parse(url));
         mContext.startActivity(i);
         mEasterCountdown = TAPS_FOR_EASTER;
     }

     public void toasts() {
        mTapToast = Toast.makeText(mContext, keks[11 - mEasterCountdown],
               mTapToast.LENGTH_SHORT);
        mTapToast.show();
    }
}
