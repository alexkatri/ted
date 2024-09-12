package temp_classes;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class ArticleMultimediaNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(ArticleMultimediaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String ArticleMultimediaNotFoundHandler(ArticleMultimediaNotFoundException ex) {
        return ex.getMessage();
    }
}
