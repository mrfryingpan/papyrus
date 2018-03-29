package papyrus.util;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;

public class Animations {
    private static final int DEFAULT_DURATION = 250;
    private static int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private static int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public static void fadeOut(final View view) {
        fadeOut(view, null);
    }

    public static void fadeOut(final View view, final Runnable completion) {
        view.animate()
                .alpha(0f)
                .withEndAction(new Runnable() {
                    public void run() {
                        view.setVisibility(View.INVISIBLE);
                        if (completion != null) {
                            completion.run();
                        }
                    }
                })
                .start();
    }

    public static void fadeIn(final View view) {
        fadeIn(view, null);
    }

    public static void fadeIn(final View view, final Runnable completion) {
        view.animate()
                .alpha(1f)
                .withStartAction(new Runnable() {
                    public void run() {
                        view.setAlpha(0f);
                        view.setVisibility(View.VISIBLE);
                    }
                })
                .withEndAction(new Runnable() {
                    public void run() {
                        if (completion != null) {
                            completion.run();
                        }
                    }
                })
                .start();
    }

    public static void slideOffLeft(final View view) {
        view.animate()
                .translationX(-screenWidth)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(View.GONE);
                    }
                })
                .start();
    }

    public static void slideOffRight(final View view) {
        view.animate()
                .translationX(screenWidth)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(View.GONE);
                    }
                })
                .start();
    }

    public static void slideOnLeft(final View view) {
        view.setTranslationX(-screenWidth);
        view.animate()
                .translationX(0)
                .withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(View.VISIBLE);
                    }
                })
                .start();
    }

    public static void slideOnRight(final View view) {
        view.setTranslationX(screenWidth);
        view.animate()
                .translationX(0)
                .withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(View.VISIBLE);
                    }
                })
                .start();
    }

    public static void growAndShrink(final View view) {
        view.animate()
                .scaleX(1.5f)
                .scaleY(1.5f)
                .withEndAction(new Runnable() {
                    public void run() {
                        view.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .start();
                    }
                }).start();
    }

    public static void slideInBottom(View view) {
        slideInBottom(view, null);
    }

    public static void slideInBottom(final View view, final Runnable completion) {
        view.setTranslationY(screenHeight);
        view.animate()
                .translationY(0)
                .withStartAction(new Runnable() {
                    public void run() {
                        view.setVisibility(View.VISIBLE);
                        if (completion != null) {
                            completion.run();
                        }
                    }
                })
                .start();
    }

    public static void slideOffBottom(View view) {
        slideInBottom(view, null);
    }

    public static void slideOffBottom(final View view, final Runnable completion) {
        view.animate()
                .translationY(screenHeight)
                .withEndAction(new Runnable() {
                    public void run() {
                        view.setVisibility(View.GONE);
                        if (completion != null) {
                            completion.run();
                        }
                    }
                })
                .start();
    }

    public static void slideInTop(View view) {
        slideInTop(view, null);
    }

    public static void slideInTop(final View view, final Runnable completion) {
        view.setTranslationY(-screenHeight);
        view.animate()
                .translationY(0)
                .withStartAction(new Runnable() {
                    public void run() {
                        view.setVisibility(View.VISIBLE);
                    }
                })
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        if (completion != null) {
                            completion.run();
                        }
                    }
                })
                .start();
    }

    public static void slideVerticalFrom(int start, View view) {
        slideVerticalFrom(start, view, null);
    }

    public static void slideVerticalFrom(int start, final View view, final Runnable completion) {
        view.setTranslationY(start);
        view.animate()
                .translationY(0)
                .withStartAction(new Runnable() {
                    public void run() {
                        view.setVisibility(View.VISIBLE);
                    }
                })
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        if (completion != null) {
                            completion.run();
                        }
                    }
                })
                .start();
    }

    public static void slideOffTop(View view) {
        slideOffTop(view, null);
    }

    public static void slideOffTop(final View view, final Runnable completion) {
        view.animate()
                .translationY(-screenHeight)
                .withStartAction(new Runnable() {
                    public void run() {
                        view.setVisibility(View.VISIBLE);
                    }
                })
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        if (completion != null) {
                            completion.run();
                        }
                    }
                })
                .start();
    }

    private static int resolveViewHeight(View view) {
        ViewGroup parent = (ViewGroup) view.getParent();
        int width = parent.getWidth();
        width -= (parent.getPaddingLeft() + parent.getPaddingRight());

        ViewGroup.LayoutParams params = view.getLayoutParams();

        int heightMargins = 0;
        if (params instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams margins = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            width -= (margins.leftMargin + margins.rightMargin);
            heightMargins = margins.topMargin + margins.bottomMargin;
        }
        view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY), ViewGroup.LayoutParams.WRAP_CONTENT);
        return view.getMeasuredHeight() + view.getPaddingTop() + view.getPaddingBottom() + heightMargins + Res.dpi(8);
    }

    public static void grow(View view) {
        grow(view, resolveViewHeight(view));
    }

    public static void grow(final View view, int targetHeight) {
        ValueAnimator anim = ValueAnimator.ofInt(view.getHeight(), targetHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = (Integer) valueAnimator.getAnimatedValue();
                view.setLayoutParams(layoutParams);
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.requestLayout();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.setDuration(250);
        anim.start();
    }

    public static void shrink(final View view) {
        ValueAnimator anim = ValueAnimator.ofInt(view.getHeight(), 0);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = (Integer) valueAnimator.getAnimatedValue();
                ;
                view.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(250);
        anim.start();
    }
}
