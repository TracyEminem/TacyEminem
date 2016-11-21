package com.z.wolfkill.common.utils.reflect;

import android.content.Context;

/**
 * PACKAGE_NAME: com.z.wolfkill.common.utils.reflect
 * FUNCTIONAL_DESCRIPTION:
 * CREATE_BY: 尽际
 * CREATE_TIME: 2016/11/8 15:51
 * MODIFICATORY_DESCRIPTION:
 * MODIFY_BY:
 * MODIFICATORY_TIME:
 */

public class IDHelper {
    public static int getLayout(Context mContext, String layoutName) {
        return ResourceHelper.getInstance(mContext).getLayoutId(layoutName);
    }

    public static int getViewID(Context mContext, String IDName) {
        return ResourceHelper.getInstance(mContext).getId(IDName);
    }

    public static int getDrawable(Context context, String drawableName) {
        return ResourceHelper.getInstance(context).getDrawableId(drawableName);
    }

    public static int getAttr(Context context, String attrName) {
        return ResourceHelper.getInstance(context).getAttrId(attrName);
    }

    public static int getString(Context context, String stringName) {
        return ResourceHelper.getInstance(context).getStringId(stringName);
    }
}
