package com.techie.dharmaraj.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.techie.dharmaraj.bakingapp.R;


public class UpdateWidgetIntentService extends IntentService {

    private static final String ACTION_UPDATE_WIDGET = "com.techie.dharmaraj.bakingapp.action.UPDATE_WIDGET";

    public UpdateWidgetIntentService() {
        super("UpdateWidgetIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdateWidget(Context context) {
        Intent intent = new Intent(context, UpdateWidgetIntentService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_WIDGET.equals(action)) {
                handleActionUpdateWidget();
            }
        }
    }

    /**
     * Handle action UpdateWidget in the provided background thread with the provided
     * parameters.
     */
    private void handleActionUpdateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidgetProvider.class));
        //Trigger data update to handle the ListView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
        IngredientsWidgetProvider.updateAppWidgets(this,appWidgetManager,appWidgetIds);
    }
}
