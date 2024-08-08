package com.cyecize.app.api.frontend.index;

import com.cyecize.app.error.ApiException;
import com.cyecize.http.HttpStatus;
import com.cyecize.javache.JavacheConfigValue;
import com.cyecize.javache.services.JavacheConfigService;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.solet.HttpSoletResponse;
import com.cyecize.solet.SoletConstants;
import com.cyecize.summer.common.annotations.Configuration;
import com.cyecize.summer.common.annotations.PostConstruct;
import com.cyecize.summer.common.annotations.Service;
import com.cyecize.summer.utils.PathUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IndexServingServiceImpl implements IndexServingService {

    @Configuration(SoletConstants.SOLET_CONFIG_ASSETS_DIR)
    private final String assetsDir;

    @Configuration(SoletConstants.SOLET_CFG_WORKING_DIR)
    private final String workingDir;

    @Configuration(SoletConstants.SOLET_CONFIG_SERVER_CONFIG_SERVICE_KEY)
    private final JavacheConfigService serverCfg;

    private Path indexPath1;
    private Path indexPath2;

    @PostConstruct
    public void init() {
        this.indexPath1 = Path.of(PathUtils.appendPath(this.assetsDir, "/index.html"));

        final String path2 = PathUtils.appendPath(
                this.workingDir,
                this.serverCfg.getConfigParamString(JavacheConfigValue.APP_RESOURCES_DIR_NAME)
        );
        this.indexPath2 = Path.of(path2, "/index.html");
    }

    @Override
    public boolean serveIndexFile(HttpSoletRequest request,
            HttpSoletResponse response) {
        final Path path;
        if (Files.exists(this.indexPath1)) {
            path = this.indexPath1;
        } else if (Files.exists(this.indexPath2)) {
            path = this.indexPath2;
        } else {
            return false;
        }

        response.setStatusCode(HttpStatus.OK);

        final byte[] result;
        try {
            result = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiException(String.format(
                    "Could not server index.html for file request %s. Error while reading index file",
                    request.getRequestURI()
            ));
        }

        response.addHeader("Content-Type", "text/html");
        response.addHeader("Content-Disposition", "inline;");
        response.addHeader("Content-Length", result.length + "");

        try {
            response.getOutputStream().write(result);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiException(String.format(
                    "Could not serve index.html file for request %s!",
                    request.getRequestURI()
            ));
        }

        return true;
    }

    @Data
    static class Pair {

        public final String key;
        public final String value;
    }
}