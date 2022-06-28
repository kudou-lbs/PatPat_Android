package com.lbs.patpat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lbs.patpat.databinding.ActivityPostWriteBinding;
import com.lbs.patpat.global.MyActivity;

public class PostWriteActivity extends MyActivity {

    ActivityPostWriteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPostWriteBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        binding.postWriteImgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(1);
            }
        });
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
        if(data!=null) Glide.with(this).load(data.getData()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.postWriteImg);
    }
}