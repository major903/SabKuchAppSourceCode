package vedam.subkuch.uicomponent;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.core.os.BundleCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.models.Image;
import vedam.subkuch.utils.ImageSetter;
import vedam.subkuch.utils.UiUtil;

/**
 * Created by nansari on 11/3/2016.
 */
public class SlideShowDialogFragment extends DialogFragment {

    private ArrayList<Image> alImages;
    private ViewPager viewPager;
    private boolean isImageUrls;

    public static SlideShowDialogFragment newInstance(Bundle bundle) {
        SlideShowDialogFragment f = new SlideShowDialogFragment();
        f.setArguments(bundle);
        return f;
    }

    @Override
    @SuppressWarnings("unchecked")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_slide_show, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);

        alImages = (ArrayList<Image>) BundleCompat.getSerializable(
                getArguments(), Constants.EXTRA_IMAGE_ITEMS, ArrayList.class);
        int selectedPosition = getArguments().getInt(Constants.EXTRA_POSITION);
        isImageUrls = getArguments().getBoolean(Constants.EXTRA_IS_IMAGE_URLS);

        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);

        setCurrentItem(selectedPosition);

        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return v;
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.TransparentDialogTheme);
    }

    //  adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.layout_image_preview, container, false);

            ImageView imageViewPreview = (ImageView) view.findViewById(R.id.iv_preview);

            String image = alImages.get(position).getUrl();

            ImageSetter.ImageBuilder builder = new ImageSetter.ImageBuilder(getContext());
            builder.setPlaceholderResource(R.drawable.transparent).setErrorResource(R.drawable.grey)
                    .fit().centerInside().setTarget(imageViewPreview);

            if (isImageUrls)
                UiUtil.setImageView(builder.setImageLink(image).build());
            else {
                UiUtil.setImageView(builder.setFilePath(image).build());
            }
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return alImages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
