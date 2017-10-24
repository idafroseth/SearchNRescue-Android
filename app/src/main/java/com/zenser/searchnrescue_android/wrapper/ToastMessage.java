package com.zenser.searchnrescue_android.wrapper;

/**
 * Container object for message and level related to Toaster.
 * Holds the the message and int representation for warning level.
 * <p>
 * Usage with {@link Toaster}
 *
 * @see Toaster Toaster
 */
public class ToastMessage {
    private String message;
    private int level;

    /**
     * Create new container for message with level.
     *
     * @param message Message to toast user.
     * @param level   Should be defined by
     *                {@link Toaster#INFO},
     *                {@link Toaster#WARNING},
     *                {@link Toaster#ERROR}
     * @see Toaster
     */
    public ToastMessage(String message, int level) {
        this.message = message;
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isEmpty() {
        return null == message || message.isEmpty();
    }
}
