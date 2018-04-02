package com.example.acer.last;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
// Importing UploadService Package.

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;


import java.util.UUID;

public class Main2Activity extends AppCompatActivity {
    Button UploadButton;
    String PdfPathHolder, PdfID; //PdfNameHolder;
    // Creating URI .
    Uri uri;

    // Server URL.
    public static final String PDF_UPLOAD_HTTP_URL = "https://android-examples.000webhostapp.com/server_upload_pdf.php";

    // Pdf upload request code.
    public int PDF_REQ_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Method to enable runtime permission.
        RequestRunTimePermission();
        // Assign ID'S to button
        UploadButton = (Button) findViewById(R.id.Button_Upload_PDF_ID);

        // Adding click listener to Button.
        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // PDF selection code start from here .
                // Creating intent object.
                Intent intent = new Intent();

                // Setting up default file pickup time as PDF.
                intent.setType("application/pdf");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PDF_REQ_CODE);
                PdfUploadFunction();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PDF_REQ_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();

            // After selecting the PDF set PDF is Selected text inside Button.
            UploadButton.setText("PDF is Selected");

        }
    }

    // PDF upload function starts from here.
    public void PdfUploadFunction() {

        // Getting pdf name from EditText.
        // PdfNameHolder = PdfNameEditText.getText().toString().trim();

        // Getting file path using Filepath class.
        PdfPathHolder = FilePath.getPath(this, uri);

        // If file path object is null then showing toast message to move file into internal storage.
        if (PdfPathHolder == null) {

            Toast.makeText(this, "Please move your PDF file to internal storage & try again.", Toast.LENGTH_LONG).show();

        }
        // If file path is not null then PDF uploading file process will starts.
        else {

            try {

                PdfID = UUID.randomUUID().toString();

                new MultipartUploadRequest(this, PdfID, PDF_UPLOAD_HTTP_URL)
                        .addFileToUpload(PdfPathHolder, "pdf")
                        //.addParameter("name", PdfNameHolder)
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(5)
                        .startUpload();

            } catch (Exception exception) {

                Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    // Requesting run time permission method starts from here.
    public void RequestRunTimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(Main2Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            Toast.makeText(Main2Activity.this, "READ_EXTERNAL_STORAGE permission Access Dialog", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(Main2Activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        }
    }


    public void onRequestPermissionsResult(int RC, String per[], int[] Result) {

        switch (RC) {

            case 1:

                if (Result.length > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(Main2Activity.this, "Permission Granted", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(Main2Activity.this, "Permission Canceled", Toast.LENGTH_LONG).show();

                }
                break;
        }


    }
}