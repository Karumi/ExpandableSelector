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

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

/**
 * Performs all the animations and size or position changes related to the
 * ExpandableSelectorComponent and controls the view state in terms of collapsed/expanded
 * animation.
 */
public class ExpandableSelectorAnimator {

  private static final String Y_ANIMATION = "translationY";
  private static final float CONTAINER_ANIMATION_OFFSET = 1.16f;

  private final View container;
  private final int animationDuration;
  private final int expandInterpolatorId;
  private final int collapseInterpolatorId;
  private final int containerInterpolatorId;

  private List<View> buttons;
  private boolean isCollapsed = true;
  private boolean hideFirstItemOnCollapse;

  public ExpandableSelectorAnimator(View container, int animationDuration, int expandInterpolatorId, int
          collapseInterpolatorId, int containerInterpolatorId) {
    this.container = container;
    this.animationDuration = animationDuration;
    this.expandInterpolatorId = expandInterpolatorId;
    this.collapseInterpolatorId = collapseInterpolatorId;
    this.containerInterpolatorId = containerInterpolatorId;
  }

  /**
   * Returns true if the ExpandableSelector widget is collapsed or false if is expanded.
   */
  public boolean isCollapsed() {
    return isCollapsed;
  }

  /**
   * Returns true if the ExpandableSelector widget is expanded or false if is collapsed.
   */
  public boolean isExpanded() {
    return !isCollapsed();
  }

  /**
   * Configures the List of buttons used to calculate the animation parameters.
   */
  public void setButtons(List<View> buttons) {
    this.buttons = buttons;
  }

  /**
   * Expands the ExpandableSelector performing a resize animation and at the same time moves the
   * buttons configures as childrens to the associated position given the order in the List<View>
   * used to keep the reference to the buttons. The visibility of the buttons inside the
   * ExpandableSelector changes to View.VISIBLE before to perform the animation.
   */
  public void expand(Listener listener) {
    setCollapsed(false);
    changeButtonsVisibility(View.VISIBLE);
    expandButtons();
    expandContainer(listener);
  }

  /**
   * Collapses the ExpandableSelector performing a resize animation and at the same time moves the
   * buttons configures as childrens to the associated position given the order in the List<View>
   * used to keep the reference to the buttons. The visibility of the buttons inside the
   * ExpandableSelector changes to View.INVISIBLE after the resize animation.
   */
  public void collapse(Listener listener) {
    setCollapsed(true);
    collapseButtons();
    collapseContainer(listener);
  }

  /**
   * Configures the Button/ImageButton added to the ExpandableSelector to match with the initial
   * configuration needed by the component.
   */
  public void initializeButton(View button) {
    changeGravityToBottomCenterHorizontal(button);
  }

  /**
   * Configures the ExpandableSelectorAnimator to change the first item visibility to View.VISIBLE
   * /
   * View.INVISIBLE once the collapse/expand animation has been performed.
   */
  public void setHideFirstItemOnCollapse(boolean hideFirstItemOnCollapsed) {
    this.hideFirstItemOnCollapse = hideFirstItemOnCollapsed;
  }

  /**
   * Returns the component to the initial state without remove configuration related to animation
   * durations of if the first item visibility has to be changed.
   */
  public void reset() {
    this.buttons = new ArrayList<View>();
    this.isCollapsed = true;
  }

  private void setCollapsed(boolean isCollapsed) {
    this.isCollapsed = isCollapsed;
  }

  private void expandButtons() {
    int numberOfButtons = buttons.size();
    Animator[] animations = new Animator[numberOfButtons];
    for (int i = 0; i < numberOfButtons; i++) {
      View button = buttons.get(i);
      TimeInterpolator interpolator = getExpandAnimatorInterpolation();
      float toY = calculateExpandedYPosition(i);
      animations[i] = createAnimatorForButton(interpolator, button, toY);
    }
    playAnimatorsTogether(animations);
  }

  private void collapseButtons() {
    int numberOfButtons = buttons.size();
    TimeInterpolator interpolator = getCollapseAnimatorInterpolation();
    Animator[] animations = new Animator[numberOfButtons];
    for (int i = 0; i < numberOfButtons; i++) {
      View button = buttons.get(i);
      float toY = 0;
      animations[i] = createAnimatorForButton(interpolator, button, toY);
    }
    playAnimatorsTogether(animations);
  }

  private void expandContainer(final Listener listener) {
    float toWidth = container.getWidth();
    float toHeight = getSumHeight();
    Interpolator interpolator = getContainerAnimationInterpolator();
    ResizeAnimation resizeAnimation =
        createResizeAnimation(toWidth, interpolator, toHeight, listener);
    container.startAnimation(resizeAnimation);
  }

  private void collapseContainer(final Listener listener) {
    float toWidth = container.getWidth();
    float toHeight = getFirstItemHeight();
    Interpolator interpolator = getContainerAnimationInterpolator();
    ResizeAnimation resizeAnimation =
        createResizeAnimation(toWidth, interpolator, toHeight, new Listener() {
          @Override public void onAnimationFinished() {
            changeButtonsVisibility(View.INVISIBLE);
            listener.onAnimationFinished();
          }
        });
    container.startAnimation(resizeAnimation);
  }

  private ObjectAnimator createAnimatorForButton(TimeInterpolator interpolator, View button,
      float toY) {
    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(button, Y_ANIMATION, toY);
    objectAnimator.setInterpolator(interpolator);
    objectAnimator.setDuration(animationDuration);
    return objectAnimator;
  }

  private ResizeAnimation createResizeAnimation(float toWidth, Interpolator interpolator,
      float toHeight, final Listener listener) {
    ResizeAnimation resizeAnimation = new ResizeAnimation(container, toWidth, toHeight);
    resizeAnimation.setInterpolator(interpolator);
    resizeAnimation.setDuration((long) (animationDuration * CONTAINER_ANIMATION_OFFSET));
    resizeAnimation.setAnimationListener(new AbstractAnimationListener() {
      @Override public void onAnimationEnd(Animation animation) {
        listener.onAnimationFinished();
      }
    });
    return resizeAnimation;
  }

  private void playAnimatorsTogether(Animator[] animations) {
    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(animations);
    animatorSet.start();
  }

  private float calculateExpandedYPosition(int buttonPosition) {
    int numberOfButtons = buttons.size();
    float y = 0;
    for (int i = numberOfButtons - 1; i > buttonPosition; i--) {
      View button = buttons.get(i);
      y = y - button.getHeight() - getMarginRight(button) - getMarginLeft(button);
    }
    return y;
  }

  private void changeButtonsVisibility(int visibility) {
    int lastItem = hideFirstItemOnCollapse ? buttons.size() : buttons.size() - 1;
    for (int i = 0; i < lastItem; i++) {
      View button = buttons.get(i);
      button.setVisibility(visibility);
    }
  }

  private TimeInterpolator getExpandAnimatorInterpolation() {
    return AnimationUtils.loadInterpolator(container.getContext(), expandInterpolatorId);
  }

  private TimeInterpolator getCollapseAnimatorInterpolation() {
    return AnimationUtils.loadInterpolator(container.getContext(), collapseInterpolatorId);
  }

  private Interpolator getContainerAnimationInterpolator() {
    return AnimationUtils.loadInterpolator(container.getContext(), containerInterpolatorId);
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

  public interface Listener {
    void onAnimationFinished();
  }
}
