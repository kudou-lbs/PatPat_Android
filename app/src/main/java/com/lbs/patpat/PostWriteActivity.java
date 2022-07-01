package com.lbs.patpat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.gzuliyujiang.imagepicker.ActivityBuilder;
import com.github.gzuliyujiang.imagepicker.CropImageView;
import com.github.gzuliyujiang.imagepicker.ImagePicker;
import com.github.gzuliyujiang.imagepicker.PickCallback;
import com.github.gzuliyujiang.imagepicker.*;
import com.lbs.patpat.databinding.ActivityPostWriteBinding;
import com.lbs.patpat.global.MyActivity;
import com.lbs.patpat.global.MyApplication;
import com.lbs.patpat.ui.login_register.LoginedUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostWriteActivity extends MyActivity {

    ActivityPostWriteBinding binding;
    Uri imgUri;
    String filePath;
    String fid;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPostWriteBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        intiParam();
        initClick();
    }

    private void intiParam() {
        Intent intent=getIntent();
        fid=intent.getStringExtra("fid");

        handler=new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 0:
                        handler.removeCallbacksAndMessages(null);
                        toast("上传帖子成功");
                        finish();
                        break;
                    case 1:
                        toast("上传帖子失败");
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void initClick() {
        binding.postWriteImgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
                onGallery();
            }
        });
        binding.postWriteButtonPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postPost();
            }
        });
        binding.postWriteReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void requestPermission(){
        String permissions[] = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_MEDIA_LOCATION,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
        };

        try {
            ActivityCompat.requestPermissions(this, permissions,0x0010);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
//    //调用选择图片
//    private void openGallery(int type){
//        Intent gallery = new Intent(Intent.ACTION_PICK);
//        gallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        startActivityForResult(gallery, type);
//    }
    //选择图片结果
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 这里没有判断是否匹配，data为空
        if(data==null)return;
        imgUri=data.getData();
//        Log.d("TEST", "onActivityResult: "+imgUri);
//        // 要查询的列字段名称
//        String[] filePathColumns = {MediaStore.Images.Media.DOCUMENT_ID,MediaStore.Images.Media.DATA};
//
//        // 到数据库中查询 , 查询 _data 列字段信息
//        Cursor cursor = getContentResolver().query(
//                imgUri,
//                filePathColumns,
//                null,
//                null,
//                null);
//        cursor.moveToFirst();
//        // 获取 _data 列所在的列索引
//        int columnIndex=-1;
//        for (String col : filePathColumns){
//            columnIndex = cursor.getColumnIndex(col);
//            if(cursor.getString(columnIndex)!=null){
//                filePath = cursor.getString(columnIndex);
//            }
//
//        }

        // 获取图片的存储路径
        //filePath = cursor.getString(columnIndex);
        //Log.d("TEST", String.valueOf(columnIndex)+"onActivityResult: filepath "+filePath);




        if(data!=null)
            Glide.with(this)
                    .load(data.getData())
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(binding.postWriteImg);
    }

    //上传帖子
    private void postPost(){
        String title=binding.postWriteEditTitle.getText().toString();
        String text=binding.postWriteEditText.getText().toString();
        if(title.trim().length()==0){
            toast("请输入标题");
            return;
        }else if(text.trim().length()==0){
            toast("请输入正文");
            return;
        }
        String url=getString(R.string.server_ip)
                +"/post?fid="+fid
                +"&uid="+MainActivity.getUid()
                
                +"&title="+title
                +"&content="+text;
        Log.d("lbs",url);

        Request request;
        if(imgUri!=null){
            File file= null;
                //file = new File(imgUri.toString());
            try {
                file = getFile(getApplicationContext(),imgUri);
                Log.d("TEST", "File name:"+file.canRead());
            } catch (IOException e) {
                e.printStackTrace();
            }


            MultipartBody body=new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                            "image",
                            "image.png",
                            MultipartBody.create(file,MediaType.parse("image/png"))
                            ).build();
            Log.d("lbssss",body.toString());
            request=new Request.Builder()
                    .url(url)
                    .addHeader("token",MainActivity.getToken())
                    .post(body)
                    .build();
        }else{
            FormBody body=new FormBody.Builder().build();
            request=new Request.Builder()
                    .url(url)
                    .addHeader("token",MainActivity.getToken())
                    .post(body)
                    .build();
        }
        OkHttpClient client = new OkHttpClient();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Message message=new Message();
                message.what=1;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Message message=new Message();
                message.what=0;
                handler.sendMessage(message);
            }
        });
    }

    private final PickCallback cropCallback = new PickCallback() {
        @Override
        public void onPermissionDenied(String[] permissions, String message) {
            Toast.makeText(PostWriteActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void cropConfig(ActivityBuilder builder) {
            builder.setMultiTouchEnabled(true)
                    .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setRequestedSize(0, 0)
                    .setOutputCompressQuality(90)
                    .setFixAspectRatio(true)
                    .setAspectRatio(1, 1);
        }

        @Override
        public void onCropImage(@Nullable Uri imageUri) {
            Glide.with(PostWriteActivity.this)
                    .load(imageUri)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    //.circleCrop()
                    .into(binding.postWriteImg);

            filePath = imageUri.toString();

        }
    };

    public void onCamera(View view) {
        ImagePicker.getInstance().startCamera(this, true, cropCallback);
    }

    public void onGallery() {
        ImagePicker.getInstance().startGallery(this, false, cropCallback);
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        //ImagePicker.getInstance().onActivityResult(this, requestCode, resultCode, data);
//
//        imgUri = data.getData();
//        filePath = "data.getData().toString()";
//
////
//        if(data!=null)
//            Glide.with(this)
//                    .load(data.getData())
//                    .skipMemoryCache(true)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .into(binding.postWriteImg);
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ImagePicker.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    private void toast(String s){
        Toast.makeText(PostWriteActivity.this,s,Toast.LENGTH_SHORT).show();
    }

    public static File getFile(Context context, Uri uri) throws IOException {
        File destinationFilename = new File(context.getFilesDir().getPath() + File.separatorChar + queryName(context, uri));
        try (InputStream ins = context.getContentResolver().openInputStream(uri)) {
            createFileFromStream(ins, destinationFilename);
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
        return destinationFilename;
    }

    public static void createFileFromStream(InputStream ins, File destination) {
        try (OutputStream os = new FileOutputStream(destination)) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = ins.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static String queryName(Context context, Uri uri) {
        Cursor returnCursor =
                context.getContentResolver().query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }
}