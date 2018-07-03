package sree.myparty.misc;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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
import sree.myparty.R;
import sree.myparty.utils.Constants;

public class PostNews extends AppCompatActivity {
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    File GlobalImagePath = null;
    ByteArrayOutputStream GLOBALbytes = null;
    String imageFilePath;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;


    @BindView(R.id.btn_news_uploadphoto)
    Button uploadImage;

    @BindView(R.id.news_image)
    ImageView img_thumbnail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post_news);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_news_uploadphoto)
    public void onButtonClick(View v) {
        selectImage();
    }

    private void selectImage() {
        final CharSequence[] items = {"TAKE PICTURE", "GALLERY","CANCEL"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PostNews.this);
        // builder.setTitle(getResources().getString(R.string.pleaseataachsurveyimage));
        // builder.setCustomTitle(R.layout.custom_date_layout)
       /* View view = LayoutInflater.from(this).inflate(R.layout.titlebar, null);
        builder.setCustomTitle(view);*/
        //builder.setMessage(getResources().getString(R.string.pleaseataachsurveyimage));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals(getResources().getString(R.string.takephoto))) {
                    boolean result = checkPermission();
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
        // intent.setAction(Intent.ACTION_GET_CONTENT);//
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
                    pictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
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

            else if (requestCode == REQUEST_CAMERA)


                onCaptureImageResult(data, null, true);

        }

    }
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                onCaptureImageResult(null, bm, false);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Toast.makeText(getActivity(),data.getData().getPath().toString(),Toast.LENGTH_SHORT).show();
        // tvImageName.setText(data.getData().getPath().toString());

        //ivImage.setImageBitmap(bm);
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
            if (ContextCompat.checkSelfPermission(PostNews.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(PostNews.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PostNews.this);
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

    private void onCaptureImageResult(Intent data, Bitmap bitmap, boolean fromCamera) {
        Bitmap thumbnail = null;

        if (fromCamera) {

            try {


                thumbnail = BitmapFactory.decodeFile(imageFilePath);

                thumbnail=rotateImageIfRequired(thumbnail, Uri.parse(imageFilePath));

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



            } catch (Exception e) {
                // Toast.makeText(getActivity(),"Please select appropriate image",Toast.LENGTH_SHORT).show();
               /* showSignupResultDialog(
                        getResources().getString(R.string.app_name),
                        getResources().getString(R.string.pleaseSelectAppropriateImage),
                        getResources().getString(R.string.Ok));
*/

        /*File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
*/

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
    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage)  {

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
}
