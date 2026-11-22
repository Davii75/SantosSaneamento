package com.exemplo.chamados.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class TratadorDeErros {

    /** Foto maior que 5 MB: volta para o formulário com um aviso, em vez de mostrar erro feio. */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String fotoGrande() {
        return "redirect:/chamados/novo?fotoGrande";
    }
}
