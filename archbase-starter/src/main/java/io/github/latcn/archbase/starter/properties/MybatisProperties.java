package io.github.latcn.archbase.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "archbase.mybatis")
public class MybatisProperties {
    private boolean enabled = false;
    private boolean autoFill = true;

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public boolean isAutoFill() { return autoFill; }
    public void setAutoFill(boolean autoFill) { this.autoFill = autoFill; }
}