package nick.iamjob.util;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import timber.log.Timber;

public class GlobalTagTree extends Timber.Tree {
    private static final String TAG = "asdf";  // I am not creative
    private static final int MAX_LOG_LENGTH = 4000;

    /**
     * Copy/pasted from Timber.DebugTree, with all uses of `tag` replaced by the TAG constant
     */
    @Override
    protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {
        if (message.length() < MAX_LOG_LENGTH) {
            if (priority == Log.ASSERT) {
                Log.wtf(TAG, message);
            } else {
                Log.println(priority, TAG, message);
            }
            return;
        }

        // Split by line, then ensure each line can fit into Log's maximum length.
        for (int i = 0, length = message.length(); i < length; i++) {
            int newline = message.indexOf('\n', i);
            newline = newline != -1 ? newline : length;
            do {
                int end = Math.min(newline, i + MAX_LOG_LENGTH);
                String part = message.substring(i, end);
                if (priority == Log.ASSERT) {
                    Log.wtf(TAG, part);
                } else {
                    Log.println(priority, TAG, part);
                }
                i = end;
            } while (i < newline);
        }
    }
}
