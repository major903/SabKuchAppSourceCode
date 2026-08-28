package vedam.subkuch.ui.events;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import vedam.subkuch.R;

/** Lightweight shimmer used while a course thumbnail is being decoded or downloaded. */
public final class ThumbnailShimmerView extends View {
    private static final long SHIMMER_DELAY_MS = 180L;
    private static final long SHIMMER_DURATION_MS = 1_200L;

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Matrix shaderMatrix = new Matrix();
    private final Runnable delayedStart = this::startIfNeeded;

    private ValueAnimator animator;
    private LinearGradient gradient;
    private boolean loading;

    public ThumbnailShimmerView(Context context) {
        this(context, null);
    }

    public ThumbnailShimmerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThumbnailShimmerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);
    }

    public void showWhileLoading() {
        loading = true;
        removeCallbacks(delayedStart);
        stopAnimator();
        setVisibility(GONE);
        postDelayed(delayedStart, SHIMMER_DELAY_MS);
    }

    public void hideShimmer() {
        loading = false;
        removeCallbacks(delayedStart);
        stopAnimator();
        setVisibility(GONE);
    }

    private void startIfNeeded() {
        if (!loading || !isAttachedToWindow()) return;

        setVisibility(VISIBLE);
        if (ValueAnimator.areAnimatorsEnabled()) {
            startAnimator();
        } else {
            invalidate();
        }
    }

    private void startAnimator() {
        if (animator != null || getWidth() == 0) return;

        animator = ValueAnimator.ofFloat(-1f, 1f);
        animator.setDuration(SHIMMER_DURATION_MS);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(valueAnimator -> {
            if (gradient == null) return;
            float translation = getWidth() * (float) valueAnimator.getAnimatedValue();
            shaderMatrix.setTranslate(translation, 0f);
            gradient.setLocalMatrix(shaderMatrix);
            invalidate();
        });
        animator.start();
    }

    private void stopAnimator() {
        if (animator == null) return;
        animator.cancel();
        animator = null;
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        if (width <= 0) return;

        int baseColor = ContextCompat.getColor(getContext(), R.color.learn_shimmer_base);
        int highlightColor = ContextCompat.getColor(getContext(), R.color.learn_shimmer_highlight);
        gradient = new LinearGradient(
                0f,
                0f,
                width,
                0f,
                new int[]{baseColor, baseColor, highlightColor, baseColor, baseColor},
                new float[]{0f, 0.28f, 0.5f, 0.72f, 1f},
                Shader.TileMode.CLAMP
        );
        paint.setShader(gradient);

        // A GONE view is measured only after the delayed loading state makes it visible.
        if (loading && getVisibility() == VISIBLE && ValueAnimator.areAnimatorsEnabled()) {
            startAnimator();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0f, 0f, getWidth(), getHeight(), paint);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (loading) {
            removeCallbacks(delayedStart);
            postDelayed(delayedStart, SHIMMER_DELAY_MS);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        removeCallbacks(delayedStart);
        stopAnimator();
        super.onDetachedFromWindow();
    }
}
