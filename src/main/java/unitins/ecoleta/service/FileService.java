package unitins.ecoleta.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

import jakarta.validation.ValidationException;

public abstract class FileService {

    private final String PATH;

    private final Set<String> SUPPORTED_MIME_TYPES;

    private final int MAX_FILE_SIZE;

    private final static String DEFAULT_IMAGE_PATH = "/assets/images/default.png";

    public FileService(String path, Set<String> supportedMimeTypes, int maxFileSize) {
        this.PATH = System.getProperty("user.home") + File.separator + "spring" + File.separator + path + File.separator;
        this.SUPPORTED_MIME_TYPES = supportedMimeTypes;
        this.MAX_FILE_SIZE = maxFileSize;
    }

    public File download(String nomeArquivo) {
        File file = new File(PATH + nomeArquivo);

        if (!file.exists() || !file.isFile())
            file = loadDefaultImage();

        return file;
    }

    public String upload(String nomeArquivo, byte[] arquivo) {
        try {
            verificarTamanhoArquivo(arquivo);
            String mimeType = getMimeType(nomeArquivo);

            Path diretorioArquivos = Paths.get(PATH);
            Files.createDirectories(diretorioArquivos);

            String extensao = mimeType != null && mimeType.contains("/") ?
                    mimeType.substring(mimeType.lastIndexOf("/") + 1) : getExtensionFromFilename(nomeArquivo);

            String nomeAleatorioArquivo = gerarNomeArquivo(extensao);
            Path destino = diretorioArquivos.resolve(nomeAleatorioArquivo);

            File file = destino.toFile();
            if (file.exists())
                throw new IOException("Este arquivo já existe");

            // cria o arquivo (criar diretório já feito acima)
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(arquivo);
                fos.flush();
            }

            return nomeAleatorioArquivo;
        } catch (Exception e) {
            throw new ValidationException("Erro ao processar arquivo: " + e.getMessage());
        }
    }

    public boolean delete(String nomeArquivo) {
        try {
            return Files.deleteIfExists(Paths.get(PATH).resolve(nomeArquivo));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String gerarNomeArquivo(String extensao) {
        String nome = UUID.randomUUID() + "." + extensao;
        if (Paths.get(PATH).resolve(nome).toFile().exists()) {
            nome = gerarNomeArquivo(extensao);
        }
        return nome;
    }

    private void verificarTamanhoArquivo(byte[] arquivo) throws IOException {
        if (arquivo.length > MAX_FILE_SIZE)
            throw new IOException("Arquivo maior que " + (MAX_FILE_SIZE / (1024 * 1024)) + " Mb");
    }

    private String getMimeType(String nomeArquivo) throws IOException {
        // Tenta inferir pelo arquivo (pode retornar null)
        Path possible = Paths.get(nomeArquivo);
        String mimeType = null;
        try {
            mimeType = Files.probeContentType(possible);
        } catch (Exception ignored) {
        }

        // Se probeContentType não encontrou (null), tenta pela extensão
        if (mimeType == null) {
            String ext = getExtensionFromFilename(nomeArquivo);
            if (ext != null) {
                switch (ext.toLowerCase()) {
                    case "png": mimeType = "image/png"; break;
                    case "jpg":
                    case "jpeg": mimeType = "image/jpeg"; break;
                    case "gif": mimeType = "image/gif"; break;
                    case "webp": mimeType = "image/webp"; break;
                    case "pdf": mimeType = "application/pdf"; break;
                    default: mimeType = null;
                }
            }
        }

        if (mimeType == null || !SUPPORTED_MIME_TYPES.contains(mimeType)) {
            throw new IOException("O tipo de arquivo " + mimeType + " não é suportado!");
        }

        return mimeType;
    }

    private String getExtensionFromFilename(String nomeArquivo) {
        if (nomeArquivo == null) return "bin";
        int idx = nomeArquivo.lastIndexOf('.');
        if (idx == -1 || idx == nomeArquivo.length() - 1) return "bin";
        return nomeArquivo.substring(idx + 1);
    }

    private File loadDefaultImage() {
        try {
            URL resourceUrl = FileService.class.getResource(DEFAULT_IMAGE_PATH);
            if (resourceUrl != null) {
                return new File(resourceUrl.toURI());
            } else {
                // default not found — retornar null (ou lance exceção se preferir)
                return null;
            }
        } catch (URISyntaxException e) {
            return null;
        }
    }
}
