package com.jorizci.evernoteclient.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.jorizci.evernoteclient.EvernoteClientApp;
import com.jorizci.evernoteclient.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class CreateNoteOcr extends AppCompatActivity implements EvernoteCallback<List<Notebook>>{

    private List<Notebook> notebooks;
    private static final String TESSDATA_PATH = "tessdata";
    private Bitmap drawBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint DrawBitmapPaint;
    RelativeLayout relativeLayout;
    CustomView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note_ocr);
        view = new CustomView(this);
        relativeLayout = (RelativeLayout) findViewById(R.id.ocr_relative_layout);
        relativeLayout.addView(view);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(getResources()
                .getColor(android.R.color.holo_green_dark));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(20);

        EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient().listNotebooksAsync(this);
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
        switch (item.getItemId()){
            case R.id.accept:
                acceptAction();
                return true;
            case R.id.clean:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        relativeLayout.removeView(view);
                        view =  new CustomView(CreateNoteOcr.this);
                        relativeLayout.addView(view);
                    }
                });
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void acceptAction() {
        checkInstallOfFiles();

        try {
            TessBaseAPI tessBaseApi = new TessBaseAPI();
            boolean returns = tessBaseApi.init(getFilesDir().getPath(), "eng");
            tessBaseApi.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*()_+=-[]}{;:'|~`,./<>?");
            tessBaseApi.setImage(drawBitmap);
            String text = tessBaseApi.getUTF8Text();


            tessBaseApi.end();
        } catch (Exception e) {
            Log.e(EvernoteClientApp.APP_LOG_CODE, e.getMessage(), e);
        }

    }

    private void createNote(String noteContent) {
        EditText fieldNoteTitle = (EditText) findViewById(R.id.field_note_title);
        Spinner fieldNotebookSpinner = (Spinner) findViewById(R.id.notebook_spinner);

        Note note = new Note();
        note.setTitle(fieldNoteTitle.getText().toString());
        note.setNotebookGuid(notebooks.get(fieldNotebookSpinner.getSelectedItemPosition()).getGuid());
        note.setContent(EvernoteUtil.NOTE_PREFIX + noteContent + EvernoteUtil.NOTE_SUFFIX);

        EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient().createNoteAsync(note, new EvernoteCallback<Note>() {
            @Override
            public void onSuccess(Note result) {
                Toast.makeText(getApplicationContext(), "Note Created", Toast.LENGTH_LONG).show();
                NavUtils.navigateUpFromSameTask(CreateNoteOcr.this);
            }

            @Override
            public void onException(Exception exception) {
                Log.e(EvernoteClientApp.APP_LOG_CODE, "Exception creating note ", exception);
                Toast.makeText(getApplicationContext(), "Unexpected error", Toast.LENGTH_LONG).show();
                NavUtils.navigateUpFromSameTask(CreateNoteOcr.this);
            }
        });
    }

    /**
     * Tesseract needs the training files to be deployed on an accessible
     * path, so we copy all asset files to the private storage of the app
     * in the device.
     */
    private void checkInstallOfFiles() {
        //Check path in internal data
        File tessDataPath = new File(getFilesDir(), TESSDATA_PATH);
        if (!tessDataPath.exists()) {
            //Create path
            tessDataPath.mkdir();

            //Need to install files (requisite to tesseract)
            InputStream input = null;
            OutputStream output = null;
            try {
                for (String fileName : getAssets().list(TESSDATA_PATH)) {
                    input = getAssets().open(TESSDATA_PATH + File.separator + fileName);
                    File outputFile = new File(tessDataPath, fileName);
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
    }

    private void setNoteCreatorUiContent() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Notebook initialization
                List<String> list = generateNotebookNameList();
                Log.d(EvernoteClientApp.APP_LOG_CODE, "Notebooks available " + list);
                ArrayAdapter<String> notebookAdapter = new ArrayAdapter<String>(CreateNoteOcr.this, android.R.layout.simple_spinner_item, list);
                notebookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                Spinner notebookSpinner = (Spinner) findViewById(R.id.notebook_spinner);
                notebookSpinner.setAdapter(notebookAdapter);
                notebookSpinner.setSelection(0);
            }
        });
    }

    private List<String> generateNotebookNameList() {
        List<String> notebookNames = new ArrayList<>();
        for (Notebook notebook : notebooks) {
            notebookNames.add(notebook.getName());
        }
        return notebookNames;
    }

    @Override
    public void onSuccess(List<Notebook> result) {
        notebooks = result;
        setNoteCreatorUiContent();
    }

    @Override
    public void onException(Exception exception) {
        NavUtils.navigateUpFromSameTask(this);
    }
}
