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

package com.starfireaviation.slack.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Question.
 */
@Data
public class Question implements Serializable {

    /**
     * Remote Question ID.
     */
    private Long remoteId;

    /**
     * Old Question ID.
     */
    private Long oldQuestionId;

    /**
     * Question Text.
     */
    private String text;

    /**
     * Chapter ID.
     */
    private Long chapterId;

    /**
     * SMC ID.
     */
    private Long smcId;

    /**
     * ACS ID.
     */
    private Long acsId;

    /**
     * Source.
     */
    private String source;

    /**
     * Course.
     */
    private String course;

    /**
     * Last Modified.
     */
    private Date lastModified;

    /**
     * Unit.
     */
    private String unit;

    /**
     * SubUnit.
     */
    private String subUnit;

    /**
     * Explanation.
     */
    private String explanation;

    /**
     * LearningStatementCode.
     */
    private String learningStatementCode;

    /**
     * Answers.
     */
    private List<Answer> answers;

    /**
     * ReferenceMaterials.
     */
    private List<ReferenceMaterial> referenceMaterials;
}
