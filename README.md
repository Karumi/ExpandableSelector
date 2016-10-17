![Karumi logo][karumilogo] ExpandableSelector [![Build Status](https://travis-ci.org/Karumi/ExpandableSelector.svg?branch=master)](https://travis-ci.org/Karumi/ExpandableSelector) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.karumi/expandableselector/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.karumi/expandableselector) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ExpandableSelector-green.svg?style=flat)](https://android-arsenal.com/details/1/1987)
==================

ExpandableSelector is an Android library created to show a list of Button/ImageButton widgets inside a animated container which can be collapsed or expanded.

Screenshots
-----------

![Demo Screenshot][1]

Usage
-----

To use ``ExpandableSelector`` in your application you have to follow this steps:

* 1 - Add ``ExpandableSelector`` to your layout:

```xml

 <com.karumi.expandableselector.ExpandableSelector
      android:id="@+id/es_sizes"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"/>

```

* 2 - During your Activity/Fragment creation lifecycle create a list of ``ExpandableItem`` instances and configure them to be used inside your ``ExpandableSelector`` widget:

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

***Remember that declaring a ``ExpandableItemStyle`` as described before with the layout height and width is mandatory to avoid inflation errors.***

The resources you can show in the Button/ImageButton widgets automatically added to ``ExpandableSelector`` are:

* Background resource identifier configured as Button/ImageButton background.
* Resource identifier configured as ImageButton image source.
* Title configured as Button text.

All this information will be provided to the ``ExpandableSelector`` inside a ``List<ExpandableItem>`` object created by the library user.

Some extra configuration parameters can be provided from the XML like styleable attributes:

```xml

  <com.karumi.expandableselector.ExpandableSelector
      xmlns:expandable_selector="http://schemas.android.com/apk/res-auto"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      expandable_selector:hide_background_if_collapsed="true"
      expandable_selector:hide_first_item_on_collapse="true"
      expandable_selector:expand_interpolator="@android:anim/accelerate_interpolator"
      expandable_selector:collapse_interpolator="@android:anim/decelerate_interpolator"
      expandable_selector:container_interpolator="@android:anim/accelerate_decelerate_interpolator"
      expandable_selector:animation_duration="100">

```

The attributes you can configure are:

* expandable_selector:hide_background_if_collapsed: Changes the background associated to the ``ExpandableSelector`` widget to a transparent one while the widget is collapsed.
* expandable_selector:hide_first_item_on_collapse: Changes the first item visibility to View.INVISIBLE when the ``ExpandableSelector`` is collapsed.
* expandable_selector:expand_interpolator: Changes the interpolator used in the expand animation (applies to the items), you can use an interpolator available in the platform, or create your own.
* expandable_selector:collapse_interpolator: Changes the interpolator used in the collapse animation (applies to the items).
* expandable_selector:container_interpolator: Changes the interpolator used in the expand & collapse animation (applies to the container)
* expandable_selector:animation_duration: Changes the animation duration in milliseconds to the one indicated.

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


[1]: ./art/screenshot_demo_1.gif
[karumilogo]: https://cloud.githubusercontent.com/assets/858090/11626547/e5a1dc66-9ce3-11e5-908d-537e07e82090.png
