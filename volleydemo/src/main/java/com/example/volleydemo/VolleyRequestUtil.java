package com.example.volleydemo;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建日期：2017/10/23 on 下午6:02
 * 描述:该类测试以下4种Volley请求：
 * 1. StringRequest
 * 2. JsonObjectRequest
 * 3. JsonArrayObject
 * 4. MyVolleyRequest
 * <p>
 * volleyStringRequestDemo_GET()方法有详细的注释，其他方法对照此方法
 * 作者:yangliang
 */
public class VolleyRequestUtil {
    //Volley请求网络都是基于请求队列的，开发者只要把请求放在请求队列中就可以了，请求队列会依次进行请求
    //一般情况下，一个应用程序如果网络请求没有特别频繁则完全可以只有一个请求队列（对应Application），
    // 如果非常多或其他情况，则可以是一个Activity对应一个网络请求队列，这就要看具体情况了
    //创建请求队列
//  RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

    public static final String VOLLEY_TAG = "tag_volley_request";

    public static final String JUHE_APPKEY = "effd958a513778eadd21f8d29a675644";
    public static final String JUHE_API_URL = "http://v.juhe.cn/postcode/query";

    /*
    * 聚合数据查询邮编对应的地址的url，用于测试
    * 请求示例：http://v.juhe.cn/postcode/query?postcode=邮编&key=申请的KEY
    * http://v.juhe.cn/postcode/query?postcode=210044&key=effd958a513778eadd21f8d29a675644
    */
    private String postcode = "210044";
    private String url_GET = JUHE_API_URL + "?postcode=" + postcode + "&key=" + JUHE_APPKEY;

    /**
     * Volley的StringRequest使用示例
     * HTTP method : GET
     */
    public void volleyStringRequestDemo_GET() {
        //Volley request，参数：请求方式，请求的URL，请求成功的回调，请求失败的回调
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_GET,
                new Response.Listener<String>() {
                    /**
                     * 请求成功的回调
                     * @param response 请求返回的数据
                     */
                    @Override
                    public void onResponse(String response) {
                        // TODO: 2017/10/23 处理返回结果
                        Log.i("tag===onResponse", "GET_StringRequest:" + response);
                        //POST_JsonObjectRequest:{"error_code":10001,"result":null,"reason":"错误的请求KEY!","resultcode":"101"}
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * 请求失败的回调
                     * @param error VolleyError
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: 2017/10/23 处理错误
                        Log.e("tag===onErrorResponse", "GET_StringRequest:" + error.toString());
                    }
                });

        //为request设置tag，通过该tag在全局队列中访问request
        stringRequest.setTag(VOLLEY_TAG);//StringRequestTest_GET

        //将request加入全局队列
        MyApplication.getRequestQueue().add(stringRequest);
    }

    /**
     * Volley的StringRequest使用示例
     * HTTP method : POST
     * 内部注释和方法volleyStringRequestDemo_GET()相同
     */
    public void volleyStringRequestDome_POST() {
        String url = JUHE_API_URL;
        // StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // TODO: 2017/10/23 处理返回结果
                        Log.i("tag===onResponse", "POST_StringRequest:" + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: 2017/10/23 处理错误
                        Log.e("tag===onErrorResponse", "POST_StringRequest:" + error.toString());
                    }
                }) {
            /**
             * 返回含有POST或PUT请求的参数的Map
             * StringRequest中并没有提供设置POST参数的方法，但是当发出POST请求的时候，
             * Volley会尝试调用StringRequest的父类——Request中的getParams()方法来获取POST参数，
             * 那么解决方法自然也就有了，我们只需要在StringRequest的匿名类中重写getParams()方法，在这里设置POST参数就可以了
             */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> paramMap = new HashMap<>();
                // TODO: 处理POST参数
                paramMap.put("postcode", postcode);
                paramMap.put("key", JUHE_APPKEY);
                return paramMap;
            }
        };

        //为request设置tag，通过该tag在全局队列中访问request
        stringRequest.setTag(VOLLEY_TAG);//StringRequest_POST

        //将request加入全局队列
        MyApplication.getRequestQueue().add(stringRequest);
    }

    /**
     * Volley的JsonObjectRequest使用示例
     * HTTP method : GET
     * 内部注释和方法volleyStringRequestDemo_GET()相同
     * 第三个参数：request的参数(POST)，传入null表示没有需要传递的参数,此处为GET方式传输，参数已经包含在URL中
     */
    public void volleyJsonObjectRequestDemo_GET() {
        // JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_GET, null, new Response.Listener<JSONObject>()
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_GET, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // TODO: 2017/10/23 处理返回结果
                        Log.i("tag===onResponse", "GET_JsonObjectRequest:" + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: 2017/10/23 处理错误
                        Log.e("tag===onErrorResponse", "GET_JsonObjectRequest:" + error.toString());
                    }
                });

        //为request设置tag，通过该tag在全局队列中访问request
        jsonObjectRequest.setTag(VOLLEY_TAG);//JsonObjectRequestTest_GET

        //将request加入全局队列
        MyApplication.getRequestQueue().add(jsonObjectRequest);

    }

    /**
     * Volley的JsonObjectRequest使用示例
     * HTTP method : POST
     * 内部注释和方法volleyStringRequestDemo_GET()相同
     */
    public void volleyJsonObjectRequestDome_POST() {
        String url = JUHE_API_URL;
        Map<String, String> paramMap = new HashMap<>();
        // TODO: 处理POST参数
        paramMap.put("postcode", postcode);
        paramMap.put("key", JUHE_APPKEY);
        //产生JsonObject类型的参数
        JSONObject jsonParam = new JSONObject(paramMap);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonParam,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // TODO: 处理返回结果
                        Log.i("tag===onResponse", "POST_JsonObjectRequest:" + response.toString());
                        //POST_JsonObjectRequest:{"error_code":10001,"result":null,"reason":"错误的请求KEY!","resultcode":"101"}

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: 2017/10/23 处理错误
                        Log.e("tag===onErrorResponse", "POST_JsonObjectRequest:" + error.toString());
                    }
                });

        //为request设置tag，通过该tag在全局队列中访问request
        jsonObjectRequest.setTag(VOLLEY_TAG);//JsonObjectRequestTest_POST

        //将request加入全局队列
        MyApplication.getRequestQueue().add(jsonObjectRequest);

    }

    /**
     * Volley的JsonArrayRequest使用示例
     * HTTP method : GET
     * 内部注释和方法volleyStringRequestDemo_GET()相同
     */
    public void volleyJsonArrayRequestDemo_GET() {
        //第三个参数：request的参数(POST)，传入null表示没有需要传递的参数,此处为GET方式传输，参数已经包含在URL中
        //JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url_GET, null, new Response.Listener<JSONArray>()
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url_GET, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // TODO: 处理返回结果
                        Log.i("tag===onErrorResponse", "GET_JsonArrayRequest:" + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: 处理错误
                        Log.e("tag===onErrorResponse", "GET_JsonArrayRequest:" + error.toString());
                    }
                });

        //为request设置tag，通过该tag在全局队列中访问request
        jsonArrayRequest.setTag(VOLLEY_TAG);//JsonArrayRequestTest_GET

        //将request加入全局队列
        MyApplication.getRequestQueue().add(jsonArrayRequest);
    }

}
