package me.fanjie.testfaceplusplus.facedetect;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.plattysoft.leonids.ParticleSystem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.fanjie.testfaceplusplus.R;
import me.fanjie.testfaceplusplus.base.BaseActivity;
import me.fanjie.testfaceplusplus.core.C;
import me.fanjie.testfaceplusplus.core.Callback;
import me.fanjie.testfaceplusplus.core.Net;
import me.fanjie.testfaceplusplus.utils.JLog;
import okhttp3.ResponseBody;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static me.fanjie.testfaceplusplus.utils.JTextUtils.isEmpty;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private static final List<LipColor> lipColors = new ArrayList<>();

    static {
        lipColors.add(new LipColor(0, 0, 0, "中毒了小姐姐-_-"));
        lipColors.add(new LipColor(202, 132, 130, "裸色"));
        lipColors.add(new LipColor(203, 94, 114, "烟熏玫瑰色"));
        lipColors.add(new LipColor(225, 96, 116, "偏橘粉嫩色"));
        lipColors.add(new LipColor(195, 108, 127, "珊瑚粉"));
        lipColors.add(new LipColor(224, 68, 108, "梅子红"));
        lipColors.add(new LipColor(220, 78, 76, "樱桃浆果色"));
        lipColors.add(new LipColor(144, 8, 28, "浓郁深紫红色"));
        lipColors.add(new LipColor(197, 85, 99, "砖红色"));
        lipColors.add(new LipColor(232, 101, 143, "亮水红色"));
        lipColors.add(new LipColor(214, 89, 123, "魅惑粉"));
        lipColors.add(new LipColor(234, 100, 99, "蜜桃珊瑚色"));
        lipColors.add(new LipColor(214, 87, 117, "桃粉色"));
        lipColors.add(new LipColor(205, 72, 137, "紫粉色"));
        lipColors.add(new LipColor(199, 58, 77, "蜜桃红"));
        lipColors.add(new LipColor(199, 38, 92, "紫红色"));
        lipColors.add(new LipColor(188, 67, 98, "粉玫瑰色"));
        lipColors.add(new LipColor(202, 100, 122, "淡雅裸粉"));
        lipColors.add(new LipColor(198, 67, 81, "裸茶玫瑰"));
        lipColors.add(new LipColor(182, 70, 69, "牡丹"));
        lipColors.add(new LipColor(204, 64, 135, "紫罗兰"));
        lipColors.add(new LipColor(236, 80, 29, "暖橘色"));
        lipColors.add(new LipColor(209, 46, 39, "妖艳红"));
    }

    private static final int REQUEST_CODE_TAKE_IMAGE = 101;
    private static final int REQUEST_CODE_CAMERA = 102;
    private Bitmap faceBitmap;
    private ImageView ivFace;
    private TextView tvLipColorName;
    private View vLipColor;
    private LinearLayout llResult;
    private String faceToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_face && !isEmpty(faceToken)) {
            Net.getInstance().create(FaceApi.class)
                    .addFace(C.KEY, C.SECRET, C.FACE_SET, faceToken)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void getBody(ResponseBody body) {

                        }
                    });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TAKE_IMAGE && resultCode == RESULT_OK) {
            requestDetect(data.getData());
        } else if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            File f = new File(photoPath);
            Uri contentUri = Uri.fromFile(f);
            requestDetect(contentUri);
        }
    }

    public void takePhotoByLib(View view) {
        takeImage();
    }

    public void takePhotoByCamera(View view) {
        takeImageByCamera();
    }


    private void requestDetect(Uri uri) {
        try {
            faceBitmap = scaleBitmapFormUri(uri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            faceBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            baos.flush();
            baos.close();
            String img64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            Log.d(TAG, "requestDetect: img64" + img64.length());
            Glide.with(this).load(baos.toByteArray()).into(ivFace);
            detectFace(img64);
        } catch (IOException e) {
            JLog.e(e);
        } finally {
            dismissLoading();
        }
    }

    private void detectFace(String img64) {
        Net.getInstance().create(FaceApi.class)
                .detect(C.KEY, C.SECRET, 1, img64)
                .enqueue(new Callback<DetectResp>() {
                    @Override
                    public void getBody(DetectResp body) {
                        List<DetectResp.FacesBean> faces = body.getFaces();
                        if (faces == null || faces.isEmpty()) {
                            Toast.makeText(MainActivity.this, "抱歉，傻逼AI没找到脸", Toast.LENGTH_SHORT).show();
                            llResult.setVisibility(View.INVISIBLE);
                            return;
                        }
                        DetectResp.FacesBean face = faces.get(0);
                        DetectResp.FacesBean.LandmarkBean landmark = face.getLandmark();
                        DetectResp.FacesBean.LandmarkBean.MouthLowerLipBottomBean lipBottom = landmark.getMouth_lower_lip_bottom();
                        DetectResp.FacesBean.LandmarkBean.MouthLowerLipTopBean lipTop = landmark.getMouth_lower_lip_top();
                        int x = (lipTop.getX() + lipBottom.getX()) / 2;
                        int y = (lipTop.getY() + lipBottom.getY()) / 2;
                        drawPointInLip(x, y);
                        int pixel = faceBitmap.getPixel(x, y);
                        int r = Color.red(pixel);
                        int g = Color.green(pixel);
                        int b = Color.blue(pixel);
                        vLipColor.setBackgroundColor(Color.rgb(r, g, b));
                        LipColor sColor = getSimilarityColor(r, g, b);
                        tvLipColorName.setTextColor(Color.rgb(sColor.r, sColor.g, sColor.b));
                        tvLipColorName.setText(sColor.name);
                        llResult.setVisibility(View.VISIBLE);
                        faceToken = face.getFace_token();
                        Log.d(TAG, "getBody: faceToken = " + faceToken);
                        compareFace(faceToken);
                    }
                });
    }

    private void compareFace(String faceToken) {
        Net.getInstance().create(FaceApi.class)
                .compare(C.KEY, C.SECRET, faceToken, C.FACE_FAIRY)
                .enqueue(new Callback<CompareResp>() {
                    @Override
                    public void getBody(CompareResp body) {
                        if (body.getConfidence() > 70) {
                            showFairy();
                        }
                    }
                });
    }

    private void showFairy() {
        Toast.makeText(this, "惊现小仙女！！！", Toast.LENGTH_LONG).show();
        new ParticleSystem(this, 150, R.mipmap.ic_heart, 5000)
                .setSpeedRange(0.1f, 0.15f)
                .setScaleRange(0.5f, 1f)
                .setRotationSpeed(180)
                .setFadeOut(4000)
                .oneShot(ivFace, 150);
    }

    private void drawPointInLip(int x, int y) {
        Bitmap bitmap = Bitmap.createBitmap(faceBitmap.getWidth(), faceBitmap.getHeight(), faceBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setAntiAlias(true);
        canvas.drawBitmap(faceBitmap, 0, 0, paint);
        canvas.drawCircle(x, y, 1.5f, paint);
        canvas.save();
        ivFace.setImageBitmap(bitmap);
    }


    //    相似
    private LipColor getSimilarityColor(int r, int b, int g) {
        double minSqrt = Double.MAX_VALUE;
        LipColor similarityColor = new LipColor(0, 0, 0, "");
        for (LipColor c : lipColors) {
            double sqrt = sqrt(pow(r - c.r, 2) + pow(b - c.b, 2) + pow(g - c.g, 2));
            if (sqrt < minSqrt) {
                similarityColor = c;
                minSqrt = sqrt;
            }
        }

        return similarityColor;
    }


    private void takeImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_TAKE_IMAGE);
    }

    private void takeImageByCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
                photoPath = photoFile.getAbsolutePath();
            } catch (IOException ex) {
                JLog.e(ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                        "me.fanjie.testfaceplusplus.fileprovider",
                        photoFile);
//                向响应意图的所有应用授权使用空间，避免报错
                List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        }
    }

    private String photoPath;

    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    private Bitmap scaleBitmapFormUri(Uri uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (height > width) {
            double s = height / 400D;
            width /= s;
            height = 400;
        } else {
            double s = width / 400D;
            height /= s;
            width = 400;
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    private void checkPer() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    10086);
        }
    }

    private void initView() {
        ivFace = (ImageView) findViewById(R.id.iv_face);
        tvLipColorName = (TextView) findViewById(R.id.tv_lip_color_name);
        vLipColor = findViewById(R.id.v_lip_color);
        llResult = (LinearLayout) findViewById(R.id.ll_result);
    }
}
