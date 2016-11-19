package com.main.schat.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.layout.khanguyen.simchat.CustomSetting;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.main.schat.activities.R;
import com.object.contain.khanguyen.simchat.Messaging;
import com.object.contain.khanguyen.simchat.User;
import com.socket.contain.khanguyen.simchat.Constants;
import com.state.SaveSharedPreference;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

/**
 * Created by kha on 30/10/2016.
 */

public class Tab4Fragment extends Fragment {

    private String[] arrLg;
    private String[] arrCl;
    private ArrayAdapter<String> mAdapter;
    private TextView usrName, phone;
    private String pathImage;
    Base64 base64;
    // ListView Setting
    private ListView mListView;
    private CustomSetting customst;
    private ImageView img_User_Icon;
    private String[] nameSettingArray = {"Đổi mật khẩu","Photos và Camera","Thay đổi ngôn ngữ","Đăng xuất"};
    private Integer[] imageArray =
            {
                    R.drawable.changepass,
                    R.drawable.gallary,
                    R.drawable.language,
                    R.drawable.logout
            };
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.tab4fragment, container, false);
        usrName = (TextView) rootView.findViewById(R.id.MyNameUser);
        phone = (TextView) rootView.findViewById(R.id.MyNumberPhone);
        usrName.setText(UiMychat.mUserName.toString());
        phone.setText(UiMychat.phone_current.toString());

        mListView = (ListView)rootView.findViewById(R.id.mListView);
        img_User_Icon = (ImageView) rootView.findViewById(R.id.profile_image);

        if(UiMychat.mImage_profile.equals("")){
            img_User_Icon.setImageResource(R.drawable.user);
            Log.d("//fdsfd","null");
        }else{
            byte[] hinhanh = base64.decode(UiMychat.mImage_profile,0);

            Bitmap bitmap = BitmapFactory.decodeByteArray(hinhanh,0,hinhanh.length);
            Log.d("//fdsfd","not null");
            img_User_Icon.setImageBitmap(bitmap);
        }



        customst = new CustomSetting(getActivity(),nameSettingArray,imageArray);
        mListView.setAdapter(customst);
        img_User_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();

            }
        });


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    MyDialogFragment myDialogFragment =  MyDialogFragment.newInstance("Đổi mật khẩu");
                    myDialogFragment.show(fragmentManager,"MyDialog");
                }
                if(i == 3) {
                    showAlertDialog();
                }
            }
        });
        return rootView;
    }
    private void choosePicture(){

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,321);
    }
    private void showAlertDialog() {

        FragmentManager fm = getActivity().getSupportFragmentManager();

        MyAlertDialogFragment alertDialog = MyAlertDialogFragment.newInstance("Bạn muốn thoát chứ?");

        alertDialog.show(fm, "fragment_alert");

    }
    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 321 && resultCode == getActivity().RESULT_OK){
            try {
                Uri imageURI = data.getData();
                File file = new File(imageURI.getPath());
                pathImage = file.getAbsolutePath();
                InputStream is = getActivity().getContentResolver().openInputStream(imageURI);

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;

                Bitmap bm = BitmapFactory.decodeStream(is,null,options);
                bm = decodeUri(getActivity(),imageURI,25);

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    bm = directImage(bm,pathImage);
                }



                img_User_Icon.setImageBitmap(bm);
                byte[] bytes = getByteArrayFromBipmap(bm);
                String img = base64.encodeToString(bytes,0);
                User user = new User();
                user.setUser_name(UiMychat.mUserName);
                user.setPhone(UiMychat.phone_current);
                user.setSocketId(UiMychat.mSocketId);
                user.setImage(img);
                user.setStatus(true);
                JSONObject jsOb = new JSONObject();
                jsOb.put("phone",user.getPhone());
                jsOb.put("username",user.getUser_name());
                jsOb.put("socketid",user.getSocketId());
                jsOb.put("status",user.isStatus());
                jsOb.put("profile",user.getImage());
                mSocket.emit("client gui image dai dien", img, jsOb);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public byte[] getByteArrayFromBipmap(Bitmap bitmap){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }



    // sua huong cua anh
    private Bitmap directImage(Bitmap bitmap, String photoPath) {
        ExifInterface ei = null;
        Bitmap bm = null;
        try {
            ei = new ExifInterface(photoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                bm = rotateImage(bitmap, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                bm = rotateImage(bitmap, 180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                bm = rotateImage(bitmap, 270);
                break;
            case ExifInterface.ORIENTATION_NORMAL:
                bm = bitmap;
                break;
            default:
                bm = bitmap;
                break;
        }
        return bm;
    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix,
                true);
    }
    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth
                , height_tmp = o.outHeight;
        int scale = 1;

        while(true) {
            if(width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }
}