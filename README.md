# NotePad
## 增加功能：
## 1.UI美化
## 2.增加时间戳
## 3.修改背景
## 4.搜索框模糊查询

以下阐述功能实现步骤并附上效果图

### 1.UI美化
源代码的界面风格是远古黑，我改成了全白，并替换了一些按钮图标的样式
#### 主界面是这样的

![](https://github.com/hellodidi/NotePad-master-chen/blob/master/pic/1.png "mainUI");

#### 编辑界面是这样的

![](https://github.com/hellodidi/NotePad-master-chen/blob/master/pic/4.png "editUI");
### 2.增加时间戳
首先查看数据表创建过程，看到有了创建时间(COLUMN_NAME_CREATE_DATE)和修改时间(COLUMN_NAME_MODIFICATION_DATE)这两个字段，但由于是long类型，所以肯定需要进行转换，所以我声明一个dateutil类用于类型转换，如下：
```
public class DateUtil {
    public static String timeStamp2Date(String seconds, String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if(format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date(Long.valueOf(seconds)));
    }
```
接着在noteEditor中的Updatenote进行更新时间的调用
```
        String updateTiemStr = DateUtil.timeStamp2Date(String.valueOf(System.currentTimeMillis()),null);
        values.put(NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE, updateTiemStr);
```
然后修改一下布局文件里面的，增加了一个textview用于显示notedate

```
<TextView
        android:id="@+id/note_date"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textAppearance="?android:attr/textAppearanceLarge"
        android:layout_alignParentLeft="true"
        android:paddingLeft="5dip"
        android:layout_alignParentBottom="true"
        android:textSize="20px"
        android:layout_marginLeft="10dp"
        android:layout_marginBottom="10dp"
        android:singleLine="true"
        />
```
        
 然后效果就差不多有了
 
 ![](https://github.com/hellodidi/NotePad-master-chen/blob/master/pic/5.png "timestamp");
 
 ### 3.修改背景
 首先我创建了一个alertdialog的xml，列出想要几种颜色，其中我用button对应颜色，大致如下
 ```
   <Button
        android:id="@+id/green"
        android:layout_height="65dp"
        android:layout_width="0dp"
        android:layout_weight="1"
        android:background="#EEEED1"
        android:layout_marginRight="2dp"
        android:layout_marginTop="15dp"
        android:layout_marginBottom="15dp"
        />
 ```
 
 然后在notelist和noteEditor这两个有菜单栏的类中为按钮添加click事件，大致如下
 ```
  Button button_green = (Button) layout.findViewById(R.id.green);
                button_green.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RelativeLayout note_list = (RelativeLayout)findViewById(R.id.noteLayout);
                        note_list.setBackgroundColor(Color.parseColor("#EEEED1"));
                        aDialog.dismiss();
                    }
                });
 ```
 这样就可以起到改变背景颜色的效果，效果如图

 ![](https://github.com/hellodidi/NotePad-master-chen/blob/master/pic/2.png "alertdialog");
 
 ![](https://github.com/hellodidi/NotePad-master-chen/blob/master/pic/3.png "Background-color changed");
 
### 4.搜索框实现模糊搜索
搜索功能我用于对备忘录标题的搜索，所以我把框放在了主界面中listitems的上方

#### 首先我在主界面中添加了一个系统的searchview组件
  ```
   <SearchView
        android:id="@+id/searchview"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@xml/corners_bg">
    </SearchView>
```
#### 在activity中进行初始化之后创建setOnQueryTextListener监听器实现监听事件，使用 adapter.swapCursor(cursor)对新数据进行展示，这个点有点像网页中的ajax技术，并对字符输入时的输入法进行隐藏处理
```
        final SearchView mSearchView = (SearchView)findViewById(R.id.searchview);
        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            private String TAG = getClass().getSimpleName();

            @Override
            public boolean onQueryTextChange(String queryText) {
                Log.d(TAG, "onQueryTextChange = " + queryText);
                String selection = NotePad.Notes.COLUMN_NAME_TITLE + " LIKE '%" + queryText + "%' " ;
                // String[] selectionArg = { queryText };
                Cursor cursor = getContentResolver().query(getIntent().getData(), PROJECTION, selection, null, null);
                adapter.swapCursor(cursor); // 交换指针，展示新的数据
                return true;
            }

           
            @Override
            public boolean onQueryTextSubmit(String queryText) {
                Log.d(TAG, "onQueryTextSubmit = " + queryText);

                if (mSearchView != null) {
                    // 得到输入管理对象
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {   
                        imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0); 
                    }
                    mSearchView.clearFocus(); // 不获取焦点
                }
                return true;
            }

        });

    }
 ```   
 搜索功能效果如下
 
 ![](https://github.com/hellodidi/NotePad-master-chen/blob/master/pic/6.png "search");
  
  
  
  
#### 以上
