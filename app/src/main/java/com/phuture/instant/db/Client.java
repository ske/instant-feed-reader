package com.phuture.instant.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.phuture.instant.db.migrations.AddNewSources20200115;
import com.phuture.instant.model.Source;

public class Client extends RoomDatabase.Callback {

    protected Context ctx;
    protected static Client _instance;
    protected Database db;
    protected boolean firstInit = false;

    private Client(Context ctx) {
        this.ctx = ctx;

        final AddNewSources20200115 migration1_2 = new AddNewSources20200115();

        db = Room.databaseBuilder(ctx, Database.class, "instant")
                .allowMainThreadQueries() // FIXME should remove this and do db ops async
                .addCallback(this)
                .addMigrations(migration1_2)
                .build();

        db.sourceDao().getAll();

        if (firstInit) {
            populateBaseData();
        }
    }

    protected void populateBaseData() {
        try {
            db.sourceDao().insert(new Source("hang_hu","hang.hu", "https://hang.hu/feed"));
            db.sourceDao().insert(new Source("444_hu", "444.hu", "https://444.hu/feed"));
            db.sourceDao().insert(new Source("azonnali_hu", "azonnali.hu", "https://azonnali.hu/rss"));

            db.sourceDao().insert(new Source("telex_hu","telex.hu", "https://telex.hu/rss"));
            db.sourceDao().insert(new Source("hvg_hu", "hvg.hu", "https://hvg.hu/rss"));
            db.sourceDao().insert(new Source("media1_hu", "media1.hu", "https://media1.hu/feed"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
        super.onCreate(db);
        firstInit = true;
    }

    public static synchronized Client instance(Context ctx) {
        if (_instance == null) {
            _instance = new Client(ctx);
        }
        return _instance;
    }

    public Database getDb() {
        return db;
    }
}
