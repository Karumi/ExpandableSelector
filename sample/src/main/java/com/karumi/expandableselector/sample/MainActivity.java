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

package com.karumi.expandableselector.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.karumi.expandableselector.ExpandableItem;
import com.karumi.expandableselector.ExpandableSelector;
import com.karumi.expandableselector.ExpandableSelectorListener;
import com.karumi.expandableselector.OnExpandableItemClickListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

  private View colorsHeaderButton;
  private ExpandableSelector colorsExpandableSelector;
  private ExpandableSelector sizesExpandableSelector;
  private ExpandableSelector iconsExpandableSelector;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity);
    initializeColorsExpandableSelector();
    initializeSizesExpandableSelector();
    initializeIconsExpandableSelector();
    initializeCloseAllButton();
  }

  private void initializeCloseAllButton() {
    final View closeButton = findViewById(R.id.bt_close);
    closeButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        colorsExpandableSelector.collapse();
        sizesExpandableSelector.collapse();
        iconsExpandableSelector.collapse();
        updateIconsFirstButtonResource(R.mipmap.ic_keyboard_arrow_up_black_36dp);
      }
    });
    colorsExpandableSelector.setExpandableSelectorListener(new ExpandableSelectorListener() {
      @Override public void onCollapse() {

      }

      @Override public void onExpand() {

      }

      @Override public void onCollapsed() {
        colorsHeaderButton.setVisibility(View.VISIBLE);
      }

      @Override public void onExpanded() {

      }
    });
  }

  private void initializeColorsExpandableSelector() {
    colorsExpandableSelector = (ExpandableSelector) findViewById(R.id.es_colors);
    List<ExpandableItem> expandableItems = new ArrayList<ExpandableItem>();
    expandableItems.add(new ExpandableItem(R.drawable.item_brown));
    expandableItems.add(new ExpandableItem(R.drawable.item_green));
    expandableItems.add(new ExpandableItem(R.drawable.item_orange));
    expandableItems.add(new ExpandableItem(R.drawable.item_pink));
    colorsExpandableSelector.showExpandableItems(expandableItems);
    colorsHeaderButton = findViewById(R.id.bt_colors);
    colorsHeaderButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        colorsHeaderButton.setVisibility(View.INVISIBLE);
        colorsExpandableSelector.expand();
      }
    });
    colorsExpandableSelector.setOnExpandableItemClickListener(new OnExpandableItemClickListener() {
      @Override public void onExpandableItemClickListener(int index, View view) {
        switch (index) {
          case 0:
            colorsHeaderButton.setBackgroundResource(R.drawable.item_brown);
            break;
          case 1:
            colorsHeaderButton.setBackgroundResource(R.drawable.item_green);
            break;
          case 2:
            colorsHeaderButton.setBackgroundResource(R.drawable.item_orange);
            break;
          default:
            colorsHeaderButton.setBackgroundResource(R.drawable.item_pink);
        }
        colorsExpandableSelector.collapse();
      }
    });
  }

  private void initializeSizesExpandableSelector() {
    sizesExpandableSelector = (ExpandableSelector) findViewById(R.id.es_sizes);
    List<ExpandableItem> expandableItems = new ArrayList<ExpandableItem>();
    expandableItems.add(new ExpandableItem("XL"));
    expandableItems.add(new ExpandableItem("L"));
    expandableItems.add(new ExpandableItem("M"));
    expandableItems.add(new ExpandableItem("S"));
    sizesExpandableSelector.showExpandableItems(expandableItems);
    sizesExpandableSelector.setOnExpandableItemClickListener(new OnExpandableItemClickListener() {
      @Override public void onExpandableItemClickListener(int index, View view) {
        switch (index) {
          case 1:
            ExpandableItem firstItem = sizesExpandableSelector.getExpandableItem(1);
            swipeFirstItem(1, firstItem);
            break;
          case 2:
            ExpandableItem secondItem = sizesExpandableSelector.getExpandableItem(2);
            swipeFirstItem(2, secondItem);
            break;
          case 3:
            ExpandableItem fourthItem = sizesExpandableSelector.getExpandableItem(3);
            swipeFirstItem(3, fourthItem);
            break;
        }
        sizesExpandableSelector.collapse();
      }

      private void swipeFirstItem(int position, ExpandableItem clickedItem) {
        ExpandableItem firstItem = sizesExpandableSelector.getExpandableItem(0);
        sizesExpandableSelector.updateExpandableItem(0, clickedItem);
        sizesExpandableSelector.updateExpandableItem(position, firstItem);
      }
    });
  }

  private void initializeIconsExpandableSelector() {
    iconsExpandableSelector = (ExpandableSelector) findViewById(R.id.es_icons);
    List<ExpandableItem> expandableItems = new ArrayList<ExpandableItem>();
    ExpandableItem item = new ExpandableItem();
    item.setResourceId(R.mipmap.ic_keyboard_arrow_up_black_36dp);
    expandableItems.add(item);
    item = new ExpandableItem();
    item.setResourceId(R.mipmap.ic_gamepad_black_36dp);
    expandableItems.add(item);
    item = new ExpandableItem();
    item.setResourceId(R.mipmap.ic_device_hub_black_36dp);
    expandableItems.add(item);
    iconsExpandableSelector.showExpandableItems(expandableItems);
    iconsExpandableSelector.setOnExpandableItemClickListener(new OnExpandableItemClickListener() {
      @Override public void onExpandableItemClickListener(int index, View view) {
        if (index == 0 && iconsExpandableSelector.isExpanded()) {
          iconsExpandableSelector.collapse();
          updateIconsFirstButtonResource(R.mipmap.ic_keyboard_arrow_up_black_36dp);
        }
      }
    });
    iconsExpandableSelector.setExpandableSelectorListener(new ExpandableSelectorListener() {
      @Override public void onCollapse() {

      }

      @Override public void onExpand() {
        updateIconsFirstButtonResource(R.mipmap.ic_keyboard_arrow_down_black_36dp);
      }

      @Override public void onCollapsed() {

      }

      @Override public void onExpanded() {

      }
    });
  }

  private void updateIconsFirstButtonResource(int ic_keyboard_arrow_up_black_36dp) {
    ExpandableItem arrowUpExpandableItem = new ExpandableItem();
    arrowUpExpandableItem.setResourceId(ic_keyboard_arrow_up_black_36dp);
    iconsExpandableSelector.updateExpandableItem(0, arrowUpExpandableItem);
  }

  private void showToast(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }
}
