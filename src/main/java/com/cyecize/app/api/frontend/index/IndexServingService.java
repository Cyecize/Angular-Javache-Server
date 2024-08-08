package com.cyecize.app.api.frontend.index;

import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.solet.HttpSoletResponse;

public interface IndexServingService {

    boolean serveIndexFile(HttpSoletRequest request, HttpSoletResponse response);
}
