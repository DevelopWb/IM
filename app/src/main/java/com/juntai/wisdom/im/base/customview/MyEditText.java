package com.juntai.wisdom.im.base.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Toast;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022/3/22 10:18
 * @UpdateUser: 更新者
 * @UpdateDate: 2022/3/22 10:18
 */
public class MyEditText extends android.support.v7.widget.AppCompatEditText  {
   private static final int ID_SELECTION_MODE = android.R.id.selectTextMode;
   // Selection context mode
   private static final int ID_SELECT_ALL = android.R.id.selectAll;
   private static final int ID_CUT = android.R.id.cut;
   private static final int ID_COPY = android.R.id.copy;
   private static final int ID_PASTE = android.R.id.paste;

   private final Context mContext;

   /*
    * Just the constructors to create a new EditText...
    */
   public MyEditText(Context context) {
      super(context);
      this.mContext = context;
   }

   public MyEditText(Context context, AttributeSet attrs) {
      super(context, attrs);
      this.mContext = context;
   }

   public MyEditText(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      this.mContext = context;
   }

//   @Override
//   protected void onCreateContextMenu(ContextMenu menu) {
////代码效果，有弹出框选择 粘贴，复制，剪切，类似qq效果.....
//      menu.add(0, ID_PASTE, 0, "粘贴").setOnMenuItemClickListener(this);
//      menu.add(0, ID_CUT, 0, "剪切").setOnMenuItemClickListener(this);
//      menu.add(0, ID_COPY, 0, "复制").setOnMenuItemClickListener(this);
//      menu.add(0, ID_SELECT_ALL, 0, "全选").setOnMenuItemClickListener(this);
//      super.onCreateContextMenu(menu);
//   }


//   @Override
//   public boolean onMenuItemClick(MenuItem item) {
//      // TODO Auto-generated method stub
//      return onTextContextMenuItem(item.getItemId());
//   }



   @Override
   public boolean onTextContextMenuItem(int id) {
      // Do your thing:
      boolean consumed = super.onTextContextMenuItem(id);
      // React:
      switch (id) {
         case android.R.id.cut:
            onTextCut();
            break;
         case android.R.id.paste:
            onTextPaste();
            break;
         case android.R.id.copy:
            onTextCopy();
      }
      return consumed;
   }

   /**
    * Text was cut from this EditText.
    */
   public void onTextCut() {
      Toast.makeText(mContext, "Cut!", Toast.LENGTH_SHORT).show();
   }

   /**
    * Text was copied from this EditText.
    */
   public void onTextCopy() {
      Toast.makeText(mContext, "Copy!", Toast.LENGTH_SHORT).show();
   }

   /**
    * Text was pasted into the EditText.
    */
   public void onTextPaste() {
      Toast.makeText(mContext, "Paste!", Toast.LENGTH_SHORT).show();
   }
}