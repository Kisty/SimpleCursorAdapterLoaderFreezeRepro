package uk.co.imagitech.simplecursoradapterloaderfreezerepro;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.os.OperationCanceledException;

import java.util.Arrays;
import java.util.List;

public class UserCursorLoader extends AsyncTaskLoader<Cursor> {

    AppDatabase dbHelper = null;

    Cursor mCursor = null;

    public UserCursorLoader(Context context) {
        super(context);

        //Get database
        dbHelper = Room.databaseBuilder(context,
                AppDatabase.class, "users.db")
                .fallbackToDestructiveMigration().build();


    }

    /* Runs on a worker thread */
    @Override
    public Cursor loadInBackground() {
        synchronized (this) {
            if (isLoadInBackgroundCanceled()) {
                throw new OperationCanceledException();
            }
        }

        //Add test entries if first launch
        if (dbHelper.userDao().getCount() < 20) {
            List<User> users = Arrays.asList(
                    new User("Alice", "Armstrong"),
                    new User("Bob", "Bernard"),
                    new User("Charlie", "Chaplin"),
                    new User("Derrick", "Dumpling"),
                    new User("Edmund", "Esquire"),
                    new User("Alice", "Armstrong"),
                    new User("Bob", "Bernard"),
                    new User("Charlie", "Chaplin"),
                    new User("Derrick", "Dumpling"),
                    new User("Edmund", "Esquire"),
                    new User("Alice", "Armstrong"),
                    new User("Bob", "Bernard"),
                    new User("Charlie", "Chaplin"),
                    new User("Derrick", "Dumpling"),
                    new User("Edmund", "Esquire"),
                    new User("Alice", "Armstrong"),
                    new User("Bob", "Bernard"),
                    new User("Charlie", "Chaplin"),
                    new User("Derrick", "Dumpling"),
                    new User("Edmund", "Esquire"));
            dbHelper.userDao().insertAll(users);
        }

        return dbHelper.userDao().getAll();
    }

    /* Runs on the UI thread */
    @Override
    public void deliverResult(Cursor cursor) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            if (cursor != null) {
                cursor.close();
            }
            return;
        }
        Cursor oldCursor = mCursor;
        mCursor = cursor;

        if (isStarted()) {
            super.deliverResult(cursor);
        }

        if (oldCursor != null && oldCursor != cursor && !oldCursor.isClosed()) {
            oldCursor.close();
        }
    }

    /**
     * Starts an asynchronous load of the contacts list data. When the result is ready the callbacks
     * will be called on the UI thread. If a previous load has been completed and is still valid
     * the result may be passed to the callbacks immediately.
     * <p>
     * Must be called from the UI thread
     */
    @Override
    protected void onStartLoading() {
        if (mCursor != null) {
            deliverResult(mCursor);
        }
        if (takeContentChanged() || mCursor == null) {
            forceLoad();
        }
    }

    /**
     * Must be called from the UI thread
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        mCursor = null;
    }
}
