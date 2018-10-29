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
import android.os.SystemProperties;
import android.support.annotation.VisibleForTesting;
 import com.android.settings.R;
 public class SereinVersionDialogController {
     @VisibleForTesting
    private static final int SEREIN_VERSION_VALUE_ID = R.id.serein_version;
    private static final String SEREIN_VERSION = "serein_version";
    private static final String SEREIN_PROP = "ro.serein.version";
     private final FirmwareVersionDialogFragment mDialog;
    private final Context mContext;
     public SereinVersionDialogController(FirmwareVersionDialogFragment dialog) {
        mDialog = dialog;
        mContext = dialog.getContext();
    }
     public void initialize() {
         mDialog.setText(SEREIN_VERSION_VALUE_ID, SystemProperties.get(SEREIN_PROP,
                mContext.getResources().getString(R.string.device_info_default)));
    }
}
