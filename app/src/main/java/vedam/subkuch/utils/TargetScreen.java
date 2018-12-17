package vedam.subkuch.utils;

import vedam.subkuch.base.BaseActivity;


/**
 * Created by msharm6 on 6/12/2016.
 */
public enum TargetScreen {

    SCREEN_BASE(BaseActivity.class);


    private final Class<?> cls;

    TargetScreen(Class<?> cls) {
        this.cls = cls;
    }

    public Class<?> getTargetScreenClass() {

        return this.cls;
    }
}
