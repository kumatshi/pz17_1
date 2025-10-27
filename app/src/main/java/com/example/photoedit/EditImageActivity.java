package com.example.photoedit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

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
        originalBitmap = ImageHolder.getImage();

        if (originalBitmap != null) {
            currentBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
            imageView.setImageBitmap(currentBitmap);
            Toast.makeText(this, "Изображение загружено успешно!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Ошибка: изображение не найдено", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public void onRotateClick(View view) {
        if (currentBitmap == null) return;

        try {
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
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка при повороте", Toast.LENGTH_SHORT).show();
        }
    }

    public void onGrayscaleClick(View view) {
        if (currentBitmap == null) return;

        try {
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
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка при применении фильтра", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBrightnessClick(View view) {
        if (currentBitmap == null) return;

        try {
            Bitmap brightBitmap = Bitmap.createBitmap(
                    currentBitmap.getWidth(),
                    currentBitmap.getHeight(),
                    Bitmap.Config.ARGB_8888
            );

            Canvas canvas = new Canvas(brightBitmap);
            Paint paint = new Paint();

            ColorMatrix matrix = new ColorMatrix(new float[] {
                    1.3f, 0, 0, 0, 30,
                    0, 1.3f, 0, 0, 30,
                    0, 0, 1.3f, 0, 30,
                    0, 0, 0, 1, 0
            });

            paint.setColorFilter(new ColorMatrixColorFilter(matrix));
            canvas.drawBitmap(currentBitmap, 0, 0, paint);

            currentBitmap = brightBitmap;
            imageView.setImageBitmap(currentBitmap);
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка при изменении яркости", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBlurClick(View view) {
        if (currentBitmap == null) return;

        try {
            int width = Math.max(currentBitmap.getWidth() / 8, 1);
            int height = Math.max(currentBitmap.getHeight() / 8, 1);

            Bitmap smallBitmap = Bitmap.createScaledBitmap(currentBitmap, width, height, true);
            Bitmap blurredBitmap = Bitmap.createScaledBitmap(smallBitmap,
                    currentBitmap.getWidth(), currentBitmap.getHeight(), true);

            // Очищаем память
            if (smallBitmap != blurredBitmap) {
                smallBitmap.recycle();
            }

            currentBitmap = blurredBitmap;
            imageView.setImageBitmap(currentBitmap);
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка при размытии", Toast.LENGTH_SHORT).show();
        }
    }

    public void onResetClick(View view) {
        if (originalBitmap != null) {
            currentBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
            imageView.setImageBitmap(currentBitmap);
            Toast.makeText(this, "Изображение сброшено", Toast.LENGTH_SHORT).show();
        }
    }

    public void onSaveClick(View view) {
        Toast.makeText(this, "Изображение отредактировано!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Очистка памяти
        if (currentBitmap != null && currentBitmap != originalBitmap) {
            currentBitmap.recycle();
        }
    }
}