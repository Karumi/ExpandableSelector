HeaderRecyclerView
==================

ExpandableSelector is an Android library created to be able to show a list of Button/ImageButton widgets inside a animated container which can be collapsed expanded.

Screenshots
-----------

![Demo Screenshot][1]

Usage
-----

To use ``ExpandableSelector`` in your application you have to follow this steps:

* 1 - Add ``ExpandableSelector`` to your layout:

```xml

 <com.karumi.expandableselector.ExpandableSelector
      android:id="@+id/es_colors"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"/>

```

* 2 - During your Activity/Fragment creation lifecycle create a list of ``ExpandableItem`` instances and configure them to be used inside your ``ExpandableSelector`` instance:

```java

private void initializeSizesExpandableSelector() {
    ExpandableSelector sizesExpandableSelector = (ExpandableSelector) findViewById(R.id.es_sizes);
    List<ExpandableItem> expandableItems = new ArrayList<ExpandableItem>();
    expandableItems.add(new ExpandableItem("XL"));
    expandableItems.add(new ExpandableItem("L"));
    expandableItems.add(new ExpandableItem("M"));
    expandableItems.add(new ExpandableItem("S"));
    sizesExpandableSelector.showExpandableItems(expandableItems);
}

```

* 3 - To be able to listen the animation events configure a ``ExpandableSelectorListener`` instance:

```java

private void configureExpandableSelectorListener() {

    sizesExpandableSelector.setExpandableSelectorListener(new ExpandableSelectorListener() {
          @Override public void onCollapse() {
            //Do something here
          }

          @Override public void onExpand() {
            //Do something here
          }

          @Override public void onCollapsed() {
            //Do something here
          }

          @Override public void onExpanded() {
            //Do something here
          }
        });
}

```

* 4 - To be able to listen the click events configure a ``OnExpandableItemClickListener`` instance:

```java

private void configureExpandableSelectorClickListener() {

    sizesExpandableSelector.setOnExpandableItemClickListener(new OnExpandableItemClickListener() {
          @Override public void onExpandableItemClickListener(int index, View view) {
             //Do something here
          }
        });
}

```

Customization
-------------

You can easily customize the appearance of your ``ExpandableItem`` instances. Just asign a value to ``expandableItemSyle`` attribute in your theme and make it reference a custom style.

```xml

<style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
    <item name="expandableItemStyle">@style/ExpandableItemStyle</item>
</style>

<style name="ExpandableItemStyle">
    <item name="android:layout_height">@dimen/item_size</item>
    <item name="android:layout_width">@dimen/item_size</item>
    <item name="android:background">@drawable/bg_item_with_title</item>
    <item name="android:layout_margin">@dimen/item_margin</item>
    <item name="android:textColor">@color/gray</item>
    <item name="android:textSize">@dimen/item_text_size</item>
</style>

```

The resources you can show in the Button/ImageButton widgets automatically added to ``ExpandableSelector`` are:

* Background resource identifier configured as Button/ImageButton background.
* Resource identifier configured as ImageButton image source.
* Title configured as Button text.

All this information will be provided to the ``ExpandableSelector`` inside a ``List<ExpandableItem>`` object created by the library user.

Add it to your project
----------------------

Add ``ExpandableSelector`` dependency to your ``build.gradle`` file

```groovy

dependencies{
    compile 'com.karumi:expandableselector:1.0.0'
}

```

or to your ``pom.xml`` if you are using Maven instead of Gradle

```xml

<dependency>
    <groupId>com.karumi</groupId>
    <artifactId>expandableselector</artifactId>
    <version>1.0.0</version>
    <type>aar</type>
</dependency>

```

Do you want to contribute?
--------------------------

Please, do it! We'd like to improve this library with your help :)

License
-------

    Copyright 2015 Karumi

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
