package com.bosc.txx.exception;

/**
 * 批量转账异常类
 * 
 * @author zhoulei
 * @date 2025/8/31
 */
public class BatchTransferException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 工号
     */
    private String employeeNo;
    
    /**
     * 错误类型
     */
    private String errorType;
    
    public BatchTransferException(String message) {
        super(message);
    }
    
    public BatchTransferException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public BatchTransferException(String employeeNo, String errorType, String message) {
        super(String.format("工号[%s]批量转账失败 - %s: %s", employeeNo, errorType, message));
        this.employeeNo = employeeNo;
        this.errorType = errorType;
    }
    
    public BatchTransferException(String employeeNo, String errorType, String message, Throwable cause) {
        super(String.format("工号[%s]批量转账失败 - %s: %s", employeeNo, errorType, message), cause);
        this.employeeNo = employeeNo;
        this.errorType = errorType;
    }
    
    public String getEmployeeNo() {
        return employeeNo;
    }
    
    public String getErrorType() {
        return errorType;
    }
}
