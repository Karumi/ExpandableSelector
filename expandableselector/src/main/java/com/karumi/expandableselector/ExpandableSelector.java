package com.karumi.expandableselector;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import java.util.Collections;
import java.util.List;

/**
 * FrameLayout extension used to show a list of ExpandableItems instances which can be collapsed
 * and expanded using an animation.
 */
public class ExpandableSelector extends FrameLayout {

  private final AttributeSet attrs;
  private List<ExpandableItem> expandableItems = Collections.EMPTY_LIST;

  public ExpandableSelector(Context context) {
    this(context, null);
  }

  public ExpandableSelector(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ExpandableSelector(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.attrs = attrs;
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public ExpandableSelector(Context context, AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    this.attrs = attrs;
  }

  /**
   * Configures a List<ExpandableItem> to be shown. By default, the list of ExpandableItems is
   * going to be shown collapsed. Please take into account that this method creates ImageButtons
   * based on the size of the list passed as parameter. Don't use this library as a RecyclerView
   * and
   * take into account the number of elements to show.
   */
  public void setExpandableItems(List<ExpandableItem> expandableItems) {
    validateExpandableItems(expandableItems);
    this.expandableItems = expandableItems;
    renderItems();
  }

  private void renderItems() {
    int numberOfItems = expandableItems.size() - 1;
    LayoutInflater inflater = LayoutInflater.from(getContext());
    for (int i = numberOfItems; i >= 0; i--) {
      View button = inflater.inflate(R.layout.expandable_item, this, false);
      //View button = new ImageButton(getContext(), attrs, R.attr.expandableItemStyle);
      addView(button);
    }
  }

  private void validateExpandableItems(List<ExpandableItem> expandableItems) {
    if (expandableItems == null) {
      throw new IllegalArgumentException(
          "The List<ExpandableItem> passed as argument can't be null");
    }
  }
}
