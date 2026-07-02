package io.github.latcn.archbase.web.spring.context;

public class RequestContext {
    private final String traceId;
    private final Long userId;
    private final String tenantId;

    private RequestContext(Builder builder) {
        this.traceId = builder.traceId;
        this.userId = builder.userId;
        this.tenantId = builder.tenantId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getTraceId() { return traceId; }
    public Long getUserId() { return userId; }
    public String getTenantId() { return tenantId; }

    public static class Builder {
        private String traceId;
        private Long userId;
        private String tenantId;

        public Builder traceId(String traceId) {
            this.traceId = traceId;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public RequestContext build() {
            return new RequestContext(this);
        }
    }
}