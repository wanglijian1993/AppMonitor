/*
 * Copyright (C) 2019 THL A29 Limited, a Tencent company. All rights reserved.
 * DO NOT ALTER OR REMOVE NOTICES OR THIS FILE HEADER.
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

package com.wlj.catchnativecrash.app_crash;

import android.app.Activity;

public interface IForeBackInterface {
    void onForeground(Activity activity);
    void onBackground(Activity activity);
    void onCreate(Activity activity);
    void onResume(Activity activity);
    void onStop(Activity activity);
    void onDestroy(Activity activity);
}