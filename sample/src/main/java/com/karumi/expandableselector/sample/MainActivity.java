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
            showToast("Brown button clicked");
            break;
          case 1:
            showToast("Green button clicked");
            break;
          case 2:
            showToast("Oragne button clicked");
            break;
          default:
            showToast("Pink button clicked");
        }
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
          case 0:
            showToast("XL button clicked");
            break;
          case 1:
            showToast("L button clicked");
            break;
          case 2:
            showToast("M button clicked");
            break;
          default:
            showToast("S button clicked");
        }
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
        switch (index) {
          case 0:
            showToast("Arrow button clicked");
            break;
          case 1:
            showToast("Gamepad button clicked");
            break;
          default:
            showToast("Hub button clicked");
        }
      }
    });
  }

  private void showToast(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }
}
