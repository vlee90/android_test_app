package com.vincent.android;

import android.content.Context;
import android.support.annotation.NonNull;

import com.lee.vincent.android.slidingtabscolors.R;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tagmanager.Container;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.tagmanager.TagManager;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by vincentlee on 3/7/17.
 */

public class AnalyticsManager {

    private static AnalyticsManager sAnalyticsManager;
    private static Container sContainer;
    private static TagManager sTagManager;
    private static ContainerHolder sContainerHolder;
    private static ArrayList<Map<String, Object>> storedHits = new ArrayList<>();

    public static AnalyticsManager getInstance() {
        if (sAnalyticsManager == null) {
            sAnalyticsManager = new AnalyticsManager();
        }
        return sAnalyticsManager;
    }

    public static void startAnalytics(Context context) {
        if (sTagManager == null) {
            sTagManager = TagManager.getInstance(context);
            sTagManager.setVerboseLoggingEnabled(true);
            PendingResult<ContainerHolder> pendingResult = sTagManager.loadContainerPreferFresh("GTM-KTTVCK5", R.raw.gtm_kttvck5);
            pendingResult.setResultCallback(new ResultCallback<ContainerHolder>() {
                @Override
                public void onResult(@NonNull ContainerHolder containerHolder) {
                    sContainerHolder = containerHolder;
                    sContainer = containerHolder.getContainer();

                    pushStoredHits();
                }
            });
        }
    }

    public static void dataLayerPush(Map<String, Object> values) {
        filterEvent(values);
    }

    private static void filterEvent(Map<String, Object> values) {
        if (sContainer == null) {
            storedHits.add(values);
        }
        else {
            sTagManager.getDataLayer().push(values);
        }
    }

    private static void pushStoredHits() {
        for(Map<String, Object> hit : storedHits) {
            DataLayer dataLayer = sTagManager.getDataLayer();
            dataLayer.push(hit);
        }
    }

}
