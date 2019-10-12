package com.wuzz.config;

/**
 * 返回对象实体类
 *
 * @author wuzz
 * @date 11:30 2019/7/29
 */
public class AjaxJson {
    // 1 正常  -1 未登录 -2 参数异常 -3 通知 -4 系统异常
    private int code = 1;
    private String msg = "操作成功";
    private Object obj;

    public AjaxJson() {
    }

    public AjaxJson(int code) {
        this.code = code;
    }

    public AjaxJson(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
