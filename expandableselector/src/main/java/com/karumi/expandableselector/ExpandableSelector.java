package com.karumi.expandableselector;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
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
  private boolean isCollapsed = true;

  public ExpandableSelector(Context context) {
    this(context, null);
  }

  public ExpandableSelector(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ExpandableSelector(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initializeView();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public ExpandableSelector(Context context, AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    initializeView();
  }

  /**
   * Configures a List<ExpandableItem> to be shown. By default, the list of ExpandableItems is
   * going to be shown collapsed. Please take into account that this method creates ImageButtons
   * based on the size of the list passed as parameter. Don't use this library as a RecyclerView
   * and take into account the number of elements to show.
   */
  public void setExpandableItems(List<ExpandableItem> expandableItems) {
    validateExpandableItems(expandableItems);
    this.expandableItems = expandableItems;
    renderItems();
  }

  public void expand() {
    int itemPosition = 0;
    View button = buttons.get(0);
    ObjectAnimator.ofFloat(button, Y_ANIMATION, -100).start();
  }

  public void collapse() {
    int itemPosition = 0;
    Animation animation = new TranslateAnimation(0, 0, 0, 100);
    buttons.get(0).startAnimation(animation);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    if (event.getActionMasked() == MotionEvent.ACTION_UP) {
      if (isCollapsed) {
        expand();
      } else {
        collapse();
      }
    }
    return true;
  }

  private void initializeView() {
  }

  private void renderItems() {
    int numberOfItems = expandableItems.size() - 1;
    LayoutInflater inflater = LayoutInflater.from(getContext());
    for (int i = numberOfItems; i >= 0; i--) {
      View button = inflater.inflate(R.layout.expandable_item, this, false);
      addView(button);
      changeGravityToBottomCenterHorizontall(button);
      buttons.add(button);
    }
    resize();
  }

  private void changeGravityToBottomCenterHorizontall(View view) {
    ((LayoutParams) view.getLayoutParams()).gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
  }

  private void resize() {
    post(new Runnable() {
      @Override public void run() {
        getLayoutParams().height = getSumHeight();
        getLayoutParams().width = getMaxWidth();
      }
    });
  }

  private int getMaxWidth() {
    int maxWidth = 0;
    for (View button : buttons) {
      maxWidth = Math.max(maxWidth, button.getWidth());
    }
    return maxWidth;
  }

  private int getSumHeight() {
    int sumHeight = 0;
    for (View button : buttons) {
      sumHeight += button.getHeight();
    }
    return sumHeight;
  }

  private void validateExpandableItems(List<ExpandableItem> expandableItems) {
    if (expandableItems == null) {
      throw new IllegalArgumentException(
          "The List<ExpandableItem> passed as argument can't be null");
    }
  }
}
