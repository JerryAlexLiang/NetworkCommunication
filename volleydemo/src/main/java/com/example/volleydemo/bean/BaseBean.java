package com.example.volleydemo.bean;

/**
 * 创建日期：2017/10/24 on 下午3:32
 * 描述:为了统一数据，新建BaseBean类
 * 这里使用了泛型T来对data进行处理，这样不管返回的结果如何，
 * 我们都不用再新建类去处理status以及desc，直接解析data即可。
 * 作者:yangliang
 */
public class BaseBean<T> {

    //返回码
    private int status;

    //返回信息
    private String desc;

    //我们需要的数据
    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
