package com.example.zhuosheng.ee3080app;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class ListImageActivity extends AppCompatActivity {

    // Declare variables
    private String[] FilePathStrings;
    private String[] FileNameStrings;
    List<TakenPicture> history;
    GridView grid;
    GridViewAdapter adapter;
//    File file;
    private final int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_image_activity);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            }
            else
            {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
//        // Check for SD Card
//        if (!Environment.getExternalStorageState().equals(
//                Environment.MEDIA_MOUNTED)) {
//            Toast.makeText(this, "Error! No SDCARD Found!", Toast.LENGTH_LONG)
//                    .show();
//        } else {
//            // Locate the image folder in your SD Card
//            file = new File(Environment.getExternalStorageDirectory()
//                    + File.separator + "EE3080App");
//            file.mkdirs();
//        }
        history = MyPreferences.loadSharedPreferencesLogList(this.getApplicationContext());
        for (Iterator<TakenPicture> iter = history.listIterator(); iter.hasNext(); ) {
            TakenPicture pic = iter.next();
            if(!(new File(pic.getLinkToPicture()).exists()))
                iter.remove();
        }
        MyPreferences.saveSharedPreferencesLogList(this,history);
        // Create a String array for FilePathStrings
        FilePathStrings = new String[history.size()];
        // Create a String array for FileNameStrings
        FileNameStrings = new String[history.size()];
        for (int i = 0; i < history.size(); i++) {
            // Get the path of the image file
            FilePathStrings[i] = history.get(i).getLinkToPicture();
            // Get the name image file
            FileNameStrings[i] = history.get(i).getMainName();
        }

        // Locate the GridView in gridview.xml
        grid = (GridView) findViewById(R.id.gridview);
        // Pass String arrays to GridViewAdapter Class
        adapter = new GridViewAdapter(this, FilePathStrings, FileNameStrings);
        grid.setAdapter(adapter);

        // Capture gridview item click
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ListImageActivity.this, ViewImage.class);
                // Pass String arrays FilePathStrings
                i.putExtra("filepath", FilePathStrings);
                // Pass String arrays FileNameStrings
                i.putExtra("filename", FileNameStrings);
                // Pass click position
                i.putExtra("position", position);
                startActivity(i);
            }
        });
    }
    }