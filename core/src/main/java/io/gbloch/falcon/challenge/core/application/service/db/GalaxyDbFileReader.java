package io.gbloch.falcon.challenge.core.application.service.db;

import com.google.common.graph.MutableValueGraph;

public interface GalaxyDbFileReader {

    MutableValueGraph<String, Integer> readFile(String dbFilePath) throws GalaxyDbException;
}
