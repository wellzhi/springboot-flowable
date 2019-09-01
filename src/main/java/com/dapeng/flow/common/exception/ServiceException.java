package com.dapeng.flow.common.exception;

import com.dapeng.flow.common.result.ResponseCode;


/**
 * 服务（业务）异常如“ 账号或密码错误 ”，该异常只做INFO级别的日志记录
 *
 * @author liuws
 */
public class ServiceException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -1341525886349376572L;
	
	private int code;
    private String extraMessage;

    public ServiceException() {
        this.code = ResponseCode.FAILED.getCode();
        this.extraMessage = ResponseCode.FAILED.getMessage();
    }

    public ServiceException(Throwable cause) {
        super(cause);
        this.code = ResponseCode.FAILED.getCode();
        this.extraMessage = ResponseCode.FAILED.getMessage();
    }

    public ServiceException(String extraMessage) {
        super(extraMessage);
        this.code = ResponseCode.FAILED.getCode();
        this.extraMessage = extraMessage;
    }

    public ServiceException(String extraMessage, Throwable cause) {
        super(extraMessage, cause);
        this.code = ResponseCode.FAILED.getCode();
        this.extraMessage = extraMessage;
    }

    public ServiceException(int code, String extraMessage) {
        super(extraMessage);
        this.code = code;
        this.extraMessage = extraMessage;
    }

    public ServiceException(int code, String extraMessage, Throwable cause) {
        super(extraMessage, cause);
        this.code = code;
        this.extraMessage = extraMessage;
    }

    /**
     * 推荐用法
     */
    public ServiceException(ResponseCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.extraMessage = resultCode.getMessage();
    }

    public ServiceException(ResponseCode resultCode, Throwable cause) {
        super(resultCode.getMessage(), cause);
        this.code = resultCode.getCode();
    }

    public int getCode() {
        return code;
    }

    public String getExtraMessage() {
        return extraMessage;
    }
}
