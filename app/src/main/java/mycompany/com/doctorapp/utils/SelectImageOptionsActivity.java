package mycompany.com.doctorapp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import mycompany.com.doctorapp.R;



public class SelectImageOptionsActivity extends Activity implements View.OnClickListener,Serializable {

    private Button mCamera,mGallery;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
    private int PICK_IMAGE_REQUEST = 1;
    private File file;
    private   Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_select_image_options);


        Dialog editProfileDialog = new Dialog(this);
        editProfileDialog.setTitle("Select Option");
        editProfileDialog.setContentView(R.layout.select_image_options2);
        initView(editProfileDialog);
        editProfileDialog.setCancelable(true);
        editProfileDialog.show();

    }

    private void initView(Dialog editProfileDialog) {
        mCamera = (Button) editProfileDialog.findViewById(R.id.select_image_camera);
        mCamera.setOnClickListener(this);

        mGallery = (Button) editProfileDialog.findViewById(R.id.select_image_gallery);
        mGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.select_image_camera:
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentCamera, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                break;
            case R.id.select_image_gallery:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                break;
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                if (bmp != null) {
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                }
                byte[] byteArray = stream.toByteArray();

                bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                convertToFile(bitmap,(int) System.currentTimeMillis());
                callToast("Image selected!");
                Intent intent = new Intent();
                intent.putExtra("imagedata",file);
                setResult(200,intent);
                finish();

            }
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                convertToFile(bitmap, (int) System.currentTimeMillis());
                callToast("Image selected!");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private void convertToFile(Bitmap bitmap, int i) {
        file = new File(this.getCacheDir(), "file"+i);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void callToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

}
