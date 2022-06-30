package com.lbs.patpat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.gzuliyujiang.imagepicker.ActivityBuilder;
import com.github.gzuliyujiang.imagepicker.CropImageView;
import com.github.gzuliyujiang.imagepicker.ImagePicker;
import com.github.gzuliyujiang.imagepicker.PickCallback;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lbs.patpat.databinding.ActivityPersonalBinding;
import com.lbs.patpat.fragment.PersonalAboutFragment;
import com.lbs.patpat.fragment.PersonalPublishFragment.PersonalPublishFragment;
import com.lbs.patpat.global.MyActivity;
import com.lbs.patpat.global.MyApplication;
import com.lbs.patpat.ui.login_register.LoginedUser;
import com.lbs.patpat.viewmodel.FollowAndFanViewModel;
import com.lbs.patpat.viewmodel.UserViewModel;
import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.ZoomMediaLoader;
import com.previewlibrary.enitity.IThumbViewInfo;
import com.previewlibrary.loader.IZoomMediaLoader;
import com.previewlibrary.loader.MySimpleTarget;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class PersonalActivity extends MyActivity implements View.OnClickListener {

    List<UserViewInfo> imgList;
    private ActivityPersonalBinding binding;
    private String[] tabAll;
    //当前uid
    private String currentUserId;


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPersonalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        currentUserId = getIntent().getStringExtra("uid");
        ZoomMediaLoader.getInstance().init(new ImagePreviewLoader());
        imgList = new ArrayList<>();
        initTabAndPager();
        initClick();

        //大头像显示


        //判断传入的用户
        if (currentUserId.equals(MainActivity.getUid())) {
            binding.modifyInfo.setVisibility(View.VISIBLE);
            binding.followCurrentUser.setVisibility(View.GONE);
            binding.personalBaseInfo.setEnabled(true);

            //登录用户ViewModel
            UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
            userViewModel.getLoginedUser().observe(this, new Observer<List<LoginedUser>>() {
                @Override
                public void onChanged(List<LoginedUser> loginedUsers) {

                    if (loginedUsers.isEmpty()) {
                        int id = R.drawable.icon_default;
                        Resources resources = getApplicationContext().getResources();
                        String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                                + resources.getResourcePackageName(id) + "/"
                                + resources.getResourceTypeName(id) + "/"
                                + resources.getResourceEntryName(id);
                        String url = Uri.parse(path).toString();
                        imgList.add(new UserViewInfo(url));
                        return;
                    }
                    if (loginedUsers.get(0).avatar.equals("null")) {
                        int id = R.drawable.icon_default;
                        Resources resources = getApplicationContext().getResources();
                        String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                                + resources.getResourcePackageName(id) + "/"
                                + resources.getResourceTypeName(id) + "/"
                                + resources.getResourceEntryName(id);
                        String url = Uri.parse(path).toString();
                        imgList.clear();
                        imgList.add(new UserViewInfo(url));
                        binding.personalIcon.setImageDrawable(getDrawable(R.drawable.icon_default));
                    } else {
                        String url = getString(R.string.server_ip) + loginedUsers.get(0).avatar;
                        Glide.with(PersonalActivity.this)
                                .load(url)
                                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                .into(binding.personalIcon);
                        imgList.clear();
                        imgList.add(new UserViewInfo(url));
                    }

                    if (loginedUsers.get(0).background.equals("null")) {
                        binding.personalBaseInfo.setBackground(getDrawable(R.drawable.drawer_background_custom));
                        binding.personalBaseInfo.getBackground().setAlpha(230);
                    } else
                        Glide.with(PersonalActivity.this)
                                .load(getString(R.string.server_ip) + loginedUsers.get(0).background)
                                .skipMemoryCache(true)//跳过内存缓存
                                .diskCacheStrategy(DiskCacheStrategy.NONE)//不要在disk硬盘缓存
                                .into(new AdaptiveBackground(binding.personalBaseInfo));

                    if (loginedUsers.get(0).nickname.equals("null"))
                        binding.personalNickname.setText("暂无昵称");
                    else
                        binding.personalNickname.setText(loginedUsers.get(0).nickname);
                    binding.personalFollowNum.setText("关注" + String.valueOf(loginedUsers.get(0).followNum));
                    binding.personalFanNum.setText("粉丝" + String.valueOf(loginedUsers.get(0).fansNum));
                    if (loginedUsers.get(0).intro.equals("null"))
                        binding.personalIntro.setText("这个人很懒，什么都没有写");
                    else
                        binding.personalIntro.setText(loginedUsers.get(0).intro);

                }

            });
        } else {
            binding.modifyInfo.setVisibility(View.GONE);
            binding.followCurrentUser.setVisibility(View.VISIBLE);
            binding.personalBaseInfo.setEnabled(false);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String searchUrl = getString(R.string.server_ip) + "/user/" + currentUserId;
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(searchUrl)
                            .build();
                    Response response = null;
                    try {
                        response = client.newCall(request).execute();
                        String responseBody = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseBody);
                        JSONObject data = jsonObject.getJSONObject("data");


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (data.getString("avatar").equals("null")) {
                                        int id = R.drawable.icon_default;
                                        Resources resources = getApplicationContext().getResources();
                                        String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                                                + resources.getResourcePackageName(id) + "/"
                                                + resources.getResourceTypeName(id) + "/"
                                                + resources.getResourceEntryName(id);
                                        String url = Uri.parse(path).toString();
                                        imgList.clear();
                                        imgList.add(new UserViewInfo(url));
                                        binding.personalIcon.setImageDrawable(getDrawable(R.drawable.icon_default));
                                    } else {
                                        String url = getString(R.string.server_ip) + data.getString("avatar");
                                        Glide.with(PersonalActivity.this)
                                                .load(url)
                                                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                                .into(binding.personalIcon);
                                        imgList.clear();
                                        imgList.add(new UserViewInfo(url));
                                    }

                                    if (data.getString("background").equals("null")) {
                                        binding.personalBaseInfo.setBackground(getDrawable(R.drawable.drawer_background_custom));
                                        binding.personalBaseInfo.getBackground().setAlpha(230);
                                    } else
                                        Glide.with(PersonalActivity.this)
                                                .load(getString(R.string.server_ip) + data.getString("background"))
                                                .skipMemoryCache(true)//跳过内存缓存
                                                .diskCacheStrategy(DiskCacheStrategy.NONE)//不要在disk硬盘缓存
                                                .into(new AdaptiveBackground(binding.personalBaseInfo));


                                    if (data.getString("nickname").equals("null"))
                                        binding.personalNickname.setText("暂无昵称");
                                    else
                                        binding.personalNickname.setText(data.getString("nickname"));

                                    binding.personalFollowNum.setText("关注" + data.getString("followNum"));
                                    binding.personalFanNum.setText("粉丝" + data.getString("fansNum"));
                                    if (data.getString("intro").equals("null"))
                                        binding.personalIntro.setText("这个人很懒，什么都没有写");
                                    else
                                        binding.personalIntro.setText(data.getString("intro"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    private void initTabAndPager() {
        tabAll = new String[]{"发布", "关于"};
        binding.personalPager.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        return PersonalPublishFragment.newInstance(currentUserId);
                    case 1:
                        return new PersonalAboutFragment(currentUserId);
                    default:
                        break;
                }
                return null;
            }

            @Override
            public int getItemCount() {
                return tabAll.length;
            }
        });

        new TabLayoutMediator(binding.personalTabAll, binding.personalPager, ((tab, position) -> {
            tab.setText(tabAll[position]);
        })).attach();
    }

    private void initClick() {
        binding.personalIcon.setOnClickListener(this);
        binding.personalReturn.setOnClickListener(this);
        binding.personalFollowNum.setOnClickListener(this);
        binding.personalFanNum.setOnClickListener(this);
        binding.modifyInfo.setOnClickListener(this);
        binding.followCurrentUser.setOnClickListener(this);
        binding.personalBaseInfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personal_return:
                finish();
                break;
            case R.id.personal_follow_num:

            case R.id.personal_fan_num:
                Intent intent = new Intent(PersonalActivity.this, FollowAndFansActivity.class);
                //这里要放当前用户id
                intent.putExtra("uid", MainActivity.getUid());
                startActivity(intent);
                break;
            case R.id.modifyInfo:
                startActivity(new Intent(PersonalActivity.this, ModifyInfoActivity.class));
                break;
            case R.id.personal_base_info:
                onGallery(binding.personalBaseInfo);
                break;
            case R.id.personal_icon:
                GPreviewBuilder.from(PersonalActivity.this)
                        .setData(imgList)  //数据
                        .setCurrentIndex(0)  //图片下标
                        .setSingleFling(true)  //是否在黑屏区域点击返回
                        .setDrag(false)  //是否禁用图片拖拽返回
                        .setType(GPreviewBuilder.IndicatorType.Dot)  //指示器类型
                        .start();  //启动
                break;
            case R.id.follow_current_user:
                followUser();
                break;

            default:
                break;
        }
    }

    private void followUser() {
        if(!MainActivity.getIsLogin()){
            Toast.makeText(getApplicationContext(),"请先登录",Toast.LENGTH_SHORT).show();
            return;
        }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = new RequestBody() {
                        @Nullable
                        @Override
                        public MediaType contentType() {
                            return null;
                        }

                        @Override
                        public void writeTo(@NonNull BufferedSink bufferedSink) throws IOException {

                        }
                    };
                    Request request = new Request.Builder()
                            .url(getString(R.string.server_ip)+"/user/concern?followedId="
                                    +currentUserId+"&followingId="+MainActivity.getUid())
                            .addHeader("token", MainActivity.getToken())
                            .post(body)
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                        String responseBody = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseBody);
                        if(jsonObject.getInt("code")==-1) {
                            Log.d("TEST", "返回-1");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "已经关注过了", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"成功关注",Toast.LENGTH_SHORT).show();
                                }
                            });

                        Log.d("TEST","follow"+responseBody);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
    }

    private final PickCallback cropCallback = new PickCallback() {
        @Override
        public void onPermissionDenied(String[] permissions, String message) {
            Toast.makeText(PersonalActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void cropConfig(ActivityBuilder builder) {
            builder.setMultiTouchEnabled(true)
                    .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setRequestedSize(500, 500)
                    .setFixAspectRatio(true)
                    .setAspectRatio(1, 1);
        }

        @Override
    public void onCropImage(@Nullable Uri imageUri) {
            Glide.with(PersonalActivity.this)
                    .load(imageUri)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    //.circleCrop()
                    .into(new AdaptiveBackground(binding.personalBaseInfo));

            new Thread(new Runnable() {     //改变背景
                @Override
                public void run() {
                    OkHttpClient client = new OkHttpClient();
                    File file = null;
                    try {
                        file = new File(new URI(imageUri.toString()));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    MultipartBody body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart(
                                    "filename",
                                    "image.png",
                                    MultipartBody.create(file, MediaType.parse("image/png"))
                            ).build();
                    Request request = new Request.Builder()
                            .url(getString(R.string.server_ip) + "/user/" + MainActivity.getUid() + "/background")
                            .addHeader("token", MainActivity.getToken())
                            .post(body)
                            .build();

                    Response response = null;


                    try {
                        response = client.newCall(request).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d("TEST", "run: failed");
                    }
                    Log.d("TEST", "image: " + response.isSuccessful());
                    request = new Request.Builder()
                            .url(getString(R.string.server_ip) + "/user/" + currentUserId + "/background")
                            .build();
                    try {
                        response = client.newCall(request).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String responseBody = null;
                    try {
                        responseBody = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject json = new JSONObject(responseBody);
                        String imgPath = json.getString("data");
                        LoginedUser update = MyApplication.getUserDatabase().userDao().getLoginUser().get(0).clone();
                        update.setBackground(imgPath);
                        MyApplication.getUserDatabase().userDao().deleteUser();
                        MyApplication.getUserDatabase().userDao().insertUser(update);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PersonalActivity.this, "成功更换背景", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }).start();



        }
    };

    public void onCamera(View view) {
        ImagePicker.getInstance().startCamera(this, true, cropCallback);
    }

    public void onGallery(View view) {
        ImagePicker.getInstance().startGallery(this, true, cropCallback);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImagePicker.getInstance().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ImagePicker.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    private void changeBackground() {

    }


}

