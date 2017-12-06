package com.king.batterytest.fbaselib.customview.multiimageselector;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.king.batterytest.fbaselib.R;
import com.king.batterytest.fbaselib.utils.Tools;

import java.io.File;
import java.util.ArrayList;

public class MultiImageSelectorActivity extends AppCompatActivity implements
        MultiImageSelectorFragment.Callback, Handler.Callback {

    /**
     * 最大图片选择次数，int类型，默认9
     */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /**
     * 图片选择模式，默认多选
     */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /**
     * 是否显示相机，默认显示
     */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /**
     * 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合
     */
    public static final String EXTRA_RESULT = "select_result";
    /**
     * 默认选择集
     */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";

    /**
     * 单选
     */
    public static final int MODE_SINGLE = 0;
    /**
     * 多选
     */
    public static final int MODE_MULTI = 1;

    private ArrayList<String> resultList = new ArrayList<String>();
    private int mDefaultCount;
    private int mode;
    private boolean isShow;
    private int pos;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectpic);
        mHandler = new Handler(this);
        Intent intent = getIntent();
        mDefaultCount = intent.getIntExtra(EXTRA_SELECT_COUNT, 9);
        mode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
        isShow = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        if (mode == MODE_MULTI && intent.hasExtra(EXTRA_DEFAULT_SELECTED_LIST)) {
            resultList = intent
                    .getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
        }
        pos = intent.getIntExtra("pos", -2);
        Bundle bundle = new Bundle();
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT,
                mDefaultCount);
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
        bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
        bundle.putStringArrayList(
                MultiImageSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST,
                resultList);
        Fragment f = Fragment.instantiate(this,
                MultiImageSelectorFragment.class.getName(), bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.image_grid, f)
                .commit();

    }

    @Override
    public void onSingleImageSelected(String path) {
        Intent data = new Intent();
        resultList.add(path);
        data.putStringArrayListExtra(EXTRA_RESULT, resultList);
        data.putExtra("pos", pos);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onImageSelected(String path) {
    }

    @Override
    public void onImageUnselected(String path) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onCameraShot(final File imageFile) {
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(imageFile)));
        //延迟处理下  小米tmd立刻返回文件竟然是空
        Tools.showWaitDialog(this);

        if (imageFile != null) {
            Tools.toastInBottom(this, imageFile.length() + "");


            new Thread() {
                @Override
                public void run() {
                    while (imageFile.length() == 0) {
                        try {
//                            Log.e("zk",""+imageFile.length());
                            sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
//                    Log.e("zk ok",""+imageFile.length());
                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            Tools.hideWaitDialog();
                            Intent data = new Intent();
                            resultList.add(imageFile.getAbsolutePath());
                            data.putStringArrayListExtra(EXTRA_RESULT, resultList);
                            setResult(RESULT_OK, data);
                            data.putExtra("pos", pos);
                            setResult(RESULT_OK, data);
                            finish();
                        }
                    });
                }
            }.start();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 1:

                break;

        }

        return false;
    }
}
