package com.example.simpleconnectmysql.Helper


import android.util.Log;
import com.example.simpleconnectmysql.Model.BaseParam

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


class JSONParser {
    constructor()

    companion object {
        var iss: InputStream? = null
        var jObj: JSONObject? = null
        var jArr: JSONArray? = null
        var json = ""
        var error = ""
    }



    fun makeHttpRequest(
        url: String?, method: String,
        params: MutableList<out BaseParam>?
    ): JSONObject? { // Making HTTP request
        var url = url
        try { // check for request method
            if (method == "POST") { // request method is POST
// defaultHttpClient
                val httpClient: HttpClient = DefaultHttpClient()
                val httpPost = HttpPost(url)
                httpPost.setEntity(UrlEncodedFormEntity(params))
                try {
                    Log.e("API123", " " + convertStreamToString(httpPost.getEntity().getContent()))
                    Log.e("API123", httpPost.getURI().toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val httpResponse: HttpResponse = httpClient.execute(httpPost)
                Log.e("API123", "" + httpResponse.getStatusLine().getStatusCode())
                error = java.lang.String.valueOf(httpResponse.getStatusLine().getStatusCode())
                val httpEntity: HttpEntity = httpResponse.getEntity()
                iss = httpEntity.getContent()
            } else if (method == "GET") { // request method is GET
                val httpClient = DefaultHttpClient()
                val paramString: String = URLEncodedUtils.format(params, "utf-8")
                url += "?$paramString"
                val httpGet = HttpGet(url)
                val httpResponse: HttpResponse = httpClient.execute(httpGet)
                val httpEntity: HttpEntity = httpResponse.getEntity()
                iss = httpEntity.getContent()
            }
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: ClientProtocolException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            val reader = BufferedReader(
                InputStreamReader(
                    iss, "iso-8859-1"
                ), 8
            )
            val sb = StringBuilder()
            var line: String? = null
            while (reader.readLine().also({ line = it }) != null) {
                sb.append(line + "\n")
            }
            iss!!.close()
            json = sb.toString()
            Log.d("API123", json)
        } catch (e: Exception) {
            Log.e("Buffer Error", "Error converting result $e")
        }
        // try to parse the string to a JSON object
        try {
            jObj = JSONObject(json)
            jObj!!.put("error_code", error)
        } catch (e: JSONException) {
            Log.e("JSON Parser", "Error parsing data $e")
        }
        // return JSON String
        return jObj
    }

    @Throws(java.lang.Exception::class)
    private fun convertStreamToString(iss: InputStream): String? {
        val reader = BufferedReader(InputStreamReader(iss))
        val sb = java.lang.StringBuilder()
        var line: String? = null
        while (reader.readLine().also { line = it } != null) {
            sb.append(line)
        }
        iss.close()
        return sb.toString()
    }


}