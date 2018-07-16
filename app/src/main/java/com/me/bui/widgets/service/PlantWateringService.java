package com.me.bui.widgets.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.me.bui.widgets.provider.PlantContract;
import com.me.bui.widgets.utils.PlantUtils;

/**
 * Created by mao.bui on 7/16/2018.
 */
public class PlantWateringService extends IntentService {

    public static final String ACTION_WATER_PLANTS ="com.me.bui.widgets.service.action.water_plants";

    public PlantWateringService() {
        super("PlantWateringService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_WATER_PLANTS.equals(action)) {
                handleActionWaterPlants();
            }
        }
    }

    public static void startActionWaterPlants(Context context) {
        Intent intent = new Intent(context, PlantWateringService.class);
        intent.setAction(ACTION_WATER_PLANTS);
        context.startService(intent);
    }

    private void handleActionWaterPlants() {
        Uri PLANTS_URI = PlantContract.BASE_CONTENT_URI.buildUpon().appendPath(PlantContract.PATH_PLANTS).build();
        ContentValues contentValues = new ContentValues();
        long timeNow = System.currentTimeMillis();
        contentValues.put(PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME, timeNow);
        // Update only plants that are still alive
        getContentResolver().update(
                PLANTS_URI,
                contentValues,
                PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME+">?",
                new String[]{String.valueOf(timeNow - PlantUtils.MAX_AGE_WITHOUT_WATER)});
    }
}
