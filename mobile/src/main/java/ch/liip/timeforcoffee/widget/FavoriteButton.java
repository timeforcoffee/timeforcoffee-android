package ch.liip.timeforcoffee.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import ch.liip.timeforcoffee.R;

/**
 * Created by fsantschi on 15/03/15.
 */
public class FavoriteButton extends ImageSwitcher implements View.OnClickListener, ViewSwitcher.ViewFactory
{
    private boolean mIsFavorite = false;

    private OnClickListener clickListener;

    private void init() {
        setFactory(this);
        setOnClickListener(this);
        if(mIsFavorite) {
            setImageResource(R.drawable.shape_star);
        } else {
            setImageResource(R.drawable.shape_place);
        }
        Animation inAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.grow);
        Animation outAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shrink);
        setInAnimation(inAnimation);
        setOutAnimation(outAnimation);
    }

    public void setIsFavorite(boolean isFavorite)
    {
        this.mIsFavorite = isFavorite;

        if(mIsFavorite) {
            setImageResource(R.drawable.shape_star);
        } else {
            setImageResource(R.drawable.shape_place);
        }
    }

    public FavoriteButton(Context context) {
        super(context);
        init();
    }

    public FavoriteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        if (l == this) {
            super.setOnClickListener(l);
        } else {
            clickListener = l;
        }
    }

    @Override
    public void onClick(View v) {

        mIsFavorite = !mIsFavorite;

        if(mIsFavorite) {
            setImageResource(R.drawable.shape_star);
        } else {
            setImageResource(R.drawable.shape_place);
        }

        if (clickListener != null) {
            clickListener.onClick(this);
        }
    }

    @Override
    public View makeView() {
        ImageView view = new ImageView(getContext());
        view.setScaleType(ImageView.ScaleType.FIT_CENTER);
        view.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.
                MATCH_PARENT,LayoutParams.MATCH_PARENT));
        return view;
    }
}
