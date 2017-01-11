#前言

这是一个支持侧滑删除的菜单，使用简单，可以灵活适配你的UI。效果图如下：

<img src='app/screenshot/screenshot.gif' height='480px'/>

#项目使用
MainActivity.java

```
public class MainActivity extends AppCompatActivity {

    private Swipe2DeleteViewGroup mSwipe2Delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipe2Delete = (Swipe2DeleteViewGroup) findViewById(R.id.swipe2delete);
        mSwipe2Delete.setOnItemClickListener(new Swipe2DeleteViewGroup.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int index, boolean isCenterView) {
                if (view instanceof TextView) {
                    TextView textView = (TextView) view;
                    String str = textView.getText().toString();
                    Toast.makeText(MainActivity.this, String.format("%s , isCenterView: %s", str, isCenterView), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
```
activity_main.xml

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context="com.xiaohongshu.swipe2delete.MainActivity">

    <TextView
        android:id="@+id/tv"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:gravity="center_vertical"
        android:paddingBottom="@dimen/activity_vertical_margin"
        android:paddingLeft="@dimen/activity_horizontal_margin"
        android:paddingRight="@dimen/activity_horizontal_margin"
        android:paddingTop="@dimen/activity_vertical_margin"
        android:text="侧滑删除菜单" />

    <com.xiaohongshu.swipe2delete.Swipe2DeleteViewGroup
        android:id="@+id/swipe2delete"
        android:layout_width="match_parent"
        android:layout_height="80dp"
        android:background="@android:color/white">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:gravity="center"
            android:text="主布局" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@android:color/holo_green_light"
            android:gravity="center"
            android:text="置顶"
            android:textColor="@android:color/white" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@android:color/holo_green_light"
            android:gravity="center"
            android:text="测试"
            android:textColor="@android:color/white"
            android:visibility="gone" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@android:color/holo_red_light"
            android:gravity="center"
            android:text="删除"
            android:textColor="@android:color/white" />

    </com.xiaohongshu.swipe2delete.Swipe2DeleteViewGroup>
</LinearLayout>
```
因为定义的这个ViewGroup直接把第一个子View当成中间要显示的View，其他的View都当成了需要侧滑才显示出来的MenuItem，所以在布局里面可以直接调整布局，ViewGroup会在代码里帮你处理好其他的逻辑。

#实现过程

详细实现请看博客[自定义ViewGroup实现侧滑删除菜单](http://blog.csdn.net/aishang5wpj/article/details/54093911)
 
关于
--

博客：[http://blog.csdn.net/aishang5wpj](http://blog.csdn.net/aishang5wpj)

邮箱：337487365@qq.com

License
--
Copyright 2017 aishang5wpj

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.