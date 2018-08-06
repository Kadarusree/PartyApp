package sree.myparty.DashBoard;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.beans.NewsPojo;
import sree.myparty.misc.PostNews;
import sree.myparty.pojos.UserDetailPojo;
import sree.myparty.session.SessionManager;
import sree.myparty.utils.Constants;

public class ProfileScreen extends AppCompatActivity {



    @BindView(R.id.id_profile_pic)
    CircleImageView mProfilePic;

    @BindView(R.id.id_QRCode)
    ImageView mQRcode;


   /* @BindView(R.id.id_tv_app_profilepic)
    TextView btnUpdateProfilePic;
*/
    @BindView(R.id.id_tv_points)
    TextView mPoints;

    @BindView(R.id.id_tv_refnumber)
    TextView mRefNumber;

    SessionManager mSession;


    @BindView(R.id.profile_tv_name)
    TextView name;

    @BindView(R.id.profile_tv_voterID)
    TextView voetrID;

    @BindView(R.id.profile_tv_mobileNumber)
    TextView mobileNumber;

    @BindView(R.id.profile_tv_pc)
    TextView pc;

    @BindView(R.id.profile_tv_ac)
    TextView ac;


    @BindView(R.id.img_badge)
    ImageView img_badge;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA=143;
    private String userChoosenTask;
    File GlobalImagePath = null;
    ByteArrayOutputStream GLOBALbytes = null;
    String imageFilePath;
    ProgressDialog mProgressDialog;

    FirebaseStorage mFirebaseStorage;
    StorageReference mStorageReference;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        ButterKnife.bind(this);

        mSession = new SessionManager(this);

       // Glide.with(this).load(mSession.getProfilePic()).into(mProfilePic);
        Glide.with(this).load(mSession.getQR()).into(mQRcode);

        loadImage(this,mProfilePic,mSession.getProfilePic());

        mPoints.setText("Points : "+mSession.getPoints()+"");
        checkBadge(mSession.getPoints());
        mRefNumber.setText( mSession.getRegID());

        mProgressDialog = Constants.showDialog(this);
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReference();


        name.setText("Name : " +mSession.getName());
        voetrID.setText("Voter ID : " +mSession.getVoterID());
        mobileNumber.setText("Mobile : " +mSession.getMobileNumber());
        ac.setText("Assembly Constituency : " +mSession.getAC_NAME());
        pc.setText("Parliament Constituency : " +mSession.getPC_NAME());


        getLatestData();

    }

    @OnClick(R.id.id_profile_pic)
    public void uploadPhoto(View v){
        selectImage();
    }

    public void checkBadge(int points)
    {
        if(points<=50)
        {
        img_badge.setBackgroundResource(R.drawable.platinum_badge);
        }else if(points<=100)
        {
            img_badge.setBackgroundResource(R.drawable.silver_badge);
        }
        else if(points<=150)
        {
            img_badge.setBackgroundResource(R.drawable.gold_badge);
        }else {
            img_badge.setBackgroundResource(R.drawable.diamond_badge);
        }
    }
    private void selectImage() {
        final CharSequence[] items = {getResources().getString(R.string.takephoto), getResources().getString(R.string.choosefromlib), getResources().getString(R.string.cancel)};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProfileScreen.this);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data, null, true);
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


        mProfilePic.setImageURI(data.getData());
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
            if (ContextCompat.checkSelfPermission(ProfileScreen.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileScreen.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ProfileScreen.this);
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
                    AlertDialog alert = alertBuilder.create();
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
            if (ContextCompat.checkSelfPermission(ProfileScreen.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileScreen.this, android.Manifest.permission.CAMERA)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ProfileScreen.this);
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
                    AlertDialog alert = alertBuilder.create();
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


                thumbnail = BitmapFactory.decodeFile(imageFilePath);
                mProfilePic.setImageBitmap(bitmap);


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

                uploadImageTask(bytes.toByteArray());
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

    public void uploadImageTask(byte[] data) {
        mProgressDialog.setMessage("Uploading Profile Picture");
        mProgressDialog.show();
        mStorageReference = mStorageReference.child(Constants.DB_PATH+"/ProfilePictures/" + mSession.getRegID() + ".jpg");
        UploadTask uploadTask = mStorageReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                final Uri downloadUrl = taskSnapshot.getDownloadUrl();

                reference=  MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH +"/Users/"+mSession.getRegID());

                Map<String,Object> taskMap = new HashMap<String,Object>();
                taskMap.put("profile_url", downloadUrl.toString());
                reference.updateChildren(taskMap);

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot!=null)
                        {

                            Log.d("shri",dataSnapshot.getChildrenCount()+"");
                            UserDetailPojo pojo=dataSnapshot.getValue(UserDetailPojo.class);

                            if(pojo!=null)
                            {
                                Constants.showToast("Profile Pic Uploaded",ProfileScreen.this);
                                mProgressDialog.dismiss();
                                SessionManager mSessionManager = new SessionManager(ProfileScreen.this);
                                mSessionManager.createUserSession(pojo);

                                loadImage(ProfileScreen.this,mProfilePic,downloadUrl.toString());
                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


    }


    public static void loadImage(final Activity context, ImageView imageView, String url) {
        if (context == null || context.isDestroyed()) return;

        //placeHolderUrl=R.drawable.ic_user;
        //errorImageUrl=R.drawable.ic_error;
        Glide.with(context) //passing context
                .load(url) //passing your url to load image.
                .placeholder(R.drawable.avatar) //this would be your default image (like default profile or logo etc). it would be loaded at initial time and it will replace with your loaded image once glide successfully load image using url.
                .error(R.drawable.ic_warning)//in case of any glide exception or not able to download then this image will be appear . if you won't mention this error() then nothing to worry placeHolder image would be remain as it is.
                .diskCacheStrategy(DiskCacheStrategy.ALL) //using to load into cache then second time it will load fast.
                .animate(R.anim.fade_in) // when image (url) will be loaded by glide then this face in animation help to replace url image in the place of placeHolder (default) image.
                .fitCenter()//this method help to fit image into center of your ImageView
                .into(imageView); //pass imageView reference to appear the image.
    }


    public void getLatestData()
    {
        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH + "/Users/" + mSession.getRegID()).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot != null) {

                 UserDetailPojo pojo = dataSnapshot.getValue(UserDetailPojo.class);

                if (pojo != null) {
                    SessionManager mSessionManager = new SessionManager(ProfileScreen.this);
                    mSessionManager.createUserSession(pojo);
                    mPoints.setText("Points : "+mSession.getPoints()+"");
                    checkBadge(mSession.getPoints());

                }

            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
    }

}
