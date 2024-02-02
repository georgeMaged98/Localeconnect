package gcpstorage;

import com.google.cloud.storage.Storage;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.storage.GoogleStorageResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/gcp")
public class GCPWebController {

    @Value("${gcs-resource-bucket}")
    private String bucketName;

    private Storage storage;

    private GCPWebController(Storage storage) {
        this.storage = storage;
    }

    @GetMapping(value = "/")
    public String readGcsFile(@RequestParam("filename") Optional<String> filename)
            throws IOException {
        if (filename.isPresent())
            return StreamUtils.copyToString(fetchResource(filename.get()).getInputStream(),
                    Charset.defaultCharset())
                    + "\n";
        else {
            return "";
        }

    }

    @PostMapping(value = "/")
    public String writeGcs(
            @RequestBody String data, @RequestParam("filename") Optional<String> filename)
            throws IOException {

        Resource resourceToUpdate;
        if (filename.isPresent()) {
            resourceToUpdate = fetchResource(filename.get());
            return updateResource(resourceToUpdate, data);
        } else {
            return "";
        }

    }

    private String updateResource(Resource resource, String data) throws IOException {
        try (OutputStream os = ((WritableResource) resource).getOutputStream()) {
            os.write(data.getBytes());
        }
        return "file was updated\n";
    }

    private GoogleStorageResource fetchResource(String filename) {
        return new GoogleStorageResource(
                this.storage, String.format("gs://%s/%s", this.bucketName, filename));
    }
}
