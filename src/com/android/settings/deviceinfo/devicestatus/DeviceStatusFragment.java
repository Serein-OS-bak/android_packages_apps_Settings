/*
 * Copyright (C) 2018 The Android Open Source Project
 * Copyright (C) 2019 SereinOS
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

package com.android.settings.deviceinfo.devicestatus;

import static com.android.settings.bluetooth.Utils.getLocalBtManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.UserInfo;
import android.os.Bundle;
import android.os.UserManager;
import android.provider.SearchIndexableResource;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.app.Fragment;

import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.accounts.EmergencyInfoPreferenceController;
import com.android.settings.applications.LayoutPreference;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.dashboard.SummaryLoader;
import com.android.settings.deviceinfo.BluetoothAddressPreferenceController;
import com.android.settings.deviceinfo.BrandedAccountPreferenceController;
import com.android.settings.deviceinfo.DeviceNamePreferenceController;
import com.android.settings.deviceinfo.FccEquipmentIdPreferenceController;
import com.android.settings.deviceinfo.FeedbackPreferenceController;
import com.android.settings.deviceinfo.IpAddressPreferenceController;
import com.android.settings.deviceinfo.ManualPreferenceController;
import com.android.settings.deviceinfo.PhoneNumberPreferenceController;
import com.android.settings.deviceinfo.RegulatoryInfoPreferenceController;
import com.android.settings.deviceinfo.SafetyInfoPreferenceController;
import com.android.settings.deviceinfo.WifiMacAddressPreferenceController;
import com.android.settings.deviceinfo.imei.ImeiInfoPreferenceController;
import com.android.settings.deviceinfo.simstatus.SimStatusPreferenceController;
import com.android.settings.deviceinfo.BuildNumberPreferenceController;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.widget.EntityHeaderController;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeviceStatusFragment extends DashboardFragment
        implements DeviceNamePreferenceController.DeviceNamePreferenceHost {

    Fragment mContext;

    private static final String LOG_TAG = "DeviceStatusFragment";

    private static final String KEY_MY_DEVICE_INFO_HEADER = "my_device_info_header";
    private static final String KEY_LEGAL_CONTAINER = "legal_container";

    public DeviceStatusFragment(Fragment context) {
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.about_background));

        return view;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.DEVICEINFO;
    }

    @Override
    public int getHelpResource() {
        return R.string.help_uri_about;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected String getLogTag() {
        return LOG_TAG;
    }

    @Override
    protected int getPreferenceScreenResId() {
        return R.xml.device_status;
    }

    @Override
    protected List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, getActivity(), this /* fragment */,
                getLifecycle());
    }

    private static List<AbstractPreferenceController> buildPreferenceControllers(
            Context context,
            Activity activity,
            DeviceStatusFragment fragment,
            Lifecycle lifecycle) {
        final List<AbstractPreferenceController> controllers = new ArrayList<>();
        controllers.add(new EmergencyInfoPreferenceController(context));
        controllers.add(new PhoneNumberPreferenceController(context));
        controllers.add(new BrandedAccountPreferenceController(context));
        DeviceNamePreferenceController deviceNamePreferenceController =
                new DeviceNamePreferenceController(context);
        deviceNamePreferenceController.setLocalBluetoothManager(getLocalBtManager(context));
        deviceNamePreferenceController.setHost(fragment);
        if (lifecycle != null) {
            lifecycle.addObserver(deviceNamePreferenceController);
        }
        controllers.add(deviceNamePreferenceController);
        controllers.add(
                new BuildNumberPreferenceController(context, activity, fragment, lifecycle));
        controllers.add(new SimStatusPreferenceController(context, fragment));
        controllers.add(new ImeiInfoPreferenceController(context, fragment));
        controllers.add(new IpAddressPreferenceController(context, lifecycle));
        controllers.add(new WifiMacAddressPreferenceController(context, lifecycle));
        controllers.add(new BluetoothAddressPreferenceController(context, lifecycle));
        controllers.add(new RegulatoryInfoPreferenceController(context));
        controllers.add(new SafetyInfoPreferenceController(context));
        controllers.add(new ManualPreferenceController(context));
        controllers.add(new FeedbackPreferenceController(fragment, context));
        controllers.add(new FccEquipmentIdPreferenceController(context));
	        return controllers;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final BuildNumberPreferenceController buildNumberPreferenceController =
            use(BuildNumberPreferenceController.class);
        if (buildNumberPreferenceController.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showDeviceNameWarningDialog(String deviceName) {
        DeviceNameWarningDialog.show(this, mContext);
    }

    public void onSetDeviceNameConfirm() {
        final DeviceNamePreferenceController controller = use(DeviceNamePreferenceController.class);
        controller.confirmDeviceName();
    }
}
