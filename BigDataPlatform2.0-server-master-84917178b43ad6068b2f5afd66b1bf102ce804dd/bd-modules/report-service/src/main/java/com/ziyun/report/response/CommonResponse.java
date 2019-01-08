package com.ziyun.report.response;

import com.thoughtworks.xstream.core.BaseException;
import com.ziyun.utils.requests.StatusCodeEnum;

/**
 * 响应格式
 */
public class CommonResponse {

    /**
     * 返回状态
     *
     * @see StatusCodeEnum
     */
    private Integer statusCode;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 错误返回结果msg
     */
    public static final String RESULT_MSG_FAILURE = "操作失败";

    /**
     * 正确返回结果msg
     */
    public static final String RESULT_MSG_SUCCESS = "操作成功";

    /**
     * 返回内容
     */
    private Object data;


    public CommonResponse() {
        this.statusCode = StatusCodeEnum.OK.getCode();
    }

    public CommonResponse(Object data) {
        this.statusCode = StatusCodeEnum.OK.getCode();
        this.data = data;
    }

    public CommonResponse(StatusCodeEnum statusCode, String message) {
        if (statusCode != null) {
            this.statusCode = statusCode.getCode();
        }
        this.message = message;
    }

    public CommonResponse(Object data, StatusCodeEnum statusCode, String message) {
        this.data = data;
        if (statusCode != null) {
            this.statusCode = statusCode.getCode();
        }
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    /**
     * 功能说明：返回一个请求成功的响应模板
     *
     * @param
     * @return CommonResponse
     */
    public static CommonResponse success() {
        CommonResponse response = new CommonResponse();
        response.setStatusCode(StatusCodeEnum.OK.getCode());
        response.setMessage(RESULT_MSG_SUCCESS);
        return response;
    }

    /**
     * 功能说明：返回一个请求成功的响应模板
     *
     * @param data
     * @return CommonResponse
     */
    public static CommonResponse success(Object data) {
        CommonResponse response = new CommonResponse(data);
        response.setStatusCode(StatusCodeEnum.OK.getCode());
        response.setMessage(RESULT_MSG_SUCCESS);
        return response;
    }

    /**
     * 功能说明：返回一个请求失败的响应模板
     *
     * @param message
     * @return CommonResponse 错误类别为默认的INVALID_REQUEST（请求错误）
     */
    public static CommonResponse failure(String message) {
        CommonResponse response = new CommonResponse();
        response.setMessage(message);
        response.setStatusCode(StatusCodeEnum.INVALID_REQUEST.getCode());
        return response;
    }

    /**
     * 功能说明：返回一个请求失败的响应模板
     *
     * @return CommonResponse 错误类别为默认的INVALID_REQUEST（请求错误）
     */
    public static CommonResponse failure() {
        CommonResponse response = new CommonResponse();
        response.setStatusCode(StatusCodeEnum.INVALID_REQUEST.getCode());
        return response;
    }

    /**
     * 功能说明：返回一个请求失败的响应模板
     *
     * @param e 异常参数不能为空
     * @return CommonResponse
     */
    public static CommonResponse failure(Exception e) {
        CommonResponse response = new CommonResponse();
        response.setMessage(RESULT_MSG_FAILURE);
        response.setStatusCode(StatusCodeEnum.INTERNAL_SERVER_ERROR.getCode());
        return response;
    }

    /**
     * 功能说明：返回一个请求失败的响应模板
     *
     * @param e 异常参数不能为空
     * @return CommonResponse
     */
    public static CommonResponse failure(BaseException e) {
        CommonResponse response = new CommonResponse();
        if (e != null) {
            response.setStatusCode(getStatusCode(e));
            response.setMessage(e.getMessage());
        }
        return response;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 功能说明：根据错误类别返回错误码
     *
     * @param e
     * @return Integer
     */
    public static Integer getStatusCode(BaseException e) {
        return StatusCodeEnum.INTERNAL_SERVER_ERROR.getCode();
    }

    @Override
    public String toString() {
        return "CommonResponse{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public String toJsonString() {
        return "{" +
                "\"statusCode\":\"" + statusCode +
                "\", \"message\":\"" + message +
                "\", \"data\":\"" + data +
                "\"}";
    }
}
