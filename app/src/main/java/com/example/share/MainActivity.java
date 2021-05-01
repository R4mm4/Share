package com.example.share;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Share;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_VIDEO_CODE = 1000;
    Button btnCompartirLink,btnCompartirFoto,btnCompartirVideo;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            SharePhoto sharePhoto = new SharePhoto.Builder().setBitmap(bitmap).build();
            if (ShareDialog.canShow(SharePhotoContent.class)){
                SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(sharePhoto).build();
                shareDialog.show(content);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);
        btnCompartirLink=(Button)findViewById(R.id.btnCompartiLink);
        btnCompartirFoto=(Button)findViewById(R.id.btnCompartirFoto);
        btnCompartirVideo=(Button)findViewById(R.id.btnCompartirVideo);

        callbackManager=CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        btnCompartirLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(MainActivity.this,"Se ha compartido de forma correcta",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(MainActivity.this,"Se ha canselado",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
                ShareLinkContent linkContent = new ShareLinkContent.Builder().setQuote("Se compartio el link")
                        .setContentUrl(Uri.parse("http://youtube.com"))
                        .build();
                if(ShareDialog.canShow(ShareLinkContent.class)){
                    shareDialog.show(linkContent);
                }
            }
        });
        btnCompartirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(MainActivity.this,"Se ha compartido de forma correcta",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(MainActivity.this,"Se ha canselado",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });

                Picasso.with(getBaseContext()).load("https://comicvine1.cbsistatic.com/uploads/scale_super/11118/111187046/5661781-5629844810-gravi.jpg")
                        .into(target);
            }
        });
        btnCompartirVideo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Seleccionar Video"),REQUEST_VIDEO_CODE);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_VIDEO_CODE) {
                Uri selectedVideo = data.getData();
                ShareVideo video = new ShareVideo.Builder()
                        .setLocalUrl(selectedVideo)
                        .build();
                ShareVideoContent videoContent = new ShareVideoContent.Builder()
                        .setContentTitle("Este es un video")
                        .setContentDescription("Un video mas")
                        .setVideo(video)
                        .build();
                if (ShareDialog.canShow(ShareVideoContent.class)) {
                    shareDialog.show(videoContent);
                }
            }
        }
    }
    private void clave(){
        try{
            PackageInfo info = getPackageManager().getPackageInfo("com.example.share",
                    PackageManager.GET_SIGNATURES);
            for(Signature signature:info.signatures){
                MessageDigest md =MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }
}
