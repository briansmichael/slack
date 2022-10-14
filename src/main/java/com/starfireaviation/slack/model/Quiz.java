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
import java.time.LocalDateTime;
import java.util.List;

/**
 * Quiz.
 */
@Data
public class Quiz implements Serializable {

    /**
     * Default SerialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID.
     */
    private Long id;

    /**
     * Title.
     */
    private String title;

    /**
     * Quiz started?
     */
    private boolean started = false;

    /**
     * LocalDateTime - startTime.
     */
    private LocalDateTime startTime;

    /**
     * Quiz completed?
     */
    private boolean completed = false;

    /**
     * LocalDateTime - completedTime.
     */
    private LocalDateTime completedTime;

    /**
     * Questions.
     */
    private List<Question> questions;

    /**
     * LessonPlan ID.
     */
    private Long lessonPlanId;

    /**
     * QuizType.
     */
    private QuizType quizType;

}
