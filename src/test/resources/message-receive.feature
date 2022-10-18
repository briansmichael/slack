@MessageReceive
Feature: Message Receive
  As an application
  I want to receive messages from the messages application
  So that they can be sent to slack users

  Scenario: Receive a message to be sent to a user
    Given There is a message to be delivered
    When I poll for a message
    Then I should receive a message
