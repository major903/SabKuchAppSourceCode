package vedam.subkuch.utils;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by nansari on 11/7/2016.
 */
public class ImageSetter {

    private Context context;
    private String imageLink;
    private int imageResource;
    private int placeholderResource;
    private String filePath;
    private int errorResource;
    private ImageView ivTarget;
    private int maxSize;
    private boolean isFit;
    private boolean isCenterCrop;
    private boolean isCenterInside;
    private boolean isDefaultsSet;

    private ImageSetter(ImageBuilder builder) {
        this.context = builder.context;
        this.imageLink = builder.imageLink;
        this.imageResource = builder.imageResource;
        this.filePath = builder.filePath;
        this.placeholderResource = builder.placeholderResource;
        this.errorResource = builder.errorResource;
        this.ivTarget = builder.ivTarget;
        this.maxSize = builder.maxSize;
        this.isFit = builder.isFit;
        this.isCenterCrop = builder.isCenterCrop;
        this.isCenterInside = builder.isCenterInside;
        this.isDefaultsSet = builder.isDefaultsSet;
    }

    public Context getContext() {
        return context;
    }

    public String getImageLink() {
        return imageLink;
    }

    public int getImageResource() {
        return imageResource;
    }

    public int getPlaceholderResource() {
        return placeholderResource;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getErrorResource() {
        return errorResource;
    }

    public ImageView getIvTarget() {
        return ivTarget;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public boolean isFit() {
        return isFit;
    }

    public boolean isCenterCrop() {
        return isCenterCrop;
    }

    public boolean isCenterInside() {
        return isCenterInside;
    }

    public boolean isDefaultsSet() {
        return isDefaultsSet;
    }

    public static class ImageBuilder {
        private Context context;
        private String imageLink;
        private int imageResource;
        private String filePath;
        private int placeholderResource;
        private int errorResource;
        private ImageView ivTarget;
        private int maxSize;
        private boolean isFit;
        private boolean isCenterCrop;
        private boolean isCenterInside;
        private boolean isDefaultsSet;

        public ImageBuilder(Context context) {
            this.context = context;
        }

        public ImageBuilder setImageLink(String imageLink) {
            this.imageLink = imageLink;
            return this;
        }

        public ImageBuilder setImageResource(int imageResource) {
            this.imageResource = imageResource;
            return this;
        }

        public ImageBuilder setFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public ImageBuilder setPlaceholderResource(int placeholderResource) {
            this.placeholderResource = placeholderResource;
            return this;
        }

        public ImageBuilder setErrorResource(int errorResource) {
            this.errorResource = errorResource;
            return this;
        }

        public ImageBuilder setTarget(ImageView ivTarget) {
            this.ivTarget = ivTarget;
            return this;
        }

        public ImageBuilder setMaxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public ImageBuilder fit() {
            this.isFit = true;
            return this;
        }

        public ImageBuilder centerCrop() {
            this.isCenterCrop = true;
            return this;
        }

        public ImageBuilder centerInside() {
            this.isCenterInside = true;
            return this;
        }

        /**
         * If this is set, the placeholder and error resource is R.drawable.grey
         *
         * @return The ImageBuilder
         */
        public ImageBuilder setDefaults() {
            this.isDefaultsSet = true;
            return this;
        }

        public ImageSetter build() {

            return new ImageSetter(this);
        }

    }
}
