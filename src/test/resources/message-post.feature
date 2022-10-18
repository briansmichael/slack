@MessagePost
Feature: Message Post
  As a user
  I want to post messages to Slack
  So that they can be received by slack users

  Scenario: Send a direct message to a user
    Given I have a message
    When I send the message
    Then I should receive a successful response
