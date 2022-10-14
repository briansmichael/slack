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

package com.starfireaviation.slack.util;

import com.starfireaviation.model.ResponseOption;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SMSResponseParser.
 */
public class ResponseParser {

    /**
     * A_PATTERN.
     */
    public static final Pattern A_PATTERN = Pattern.compile("^(A)$", Pattern.CASE_INSENSITIVE);

    /**
     * B_PATTERN.
     */
    public static final Pattern B_PATTERN = Pattern.compile("^(B)$", Pattern.CASE_INSENSITIVE);

    /**
     * C_PATTERN.
     */
    public static final Pattern C_PATTERN = Pattern.compile("^(C)$", Pattern.CASE_INSENSITIVE);

    /**
     * D_PATTERN.
     */
    public static final Pattern D_PATTERN = Pattern.compile("^(D)$", Pattern.CASE_INSENSITIVE);

    /**
     * STOP_PATTERN.
     */
    public static final Pattern STOP_PATTERN = Pattern.compile("^(STOP)$", Pattern.CASE_INSENSITIVE);

    /**
     * SKIP_PATTERN.
     */
    public static final Pattern SKIP_PATTERN = Pattern.compile("^(SKIP)$", Pattern.CASE_INSENSITIVE);

    /**
     * CONFIRM_PATTERN.
     */
    public static final Pattern CONFIRM_PATTERN = Pattern.compile("^(CONFIRM)$", Pattern.CASE_INSENSITIVE);

    /**
     * DECLINE_PATTERN.
     */
    public static final Pattern DECLINE_PATTERN = Pattern.compile("^(DECLINE)$", Pattern.CASE_INSENSITIVE);

    /**
     * Determines user's response.
     *
     * @param message to be evaluated
     * @return SMSResponseOption
     */
    public static ResponseOption determineResponse(final String message) {
        ResponseOption responseOption = ResponseOption.UNKNOWN;
        if (isStopResponse(message)) {
            responseOption = ResponseOption.STOP;
        } else if (isSkipResponse(message)) {
            responseOption = ResponseOption.SKIP;
        } else if (isDeclineResponse(message)) {
            responseOption = ResponseOption.DECLINE;
        } else if (isConfirmResponse(message)) {
            responseOption = ResponseOption.CONFIRM;
        } else if (isAResponse(message)) {
            responseOption = ResponseOption.A;
        } else if (isBResponse(message)) {
            responseOption = ResponseOption.B;
        } else if (isCResponse(message)) {
            responseOption = ResponseOption.C;
        } else if (isDResponse(message)) {
            responseOption = ResponseOption.D;
        }
        return responseOption;
    }

    /**
     * Evaluates user's message to see if it is a STOP message.
     *
     * @param message to be evaluated
     * @return if a STOP message
     */
    private static boolean isStopResponse(final String message) {
        boolean isStop = false;
        Matcher matcher = STOP_PATTERN.matcher(message);
        if (matcher.find()) {
            isStop = true;
        }
        return isStop;
    }

    /**
     * Evaluates user's message to see if it is a SKIP message.
     *
     * @param message to be evaluated
     * @return if a SKIP message
     */
    private static boolean isSkipResponse(final String message) {
        boolean isSkip = false;
        Matcher matcher = SKIP_PATTERN.matcher(message);
        if (matcher.find()) {
            isSkip = true;
        }
        return isSkip;
    }

    /**
     * Evaluates user's message to see if it is a CONFIRM message.
     *
     * @param message to be evaluated
     * @return if a CONFIRM message
     */
    private static boolean isConfirmResponse(final String message) {
        boolean isConfirm = false;
        Matcher matcher = CONFIRM_PATTERN.matcher(message);
        if (matcher.find()) {
            isConfirm = true;
        }
        return isConfirm;
    }

    /**
     * Evaluates user's message to see if it is a DECLINE message.
     *
     * @param message to be evaluated
     * @return if a DECLINE message
     */
    private static boolean isDeclineResponse(final String message) {
        boolean isDecline = false;
        Matcher matcher = DECLINE_PATTERN.matcher(message);
        if (matcher.find()) {
            isDecline = true;
        }
        return isDecline;
    }

    /**
     * Evaluates user's message to see if it is a A message.
     *
     * @param message to be evaluated
     * @return if a A message
     */
    private static boolean isAResponse(final String message) {
        boolean isA = false;
        Matcher matcher = A_PATTERN.matcher(message);
        if (matcher.find()) {
            isA = true;
        }
        return isA;
    }

    /**
     * Evaluates user's message to see if it is a B message.
     *
     * @param message to be evaluated
     * @return if a B message
     */
    private static boolean isBResponse(final String message) {
        boolean isB = false;
        Matcher matcher = B_PATTERN.matcher(message);
        if (matcher.find()) {
            isB = true;
        }
        return isB;
    }

    /**
     * Evaluates user's message to see if it is a C message.
     *
     * @param message to be evaluated
     * @return if a C message
     */
    private static boolean isCResponse(final String message) {
        boolean isC = false;
        Matcher matcher = C_PATTERN.matcher(message);
        if (matcher.find()) {
            isC = true;
        }
        return isC;
    }

    /**
     * Evaluates user's message to see if it is a D message.
     *
     * @param message to be evaluated
     * @return if a D message
     */
    private static boolean isDResponse(final String message) {
        boolean isD = false;
        Matcher matcher = D_PATTERN.matcher(message);
        if (matcher.find()) {
            isD = true;
        }
        return isD;
    }

}
