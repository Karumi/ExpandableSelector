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

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import java.util.List;

/**
 * Performs all the animations and size or position changes related to the
 * ExpandableSelectorComponent.
 */
public class ExpandableSelectorAnimator {

  private static final String Y_ANIMATION = "translationY";
  private static final float CONTAINER_ANIMATION_OFFSET = 1.16f;

  private final View container;
  private final int animationDuration;

  private List<View> buttons;
  private boolean isCollapsed = true;
  private boolean hideFirstItemOnCollapse;

  public ExpandableSelectorAnimator(View container, int animationDuration) {
    this.container = container;
    this.animationDuration = animationDuration;
  }

  public boolean isCollapsed() {
    return isCollapsed;
  }

  public void setButtons(List<View> buttons) {
    this.buttons = buttons;
  }

  public void expand(Listener listener) {
    setCollapsed(false);
    changeButtonsVisibility(View.VISIBLE);
    expandButtons();
    expandContainer(listener);
  }

  public void collapse(Listener listener) {
    setCollapsed(true);
    collapseButtons();
    collapseContainer(listener);
  }

  private void setCollapsed(boolean isCollapsed) {
    this.isCollapsed = isCollapsed;
  }

  private void expandButtons() {
    int numberOfButtons = buttons.size();
    for (int i = 0; i < numberOfButtons; i++) {
      View button = buttons.get(i);
      TimeInterpolator interpolator = getExpandAnimatorInterpolation();
      float toY = calculateExpandedYPosition(i);
      ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(button, Y_ANIMATION, toY);
      objectAnimator.setInterpolator(interpolator);
      objectAnimator.setDuration(animationDuration);
      objectAnimator.start();
    }
  }

  private void collapseButtons() {
    int numberOfButtons = buttons.size();
    TimeInterpolator interpolator = getCollapseAnimatorInterpolation();
    for (int i = 0; i < numberOfButtons; i++) {
      View button = buttons.get(i);
      ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(button, Y_ANIMATION, 0);
      objectAnimator.setInterpolator(interpolator);
      objectAnimator.setDuration(animationDuration);
      objectAnimator.start();
    }
  }

  private float calculateExpandedYPosition(int buttonPosition) {
    int numberOfButtons = buttons.size();
    float y = 0;
    for (int i = numberOfButtons - 1; i > buttonPosition; i--) {
      View button = buttons.get(i);
      y = y + button.getHeight() + getMarginRight(button) + getMarginLeft(button);
    }
    return -y;
  }

  private void expandContainer(final Listener listener) {
    float toWidth = container.getWidth();
    float toHeight = getSumHeight();
    ResizeAnimation resizeAnimation = new ResizeAnimation(container, toWidth, toHeight);
    Interpolator interpolator = getContainerAnimationInterpolator();
    resizeAnimation.setInterpolator(interpolator);
    resizeAnimation.setDuration((long) (animationDuration * CONTAINER_ANIMATION_OFFSET));
    resizeAnimation.setAnimationListener(new AbstractAnimationListener() {
      @Override public void onAnimationEnd(Animation animation) {
        listener.onAnimationFinished();
      }
    });
    container.startAnimation(resizeAnimation);
  }

  private void collapseContainer(final Listener listener) {
    float toWidth = container.getWidth();
    float toHeight = getFirstItemHeight();
    ResizeAnimation resizeAnimation = new ResizeAnimation(container, toWidth, toHeight);
    Interpolator interpolator = getContainerAnimationInterpolator();
    resizeAnimation.setInterpolator(interpolator);
    resizeAnimation.setDuration((long) (animationDuration * CONTAINER_ANIMATION_OFFSET));
    resizeAnimation.setAnimationListener(new AbstractAnimationListener() {
      @Override public void onAnimationEnd(Animation animation) {
        listener.onAnimationFinished();
        changeButtonsVisibility(View.INVISIBLE);
      }
    });
    container.startAnimation(resizeAnimation);
  }

  private void changeButtonsVisibility(int visibility) {
    int lastItem = hideFirstItemOnCollapse ? buttons.size() : buttons.size() - 1;
    for (int i = 0; i < lastItem; i++) {
      View button = buttons.get(i);
      button.setVisibility(visibility);
    }
  }

  private TimeInterpolator getExpandAnimatorInterpolation() {
    return new AccelerateInterpolator();
  }

  private TimeInterpolator getCollapseAnimatorInterpolation() {
    return new DecelerateInterpolator();
  }

  private Interpolator getContainerAnimationInterpolator() {
    return new DecelerateInterpolator();
  }

  private int getSumHeight() {
    int sumHeight = 0;
    for (View button : buttons) {
      sumHeight += button.getHeight() + getMarginRight(button) + getMarginLeft(button);
    }
    return sumHeight;
  }

  private int getMarginRight(View view) {
    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
    return layoutParams.rightMargin;
  }

  private int getMarginLeft(View view) {
    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
    return layoutParams.leftMargin;
  }

  private float getFirstItemHeight() {
    View firstButton = buttons.get(0);
    int height = firstButton.getHeight();
    FrameLayout.LayoutParams layoutParams =
        (FrameLayout.LayoutParams) firstButton.getLayoutParams();
    int topMargin = layoutParams.topMargin;
    int bottomMargin = layoutParams.bottomMargin;
    return height + topMargin + bottomMargin;
  }

  private void changeGravityToBottomCenterHorizontal(View view) {
    ((FrameLayout.LayoutParams) view.getLayoutParams()).gravity =
        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
  }

  public void initializeButton(View button) {
    changeGravityToBottomCenterHorizontal(button);
  }

  public void setHideFirstItemOnCollapse(boolean hideFirstItemOnCollapsed) {
    this.hideFirstItemOnCollapse = hideFirstItemOnCollapsed;
  }

  public interface Listener {
    void onAnimationFinished();
  }
}
