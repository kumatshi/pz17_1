package com.example.photoedit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.ByteArrayOutputStream;

public class EditImageActivity extends AppCompatActivity {

    private ImageView imageView;
    private Bitmap originalBitmap;
    private Bitmap currentBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        Init();
    }

    private void Init() {
        imageView = findViewById(R.id.imageView);
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        if (byteArray != null) {
            originalBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        } else {
            Toast.makeText(this, "Ошибка загрузки изображения", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        currentBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        imageView.setImageBitmap(currentBitmap);
    }
    public void onRotateClick(View view) {
        Bitmap rotatedBitmap = Bitmap.createBitmap(
                currentBitmap.getHeight(),
                currentBitmap.getWidth(),
                Bitmap.Config.ARGB_8888
        );

        Canvas canvas = new Canvas(rotatedBitmap);
        canvas.rotate(90, rotatedBitmap.getWidth() / 2f, rotatedBitmap.getHeight() / 2f);
        canvas.translate(0, -currentBitmap.getHeight());
        canvas.drawBitmap(currentBitmap, 0, 0, null);

        currentBitmap = rotatedBitmap;
        imageView.setImageBitmap(currentBitmap);
    }
    public void onGrayscaleClick(View view) {
        Bitmap grayscaleBitmap = Bitmap.createBitmap(
                currentBitmap.getWidth(),
                currentBitmap.getHeight(),
                Bitmap.Config.ARGB_8888
        );

        Canvas canvas = new Canvas(grayscaleBitmap);
        Paint paint = new Paint();
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        paint.setColorFilter(new ColorMatrixColorFilter(matrix));
        canvas.drawBitmap(currentBitmap, 0, 0, paint);

        currentBitmap = grayscaleBitmap;
        imageView.setImageBitmap(currentBitmap);
    }
    public void onBrightnessClick(View view) {
        Bitmap brightBitmap = Bitmap.createBitmap(
                currentBitmap.getWidth(),
                currentBitmap.getHeight(),
                Bitmap.Config.ARGB_8888
        );

        Canvas canvas = new Canvas(brightBitmap);
        Paint paint = new Paint();
        ColorMatrix matrix = new ColorMatrix(new float[] {
                1.2f, 0, 0, 0, 0,    // red
                0, 1.2f, 0, 0, 0,    // green
                0, 0, 1.2f, 0, 0,    // blue
                0, 0, 0, 1, 0        // alpha
        });

        paint.setColorFilter(new ColorMatrixColorFilter(matrix));
        canvas.drawBitmap(currentBitmap, 0, 0, paint);

        currentBitmap = brightBitmap;
        imageView.setImageBitmap(currentBitmap);
    }
    public void onBlurClick(View view) {
        try {
            int width = Math.max(currentBitmap.getWidth() / 10, 1);
            int height = Math.max(currentBitmap.getHeight() / 10, 1);

            Bitmap smallBitmap = Bitmap.createScaledBitmap(currentBitmap, width, height, true);
            Bitmap blurredBitmap = Bitmap.createScaledBitmap(smallBitmap,
                    currentBitmap.getWidth(), currentBitmap.getHeight(), true);

            currentBitmap = blurredBitmap;
            imageView.setImageBitmap(currentBitmap);
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка при размытии", Toast.LENGTH_SHORT).show();
        }
    }
    public void onResetClick(View view) {
        currentBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        imageView.setImageBitmap(currentBitmap);
        Toast.makeText(this, "Изображение сброшено", Toast.LENGTH_SHORT).show();
    }
    public void onSaveClick(View view) {
        Toast.makeText(this, "Функция сохранения в разработке", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (currentBitmap != null && currentBitmap != originalBitmap) {
            currentBitmap.recycle();
        }
        if (originalBitmap != null) {
            originalBitmap.recycle();
        }
    }
}