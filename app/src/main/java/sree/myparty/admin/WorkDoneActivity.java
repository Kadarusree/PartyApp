package sree.myparty.admin;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.pojos.InfluPerson;
import sree.myparty.pojos.LatLng;
import sree.myparty.pojos.MeetingPojo;
import sree.myparty.pojos.WorkDonePojo;
import sree.myparty.utils.Constants;
import sree.myparty.volunteer.InfluencePerson;

public class WorkDoneActivity extends AppCompatActivity {

    @BindView(R.id.wd_name)
    EditText edt_workDoneName;

    @BindView(R.id.wd_area_name)
    EditText edt_areaName;

    @BindView(R.id.wd_booth_num)
    EditText edt_boothNumber;

    @BindView(R.id.wd_startDate)
    EditText edt_startDate;

    @BindView(R.id.wd_endDate)
    EditText edt_endDate;

    @BindView(R.id.wd_moneySpent)
    EditText edt_moneySpent;

    @BindView(R.id.wd_supervisor)
    EditText edt_supervisor;

    @BindView(R.id.wd_contactName)
    EditText edt_phoneNumber;

    @BindView(R.id.wd_upload_photo)
    EditText edt_uploadPhoto;


    @BindView(R.id.btn_wd_save)
    Button workDone;

    String name, areaName, startDate, boothNumber, endDate, contracterName, phoneNumber, photo_uri;
    double moneySpent;


    DatabaseReference mDbref;
    byte[] image_array;


    //Related to Image
    private int REQUEST_CAMERA = 0, SELECT_FILE = 2;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 143;
    private String userChoosenTask;
    File GlobalImagePath = null;
    ByteArrayOutputStream GLOBALbytes = null;
    String imageFilePath;
    ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_done);
        ButterKnife.bind(this);
        mProgressDialog = Constants.showDialog(this);

        if (!Constants.isAdmin){
            workDone.setVisibility(View.GONE);
        }

        edt_startDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showDateTimePicker(edt_startDate);
                }
            }
        });

        edt_endDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showDateTimePicker(edt_endDate);
                }
            }
        });
        edt_uploadPhoto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    selectImage();
                }
            }
        });
    }


    @OnClick(R.id.wd_upload_photo)
    public void pickPhoto(View view) {
        selectImage();
    }

    private void selectImage() {
        final CharSequence[] items = {getResources().getString(R.string.takephoto), getResources().getString(R.string.choosefromlib), getResources().getString(R.string.cancel)};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(WorkDoneActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals(getResources().getString(R.string.takephoto))) {
                    boolean result = checkCameraPermission();
                    userChoosenTask = getResources().getString(R.string.takephoto);
                    if (result)
                        cameraIntent();

                } else if (items[item].equals(getResources().getString(R.string.choosefromlib))) {
                    boolean result = checkPermission();
                    userChoosenTask = getResources().getString(R.string.choosefromlib);
                    if (result)
                        galleryIntent();

                } else if (items[item].equals(getResources().getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

    }


    private void cameraIntent() {

        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                    pictureIntent.setClipData(ClipData.newRawUri("", photoUri));
                    pictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                startActivityForResult(pictureIntent, REQUEST_CAMERA);
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED) {
                        cameraIntent();
                    }

                } else {

                }

                break;
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        galleryIntent();
                    }

                } else {

                }

                break;

        }
    }

    private void onSelectFromGalleryResult(Intent data) {


        edt_uploadPhoto.setText(data.getData().toString());
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                onCaptureImageResult(null, bm, false);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_FILE) {
            if (resultCode == RESULT_OK) {
                edt_uploadPhoto.setText(data.getData().toString());

                onSelectFromGalleryResult(data);
            }
        } else if (requestCode == REQUEST_CAMERA) {
            {
                if (resultCode == RESULT_OK) {

                    onCaptureImageResult(data, null, true);
                }
            }

        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(WorkDoneActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(WorkDoneActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(WorkDoneActivity.this);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle(getResources().getString(R.string.permission));
                    alertBuilder.setMessage(getString(R.string.externalstorage));
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.M)
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            // ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    android.support.v7.app.AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkCameraPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(WorkDoneActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(WorkDoneActivity.this, android.Manifest.permission.CAMERA)) {
                    android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(WorkDoneActivity.this);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle(getResources().getString(R.string.permission));
                    alertBuilder.setMessage(getString(R.string.externalstorage));
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.M)
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                            // ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    android.support.v7.app.AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void onCaptureImageResult(Intent data, Bitmap bitmap, boolean fromCamera) {
        Bitmap thumbnail = null;

        if (fromCamera) {

            try {

                edt_uploadPhoto.setText(imageFilePath);

                thumbnail = BitmapFactory.decodeFile(imageFilePath);
                thumbnail = rotateImageIfRequired(thumbnail, Uri.parse(imageFilePath));

            } catch (Exception e) {

            }

        } else {
            thumbnail = bitmap;

        }


        if (thumbnail != null) {
            thumbnail = ShrinkBitmap(thumbnail);
            Bitmap dest = Bitmap.createBitmap(thumbnail.getWidth(), thumbnail.getHeight(), Bitmap.Config.ARGB_8888);


            Canvas cs = new Canvas(dest);
            Paint tPaint = new Paint();
            tPaint.setTextSize(20);
            tPaint.setColor(Color.BLUE);
            tPaint.setStyle(Paint.Style.FILL);
            float height = tPaint.measureText("yY");
            cs.drawBitmap(thumbnail, 0, 0, tPaint);
            cs.drawText(getDate(System.currentTimeMillis()), 20f, height + 15f, tPaint);


            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            try {
                dest.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                ContextWrapper cw = new ContextWrapper(this);
                File directory = cw.getDir(Constants.GKA_IMAGE_STORAGE_PATH, Context.MODE_PRIVATE);
                File mypath = new File(directory, System.currentTimeMillis() + ".jpg");
                setImagePath(mypath, bytes);


                image_array = bytes.toByteArray();
            } catch (Exception e) {


            }
            // ivImage.setImageBitmap(thumbnail);
        } else {
            Toast.makeText(this, "Image uploading Error", Toast.LENGTH_SHORT).show();
        }
    }

    Bitmap ShrinkBitmap(Bitmap file) {


        int nh = (int) (file.getHeight() * (512.0 / file.getWidth()));
        Bitmap resized = Bitmap.createScaledBitmap(file, 512, nh, true);
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.JPEG, 100, blob);
        byte[] array = blob.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(array);
        return BitmapFactory.decodeStream(bis);

    }

    public void setImagePath(File path, ByteArrayOutputStream bytes) {
        GlobalImagePath = path;
        GLOBALbytes = bytes;
    }

    public static String getDate(long milliSeconds) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) {

        ExifInterface ei = null;
        try {
            ei = new ExifInterface(selectedImage.getPath());

            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    @OnClick(R.id.btn_wd_save)
    public void SaveWork(View view) {

        name = edt_workDoneName.getText().toString();
        areaName = edt_areaName.getText().toString();
        boothNumber = edt_boothNumber.getText().toString();
        startDate = edt_startDate.getText().toString();
        endDate = edt_endDate.getText().toString();
        contracterName = edt_supervisor.getText().toString();
        //  moneySpent = edt_moneySpent.getText().toString();
        phoneNumber = edt_phoneNumber.getText().toString();
        photo_uri = edt_uploadPhoto.getText().toString();

        if (validations()) {
            moneySpent = Double.parseDouble(edt_moneySpent.getText().toString());
            mDbref = MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/WorkDone");
            String key = mDbref.push().getKey();
            uploadImageTask(image_array, key, mDbref);
        }

    }


    public void showDateTimePicker(final EditText mEditText) {
        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();
        new DatePickerDialog(WorkDoneActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(WorkDoneActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        Log.v("TAG", "The choosen one " + date.getTime());
                        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
                        mEditText.setText(format1.format(date.getTime()));
                        //  meetingDateTime.setEnabled(false);
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    public void uploadImageTask(byte[] data, final String key, final DatabaseReference mRef) {
        mProgressDialog.setMessage("Uploading Profile Picture");
        mProgressDialog.show();
        UploadTask uploadTask = MyApplication.getFirebaseStorage()
                .getReference(Constants.DB_PATH + "/WorkDone/" + key + ".jpg")
                .putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                mProgressDialog.dismiss();
                Constants.showToast("Failed To upload profile picture", WorkDoneActivity.this);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final Uri downloadUrl = taskSnapshot.getDownloadUrl();

                WorkDonePojo mWorkDone = new WorkDonePojo(key,
                        name,
                        areaName,
                        boothNumber,
                        startDate,
                        endDate,
                        downloadUrl.toString(),
                        contracterName,
                        phoneNumber,
                        "Admin",
                        System.currentTimeMillis() + "",
                        moneySpent, 0, 0);

                save(mWorkDone, mDbref, key);

            }
        });


    }

    private void save(WorkDonePojo mPojo, DatabaseReference mRef, String key) {
        mProgressDialog.show();
        mRef.child(key).setValue(mPojo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mProgressDialog.dismiss();
                if (task.isSuccessful()) {
                    Constants.showToast("Added SucessFully", WorkDoneActivity.this);
                } else {
                    Constants.showToast("Failed To add", WorkDoneActivity.this);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mProgressDialog.dismiss();

                Constants.showToast("Failed To add", WorkDoneActivity.this);

            }
        });
    }

    private boolean validations() {
        boolean isValid = false;
        if (name.length() < 3) {
            edt_workDoneName.setError("Enter Work Name");
        } else if (areaName.length() < 3) {
            edt_areaName.setError("Enter Area Name");
        } else if (startDate.length() < 3) {
            edt_startDate.setError("Select Start Date");
        } else if (edt_endDate.length() < 3) {
            edt_endDate.setError("Select End Date");
        } else if (boothNumber.length() == 0) {
            edt_boothNumber.setError("Select Start Date");
        } else if (contracterName.length() < 3) {
            edt_supervisor.setError("Select End Date");
        } else if (photo_uri.length() < 3) {
            edt_uploadPhoto.setError("Upload Photo");
        } else if (phoneNumber.length() < 10) {
            edt_phoneNumber.setError("Enter Valid Phone Number");
        } else if (edt_moneySpent.getText().length() == 0) {
            edt_moneySpent.setError("Enter Money Spent");
        } else {
            isValid = true;
        }

        return isValid;
    }


    @OnClick(R.id.wd_startDate)
    public void pickStartDate(View view) {
        showDateTimePicker(edt_startDate);
    }

    @OnClick(R.id.wd_endDate)
    public void pickEndDate(View view) {
        showDateTimePicker(edt_endDate);
    }
}
