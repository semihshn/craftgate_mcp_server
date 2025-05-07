package com.craftgate.mcp.mcp_craftgate;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolConfig {

    @Bean
    public ToolCallbackProvider craftgateTools(CraftgatePaymentInfoTool craftgatePaymentInfoTool) {
        return MethodToolCallbackProvider.builder().toolObjects(craftgatePaymentInfoTool).build();
    }
} 