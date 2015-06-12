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

import android.animation.Animator;
import android.view.View;

/**
 * AnimatorListener implementation used to change the visibility of the view in construction once
 * the animation has finished.
 */
public class VisibilityAnimatorListener implements Animator.AnimatorListener {

  private final View view;
  private final int visibility;

  public VisibilityAnimatorListener(View view, int visibility) {
    this.view = view;
    this.visibility = visibility;
  }

  @Override public void onAnimationStart(Animator animation) {

  }

  @Override public void onAnimationEnd(Animator animation) {
    if (!animation.isRunning()) {
      view.setVisibility(visibility);
    }
  }

  @Override public void onAnimationCancel(Animator animation) {

  }

  @Override public void onAnimationRepeat(Animator animation) {

  }
}
