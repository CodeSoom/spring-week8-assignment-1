package com.codesoom.assignment.common.exception;

import com.codesoom.assignment.common.exception.dto.ErrorResponse;
import com.codesoom.assignment.session.exception.InvalidTokenException;
import com.codesoom.assignment.session.exception.LoginFailException;
import com.codesoom.assignment.user.exception.UserEmailDuplicationException;
import com.codesoom.assignment.user.exception.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@RestControllerAdvice
public class ControllerErrorAdvice extends ResponseEntityExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFound() {
        return new ErrorResponse("User not found");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserEmailDuplicationException.class)
    public ErrorResponse handleUserEmailIsAlreadyExisted() {
        return new ErrorResponse("User's email address is already existed");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LoginFailException.class)
    public ErrorResponse handleLoginFailException() {
        return new ErrorResponse("Log-in failed");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidTokenException.class)
    public ErrorResponse handleInvalidAccessTokenException() {
        return new ErrorResponse("Invalid access token");
    }

    // 현재는 Security가 알아서 isAuthenticated() 실패 시 401, hasAnyAuthority() 실패 시 403으로 반환한다
    // 예외 응답 body 메시지를 컨트롤 하려면 핸들러에서 잡아줘야함
    // 그럴려면 내가 직접 AccessDeniedException 에서 isAuthenticated 실패인지 hasAnyAuthority 실패인지 판별해줘야 함
    // TODO: isAuthenticated()가 실패하면 401, hasAnyAuthority()가 실패하면 403으로 던지는 방법 서칭 필요
/*    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse handleAccessDeniedException() {
        return new ErrorResponse("Access denied");
    }

    // TODO: AccessDeniedException 핸들링 완료 시 주석 해제 예정
    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<ErrorResponse> handleException(final HttpServletRequest request,
                                                            final Exception exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.from(request, exception));
    }*/

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.from(exception));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse handleConstraintValidateError(ConstraintViolationException exception) {
        String messageTemplate = getViolatedMessage(exception);
        return new ErrorResponse(messageTemplate);
    }

    private String getViolatedMessage(ConstraintViolationException exception) {
        String messageTemplate = null;
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            messageTemplate = violation.getMessageTemplate();
        }
        return messageTemplate;
    }
}
