package com.starfireaviation.slack.service;

import java.io.IOException;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.api.ApiTestResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SlackService {

    private Slack slack;

    public SlackService() {
        slack = Slack.getInstance();
    }

    public void test() throws IOException, SlackApiException {
        ApiTestResponse response = slack.methods().apiTest(r -> r.foo("bar"));
        log.info("response: {}", response);
    }
}