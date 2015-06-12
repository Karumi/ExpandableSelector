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

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Animation extension created to resize a widget in two dimensions given the from and to width and
 * height.
 */
public class ResizeAnimation extends Animation {

  private View view;
  private float toHeight;
  private float fromHeight;
  private float toWidth;
  private float fromWidth;

  public ResizeAnimation(View view, float fromWidth, float fromHeight, float toWidth,
      float toHeight) {
    this.toHeight = toHeight;
    this.toWidth = toWidth;
    this.fromHeight = fromHeight;
    this.fromWidth = fromWidth;
    this.view = view;
  }

  @Override protected void applyTransformation(float interpolatedTime, Transformation t) {
    float height = (toHeight - fromHeight) * interpolatedTime + fromHeight;
    float width = (toWidth - fromWidth) * interpolatedTime + fromWidth;
    ViewGroup.LayoutParams p = view.getLayoutParams();
    p.height = (int) height;
    p.width = (int) width;
    view.requestLayout();
  }

  @Override public boolean willChangeBounds() {
    return true;
  }
}
