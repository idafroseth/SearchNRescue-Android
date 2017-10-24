package com.zenser.searchnrescue_android.wrapper;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zenser.searchnrescue_android.R;


/**
 * A static class for toasting the user with a custom message.
 * The message will be represented with a color, icon and text, defined by the usage.
 *
 * @see Toast
 */
public class Toaster {

    public static final int INFO = 0;
    public static final int ERROR = 1;
    public static final int WARNING = 2;

    /**
     * Creating a new Toast for user, with ToastMessage and a level for defining the color.
     *
     * @param context      The context to use. Usually your {@link android.app.Application}
     *                     or {@link android.app.Activity} object.
     * @param toastMessage The container object level and message
     */
    public static void createToast(Context context, ToastMessage toastMessage) {
        switch (toastMessage.getLevel()) {
            case INFO:
                showInfo(context, toastMessage.getMessage());
                break;
            case ERROR:
                showError(context, toastMessage.getMessage());
                break;
            default:
                showWarning(context, toastMessage.getMessage());
        }
    }

    /**
     * Method with default duration for {@link #showInfo(Context, CharSequence, int)}
     */
    public static void showInfo(Context context, CharSequence text) {
        showInfo(context, text, Toast.LENGTH_LONG);
    }

    /**
     * Method with default duration for {@link #showError(Context, CharSequence, int)}
     */
    public static void showError(Context context, CharSequence text) {
        showError(context, text, Toast.LENGTH_LONG);
    }

    /**
     * Method with default duration for {@link #showWarning(Context, CharSequence, int)}
     */
    public static void showWarning(Context context, CharSequence text) {
        showWarning(context, text, Toast.LENGTH_LONG);
    }

    /**
     * Creating a new Toast for user, with INFO as level.
     *
     * @param context  The context to use. Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param text     The text to show. Can be formatted text.
     * @param duration How long to display the message.  Either {@link Toast#LENGTH_SHORT} or
     *                 {@link Toast#LENGTH_LONG}
     */
    public static void showInfo(Context context, CharSequence text, int duration) {
        if (context != null) {
            View view = getView(context, R.drawable.rounded_toaster_success);

            ImageView img = (ImageView) view.findViewById(R.id.toaster_img_icon);
            img.setImageResource(R.drawable.ic_check_circle_24dp);

            createToast(context, view, text, duration);
        }
    }

    /**
     * Creating a new Toast for user, with ERROR as level.
     *
     * @param context  The context to use. Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param text     The text to show. Can be formatted text.
     * @param duration How long to display the message.  Either {@link Toast#LENGTH_SHORT} or
     *                 {@link Toast#LENGTH_LONG}
     */
    public static void showError(Context context, CharSequence text, int duration) {
        if (context != null) {
            View view = getView(context, R.drawable.rounded_toaster_error);

            ImageView img = (ImageView) view.findViewById(R.id.toaster_img_icon);
            img.setImageResource(R.drawable.ic_error_24dp);

            createToast(context, view, text, duration);
        }
    }

    /**
     * Creating a new Toast for user, with WARNING as level.
     *
     * @param context  The context to use. Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param text     The text to show. Can be formatted text.
     * @param duration How long to display the message.  Either {@link Toast#LENGTH_SHORT} or
     *                 {@link Toast#LENGTH_LONG}
     */
    public static void showWarning(Context context, CharSequence text, int duration) {
        if (context != null) {
            View view = getView(context, R.drawable.rounded_toaster_warning);

            ImageView img = (ImageView) view.findViewById(R.id.toaster_img_icon);
            img.setImageResource(R.drawable.ic_warning_24dp);

            createToast(context, view, text, duration);
        }
    }

    private static View getView(Context context, int backgroundResId) {

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.toaster_small, null);
        view.setBackground(ContextCompat.getDrawable(context, backgroundResId));

        return view;
    }

    private static void createToast(Context context, View view, CharSequence text, int duration) {
        ((TextView) view.findViewById(R.id.toaster_txt_description)).setText(text);
        showToast(context, view, duration);
    }

    private static void showToast(Context context, View view, int duration) {
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        toast.setDuration(duration);
        toast.setView(view);
        toast.show();
    }
}
