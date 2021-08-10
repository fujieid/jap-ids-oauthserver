package com.louie.oauth.handler;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.fujieid.jap.ids.endpoint.ErrorEndpoint;
import com.fujieid.jap.ids.exception.IdsException;
import com.fujieid.jap.ids.model.IdsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * 统一异常处理类<br>
 * 捕获程序所有异常，针对不同异常，采取不同的处理方式
 *
 * @author lapati5
 */
@RestControllerAdvice
public class AuthExceptionHandler {
    protected Log log = LogFactory.get();

    @Resource
    private HttpServletResponse response;

    @ExceptionHandler(value = {IdsException.class})
    public void idsExceptionHandle(IdsException e) throws IOException {
        new ErrorEndpoint().showErrorPage(e.getError(), e.getErrorDescription(), response);
    }

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public Object handle(Throwable e) {
        if (e instanceof UndeclaredThrowableException) {
            e = ((UndeclaredThrowableException) e).getUndeclaredThrowable();
        }
        log.error(ExceptionUtil.stacktraceToString(e), e);
        return new IdsResponse<String, String>().error(e.getMessage());
    }
}
