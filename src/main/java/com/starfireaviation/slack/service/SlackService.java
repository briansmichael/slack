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

package com.starfireaviation.slack.service;

import java.io.IOException;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.api.ApiTestResponse;

import com.starfireaviation.model.User;
import com.starfireaviation.slack.config.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SlackService {

    /**
     * ApplicationProperties.
     */
    private final ApplicationProperties applicationProperties;

    /**
     * Slack.
     */
    private final Slack slack;

    /**
     * SlackService.
     *
     * @param aProps ApplicationProperties
     */
    public SlackService(final ApplicationProperties aProps) {
        applicationProperties = aProps;
        slack = Slack.getInstance();
        test();
    }

    /**
     * Tests slack API.
     */
    public void test() {
        try {
            ApiTestResponse response = slack.methods().apiTest(r -> r.foo("bar"));
            log.info("Test response: {}", response);
        } catch (IOException | SlackApiException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Sends message to Slack.
     *
     * @param user User
     * @param message message to be sent
     */
    public void send(final User user, final String message) {
        if (user != null && user.isSlackEnabled() && user.isSlackVerified()) {
            try {
                final ChatPostMessageRequest chatPostMessageRequest =
                        ChatPostMessageRequest
                                .builder()
                                .username(user.getSlack())
                                .text(message)
                                .build();
                slack.methods(applicationProperties.getToken()).chatPostMessage(chatPostMessageRequest);
            } catch (IOException | SlackApiException e) {
                log.error(e.getMessage());
            }
        } else {
            try {
                final ChatPostMessageRequest chatPostMessageRequest =
                        ChatPostMessageRequest
                                .builder()
                                .channel(applicationProperties.getGroundSchoolChannel())
                                .text(message)
                                .build();
                slack.methods(applicationProperties.getToken()).chatPostMessage(chatPostMessageRequest);
            } catch (IOException | SlackApiException e) {
                log.error(e.getMessage());
            }
        }
    }

}
