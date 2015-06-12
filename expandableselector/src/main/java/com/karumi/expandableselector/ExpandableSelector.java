package com.karumi.expandableselector;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
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
  private static final int NO_RESOURCE_ID = -1;
  private static final int NO_SIZE = -1;
  private static int NO_MARING = -1;

  private List<ExpandableItem> expandableItems = Collections.EMPTY_LIST;
  private List<View> buttons = new LinkedList<View>();
  private boolean isCollapsed = true;
  private float initialPosition;

  private int itemsBackground;
  private int itemsSize;
  private int itemsMargin;

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
  public void setExpandableItems(List<ExpandableItem> expandableItems) {
    validateExpandableItems(expandableItems);
    this.expandableItems = expandableItems;
    renderExpandableItems();
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

  private void initializeView(AttributeSet attrs) {
    TypedArray attributes =
        getContext().obtainStyledAttributes(attrs, R.styleable.expandable_selector);
    initializeItemsBackground(attributes);
    initializeItemsSize(attributes);
    initializeItemsMargin(attributes);
    attributes.recycle();
  }

  private void initializeItemsBackground(TypedArray attributes) {
    itemsBackground =
        attributes.getResourceId(R.styleable.expandable_selector_expandable_item_background,
            NO_RESOURCE_ID);
  }

  private void initializeItemsSize(TypedArray attributes) {
    itemsSize =
        attributes.getDimensionPixelSize(R.styleable.expandable_selector_expandable_item_size,
            NO_SIZE);
  }

  private void initializeItemsMargin(TypedArray attributes) {
    itemsMargin =
        attributes.getDimensionPixelSize(R.styleable.expandable_selector_expandable_item_margin,
            NO_MARING);
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
    resize();
  }

  private View initializeButton(int expandableItemPosition) {
    ExpandableItem expandableItem = expandableItems.get(expandableItemPosition);
    View button = null;
    Context context = getContext();
    if (expandableItem.hasDrawableId()) {
      button = new ImageButton(context);
    } else if (expandableItem.hasTitle()) {
      button = new Button(context);
    }
    return button;
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
    if (hasItemsBackgroundConfigured()) {
      button.setBackgroundResource(itemsBackground);
      button.setPadding(0, 0, 0, 0);
    }
    LayoutParams layoutParams = ((LayoutParams) button.getLayoutParams());
    if (hasItemsMarginConfigured()) {
      layoutParams.leftMargin = itemsMargin;
      layoutParams.rightMargin = itemsMargin;
      layoutParams.topMargin = itemsMargin;
      layoutParams.bottomMargin = itemsMargin;
    }
    if (hasItemsSizeConfigured()) {
      layoutParams.width = itemsSize;
      layoutParams.height = itemsSize;
    } else {
      layoutParams.width = LayoutParams.WRAP_CONTENT;
      layoutParams.height = LayoutParams.WRAP_CONTENT;
    }
  }

  private boolean hasItemsBackgroundConfigured() {
    return itemsBackground != NO_RESOURCE_ID;
  }

  private boolean hasItemsSizeConfigured() {
    return itemsSize != NO_SIZE;
  }

  private boolean hasItemsMarginConfigured() {
    return itemsMargin != NO_MARING;
  }

  private void changeGravityToBottomCenterHorizontal(View view) {
    ((LayoutParams) view.getLayoutParams()).gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
  }

  private float calculateExpandedYPosition(int buttonPosition) {
    int numberOfButtons = buttons.size();
    float y = 0;
    for (int i = numberOfButtons - 1; i > buttonPosition; i--) {
      View button = buttons.get(i);
      y = y + button.getHeight() + itemsMargin * 2;
    }
    return -y;
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
      int buttonWidth = button.getWidth() + itemsMargin * 2;
      maxWidth = Math.max(maxWidth, buttonWidth);
    }
    return maxWidth;
  }

  private int getSumHeight() {
    int sumHeight = 0;
    for (View button : buttons) {
      sumHeight += button.getHeight() + itemsMargin * 2;
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
