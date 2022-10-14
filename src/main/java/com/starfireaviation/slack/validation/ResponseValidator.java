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

package com.starfireaviation.slack.validation;

import com.starfireaviation.slack.exception.InvalidPayloadException;
import com.starfireaviation.slack.util.ResponseParser;

/**
 * ResponseValidator.
 */
public class ResponseValidator {

    /**
     * Response Validation.
     *
     * @param message String
     * @throws InvalidPayloadException when response is not valid
     */
    public static void validate(final String message) throws InvalidPayloadException {
        switch (ResponseParser.determineResponse(message)) {
            case A:
            case B:
            case C:
            case D:
            case CONFIRM:
            case DECLINE:
            case SKIP:
            case STOP:
                return;
            default:
                throw new InvalidPayloadException();
        }
    }

}
