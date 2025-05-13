package com.org.fundatec.sistemabancario.exception.handler;

import com.org.fundatec.sistemabancario.exception.EntidadeNaoEncontradaException;
import com.org.fundatec.sistemabancario.exception.OperacaoInvalidaException;
import com.org.fundatec.sistemabancario.exception.ValidacaoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErroResponse> trataEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex) {
        ErroResponse errorResponse = new ErroResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({OperacaoInvalidaException.class, ValidacaoException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErroResponse> trataOperacaoInvalida(RuntimeException ex) {
        ErroResponse errorResponse = new ErroResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErroResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder sb = new StringBuilder(50);
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            sb.append(fieldName);
            sb.append(" - ");
            sb.append(errorMessage);
            sb.append("; ");
        });
        ErroResponse errorResponse = new ErroResponse(HttpStatus.BAD_REQUEST.value(), sb.toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErroResponse> trataErroGenerico(Exception ex) {
        ErroResponse errorResponse = new ErroResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocorreu um erro interno no servidor");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}