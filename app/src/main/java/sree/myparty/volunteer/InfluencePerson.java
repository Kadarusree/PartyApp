package sree.myparty.volunteer;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import sree.myparty.DashBoard.Dashboard;
import sree.myparty.DashBoard.ProfileScreen;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.Adapters.InfluencePersonAdapter;
import sree.myparty.admin.VolunteerList;
import sree.myparty.beans.NewsPojo;
import sree.myparty.misc.NewsListAdapter;
import sree.myparty.pojos.InfluPerson;
import sree.myparty.pojos.LatLng;
import sree.myparty.pojos.UserDetailPojo;
import sree.myparty.session.SessionManager;
import sree.myparty.utils.Constants;
import sree.myparty.utils.MyDividerItemDecoration;

public class InfluencePerson extends AppCompatActivity {

    @BindView(R.id.infl_name)
    EditText edt_name;
    @BindView(R.id.infl_booth_num)
    EditText booth_num;
    @BindView(R.id.infl_mobile_num)
    EditText edt_mobile_num;

    @BindView(R.id.btn_infl_save)
    Button btn_save;

    @BindView(R.id.infl_location)
    EditText edt_location;

    @BindView(R.id.infl_upload_photo)
    EditText edt_profile_pic;


    DatabaseReference mDbref;


    private RecyclerView recyclerView;
    private List<InfluPerson> newsList;
    private InfluencePersonAdapter mAdapter;


    int PLACE_PICKER_REQUEST = 1;


    private int REQUEST_CAMERA = 0, SELECT_FILE = 2;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 143;
    private String userChoosenTask;
    File GlobalImagePath = null;
    ByteArrayOutputStream GLOBALbytes = null;
    String imageFilePath;
    ProgressDialog mProgressDialog;


    ///Influence Person Variables//

    String name, mobile_number, profile_uri, address;
    LatLng location = null;
    int booth_number;
    byte[] image_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_influence_person);
        ButterKnife.bind(this);

        mProgressDialog = Constants.showDialog(this);
        recyclerView = findViewById(R.id.list_influencePersons);
        newsList = new ArrayList<>();
        mAdapter = new InfluencePersonAdapter(this, newsList);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new InfluencePerson.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        mProgressDialog.show();
        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/InfluencePersons").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProgressDialog.dismiss();
                newsList.clear();
                for (DataSnapshot indi : dataSnapshot.getChildren()) {
                    InfluPerson mNewsItem = indi.getValue(InfluPerson.class);
                    newsList.add(mNewsItem);
                }
                // refreshing recycler view
                mAdapter.notifyDataSetChanged();

                // stop animating Shimmer and hide the layout

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mProgressDialog.dismiss();
            }
        });
    }

    @OnClick(R.id.btn_infl_save)
    public void onButtonClick(View v) {
        name = edt_name.getText().toString().trim();
        mobile_number = edt_mobile_num.getText().toString().trim();
        profile_uri = edt_profile_pic.getText().toString();
        address = edt_location.getText().toString();
        if (validations()) {
            booth_number = Integer.parseInt(booth_num.getText().toString());
            mDbref = MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/InfluencePersons");
            String key = mDbref.push().getKey();
            uploadImageTask(image_array, key, mDbref);
        }


    }

    private boolean validations() {
        boolean isValid = false;
        if (name.length() < 3) {
            edt_name.setError("Enter Name");
        } else if (mobile_number.length() < 10) {
            edt_mobile_num.setError("Enter 10 digit mobile number");
        } else if (booth_num.getText().toString().trim().length() == 0) {
            booth_num.setError("Enter Booth Number");
        } else if (location == null) {
            edt_location.setError("Pick Adress");
        } else if (profile_uri == null || profile_uri.length() == 0) {
            edt_profile_pic.setError("Upload Profile Pic");
        } else {
            isValid = true;
        }

        return isValid;
    }

    public void save(InfluPerson mPerson, DatabaseReference mRef, String key) {
        mProgressDialog.show();
        mRef.child(key).setValue(mPerson).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mProgressDialog.dismiss();
                if (task.isSuccessful()) {
                    Constants.showToast("Added SucessFully", InfluencePerson.this);
                } else {
                    Constants.showToast("Failed To add", InfluencePerson.this);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mProgressDialog.dismiss();

                Constants.showToast("Failed To add", InfluencePerson.this);

            }
        });
    }


    @OnClick(R.id.infl_pick_location)
    public void onClick(View v) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                location = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                edt_location.setText(String.format("%s", place.getName()));
            }
        } else if (requestCode == SELECT_FILE) {
            if (resultCode == RESULT_OK) {
                edt_profile_pic.setText(data.getData().toString());

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


    @OnClick(R.id.infl_upload_photo)
    public void pickPhoto(View view) {

        selectImage();
    }

    private void selectImage() {
        final CharSequence[] items = {getResources().getString(R.string.takephoto), getResources().getString(R.string.choosefromlib), getResources().getString(R.string.cancel)};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(InfluencePerson.this);
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


        edt_profile_pic.setText(data.getData().toString());
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(InfluencePerson.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(InfluencePerson.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(InfluencePerson.this);
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
            if (ContextCompat.checkSelfPermission(InfluencePerson.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(InfluencePerson.this, android.Manifest.permission.CAMERA)) {
                    android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(InfluencePerson.this);
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

                edt_profile_pic.setText(imageFilePath);

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


    public void uploadImageTask(byte[] data, final String key, final DatabaseReference mRef) {
        mProgressDialog.setMessage("Uploading Profile Picture");
        mProgressDialog.show();
        UploadTask uploadTask = MyApplication.getFirebaseStorage()
                .getReference(Constants.DB_PATH + "/InfluencePersons/" + key + ".jpg")
                .putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                mProgressDialog.dismiss();
                Constants.showToast("Failed To upload profile picture", InfluencePerson.this);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                InfluPerson mPerson = new InfluPerson(key,name, mobile_number, System.currentTimeMillis() + "", downloadUrl.toString(), address, location, Constants.VOLUNTEER, booth_number);
                save(mPerson, mRef, key);
            }
        });


    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
