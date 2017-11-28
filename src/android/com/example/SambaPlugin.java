/**
 */
package com.example;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

import java.io.IOException;
import java.util.Date;

public class SambaPlugin extends CordovaPlugin {
	private static final String TAG = "SambaPlugin";
	NtlmPasswordAuthentication auth;

	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);

		Log.d(TAG, "Initializing " + TAG);
	}

	public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
		if (action.equals("echo")) {
			String phrase = args.getString(0);
			// Echo back the first argument
			Log.d(TAG, phrase);
		} else if (action.equals("getFiles")) {
			JSONArray files = this.getFiles(args.getString(0), args.getString(1), args.getString(2));
			callbackContext.success(files);
			return true;
		}

		else if (action.equals("getDate")) {
			// An example of returning data back to the web layer
			final PluginResult result = new PluginResult(PluginResult.Status.OK, (new Date()).toString());
			callbackContext.sendPluginResult(result);

		}
		return true;
	}

	private JSONArray getFiles(String user, String pass, String path) throws JSONException {
		try {
			auth = new NtlmPasswordAuthentication(user + ":" + pass);
			JSONArray result = new JSONArray();
			SmbFile dir = new SmbFile(path, this.auth);
			SmbFile[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				JSONObject object = new JSONObject();
				object.put("name", files[i].getName());
				object.put("path", files[i].getPath());
				object.put("createTime", (int) (files[i].createTime() / 1000));
				object.put("size", files[i].getContentLength());
				result.put(object);
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			JSONArray result = new JSONArray();
			JSONObject object = new JSONObject();
			object.put("error", e.getMessage());
			result.put(object);
			return result;
		}
	}

}
