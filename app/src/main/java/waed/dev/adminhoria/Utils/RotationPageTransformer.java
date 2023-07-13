package waed.dev.adminhoria.Utils;

import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

public class RotationPageTransformer implements ViewPager2.PageTransformer {
    private final float minAlpha;
    private final int degrees;
    private final float distanceToCentreFactor;

    public RotationPageTransformer(int degrees) {
        this(degrees, 0.7f);
    }

    public RotationPageTransformer(int degrees, float minAlpha) {
        this.degrees = degrees;
        distanceToCentreFactor = (float) Math.tan(Math.toRadians(degrees / 2f)) / 2;
        this.minAlpha = minAlpha;
    }

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();
        view.setPivotX((float) pageWidth / 2);
        view.setPivotY((float) (pageHeight + pageWidth * distanceToCentreFactor));

        if (position < -1) {
            // [-infinity, 1) off to the left by a lot
            view.setRotation(0);
            view.setAlpha(0);
        } else if (position <= 1) {
            // [-1, 1] shift the view over
            view.setTranslationX(-position * pageWidth);
            // rotate it
            view.setRotation(position * (180 - degrees));
            // Fade the page relative to its distance from the center
            view.setAlpha(Math.max(minAlpha, 1 - Math.abs(position) / 3));
        } else {
            // (1, +infinity] off to the right by a lot
            view.setRotation(0);
            view.setAlpha(0);
        }
    }
}
