package com.stylefeng.guns.rest.modular.vo;

import lombok.Data;

/**
 * @description: 返回类
 * @author: huowencheng
 * @created: 2020/09/20 16:28
 */
@Data
public class ResponseVO<T> {

    //0: 成功 1: 业务失败 999: 系统异常
    private int status;

    private String msg;

    private T data;

    private String imgPre;

    private ResponseVO() {}

    public static<T> ResponseVO success(T data) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setData(data);
        return  responseVO;
    }

    public static<T> ResponseVO success(String imgPre, T data) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setData(data);
        responseVO.setImgPre(imgPre);
        return  responseVO;
    }

    public static<T> ResponseVO success(String msg) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setMsg(msg);
        return  responseVO;
    }

    public static<T> ResponseVO serviceFail(String message) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(1);
        responseVO.setMsg(message);
        return  responseVO;
    }

    public static<T> ResponseVO appFail(String message) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(999);
        responseVO.setMsg(message);
        return  responseVO;
    }

}
