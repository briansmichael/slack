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
import java.util.Optional;

import com.starfireaviation.slack.config.ApplicationProperties;
import com.starfireaviation.slack.exception.InvalidPayloadException;
import com.starfireaviation.slack.model.Event;
import com.starfireaviation.slack.model.EventType;
import com.starfireaviation.slack.model.Message;
import com.starfireaviation.slack.model.Question;
import com.starfireaviation.slack.model.Quiz;
import com.starfireaviation.slack.model.User;
import com.starfireaviation.slack.util.TemplateUtil;
import com.starfireaviation.slack.validation.ResponseValidator;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;

import freemarker.template.Configuration;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

/**
 * SlackService.
 */
@Slf4j
@Service("gsSlackService")
public class MessageService implements SlackMessagePostedListener {

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
     * SlackSession.
     */
    private SlackSession slackSession = null;

    public MessageService(final ApplicationProperties aProps,
                          final Configuration config) {
        applicationProperties = aProps;
        freemarkerConfig = config;
    }

    /**
     * Sends a message to RSVP for an upcoming event.
     *
     * @param message Message
     */
    public void sendEventRSVPMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final Event event = getEvent(message);
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            if (event.getEventType() == EventType.GROUNDSCHOOL) {
                send(user, FreeMarkerTemplateUtils.processTemplateIntoString(
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
    public void sendEventUpcomingMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final Event event = getEvent(message);
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            if (event.getEventType() == EventType.GROUNDSCHOOL) {
                send(user, FreeMarkerTemplateUtils.processTemplateIntoString(
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
    public void sendEventStartMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final Event event = getEvent(message);
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            if (event.getEventType() == EventType.GROUNDSCHOOL) {
                send(user, FreeMarkerTemplateUtils.processTemplateIntoString(
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
    public void sendQuestionAskedMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        final Question question = getQuestion(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);

            send(user, FreeMarkerTemplateUtils.processTemplateIntoString(
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
    public void sendEventRegisterMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final Event event = getEvent(message);
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            if (event.getEventType() == EventType.GROUNDSCHOOL) {
                send(user, FreeMarkerTemplateUtils.processTemplateIntoString(
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
    public void sendEventUnregisterMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final Event event = getEvent(message);
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            if (event.getEventType() == EventType.GROUNDSCHOOL) {
                send(user, FreeMarkerTemplateUtils.processTemplateIntoString(
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
    public void sendUserDeleteMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);

            send(user, FreeMarkerTemplateUtils.processTemplateIntoString(
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
    public void sendQuizCompleteMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        final Quiz quiz = getQuiz(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);

            send(user, FreeMarkerTemplateUtils.processTemplateIntoString(
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
    public void sendUserSettingsVerifiedMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);

            send(user, FreeMarkerTemplateUtils.processTemplateIntoString(
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
    public void sendUserSettingsChangeMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);

            send(user, FreeMarkerTemplateUtils.processTemplateIntoString(
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
    public void sendPasswordResetMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);

            send(user, FreeMarkerTemplateUtils.processTemplateIntoString(
                    freemarkerConfig.getTemplate("password_reset.ftl"),
                    TemplateUtil.getModel(user, null, null, applicationProperties)));
        } catch (IOException | TemplateException e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * {@inheritDoc} Required implementation.
     */
    @Override
    public void onEvent(final SlackMessagePosted event, final SlackSession session) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        // Ignore bot user messages
        if (session.sessionPersona().getId().equals(event.getSender().getId())) {
            return;
        }
        final String message = event.getMessageContent();
        try {
            ResponseValidator.validate(message);
        } catch (InvalidPayloadException e) {
            return;
        }
        final String user = event.getUser().getUserName();
        final String msg = String.format(
                "Slack message received: user [%s]; message [%s]",
                user,
                message);
        log.info(msg);
        processUserResponse(user, message);
    }

    /**
     * Sends a last minute message to register/RSVP for an upcoming event.
     *
     * @param message Message
     */
    public void sendEventLastMinRegistrationMsg(final Message message) {
        if (!applicationProperties.isEnabled()) {
            return;
        }
        final Event event = getEvent(message);
        final User user = getUser(message);
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_LOCATION);
            if (event.getEventType() == EventType.GROUNDSCHOOL) {
                send(user, FreeMarkerTemplateUtils.processTemplateIntoString(
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
    public void sendEventCompletedMsg(final Message message) {
    }

    /**
     * Disconnects SlackSession.
     */
    public void shutdownSlackSession() {
        if (slackSession != null && slackSession.isConnected()) {
            try {
                slackSession.disconnect();
            } catch (IOException e) {
                log.warn("Unable to disconnect SlackSession", e);
            }
        }
    }

    /**
     * Initializes SlackSession.
     */
    private void initSlackSession() {
        if (slackSession == null || !slackSession.isConnected()) {
            if (slackSession == null) {
                slackSession = SlackSessionFactory.createWebSocketSlackSession(applicationProperties.getToken());
            }
            if (!slackSession.isConnected()) {
                try {
                    slackSession.connect();
                    slackSession.addMessagePostedListener(this);
                } catch (IOException e) {
                    log.warn("Unable to connect to Slack", e);
                }
            }
        }
    }

    /**
     * Sends message to Slack.
     *
     * @param user                  User
     * @param message               message to be sent
     */
    private void send(final User user, final String message) {
        initSlackSession();
        if (user != null) {
            slackSession.sendMessageToUser(slackSession.findUserByUserName(user.getSlack()), message, null);
        } else {
            Optional<SlackChannel> slackChannelOptional = slackSession
                    .getChannels()
                    .stream()
                    .filter(
                            slackChannel -> slackChannel.getName().equalsIgnoreCase(
                                    applicationProperties.getGroundSchoolChannel()))
                    .findFirst();
            slackChannelOptional.ifPresent(slackChannel -> slackSession.sendMessage(slackChannel, message));
        }
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
        return null;
    }

    private User getUser(final Message message) {
        return null;
    }

    private Question getQuestion(final Message message) {
        return null;
    }

    private Quiz getQuiz(final Message message) {
        return null;
    }
}
