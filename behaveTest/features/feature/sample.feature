Feature: Sample functional test

    Scenario Outline: sample senario
        Given sample paload "<test_case_scenario>"
        When calling service
        Then giving response
        Examples:
          | test_case_scenario |
          |  abc               |


