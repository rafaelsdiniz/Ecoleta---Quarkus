package unitins.ecoleta.service;

import java.io.File;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MaterialImageService extends FileService {

    public MaterialImageService() {
        super("images" + File.separator + "material",
              Set.of("image/jpg", "image/jpeg", "image/png"),
              1024 * 1024 * 5  // 5MB
        );
    }
}
