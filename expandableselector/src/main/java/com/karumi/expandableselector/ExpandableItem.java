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

/**
 * Contains all the information needed to render a expandable item inside a ExpandableSelector
 * widget. The information you can render is a Drawable identifier or a String used as title.
 */
public class ExpandableItem {

  private static final int NO_ID = -1;

  private final int drawableId;
  private final String title;

  public ExpandableItem(int drawableId) {
    this(drawableId, null);
  }

  public ExpandableItem(String title) {
    this(NO_ID, title);
  }

  private ExpandableItem(int drawableId, String title) {
    this.drawableId = drawableId;
    this.title = title;
  }

  public int getDrawableId() {
    return drawableId;
  }

  public String getTitle() {
    return title;
  }

  public boolean hasDrawableId() {
    return drawableId != NO_ID;
  }

  public boolean hasTitle() {
    return title != null;
  }
}
