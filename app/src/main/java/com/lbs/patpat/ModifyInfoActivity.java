package com.lbs.patpat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.github.gzuliyujiang.wheelpicker.AddressPicker;
import com.github.gzuliyujiang.wheelpicker.BirthdayPicker;
import com.github.gzuliyujiang.wheelpicker.DatePicker;
import com.github.gzuliyujiang.wheelpicker.annotation.AddressMode;
import com.github.gzuliyujiang.wheelpicker.annotation.DateMode;
import com.github.gzuliyujiang.wheelpicker.contract.OnAddressPickedListener;
import com.github.gzuliyujiang.wheelpicker.contract.OnDatePickedListener;
import com.github.gzuliyujiang.wheelpicker.contract.OnOptionPickedListener;
import com.github.gzuliyujiang.wheelpicker.entity.CityEntity;
import com.github.gzuliyujiang.wheelpicker.entity.CountyEntity;
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity;
import com.github.gzuliyujiang.wheelpicker.entity.ProvinceEntity;
import com.github.gzuliyujiang.wheelpicker.entity.SexEntity;
import com.github.gzuliyujiang.wheelpicker.widget.DateWheelLayout;
import com.github.gzuliyujiang.wheelpicker.widget.LinkageWheelLayout;
import com.github.gzuliyujiang.wheelpicker.widget.OptionWheelLayout;
import com.lbs.patpat.databinding.ActivityModifyInfoBinding;
import com.github.gzuliyujiang.wheelpicker.SexPicker;
import com.lbs.patpat.global.MyActivity;
import com.lbs.patpat.global.MyApplication;
import com.lbs.patpat.ui.login_register.LoginedUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ModifyInfoActivity extends MyActivity {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    final String TAG = "TEST";
    Handler handler;
    private DatePicker datePicker;
    private ActivityModifyInfoBinding binding;
    private SexPicker sexPicker;
    private AddressPicker addressPicker;
    private JSONObject infoJSON;
    private String localImg;
    private String errMessage;
    private Thread sendData;
    private  LoginedUser loginedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModifyInfoBinding.inflate(getLayoutInflater());
        loginedUser = MainActivity.getLoginedUser();
        setContentView(binding.getRoot());
        sexPicker = new SexPicker(this);
        addressPicker = new AddressPicker(this);
        addressPicker.setAddressMode(AddressMode.PROVINCE_CITY);
        infoJSON = new JSONObject();
        loadData();     //加载已有数据
        initView();
        initListener();
        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 0:
                        handler.removeCallbacksAndMessages(null);
                        Toast.makeText(ModifyInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case 1:
                        Toast.makeText(ModifyInfoActivity.this, errMessage, Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };

    }

    private void loadData() {
        //binding.gender.setText("男");
        binding.address.setText("广东省广州市");
        binding.birthDay.setText("2001-6-6");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String searchUrl = getString(R.string.server_ip) + "/user/" + MainActivity.getUid();
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
                            if (!data.getString("avatar").equals("null")) {
                                String url = getString(R.string.server_ip) + data.getString("avatar");
                                Glide.with(ModifyInfoActivity.this)
                                        .load(url)
                                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                        .into(binding.modifyAvatar);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            if (!data.getString("nickname").equals("null"))
                                    binding.editNickName.setText(data.getString("nickname"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            if (!data.getString("intro").equals("null"))
                                    binding.editIntro.setText(data.getString("intro"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (data.getInt("gender") != 0) {
                                        if (data.getInt("gender") == 1) {
                                            binding.gender.setText("男");
                                            Log.d(TAG, "load: 男");
                                        }
                                        else {
                                            binding.gender.setText("女");
                                            Log.d(TAG, "load: 女");
                                        }
                                    }
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



        //地址和生日未添加

    }

    private void initView() {
        //binding.datepicker.setVisibility(View.GONE);

        //生日选择
        datePicker = new DatePicker(this);
        datePicker.setBodyWidth(350);
        DateWheelLayout dateWheelLayout = datePicker.getWheelLayout();
        dateWheelLayout.setDateMode(DateMode.YEAR_MONTH_DAY);
        dateWheelLayout.setDateLabel("年", "月", "日");
        dateWheelLayout.setRange(DateEntity.target(1950, 1, 1), DateEntity.today(), DateEntity.target(2000, 1, 1));
        dateWheelLayout.setTextColor(getColor(R.color.grid_text_color));
        dateWheelLayout.setIndicatorColor(getColor(R.color.patpat));
        dateWheelLayout.setIndicatorSize(dateWheelLayout.getResources().getDisplayMetrics().density);
        dateWheelLayout.setSelectedTextBold(true);
        dateWheelLayout.setSelectedTextSize((float) 55);
        dateWheelLayout.setSelectedTextColor(getColor(R.color.patpat));
        dateWheelLayout.getYearLabelView().setTextColor(getColor(R.color.black));
        dateWheelLayout.getMonthLabelView().setTextColor(getColor(R.color.black));
        dateWheelLayout.getDayLabelView().setTextColor(getColor(R.color.black));
        dateWheelLayout.setResetWhenLinkage(false);

        LinkageWheelLayout addressWheelLayout = addressPicker.getWheelLayout();
        addressWheelLayout.setTextColor(getColor(R.color.grid_text_color));
        addressWheelLayout.setIndicatorColor(getColor(R.color.patpat));
        addressWheelLayout.setIndicatorSize(addressWheelLayout.getResources().getDisplayMetrics().density);
        addressWheelLayout.setSelectedTextBold(true);
        addressWheelLayout.setSelectedTextSize((float) 55);
        addressWheelLayout.setSelectedTextColor(getColor(R.color.patpat));

        OptionWheelLayout sexWheelLayout = sexPicker.getWheelLayout();
        sexWheelLayout.setTextColor(getColor(R.color.grid_text_color));
        sexWheelLayout.setIndicatorColor(getColor(R.color.patpat));
        sexWheelLayout.setIndicatorSize(sexWheelLayout.getResources().getDisplayMetrics().density);
        sexWheelLayout.setSelectedTextBold(true);
        sexWheelLayout.setSelectedTextSize((float) 55);
        sexWheelLayout.setSelectedTextColor(getColor(R.color.patpat));


    }

    private void initListener() {
        binding.editIntro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    infoJSON.put("intro",binding.editIntro.getText().toString());
                    loginedUser.setIntro(binding.editIntro.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        binding.editNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    infoJSON.put("nickname",binding.editNickName.getText());
                    loginedUser.setNickname(binding.editNickName.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        //性别
        sexPicker.setOnOptionPickedListener(new OnOptionPickedListener() {
            @Override
            public void onOptionPicked(int position, Object item) {
                SexEntity entity = (SexEntity) item;
                binding.gender.setText(entity.getName());

                try {
                    if (entity.getName().equals("男")) {
                        infoJSON.put("gender", 1);
                        loginedUser.setGender(1);
                        Log.d(TAG, "onOptionPicked: "+"男");
                    }

                    else {
                        infoJSON.put("gender", 2);
                        loginedUser.setGender(2);
                        Log.d(TAG, "onOptionPicked: "+"女");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        //地址
        addressPicker.setOnAddressPickedListener(new OnAddressPickedListener() {
            @Override
            public void onAddressPicked(ProvinceEntity province, CityEntity city, CountyEntity county) {
                String address = province.getName() + city.getName();
                binding.address.setText(address);
                try {
                    infoJSON.put("address", address);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        //出生日期
        datePicker.setOnDatePickedListener(new OnDatePickedListener() {
            @Override
            public void onDatePicked(int year, int month, int day) {
                String date = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
                binding.birthDay.setText(date);
                try {
                    infoJSON.put("birthday", date);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        binding.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addressPicker.show();
            }
        });
        binding.gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sexPicker.show();
            }
        });
        binding.birthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show();
            }
        });

        binding.modifyAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifyAvatar();
            }
        });
        binding.saveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInfo();
            }
        });

        binding.goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    private void modifyAvatar() {
        requestPermission();
        openGallery(1);
    }


    private void requestPermission() {
        String permissions[] = {
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        try {
            ActivityCompat.requestPermissions(this, permissions, 0x0010);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //调用选择图片
    private void openGallery(int type) {
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(gallery, type);
    }

    //选择图片结果
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 这里没有判断是否匹配，data为空
        if (data != null)
            Glide.with(this)
                    .load(data.getData())
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .circleCrop()
                    .into(binding.modifyAvatar);

        // 要查询的列字段名称
        String[] filePathColumns = {MediaStore.Images.Media.DATA};
        // 到数据库中查询 , 查询 _data 列字段信息
        Cursor cursor = getContentResolver().query(
                data.getData(),
                filePathColumns,
                null,
                null,
                null);
        cursor.moveToFirst();
        // 获取 _data 列所在的列索引
        int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
        // 获取图片的存储路径
        localImg = cursor.getString(columnIndex);
    }

    private void saveInfo() {
        sendData = new Thread(new Runnable() {
            @Override
            public void run() {
                //OkHttpClient client = new OkHttpClient();
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(1500, TimeUnit.MILLISECONDS)
                        .readTimeout(1000, TimeUnit.MILLISECONDS)
                        .writeTimeout(1000, TimeUnit.MILLISECONDS)
                        .build();

                Log.d(TAG, "123456");
                try {
                    if (localImg != null) {
                        File file = new File(localImg);

                        MultipartBody body = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart(
                                        "filename",
                                        "image.png",
                                        MultipartBody.create(file, MediaType.parse("image/png"))
                                ).build();
                        Request request = new Request.Builder()
                                .url(getString(R.string.server_ip) + "/user/" + MainActivity.getUid() + "/avatar")
                                .addHeader("token", MainActivity.getToken())
                                .post(body)
                                .build();

                        Response response = null;


                        response = client.newCall(request).execute();
                        Log.d(TAG, "image: " + response.isSuccessful());
                    }
                    RequestBody infoBody = RequestBody.create(String.valueOf(infoJSON), JSON);
                    Request infoRequest = new Request.Builder()
                            .url(getString(R.string.server_ip) + "/user/" + MainActivity.getUid())
                            .addHeader("token", MainActivity.getToken())
                            .put(infoBody)
                            .build();
                    Log.d(TAG, "text: " + infoRequest.toString());
                    client.newCall(infoRequest).execute();

                    Request request = new Request.Builder()
                            .url(getString(R.string.server_ip)+"/user/"+MainActivity.getUid()+"/avatar")
                            .build();
                    Response response = client.newCall(request).execute();

                    String responseBody = response.body().string();
                    try {
                        JSONObject json = new JSONObject(responseBody);
                        String imgPath = json.getString("data");
                        loginedUser.setAvatar(imgPath);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    MyApplication.getUserDatabase().userDao().deleteUser();
                    MyApplication.getUserDatabase().userDao().insertUser(loginedUser);
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    errMessage = "网络异常";
                    e.printStackTrace();
                }
            }

        });
        sendData.start();
    }
}

