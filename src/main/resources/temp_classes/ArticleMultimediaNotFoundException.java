package temp_classes;

public class ArticleMultimediaNotFoundException extends RuntimeException {

    public ArticleMultimediaNotFoundException(Long id) {
        super("Could not find multimedia " + id);
    }
}
