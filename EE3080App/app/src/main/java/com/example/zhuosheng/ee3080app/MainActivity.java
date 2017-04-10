package com.example.zhuosheng.ee3080app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.lang.reflect.Type;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class MainActivity extends AppCompatActivity {

    Button btnSendPicture,btnCapture,btnHistory,btnListImage;
    EditText IPAddress;
    private static int RESULT_LOAD_IMAGE = 1;
    int TAKE_PHOTO_CODE = 0;
    List<TakenPicture> takenPictures = new ArrayList<TakenPicture>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 0;
        int MY_PERMISSIONS_MANAGE_DOCUMENTS = 0;
        int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 0;


        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            }
            else
            {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.MANAGE_DOCUMENTS)!= PackageManager.PERMISSION_GRANTED)
        {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.MANAGE_DOCUMENTS))
            {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            }
            else
            {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.MANAGE_DOCUMENTS},
                        MY_PERMISSIONS_MANAGE_DOCUMENTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            }
            else
            {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        btnSendPicture = (Button) findViewById(R.id.btnSendPicture);
        btnCapture = (Button) findViewById(R.id.btnCapturePicture);
        btnHistory = (Button) findViewById(R.id.btnHistory);
        btnListImage = (Button) findViewById(R.id.btnListImage);
        IPAddress = (EditText) findViewById(R.id.IPaddress);

        btnListImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start ListImageActivity.class
                Intent myIntent = new Intent(MainActivity.this,ListImageActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
        btnSendPicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //capture picture
                //captureImage();
                String ip = IPAddress.getText().toString();
                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.dog);
                SendImage(bm, ip);
            }
        });

        btnCapture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);

            }
        });
        btnHistory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });
    }

    void SendImage(Bitmap bmp, String IP){
        //FileOutputStream fop;
        Socket socket;

        try {
            //fop=new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+ ".jpg");
            //bmp.compress(Bitmap.CompressFormat.JPEG, 100, fop);
            //fop.close();
            //Toast.makeText(MainActivity.this, "saved",Toast.LENGTH_LONG).show();

            InetAddress serverAdd = InetAddress.getByName(IP);
            SocketAddress sc_add = new InetSocketAddress(serverAdd,5000);
            socket = new Socket();
            socket.connect(sc_add,10000);

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] bytes = baos.toByteArray();
            int size = bytes.length;
            out.writeInt(size);
            out.write(bytes);
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            Toast.makeText(this,response,Toast.LENGTH_LONG).show();
            in.close();
            socket.close();
        } catch (IOException e) {
            Log.e("ImageUploader", "Error uploading image", e);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            String ip = IPAddress.getText().toString();
            TakenPicture obj = saveImageToExternalStorage(photo);
            obj.setMainName("Not set yet");
            String[] suggestion = new String[]{"?","?","?","?"};
            obj.setSuggestion(suggestion);
            List<TakenPicture> history = MyPreferences.loadSharedPreferencesLogList(this.getApplicationContext());
            history.add(obj);
            MyPreferences.saveSharedPreferencesLogList(this.getApplicationContext(),history);

            SendImage(photo, ip);
            //Uri u = data.getData();
            //CropImage.activity(u).setGuidelines(CropImageView.Guidelines.ON).start(this);
        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imgView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }

       /* if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //Uri imageUri = result.getUri();
                Bitmap photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                String ip = IPAddress.getText().toString();
                SendImage(photo, ip);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }*/
    }
    private TakenPicture saveImageToExternalStorage(Bitmap finalBitmap) {
        File myDir = new File(Environment.getExternalStorageDirectory(),"EE3080App");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        TakenPicture obj = new TakenPicture(file.getAbsolutePath());
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(this, new String[] { file.toString() }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
        return obj;

    }
}
