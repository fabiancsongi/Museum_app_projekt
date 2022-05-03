package hu.mobilalk.museum_app;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.Random;

public class OneAsyncLoader extends AsyncTaskLoader<String> {
    public OneAsyncLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        int ms = 2000;

        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Belépés vendégként";
    }
}
