package uk.co.imagitech.simplecursoradapterloaderfreezerepro;

import android.arch.persistence.room.RoomDatabase;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView listView = findViewById(android.R.id.list);
        adapter = new SimpleCursorAdapter(MainActivity.this, android.R.layout.simple_list_item_multiple_choice, null, new String[]{"first_name"}, new int[]{android.R.id.text1}, 0);
        listView.setAdapter(adapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        getSupportLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @NonNull
            @Override
            public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                return new UserCursorLoader(MainActivity.this);
            }

            @Override
            public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                adapter.changeCursor(data);

                //In real app, search through the list to restore user's last selected items
                List<String> findNames = Arrays.asList("Alice", "Bob", "Edmund", "Charlie");
                if (data != null) {
                    while (data.moveToNext()) {
                        String firstName = data.getString(data.getColumnIndex("first_name"));
                        for (String findName : findNames) {
                            if (findName.equals(firstName)) {
                                Log.d("MAIN", "Found " + firstName);
                                listView.setItemChecked(listView.getHeaderViewsCount() + data.getPosition(), true);
                            }
                        }
                    }
                }
            }

            @Override
            public void onLoaderReset(@NonNull Loader<Cursor> loader) {
                adapter.changeCursor(null);
            }
        });
    }
}
