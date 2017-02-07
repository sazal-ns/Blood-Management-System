/*
 * Copyright (c) 2017. By RTSoftBD.
 * Author: Noor Nabiul Alam Siddiqui
 */

package helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by RTsoftBD_Siddiqui on 2017-02-06.
 */

public class ShowDialog extends AppCompatActivity{
    Context context;
    String  content,title;
    Drawable icon;

    public ShowDialog(Context context, String title, String content, Drawable icon) {
        this.title = title;
        this.context = context;
        this.content = content;
        this.icon = icon;


        MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .icon(icon);
        MaterialDialog dialog = builder.build();
        dialog.show();
    }
}
