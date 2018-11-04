package com.example.administrator.bttuan6;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.RadioButton;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {
    Button btnSave, btnChange, btnCancel;
    EditText edtName, edtEmail, edtPhone;
    RadioButton radMale, radFemale;
    ImageView imgProfile;

    final int RESULT_TAKE_PICTURE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnChange = (Button) findViewById(R.id.btnChange);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        edtName = (EditText) findViewById(R.id.edtName);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPhone = (EditText) findViewById(R.id.edtPhone);

        radMale = (RadioButton) findViewById(R.id.radioMale);
        radFemale = (RadioButton) findViewById(R.id.radioFemale);

        loadImage();
        readData();

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileImgName = "CameraDemo.jpeg";
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                File output = new File(dir, fileImgName);
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
                startActivityForResult(i, RESULT_TAKE_PICTURE);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeData();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == RESULT_TAKE_PICTURE && resultCode == RESULT_OK){
            File folderDICM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File output =  new File(folderDICM,"CameraDemo.jpeg");
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgProfile = (ImageView) findViewById(R.id.imageViewUit);
            imgProfile.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void writeData(){
        String filename = "myFile.txt";

        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();

        File doc = new File (filePath,filename);

        if (doc.exists())
            doc.delete();

        String dataWrite = edtName.getText().toString()
                + "\n" + edtEmail.getText().toString()
                + "\n" + edtPhone.getText().toString()
                + "\n";

        if (radMale.isChecked())
            dataWrite = dataWrite + radMale.getText().toString();
        else if (radFemale.isChecked())
            dataWrite = dataWrite + radMale.getText().toString();

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(doc,true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fos);
            myOutWriter.append(dataWrite);
            myOutWriter.close();
            fos.close();
            Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), dataWrite, Toast.LENGTH_LONG).show();
    }

    private  void cancel() {
        finish();
        System.exit(0);
    }

    private void readData() {
        String filename = "myFile.txt";
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
        File doc = new File(filePath, filename);

        if (doc.exists()) {
            String[] data = new String[4];
            int i = -1;
            try {
                Scanner scan = new Scanner(doc);
                while (scan.hasNext()) {
                    i += 1;
                    data[i] = scan.nextLine();
                }
                scan.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            edtName.setText(data[0]);
            edtEmail.setText(data[1]);
            edtPhone.setText(data[2]);

            if (data[3].equals("Male"))
                radMale.setChecked(true);
            else
                radFemale.setChecked(true);

            Toast.makeText(getApplicationContext(),"Loaded data!",Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(),text.toString(), Toast.LENGTH_LONG).show();
        }
    }
    private void loadImage() {
        File folderDICM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File output = new File(folderDICM, "CameraDemo.jpeg");
        if (output.exists()) {
            Bitmap MyBitmap = BitmapFactory.decodeFile(output.getAbsolutePath());
            imgProfile = (ImageView) findViewById(R.id.imageViewUit);
            imgProfile.setImageBitmap(MyBitmap);
        }
    }
}