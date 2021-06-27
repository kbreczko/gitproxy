package com.github.kbreczko.gitproxy.configurations;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GitproxyTestProperties {
    public static final String DB_DOCKER_IMAGE = "mariadb:10.5.8";
    public static final String INIT_SCRIPT_PATH = "init.sql";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "root";
    public static final String DB_NAME = "gitproxy_test";
}
