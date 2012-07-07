//
//  PushNotifications.java
//
// Pushwoosh, 01/07/12.
//
// Pushwoosh Push Notifications Plugin for Cordova Android
// www.pushwoosh.com
//
// MIT Licensed

package com.pushwoosh.test.plugin.pushnotifications;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.arellomobile.android.push.PushManager;
import com.google.android.gcm.GCMRegistrar;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PushNotifications extends Plugin
{
    public static final String REGISTER = "registerDevice";
    public static final String UNREGISTER = "unregisterDevice";

    HashMap<String, String> callbackIds = new HashMap<String, String>();

    private static PushNotifications instance = null;
    private static String pushMessage = null;

    public PushNotifications()
    {
        super();
    }

    /**
     * Called when the system is about to start resuming a previous activity.
     *
     * @param multitasking Flag indicating if multitasking is turned on for app
     */
    public void onPause(boolean multitasking)
    {
        super.onPause(multitasking);
        instance = null;
    }

    /**
     * Called when the activity will start interacting with the user.
     *
     * @param multitasking Flag indicating if multitasking is turned on for app
     */
    public void onResume(boolean multitasking)
    {
        super.onResume(multitasking);
        instance = this;

        if (pushMessage != null)
        {
            String jsStatement = String
                    .format("window.plugins.pushNotification.notificationCallback(%s);", pushMessage);
            sendJavascript(jsStatement);
            pushMessage = null;
        }
    }

    /**
     * Called when the activity receives a new intent.
     */
    public void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        checkMessage(intent);
    }

    /**
     * The final call you receive before your activity is destroyed.
     */
    public void onDestroy()
    {
        super.onDestroy();

        instance = null;
    }

    /**
     * Called when a message is sent to plugin.
     *
     * @param id   The message id
     * @param data The message data
     */
    public void onMessage(String id, Object data)
    {
        super.onMessage(id, data);
    }

    @Override
    public void setContext(CordovaInterface ctx)
    {
        super.setContext(ctx);

        if (pushMessage != null)
        {
            String jsStatement = String
                    .format("window.plugins.pushNotification.notificationCallback(%s);", pushMessage);
            sendJavascript(jsStatement);
            pushMessage = null;
        }

        instance = this;
    }

    @Override
    public PluginResult execute(String action, JSONArray data, String callbackId)
    {
        Log.d("PushNotifications", "Plugin Called");

        PluginResult result = null;
        if (REGISTER.equals(action))
        {
            callbackIds.put("registerDevice", callbackId);

            JSONObject params = null;
            try
            {
                params = data.getJSONObject(0);
            } catch (JSONException e)
            {
                return new PluginResult(Status.ERROR);
            }
            PushManager mPushManager = null;
            try
            {
                mPushManager = new PushManager(ctx.getContext(), params.getString("appid"),
                                               params.getString("projectid"));
            } catch (JSONException e)
            {
                return new PluginResult(Status.ERROR);
            }

            try
            {
                mPushManager.onStartup(null, ctx.getContext());
            } catch (IllegalArgumentException e)
            {
                Toast.makeText(ctx.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            result = new PluginResult(Status.NO_RESULT);
            result.setKeepCallback(true);

            return result;
        }

        if (UNREGISTER.equals(action))
        {
            callbackIds.put("unregisterDevice", callbackId);
            result = new PluginResult(Status.NO_RESULT);
            result.setKeepCallback(true);

            GCMRegistrar.unregister(ctx.getContext());
            return result;
        }

        Log.d("DirectoryListPlugin", "Invalid action : " + action + " passed");
        return new PluginResult(Status.INVALID_ACTION);
    }

    private void checkMessage(Intent intent)
    {
        if (null != intent)
        {
            if (intent.hasExtra(PushManager.PUSH_RECEIVE_EVENT))
            {
                doOnMessageReceive(intent.getExtras().getString(PushManager.PUSH_RECEIVE_EVENT));
            }
            else if (intent.hasExtra(PushManager.REGISTER_EVENT))
            {
                doOnRegistered(intent.getExtras().getString(PushManager.REGISTER_EVENT));
            }
            else if (intent.hasExtra(PushManager.UNREGISTER_EVENT))
            {
                doOnUnregisteredError(intent.getExtras().getString(PushManager.UNREGISTER_EVENT));
            }
            else if (intent.hasExtra(PushManager.REGISTER_ERROR_EVENT))
            {
                doOnRegisteredError(intent.getExtras().getString(PushManager.REGISTER_ERROR_EVENT));
            }
            else if (intent.hasExtra(PushManager.UNREGISTER_ERROR_EVENT))
            {
                doOnUnregistered(intent.getExtras().getString(PushManager.UNREGISTER_ERROR_EVENT));
            }
        }
    }

    public void doOnRegistered(String registrationId)
    {
        String callbackId = callbackIds.get("registerDevice");
        PluginResult result = new PluginResult(Status.OK, registrationId);
        success(result, callbackId);
    }

    public void doOnRegisteredError(String errorId)
    {
        String callbackId = callbackIds.get("registerDevice");
        PluginResult result = new PluginResult(Status.OK, errorId);
        error(result, callbackId);
    }

    public void doOnUnregistered(String registrationId)
    {
        String callbackId = callbackIds.get("unregisterDevice");
        PluginResult result = new PluginResult(Status.OK, registrationId);
        success(result, callbackId);
    }

    public void doOnUnregisteredError(String errorId)
    {
        String callbackId = callbackIds.get("unregisterDevice");
        PluginResult result = new PluginResult(Status.OK, errorId);
        error(result, callbackId);
    }

    public void doOnMessageReceive(String message)
    {
        String jsStatement = String.format("window.plugins.pushNotification.notificationCallback(%s);", message);
        sendJavascript(jsStatement);
    }
}
