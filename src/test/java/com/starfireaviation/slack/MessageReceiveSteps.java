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
public class MessageReceiveSteps extends BaseSteps {

    @Before
    public void init() {
        testContext.reset();
    }

    @Given("^There is a message to be delivered$")
    public void thereIsAMessageToBeDelivered() throws Throwable {
        log.info("Given There is a message to be delivered");
    }

    @When("^I poll for a message$")
    public void iPollForAMessage() throws Throwable {
        log.info("When I poll for a message");
    }

    @Then("^I should receive a message$")
    public void iShouldReceiveAMessage() throws Throwable {
        log.info("Then I should receive a message");
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
