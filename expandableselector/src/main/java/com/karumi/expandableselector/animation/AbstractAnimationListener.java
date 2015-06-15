/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karumi.expandableselector.animation;

import android.view.animation.Animation;

/**
 * Abstract AnimationListener implementation created to avoid override every method when a
 * anonymous AnimationListener is declared. Create new instances of AbstractAnimationListener
 * instead of AnimationListener to reduce the number of methods to override.
 */
abstract class AbstractAnimationListener implements Animation.AnimationListener {

  @Override public void onAnimationStart(Animation animation) {

  }

  @Override public void onAnimationEnd(Animation animation) {

  }

  @Override public void onAnimationRepeat(Animation animation) {

  }
}
