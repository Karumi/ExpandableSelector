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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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
  private float initialPosition;

  public ExpandableSelector(Context context) {
    this(context, null);
  }

  public ExpandableSelector(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ExpandableSelector(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public ExpandableSelector(Context context, AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
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
    int numberOfButtons = buttons.size();
    for (int i = 0; i < numberOfButtons; i++) {
      View button = buttons.get(i);
      float toY = calculateExpandedYPosition(i);
      ObjectAnimator.ofFloat(button, Y_ANIMATION, toY).start();
    }
    isCollapsed = false;
  }

  public void collapse() {
    int numberOfButtons = buttons.size();
    for (int i = 0; i < numberOfButtons; i++) {
      View button = buttons.get(i);
      ObjectAnimator.ofFloat(button, Y_ANIMATION, initialPosition).start();
    }
    isCollapsed = true;
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

  private void renderItems() {
    int numberOfItems = expandableItems.size();
    LayoutInflater inflater = LayoutInflater.from(getContext());
    for (int i = 0; i < numberOfItems; i++) {
      ExpandableItem expandableItem = expandableItems.get(i);
      int expandable_item = expandableItem.hasDrawableId() ? R.layout.expandable_item_drawable
          : R.layout.expandable_item_title;
      View button = inflater.inflate(expandable_item, this, false);
      configureButton(button, expandableItems.get((i)));
      addView(button);
      changeGravityToBottomCenterHorizontal(button);
      buttons.add(button);
    }
    resize();
  }

  private void configureButton(View button, ExpandableItem expandableItem) {
    button.setClickable(false);
    if (expandableItem.hasDrawableId()) {
      ImageButton imageButton = (ImageButton) button;
      int drawableId = expandableItem.getDrawableId();
      imageButton.setImageResource(drawableId);
    } else if (expandableItem.hasTitle()) {
      Button textButton = (Button) button;
      String text = expandableItem.getTitle();
      textButton.setText(text);
    }
  }

  private void changeGravityToBottomCenterHorizontal(View view) {
    ((LayoutParams) view.getLayoutParams()).gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
  }

  private float calculateExpandedYPosition(int buttonPosition) {
    float y = 0;
    for (int i = 0; i < buttonPosition; i++) {
      View button = buttons.get(i);
      LayoutParams layoutParams = (LayoutParams) button.getLayoutParams();
      y -= button.getHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
    }
    return y;
  }

  private void resize() {
    post(new Runnable() {
      @Override public void run() {
        getLayoutParams().height = getSumHeight();
        getLayoutParams().width = getMaxWidth();
        initialPosition = buttons.get(0).getY();
      }
    });
  }

  private int getMaxWidth() {
    int maxWidth = 0;
    for (View button : buttons) {
      LayoutParams layoutParams = (LayoutParams) button.getLayoutParams();
      int buttonWidth = button.getWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
      maxWidth = Math.max(maxWidth, buttonWidth);
    }
    return maxWidth;
  }

  private int getSumHeight() {
    int sumHeight = 0;
    for (View button : buttons) {
      LayoutParams layoutParams = (LayoutParams) button.getLayoutParams();
      sumHeight += button.getHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
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
