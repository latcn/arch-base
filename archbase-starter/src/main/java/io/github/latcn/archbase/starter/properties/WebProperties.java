package io.github.latcn.archbase.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "archbase.web")
public class WebProperties {
    private boolean enabled = false;
    private String exceptionHandler = "default";

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public String getExceptionHandler() { return exceptionHandler; }
    public void setExceptionHandler(String exceptionHandler) { this.exceptionHandler = exceptionHandler; }
}