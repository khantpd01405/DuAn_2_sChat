package com.main.schat.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.layout.khanguyen.simchat.MessageAdapter;
import com.main.schat.activities.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.object.contain.khanguyen.simchat.Messaging;
import com.socket.contain.khanguyen.simchat.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kha on 20/10/2016.
 */

public class MainFragment_ChatRoom extends Fragment {

    private static final int REQUEST_LOGIN = 0;

    private static final int TYPING_TIMER_LENGTH = 600;
    private ImageButton mRecord_Button, mCapture_Button, mTakeImage_btn, mFun_Button;
    private ImageView testImage;

    private MediaRecorder myRecorder;
    private MediaPlayer myPlayer;
    private String outputFile = null;



    private RecyclerView mMessagesView;
    private EditText mInputMessageView;
    private List<Messaging> mMessages = new ArrayList<Messaging>();
    private RecyclerView.Adapter mAdapter;
    private boolean mTyping = false;
    private Handler mTypingHandler = new Handler();
    private String mUsername;
    private String mRoomName;
    String username_friend;
    private String imgPath;
    private String socketId_friend;

    public MainFragment_ChatRoom() {
        super();
    }


    public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;
    String mCurrentPhotoPath;
    //date time
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    String currentDateandTime = sdf.format(new Date());

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;
        if (context instanceof Activity){
            a =(Activity) context;


            mAdapter = new MessageAdapter(a, mMessages);
        }

    }
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);



        mUsername = UiMychat.mUserName;
        username_friend = getActivity().getIntent().getStringExtra("name");
        socketId_friend = getActivity().getIntent().getStringExtra("socketfriend");
        Toast.makeText(getActivity(), "id ban muon gui tin nhan "+ socketId_friend, Toast.LENGTH_SHORT).show();
        mRoomName = getActivity().getIntent().getStringExtra("roomName");
        username_friend = getActivity().getIntent().getStringExtra("usrname");
        Toast.makeText(getActivity(),mUsername.toString(),Toast.LENGTH_SHORT).show();
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("new message", onNewMessage);
        mSocket.on("new record", onNewRecord);
        mSocket.on("new image", onNewImage);
        mSocket.on("user joined", onUserJoined);
        mSocket.on("user left", onUserLeft);
        mSocket.on("typing", onTyping);
        mSocket.on("stop typing", onStopTyping);
//        startSignIn();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment, container, false);
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.emit("disconnect room", mRoomName);
        mSocket.emit("disconnectUser", mUsername);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("new message", onNewMessage);
        mSocket.off("user joined", onUserJoined);
        mSocket.off("new record", onNewRecord);
        mSocket.off("new image", onNewImage);
        mSocket.off("user left", onUserLeft);
        mSocket.off("typing", onTyping);
        mSocket.off("stop typing", onStopTyping);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // button
        mRecord_Button = (ImageButton) view.findViewById(R.id.record_chat);
        mCapture_Button = (ImageButton) view.findViewById(R.id.btn_capture);
        mTakeImage_btn = (ImageButton) view.findViewById(R.id.btn_take_image);
        //tool bar

        Toolbar myToolbar = (Toolbar) view.findViewById(R.id.m_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(username_friend);



        mMessagesView = (RecyclerView) view.findViewById(R.id.recycle_messages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMessagesView.setLayoutManager(layoutManager);


        addLog(getResources().getString(R.string.message_welcome));


        mMessagesView.setAdapter(mAdapter);


        mInputMessageView = (EditText) view.findViewById(R.id.message_input);
        mInputMessageView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if (id == R.id.send || id == EditorInfo.IME_NULL) {
                    attemptSend();
                    return true;
                }
                return false;
            }
        });
        mInputMessageView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (null == mUsername) return;
                if (!mSocket.connected()) return;

                if (!mTyping) {
                    mTyping = true;
                    mSocket.emit("typing", socketId_friend);
                    mSocket.emit("typing all room");
                }

                mTypingHandler.removeCallbacks(onTypingTimeout);
                mTypingHandler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ImageButton sendButton = (ImageButton) view.findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSend();
            }
        });

        // xu ly button gui hinh anh
//        mCapture_Button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendImage();
//
//            }
//        });

        //xu ly button Record
        addEventButton();
        mRecord_Button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    start(v);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    stop(v);
                    String path = outputFile = Environment.getExternalStorageDirectory().
                            getAbsolutePath() + "/schat.3gpp";
                    byte[] amthanh = FileLocal_To_Byte(path);
                    mSocket.emit("client gui am thanh",amthanh,socketId_friend);
                }


                return false;
            }
        });


    }




    private void sendImage() {
        Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), android.R.drawable.ic_btn_speak_now);
        byte[] bytes = getByteArrayFromBipmap(bitmap);
        mSocket.emit("client gui image", bytes, socketId_friend);
    }

    public byte[] getByteArrayFromBipmap(Bitmap bitmap){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    // them su kien
    private void addEventButton(){
        mTakeImage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        mCapture_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    // chup anh
    private void takePicture(){

//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.TITLE, "New Picture");
//        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
//        Uri imageUri = getActivity().getContentResolver().insert(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);


        try {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            File file = new File(Environment.getExternalStorageDirectory()+ File.separator + "image.jpg");
//        Uri imageUri = Uri.fromFile(file);
            Uri imageUri = null;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                // Running on something older than API level 11, so disable
                // the drag/drop features that use ClipboardManager APIs
                imageUri = Uri.fromFile(createImageFile());
            }else{
                imageUri = FileProvider.getUriForFile(getActivity(),getActivity().getApplicationContext().getPackageName() + ".provider", createImageFile());
            }



            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
    // chon anh
    private void choosePicture(){

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,321);
    }
    // xu ly khi lay va chup anh
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE && resultCode == getActivity().RESULT_OK){

            Uri imageUri = Uri.parse(mCurrentPhotoPath);

            File file = new File(imageUri.getPath());
            byte[] bytes = new byte[0];

            Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 700);
            bytes = getByteArrayFromBipmap(directImage(bitmap, file.getAbsolutePath()));
            addImage("You",directImage(bitmap, file.getAbsolutePath()),Messaging.TYPE_IMAGE_USER);
            mSocket.emit("client gui image", bytes,socketId_friend);



            //            String imageurl = getRealPathFromURI(imageUri);
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");


        }else if(requestCode == 321 && resultCode == getActivity().RESULT_OK){
            try {


                Uri imageURI = data.getData();
                InputStream is = getActivity().getContentResolver().openInputStream(imageURI);

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;

                Bitmap bm = BitmapFactory.decodeStream(is,null,options);
                bm = resize(bm,1000,700);
                byte[] bytes = getByteArrayFromBipmap(bm);
                addImage("You",bm,Messaging.TYPE_IMAGE_USER);
                mSocket.emit("client gui image", bytes, socketId_friend);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
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
    // full size image
    private Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight)
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth)
        {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }


//    public String getRealPathFromURI(Uri contentUri) {
//        String[] proj = { MediaStore.Images.Media.DATA };
//        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
//        int column_index = cursor
//                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }

    //resize hinh anh giam kich thuoc size

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




    //    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (Activity.RESULT_OK != resultCode) {
//            getActivity().finish();
//            return;
//        }
//
//        mUsername = data.getStringExtra("username");
//        int numUsers = data.getIntExtra("numUsers", 1);
//
//        addLog(getResources().getString(R.string.message_welcome));
//        addParticipantsLog(numUsers);
//    }


    // chuyen file thanh byte
    public byte[] FileLocal_To_Byte(String path){

        File file = new File(path);

        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bytes;
    }





    private void addLog(String message) {
        mMessages.add(new Messaging.Builder(Messaging.TYPE_LOG)
                .message(message).build());

        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void addParticipantsLog(int numUsers) {
        addLog(getResources().getQuantityString(R.plurals.message_participants, numUsers, numUsers));
    }

    private void addMessage(String username, String message) {
        mMessages.add(new Messaging.Builder(Messaging.TYPE_MESSAGE)
                .username(username).message(message).datetime(currentDateandTime).build());
        Log.d("/////////////",currentDateandTime);
        mSocket.emit("update_message", username, message,socketId_friend);
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void addImage(String username, Bitmap bitmap, int type) {
        mMessages.add(new Messaging.Builder(type)
                .username(username).image(bitmap).build());
        Log.d("/////////////",currentDateandTime);
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void addMessage_user(String username, String message) {
        mMessages.add(new Messaging.Builder(Messaging.TYPE_MESSAGE_USER)
                .username(username).message(message).datetime(currentDateandTime).build());
        Log.d("/////////////",currentDateandTime);
        mSocket.emit("update_message", username, message,socketId_friend);
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }


    private void addTyping(String username) {
        mMessages.add(new Messaging.Builder(Messaging.TYPE_ACTION)
                .username(username).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void removeTyping(String username) {
        for (int i = mMessages.size() - 1; i >= 0; i--) {
            Messaging message = mMessages.get(i);
            if (message.getType() == Messaging.TYPE_ACTION && message.getUsername().equals(username)) {
                mMessages.remove(i);
                mAdapter.notifyItemRemoved(i);
            }
        }
    }

    // gui tin nhan
    private void attemptSend() {
        if (null == mUsername) return;
        if (!mSocket.connected()) return;

        mTyping = false;

        String message = mInputMessageView.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            mInputMessageView.requestFocus();
            return;
        }

        mInputMessageView.setText("");
        addMessage_user(mUsername, message);

        // perform the sending message attempt.
        mSocket.emit("new message", message, socketId_friend);
        mSocket.emit("new message all user", message);
    }




//    private void startSignIn() {
//        mUsername = null;
//        Intent intent = new Intent(getActivity(), LoginActivity.class);
//        startActivityForResult(intent, REQUEST_LOGIN);
//    }

//    private void leave() {
//        mUsername = null;
//        mSocket.disconnect();
//        mSocket.connect();
//        startSignIn();
//    }


    // xu ly cuon
    private void scrollToBottom() {
        mMessagesView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity().getApplicationContext(),
                            R.string.error_connect, Toast.LENGTH_LONG).show();
                }
            });
        }
    };


    // xu ly cac su kien tu server


    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }
//                    Toast.makeText(getActivity(),username +" muốn nhắn tin với bạn. vào chat ngay", Toast.LENGTH_SHORT).show();
                    removeTyping(username);
                    addMessage(username, message);
                }
            });
        }
    };

    private Emitter.Listener onNewRecord = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    byte[] amthanh;
                    try {
                        amthanh = (byte[]) data.get("record");
                        playMp3FromByte(amthanh);
                    } catch (JSONException e) {
                        return;
                    }


                }
            });
        }
    };


    private Emitter.Listener onNewImage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    byte[] hinhanh;
                    try {
                        username = data.getString("username");
                        hinhanh = (byte[]) data.get("image");
                        Bitmap bitmap = BitmapFactory.decodeByteArray(hinhanh,0,hinhanh.length);
                        addImage(username,bitmap,Messaging.TYPE_IMAGE_FRIEND);
                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };



    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String text;

                    int numUsers;
                    try {
                        text = data.getString("string");
                        username_friend = data.getString("username");

                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        return;
                    }


//                    Toast.makeText(getActivity(), text + " id couple", Toast.LENGTH_SHORT).show();
                    addLog(getResources().getString(R.string.message_user_joined, username_friend));
//                    addParticipantsLog(numUsers);
                }
            });
        }
    };




    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        return;
                    }

                    addLog(getResources().getString(R.string.message_user_left, username));
//                    addParticipantsLog(numUsers);
                    removeTyping(username);
                }
            });
        }
    };

    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        return;
                    }
                    addTyping(username);
                }
            });
        }
    };

    private Emitter.Listener onStopTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        return;
                    }
                    removeTyping(username);
                }
            });
        }
    };





    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {
            if (!mTyping) return;

            mTyping = false;
            mSocket.emit("stop typing",socketId_friend);
            mSocket.emit("stop typing all room");
        }
    };






    //xu ly ghi am
    public void start(View view){
        try {

            outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/schat.3gpp";
            myRecorder = new MediaRecorder();
            myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            myRecorder.setOutputFile(outputFile);

            myRecorder.prepare();
            myRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getActivity(), "Start recording...",
                Toast.LENGTH_SHORT).show();
    }


    public void stop(View view){
        try {
            myRecorder.stop();
            myRecorder.release();
            myRecorder  = null;

            Toast.makeText(getActivity(), "Stop recording...",
                    Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    // phat am thanh
    private void playMp3FromByte(byte[] mp3SoundByteArray) {
        try {

            File tempMp3 = File.createTempFile("kurchina", "mp3", getActivity().getCacheDir());
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(mp3SoundByteArray);
            fos.close();

            MediaPlayer mediaPlayer = new MediaPlayer();

            FileInputStream fis = new FileInputStream(tempMp3);
            mediaPlayer.setDataSource(fis.getFD());

            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException ex) {
            String s = ex.toString();
            ex.printStackTrace();
        }
    }




    // xu ly toolbar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.micro:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...

                return true;

            case android.R.id.home:
                getActivity().finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }



    }
}
