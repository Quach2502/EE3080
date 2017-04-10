package com.example.zhuosheng.ee3080app;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ViewImage extends Activity {
    // Declare Variable
    TextView mainName,suggestion0,suggestion1,suggestion2,suggestion3;
    ImageView imageview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from view_image.xml
        setContentView(R.layout.view_image);

        // Retrieve data from ListImageActivity on GridView item click
        Intent i = getIntent();
        List<TakenPicture> history = MyPreferences.loadSharedPreferencesLogList(this.getApplicationContext());
        // Get the position
        int position = i.getExtras().getInt("position");

        // Get String arrays FilePathStrings
        String[] filepath = i.getStringArrayExtra("filepath");

        // Get String arrays FileNameStrings
        String[] filename = i.getStringArrayExtra("filename");
        suggestion0 = (TextView) findViewById(R.id.suggestion0Text);
        suggestion0.setText(history.get(position).getSuggestion()[0]);
        suggestion1 = (TextView) findViewById(R.id.suggestion1Text);
        suggestion1.setText(history.get(position).getSuggestion()[1]);
        suggestion2 = (TextView) findViewById(R.id.suggestion2Text);
        suggestion2.setText(history.get(position).getSuggestion()[2]);
        suggestion3 = (TextView) findViewById(R.id.suggestion3Text);
        suggestion3.setText(history.get(position).getSuggestion()[3]);
        // Locate the TextView in view_image.xml
        mainName = (TextView) findViewById(R.id.mainNameText);
        // Load the text into the TextView followed by the position
//        text.setText(filename[position]);
        mainName.setText(history.get(position).getMainName());
        // Locate the ImageView in view_image.xml
        imageview = (ImageView) findViewById(R.id.full_image_view);
        // Decode the filepath with BitmapFactory followed by the position
//        Bitmap bmp = BitmapFactory.decodeFile(filepath[position]);
        Bitmap bmp = BitmapFactory.decodeFile(history.get(position).getLinkToPicture());
        // Set the decoded bitmap into ImageView
        imageview.setImageBitmap(bmp);

    }
}
