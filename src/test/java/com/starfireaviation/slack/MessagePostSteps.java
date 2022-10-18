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

package com.starfireaviation.slack;

import com.starfireaviation.slack.config.CommonConstants;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Slf4j
public class MessagePostSteps extends BaseSteps {

    @Before
    public void init() {
        testContext.reset();
    }

    @Given("^I have a message$")
    public void iHaveAMessage() throws Throwable {
        log.info("Given I have a message");
    }

    @When("^I send the message$")
    public void iSendTheMessage() throws Throwable {
        log.info("I send the message");
    }

    @Then("^I should receive a successful response$")
    public void iShouldReceiveASuccessful() throws Throwable {
        log.info("I should receive a successful response");
    }

    private HttpHeaders getHeaders() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (testContext.getClientId() != null) {
            httpHeaders.add(CommonConstants.CLIENT_ID_HEADER_KEY, testContext.getClientId());
        }
        if (testContext.getOrganization() != null) {
            httpHeaders.add(CommonConstants.ORGANIZATION_HEADER_KEY, testContext.getOrganization());
        }
        if (testContext.getCorrelationId() != null) {
            httpHeaders.add(CommonConstants.CORRELATION_ID_HEADER_KEY, testContext.getCorrelationId());
        }
        return httpHeaders;
    }
}
