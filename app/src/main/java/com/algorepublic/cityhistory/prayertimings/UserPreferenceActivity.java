/*
 * Copyright (C) 2011 Iranian Supreme Council of ICT, The FarsiTel Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASICS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.algorepublic.cityhistory.prayertimings;

/*
 * This activity is responsible to construct the preference screen from xml resource
 * Preference screen is combined of:
 * 1. GPS On/Off (Boolean)
 * 2. Setting default location (Integer)
 * 
 * 
 * Required files:
 * res/xml/settings.xml
 */

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Window;

public class UserPreferenceActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        boolean b = requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

    }
}
