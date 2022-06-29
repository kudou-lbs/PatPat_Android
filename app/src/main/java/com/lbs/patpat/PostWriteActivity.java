package com.lbs.patpat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lbs.patpat.databinding.ActivityPostWriteBinding;
import com.lbs.patpat.global.MyActivity;

import java.io.File;
import java.io.IOException;
import java.net.URI;

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
                openGallery(1);
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
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        try {
            ActivityCompat.requestPermissions(this, permissions,0x0010);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //调用选择图片
    private void openGallery(int type){
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(gallery, type);
    }
    //选择图片结果
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 这里没有判断是否匹配，data为空
        imgUri=data.getData();
        // 要查询的列字段名称
        String[] filePathColumns = {MediaStore.Images.Media.DATA};
        // 到数据库中查询 , 查询 _data 列字段信息
        Cursor cursor = getContentResolver().query(
                imgUri,
                filePathColumns,
                null,
                null,
                null);
        cursor.moveToFirst();
        // 获取 _data 列所在的列索引
        int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
        // 获取图片的存储路径
        filePath = cursor.getString(columnIndex);
        Log.d("lbssss",filePath);

        if(data!=null) Glide.with(this).load(data.getData()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.postWriteImg);
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
        if(filePath!=null){
            File file=new File(filePath);
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

    private void toast(String s){
        Toast.makeText(PostWriteActivity.this,s,Toast.LENGTH_SHORT).show();
    }
}