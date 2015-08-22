package com.jorizci.evernoteclient.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.jorizci.evernoteclient.EvernoteClientApp;
import com.jorizci.evernoteclient.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CreateNoteOcr extends AppCompatActivity {

    private static final String TESSDATA_PATH = "tessdata";
    private Bitmap drawBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint DrawBitmapPaint;
    RelativeLayout Rl;
    CustomView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note_ocr);
        view = new CustomView(this);
        Rl = (RelativeLayout) findViewById(R.id.ocr_relative_layout);
        Rl.addView(view);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(getResources()
                .getColor(android.R.color.holo_green_dark));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(20);

    }

    private Paint mPaint;

    public class CustomView extends View {

        public CustomView(Context c) {

            super(c);
            Display Disp = getWindowManager().getDefaultDisplay();
            drawBitmap = Bitmap.createBitmap(Disp.getWidth(), Disp.getHeight(),
                    Bitmap.Config.ARGB_4444);

            mCanvas = new Canvas(drawBitmap);

            mPath = new Path();
            DrawBitmapPaint = new Paint(Paint.DITHER_FLAG);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            setDrawingCacheEnabled(true);
            canvas.drawBitmap(drawBitmap, 0, 0, DrawBitmapPaint);
            canvas.drawPath(mPath, mPaint);
            canvas.drawRect(mY, 0, mY, 0, DrawBitmapPaint);
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
            }
        }

        private void touch_up() {
            mPath.lineTo(mX, mY);

            mCanvas.drawPath(mPath, mPaint);

            mPath.reset();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_note_ocr, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d(EvernoteClientApp.APP_LOG_CODE, "try the ocr");
        TessBaseAPI tessBaseApi = new TessBaseAPI();

        Log.d(EvernoteClientApp.APP_LOG_CODE, "check internal path " + getFilesDir().getPath());

        //Check path in internal data
        File tessDataPath = new File(getFilesDir(), TESSDATA_PATH);
        Log.d(EvernoteClientApp.APP_LOG_CODE, "Install in... " + tessDataPath.getPath());
        if (!tessDataPath.exists()) {
            //Create path
            tessDataPath.mkdir();

            //Need to install files
            Log.d(EvernoteClientApp.APP_LOG_CODE, "Install files");
            InputStream input = null;
            OutputStream output = null;
            try {
                for (String fileName : getAssets().list(TESSDATA_PATH)) {
                    Log.d(EvernoteClientApp.APP_LOG_CODE, "Install file " + fileName);
                    input = getAssets().open(TESSDATA_PATH + File.separator + fileName);
                    File outputFile = new File(tessDataPath, fileName);
                    Log.d(EvernoteClientApp.APP_LOG_CODE, "Install in... " + fileName);
                    output = new FileOutputStream(outputFile);

                    //Buffered copy.
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = input.read(buffer)) > 0) {
                        output.write(buffer, 0, bytesRead);
                    }
                }
            } catch (IOException e) {
                Log.e(EvernoteClientApp.APP_LOG_CODE, e.getMessage(), e);
            } finally {
                //Close file streams
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        Log.e(EvernoteClientApp.APP_LOG_CODE, e.getMessage(), e);
                    }
                }
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        Log.e(EvernoteClientApp.APP_LOG_CODE, e.getMessage(), e);
                    }
                }
            }
        }

        Log.d(EvernoteClientApp.APP_LOG_CODE, "Check files dir ");
        for (File file : getFilesDir().listFiles()) {
            Log.d(EvernoteClientApp.APP_LOG_CODE, file.getPath() + " " + file.getName());
        }

        try {
            boolean returns = tessBaseApi.init(getFilesDir().getPath(), "eng");
            Log.d(EvernoteClientApp.APP_LOG_CODE, "initialization? " + returns);

            tessBaseApi.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*()_+=-[]}{;:'\"\\|~`,./<>?");
            tessBaseApi.setImage(drawBitmap);
            String text = tessBaseApi.getUTF8Text();

            Log.d(EvernoteClientApp.APP_LOG_CODE, "Got data: " + text);
            tessBaseApi.end();
        } catch (Exception e) {
            Log.e(EvernoteClientApp.APP_LOG_CODE, e.getMessage(), e);
        }


       /* mPaint.setXfermode(null);
        switch (item.getItemId()) {
            case R.id.erase:
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                break;
            case R.id.DELETE:
                View =  new CustomView(this);
                break;
            case R.id.draw:
                mPaint.setXfermode(null);

                break;
            case R.id.Save:
                String pattern = "mm ss";
                SimpleDateFormat formatter = new SimpleDateFormat(pattern);
                String time = formatter.format(new Date());
                String path = ("/d-codepages" + time + ".png");

                File file = new File(Environment.getExternalStorageDirectory()
                        + path);

                try {
                    DrawBitmap.compress(Bitmap.CompressFormat.PNG, 100,
                            new FileOutputStream(file));
                    Toast.makeText(this, "File Saved ::" + path, Toast.LENGTH_SHORT)
                            .show();
                } catch (Exception e) {
                    Toast.makeText(this, "ERROR" + e.toString(), Toast.LENGTH_SHORT)
                            .show();
                }

        }*/
        return super.onOptionsItemSelected(item);
    }
}
