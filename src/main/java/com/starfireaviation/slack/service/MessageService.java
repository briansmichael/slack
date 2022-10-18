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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starfireaviation.model.Event;
import com.starfireaviation.model.EventType;
import com.starfireaviation.model.Message;
import com.starfireaviation.model.NotificationEventType;
import com.starfireaviation.model.NotificationType;
import com.starfireaviation.model.Question;
import com.starfireaviation.model.Quiz;
import com.starfireaviation.model.User;
import com.starfireaviation.slack.config.ApplicationProperties;
import com.starfireaviation.slack.config.CommonConstants;
import com.starfireaviation.slack.util.TemplateUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import freemarker.template.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

/**
 * MessageService.
 */
@Slf4j
@EnableAsync
public class MessageService {

    /**
     * TEMPLATE_LOCATION.
     */
    private static final String TEMPLATE_LOCATION = "/templates";

    /**
     * ApplicationProperties.
     */
    private final ApplicationProperties applicationProperties;

    /**
     * FreeMarker Configuration.
     */
    private final Configuration freemarkerConfig;

    /**
     * HttpClient.
     */
    private final HttpClient httpClient;

    /**
     * ObjectMapper.
     */
    private final ObjectMapper objectMapper;

    /**
     * SlackService.
     */
    private final SlackService slackService;

    /**
     * MessageService.
     *
     * @param aProps ApplicationProperties
     * @param config Configuration
     * @param client HttpClient
     * @param mapper ObjectMapper
     * @param sService SlackService
     */
    public MessageService(final ApplicationProperties aProps,
                          final Configuration config,
                          final HttpClient client,
                          final ObjectMapper mapper,
                          final SlackService sService) {
        applicationProperties = aProps;
        freemarkerConfig = config;
        httpClient = client;
        objectMapper = mapper;
        slackService = sService;
    }

    /**
     * Poll messages service for any new messages.
     */
    @Async
    @Scheduled(fixedRate = CommonConstants.ONE_THOUSAND)
    public void poll() {
        try {
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://messages.starfireaviation.com/messages?notificationType="
                            + NotificationType.SLACK))
                    .GET()
                    .header(CommonConstants.ORGANIZATION_HEADER_KEY, CommonConstants.DEFAULT_ORGANIZATION)
                    .header(CommonConstants.CLIENT_ID_HEADER_KEY, CommonConstants.SLACK_CLIENT_ID)
                    .header(CommonConstants.CORRELATION_ID_HEADER_KEY, UUID.randomUUID().toString())
                    .build();
            final HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() == HttpStatus.OK.value()) {
                handleMessage(objectMapper.readValue(httpResponse.body(), Message.class));
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Handles a message received from the messages service.
     *
     * @param message Message
     */
    private void handleMessage(final Message message) {
        final NotificationEventType notificationEventType = message.getNotificationEventType();
        switch (notificationEventType) {
            case PASSWORD_RESET:
                sendPasswordResetMsg(message);
                break;
            case USER_SETTINGS:
                sendUserSettingsChangeMsg(message);
                break;
            case USER_VERIFIED:
                sendUserSettingsVerifiedMsg(message);
                break;
            case USER_DELETE:
                sendUserDeleteMsg(message);
                break;
            case EVENT_RSVP:
                sendEventRSVPMsg(message);
                break;
            case EVENT_UPCOMING:
                sendEventUpcomingMsg(message);
                break;
            case EVENT_START:
                sendEventStartMsg(message);
                break;
            case EVENT_COMPLETED:
                sendEventCompletedMsg(message);
                break;
            case EVENT_REGISTER:
                sendEventRegisterMsg(message);
                break;
            case EVENT_UNREGISTER:
                sendEventUnregisterMsg(message);
                break;
            case EVENT_LAST_MIN_REGISTRATION:
                sendEventLastMinRegistrationMsg(message);
                break;
            case QUESTION_ASKED:
                sendQuestionAskedMsg(message);
                break;
            case QUIZ_COMPLETE:
                sendQuizCompleteMsg(message);
                break;
            default:
        }
    }

    /**
     * Sends a message to RSVP for an upcoming event.
     *
     * @param message Message
     */
    private void sendEventRSVPMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final Event event = getEvent(message);
        if (event == null) {
            return;
        }
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            if (event.getEventType() == EventType.GROUNDSCHOOL) {
                slackService.send(user, FreeMarkerTemplateUtils.processTemplateIntoString(
                        freemarkerConfig.getTemplate("gs_event_rsvp.ftl"),
                        TemplateUtil.getModel(user, event, null, applicationProperties)));
            }
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message for an upcoming event.
     *
     * @param message Message
     */
    private void sendEventUpcomingMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final Event event = getEvent(message);
        if (event == null) {
            return;
        }
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            if (event.getEventType() == EventType.GROUNDSCHOOL) {
                slackService.send(user, FreeMarkerTemplateUtils.processTemplateIntoString(
                        freemarkerConfig.getTemplate("gs_user_upcoming.ftl"),
                        TemplateUtil.getModel(user, event, null, applicationProperties)));
            }
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message to a user that an event has started.
     *
     * @param message Message
     */
    private void sendEventStartMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final Event event = getEvent(message);
        if (event == null) {
            return;
        }
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            if (event.getEventType() == EventType.GROUNDSCHOOL) {
                slackService.send(user, FreeMarkerTemplateUtils.processTemplateIntoString(
                        freemarkerConfig.getTemplate("gs_event_start.ftl"),
                        TemplateUtil.getModel(user, event, null, applicationProperties)));
            }
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message that a question has been asked.
     *
     * @param message Message
     */
    private void sendQuestionAskedMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        final Question question = getQuestion(message);
        if (question == null) {
            return;
        }
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);

            slackService.send(user, FreeMarkerTemplateUtils.processTemplateIntoString(
                    freemarkerConfig.getTemplate("question.ftl"),
                    TemplateUtil.getModel(user, null, question, applicationProperties)));
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message for registering for an upcoming event.
     *
     * @param message Message
     */
    private void sendEventRegisterMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final Event event = getEvent(message);
        if (event == null) {
            return;
        }
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            if (event.getEventType() == EventType.GROUNDSCHOOL) {
                slackService.send(user, FreeMarkerTemplateUtils.processTemplateIntoString(
                        freemarkerConfig.getTemplate("gs_event_register.ftl"),
                        TemplateUtil.getModel(user, event, null, applicationProperties)));
            }
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message for unregistering from an upcoming event.
     *
     * @param message Message
     */
    private void sendEventUnregisterMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final Event event = getEvent(message);
        if (event == null) {
            return;
        }
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            if (event.getEventType() == EventType.GROUNDSCHOOL) {
                slackService.send(user, FreeMarkerTemplateUtils.processTemplateIntoString(
                        freemarkerConfig.getTemplate("gs_event_unregister.ftl"),
                        TemplateUtil.getModel(user, event, null, applicationProperties)));
            }
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message for user deletion.
     *
     * @param message Message
     */
    private void sendUserDeleteMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);

            slackService.send(user, FreeMarkerTemplateUtils.processTemplateIntoString(
                    freemarkerConfig.getTemplate("user_delete.ftl"),
                    TemplateUtil.getModel(user, null, null, applicationProperties)));
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message for quiz completion.
     *
     * @param message Message
     */
    private void sendQuizCompleteMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);

            slackService.send(user, FreeMarkerTemplateUtils.processTemplateIntoString(
                    freemarkerConfig.getTemplate("quiz_complete.ftl"),
                    TemplateUtil.getModel(user, null, null, applicationProperties)));
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message for user settings verified.
     *
     * @param message Message
     */
    private void sendUserSettingsVerifiedMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);

            slackService.send(user, FreeMarkerTemplateUtils.processTemplateIntoString(
                    freemarkerConfig.getTemplate("user_settings_verified.ftl"),
                    TemplateUtil.getModel(user, null, null, applicationProperties)));
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message for user settings changed.
     *
     * @param message Message
     */
    private void sendUserSettingsChangeMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);

            slackService.send(user, FreeMarkerTemplateUtils.processTemplateIntoString(
                    freemarkerConfig.getTemplate("user_verify_settings.ftl"),
                    TemplateUtil.getModel(user, null, null, applicationProperties)));
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a password reset message.
     *
     * @param message Message
     */
    private void sendPasswordResetMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);

            slackService.send(user, FreeMarkerTemplateUtils.processTemplateIntoString(
                    freemarkerConfig.getTemplate("password_reset.ftl"),
                    TemplateUtil.getModel(user, null, null, applicationProperties)));
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a last minute message to register/RSVP for an upcoming event.
     *
     * @param message Message
     */
    private void sendEventLastMinRegistrationMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final Event event = getEvent(message);
        if (event == null) {
            return;
        }
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            if (event.getEventType() == EventType.GROUNDSCHOOL) {
                slackService.send(user, FreeMarkerTemplateUtils.processTemplateIntoString(
                        freemarkerConfig.getTemplate("gs_user_last_min_registration.ftl"),
                        TemplateUtil.getModel(user, event, null, applicationProperties)));
            }
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Sends a message to a user that an event has completed.
     *
     * @param message Message
     */
    private void sendEventCompletedMsg(final Message message) {
    }

    /**
     * Process user response.
     *
     * @param to user
     * @param message received from user
     */
    private void processUserResponse(final String to, final String message) {
        // TODO
    }

    private Event getEvent(final Message message) {
        message.getEventId();
        return null;
    }

    private User getUser(final Message message) {
        message.getUserId();
        return null;
    }

    private Question getQuestion(final Message message) {
        message.getQuestionId();
        return null;
    }

    private Quiz getQuiz(final Message message) {
        message.getQuizId();
        return null;
    }
}
