/*
 *  Copyright (C) 2022 Starfire Aviation, LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.starfireaviation.slack.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starfireaviation.slack.service.MessageService;
import com.starfireaviation.slack.service.SlackService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.http.HttpClient;

@Configuration
@EnableScheduling
@EnableConfigurationProperties({
        ApplicationProperties.class,
})
public class ServiceConfig {

    /**
     * MessageService.
     *
     * @param aProps ApplicationProperties
     * @param config Freemarker configuration
     * @param httpClient HttpClient
     * @param objectMapper ObjectMapper
     * @param slackService SlackService
     * @return MessageService
     */
    @Bean
    public MessageService messageService(final ApplicationProperties aProps,
                                         final freemarker.template.Configuration config,
                                         final HttpClient httpClient,
                                         final ObjectMapper objectMapper,
                                         final SlackService slackService) {
        return new MessageService(aProps, config, httpClient, objectMapper, slackService);
    }

    /**
     * HttpClient.
     *
     * @return HttpClient
     */
    @Bean
    public HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }

    /**
     * ObjectMapper.
     *
     * @return ObjectMapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /**
     * SlackService.
     *
     * @param applicationProperties ApplicationProperties
     * @return SlackService
     */
    @Bean
    public SlackService slackService(final ApplicationProperties applicationProperties) {
        return new SlackService(applicationProperties);
    }
}
