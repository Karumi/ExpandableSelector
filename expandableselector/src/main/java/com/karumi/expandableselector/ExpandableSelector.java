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

package com.karumi.expandableselector;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import com.karumi.expandableselector.animation.AbstractAnimationListener;
import com.karumi.expandableselector.animation.ResizeAnimation;
import com.karumi.expandableselector.animation.VisibilityAnimatorListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * FrameLayout extension used to show a list of ExpandableItems instances which can be collapsed
 * and expanded using an animation.
 */
public class ExpandableSelector extends FrameLayout {

  private static final String Y_ANIMATION = "translationY";

  private List<ExpandableItem> expandableItems = Collections.EMPTY_LIST;
  private List<View> buttons = new LinkedList<View>();
  private boolean hideBackgroundIfCollapsed;
  private boolean isCollapsed = true;
  private Drawable expandedBackground;

  private ExpandableSelectorListener listener;
  private OnExpandableItemClickListener clickListener;

  public ExpandableSelector(Context context) {
    this(context, null);
  }

  public ExpandableSelector(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ExpandableSelector(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initializeView(attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public ExpandableSelector(Context context, AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    initializeView(attrs);
  }

  /**
   * Configures a List<ExpandableItem> to be shown. By default, the list of ExpandableItems is
   * going to be shown collapsed. Please take into account that this method creates ImageButtons
   * based on the size of the list passed as parameter. Don't use this library as a RecyclerView
   * and take into account the number of elements to show.
   */
  public void showExpandableItems(List<ExpandableItem> expandableItems) {
    validateExpandableItems(expandableItems);
    this.expandableItems = expandableItems;
    renderExpandableItems();
    hookListeners();
    bringChildrensToFront(expandableItems);
  }

  public void expand() {
    isCollapsed = false;
    notifyExpand();
    expandContainer();
    int numberOfButtons = buttons.size();
    for (int i = 0; i < numberOfButtons; i++) {
      View button = buttons.get(i);
      button.setVisibility(View.VISIBLE);
      TimeInterpolator interpolator = getExpandAnimatorInterpolation();
      float toY = calculateExpandedYPosition(i);
      ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(button, Y_ANIMATION, toY);
      objectAnimator.setInterpolator(interpolator);
      objectAnimator.start();
    }
    updateBackground();
  }

  public void collapse() {
    isCollapsed = true;
    notifyCollapse();
    collapseContainer();
    int numberOfButtons = buttons.size();
    TimeInterpolator interpolator = getCollapseAnimatorInterpolation();
    for (int i = 0; i < numberOfButtons; i++) {
      View button = buttons.get(i);
      ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(button, Y_ANIMATION, 0);
      objectAnimator.setInterpolator(interpolator);
      if (i != numberOfButtons - 1) {
        objectAnimator.addListener(new VisibilityAnimatorListener(button, View.INVISIBLE));
      }
      objectAnimator.start();
    }
  }

  public boolean isCollapsed() {
    return isCollapsed;
  }

  public boolean isExpanded() {
    return !isCollapsed();
  }

  public void setExpandableSelectorListener(ExpandableSelectorListener listener) {
    this.listener = listener;
  }

  public void setOnExpandableItemClickListener(OnExpandableItemClickListener clickListener) {
    this.clickListener = clickListener;
  }

  public void updateExpandableItem(int expandableItemPosition, ExpandableItem expandableItem) {
    validateExpandableItem(expandableItem);
    expandableItems.remove(expandableItemPosition);
    expandableItems.add(expandableItemPosition, expandableItem);
    int buttonPosition = buttons.size() - 1 - expandableItemPosition;
    configureButton(buttons.get(buttonPosition), expandableItem);
  }

  private void initializeView(AttributeSet attrs) {
    TypedArray attributes =
        getContext().obtainStyledAttributes(attrs, R.styleable.expandable_selector);
    initializeHideBackgroundIfCollapsed(attributes);
    attributes.recycle();
  }

  private void initializeHideBackgroundIfCollapsed(TypedArray attributes) {
    hideBackgroundIfCollapsed =
        attributes.getBoolean(R.styleable.expandable_selector_hide_background_if_collapsed, false);
    expandedBackground = getBackground();
    updateBackground();
  }

  private void updateBackground() {
    if (!hideBackgroundIfCollapsed) {
      return;
    }
    if (!isCollapsed) {
      setBackgroundDrawable(expandedBackground);
    } else {
      setBackgroundResource(android.R.color.transparent);
    }
  }

  private void renderExpandableItems() {
    int numberOfItems = expandableItems.size();
    for (int i = numberOfItems - 1; i >= 0; i--) {
      View button = initializeButton(i);
      addView(button);
      changeGravityToBottomCenterHorizontal(button);
      configureButton(button, expandableItems.get((i)));
      buttons.add(button);
    }
  }

  private void hookListeners() {
    final int numberOfButtons = buttons.size();
    boolean thereAreMoreThanOneButton = numberOfButtons > 1;
    if (thereAreMoreThanOneButton) {
      buttons.get(numberOfButtons - 1).setOnClickListener(new OnClickListener() {
        @Override public void onClick(View v) {
          if (isCollapsed()) {
            expand();
          } else {
            notifyButtonClicked(0, v);
          }
        }
      });
    }
    for (int i = 0; i < numberOfButtons - 1; i++) {
      final int buttonPosition = i;
      buttons.get(i).setOnClickListener(new OnClickListener() {
        @Override public void onClick(View v) {
          notifyButtonClicked(numberOfButtons - 1 - buttonPosition, v);
        }
      });
    }
  }

  private void notifyButtonClicked(int itemPosition, View button) {
    if (clickListener != null) {
      clickListener.onExpandableItemClickListener(itemPosition, button);
    }
  }

  private View initializeButton(int expandableItemPosition) {
    ExpandableItem expandableItem = expandableItems.get(expandableItemPosition);
    View button = null;
    Context context = getContext();
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    if (expandableItem.hasTitle()) {
      button = layoutInflater.inflate(R.layout.expandable_item_button, this, false);
    } else {
      button = layoutInflater.inflate(R.layout.expandable_item_image_button, this, false);
    }
    int visibility = expandableItemPosition == 0 ? View.VISIBLE : View.INVISIBLE;
    button.setVisibility(visibility);
    return button;
  }

  private void configureButton(View button, ExpandableItem expandableItem) {
    if (expandableItem.hasBackgroundId()) {
      int backgroundId = expandableItem.getBackgroundId();
      button.setBackgroundResource(backgroundId);
    }
    if (expandableItem.hasTitle()) {
      String text = expandableItem.getTitle();
      ((Button) button).setText(text);
    }
    if (expandableItem.hasResourceId()) {
      ImageButton imageButton = (ImageButton) button;
      int resourceId = expandableItem.getResourceId();
      imageButton.setImageResource(resourceId);
    }
  }

  private void changeGravityToBottomCenterHorizontal(View view) {
    ((LayoutParams) view.getLayoutParams()).gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
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

  private void expandContainer() {
    float toWidth = getWidth();
    float toHeight = getSumHeight();
    ResizeAnimation resizeAnimation = new ResizeAnimation(this, toWidth, toHeight);
    Interpolator interpolator = getExpandAnimationInterpolator();
    resizeAnimation.setInterpolator(interpolator);
    resizeAnimation.setAnimationListener(new AbstractAnimationListener() {
      @Override public void onAnimationEnd(Animation animation) {
        notifyExpanded();
      }
    });
    startAnimation(resizeAnimation);
  }

  private void collapseContainer() {
    float toWidth = getWidth();
    float toHeight = getFirstItemHeight();
    ResizeAnimation resizeAnimation = new ResizeAnimation(this, toWidth, toHeight);
    Interpolator interpolator = getCollapseAnimationInterpolator();
    resizeAnimation.setInterpolator(interpolator);
    resizeAnimation.setAnimationListener(new AbstractAnimationListener() {
      @Override public void onAnimationEnd(Animation animation) {
        updateBackground();
        notifyCollapsed();
      }
    });
    startAnimation(resizeAnimation);
  }

  private void bringChildrensToFront(List<ExpandableItem> expandableItems) {
    int childCount = getChildCount();
    int numberOfExpandableItems = expandableItems.size();
    if (childCount > numberOfExpandableItems) {
      for (int i = 0; i < childCount - numberOfExpandableItems; i++) {
        getChildAt(i).bringToFront();
      }
    }
  }

  private TimeInterpolator getExpandAnimatorInterpolation() {
    return new AccelerateInterpolator();
  }

  private TimeInterpolator getCollapseAnimatorInterpolation() {
    return new DecelerateInterpolator();
  }

  private Interpolator getExpandAnimationInterpolator() {
    return new AccelerateInterpolator();
  }

  private Interpolator getCollapseAnimationInterpolator() {
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
    FrameLayout.LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
    return layoutParams.rightMargin;
  }

  private int getMarginLeft(View view) {
    FrameLayout.LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
    return layoutParams.leftMargin;
  }

  private float getFirstItemHeight() {
    View firstButton = buttons.get(0);
    int height = firstButton.getHeight();
    FrameLayout.LayoutParams layoutParams = (LayoutParams) firstButton.getLayoutParams();
    int topMargin = layoutParams.topMargin;
    int bottomMargin = layoutParams.bottomMargin;
    return height + topMargin + bottomMargin;
  }

  private void notifyExpand() {
    if (hasListenerConfigured()) {
      listener.onExpand();
    }
  }

  private void notifyCollapse() {
    if (hasListenerConfigured()) {
      listener.onCollapse();
    }
  }

  private void notifyExpanded() {
    if (hasListenerConfigured()) {
      listener.onExpanded();
    }
  }

  private void notifyCollapsed() {
    if (hasListenerConfigured()) {
      listener.onCollapsed();
    }
  }

  private boolean hasListenerConfigured() {
    return listener != null;
  }

  private void validateExpandableItem(ExpandableItem expandableItem) {
    if (expandableItem == null) {
      throw new IllegalArgumentException(
          "You can't use a null instance of ExpandableItem as parameter.");
    }
  }

  private void validateExpandableItems(List<ExpandableItem> expandableItems) {
    if (expandableItems == null) {
      throw new IllegalArgumentException(
          "The List<ExpandableItem> passed as argument can't be null");
    }
  }
}
