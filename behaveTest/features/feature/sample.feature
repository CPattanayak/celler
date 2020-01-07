Feature: Sample functional test

    Scenario Outline: sample senario
        Given sample paload "<test_case_scenario>"
        When calling service
        Then giving response
        Examples:
          | test_case_scenario |
          |  abc               |

      Scenario Outline: Showing off behave and Selenium
        Given sample paload "<test_case_scenario>"
        When I open google.com
        Then the title should contain "<result>"
        Examples:
          | result |
          |  abc               |
