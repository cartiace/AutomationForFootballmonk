@BarcelonaSort
Feature: Barcelona Jersey Sort Validations

  # The Background runs BEFORE every Scenario
  Background: Navigate to Barcelona Page
    Given user navigates to FootballMonk home page and logs in with email "shauryathakur968@gmail.com" and password "@Shaury69"
    When user hovers on Football Jerseys menu and clicks Barcelona Jersey
    Then user should be on the Barcelona jerseys page

  # Test Case 1
  Scenario: Validate Product Names contain Barcelona
    Then top 5 products should all contain "Barcelona" in their name

  # Test Case 2
  Scenario: Validate Price Sorting Low to High
    When user selects sort by "price" low to high
    Then prices should be sorted from low to high
