package utthamkumar.myparty.misc;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import utthamkumar.myparty.R;
import utthamkumar.myparty.session.SessionManager;

public class GiftFragmentDilaog extends DialogFragment {

    String url;
    int code;
    public GiftFragmentDilaog(String s,int code) {

        url=s;
        this.code=code;
    }

    public GiftFragmentDilaog() {
    }

    ImageView image_wish;

    @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.dialog_gift, container);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
           ImageView btnClose = (ImageView)view.findViewById(R.id.btn_close);
            image_wish=(ImageView)view.findViewById(R.id. image_wish);
           btnClose.setOnClickListener(onCloseClickListener());
           loadImage(url);


        }

        private View.OnClickListener onCloseClickListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // sessionManager.AddwishSession(code,true);
                    GiftFragmentDilaog.this.dismiss();
                }
            };
        }

        private View.OnClickListener onClickListener(final String msg) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }
            };
        }

        public void loadImage(String url)
        {

            Glide.with(getActivity()) //passing context
                    .load(url) //passing your url to load image.
                    .placeholder(R.drawable.avatar) //this would be your default image (like default profile or logo etc). it would be loaded at initial time and it will replace with your loaded image once glide successfully load image using url.
                    .error(R.drawable.ic_warning)//in case of any glide exception or not able to download then this image will be appear . if you won't mention this error() then nothing to worry placeHolder image would be remain as it is.
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //using to load into cache then second time it will load fast.
                    .animate(R.anim.fade_in) // when image (url) will be loaded by glide then this face in animation help to replace url image in the place of placeHolder (default) image.
                    .fitCenter()//this method help to fit image into center of your ImageView
                    .into(image_wish); //pass imageView reference to appear the image.

        }
    }

