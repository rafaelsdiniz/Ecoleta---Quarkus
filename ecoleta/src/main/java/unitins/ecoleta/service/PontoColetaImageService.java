package unitins.ecoleta.service;

import java.io.File;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PontoColetaImageService extends FileService {

    public PontoColetaImageService() {
        super(
            "images" + File.separator + "ponto-coleta",
            Set.of("image/jpg", "image/jpeg", "image/png"),
            1024 * 1024 * 5 // 5 MB
        );
    }
}
