Feature: As a user I want to be able to search for train stations by train station name

  Background:
    And the following train stations exist:
      |name                 |
      |DARTFORD             |
      |DARTMOUTH            |
      |TOWER HILL           |
      |DERBY                |
      |LIVERPOOL            |
      |LIVERPOOL LIME STREET|
      |PADDINGTON           |
      |EUSTON               |
      |LONDON BRIDGE        |
      |VICTORIA             |

  Scenario: Search for DART
    Given the service is up and running
    And I am on a RESTFull client screen
    When I enter the train station name 'DART'
    Then the response code should be 200
    And the search should return a JSON response as following:
    """
      {
        "stations": [
          {
            "name": "DARTFORD"
          },
          {
            "name": "DARTMOUTH"
          }
        ]
      }
    """

  Scenario: Search for LIVERPOOL
    Given the service is up and running
    And I am on a RESTFull client screen
    When I enter the train station name 'LIVERPOOL'
    Then the response code should be 200
    And the search should return a JSON response as following:
    """
      {
        "stations": [
          {
            "name": "LIVERPOOL"
          },
          {
            "name": "LIVERPOOL LIME STREET"
          }
        ]
      }
    """

  Scenario: Search for DERBY
    Given the service is up and running
    And I am on a RESTFull client screen
    When I enter the train station name 'DERBY'
    Then the response code should be 200
    And the search should return a JSON response containing the 'DERBY' station

  Scenario: Search for KINGS CROSS
    Given the service is up and running
    And I am on a RESTFull client screen
    When I input 'KINGS CROSS'
    Then the search should return no characters and no stations
    # Assumption: An exception is thrown with response code 400 when the search returns no characters and no stations

  Scenario: Search for All
    Given the service is up and running
    And I am on a RESTFull client screen
    When I call the searchAll API
    Then the response code should be 200
    And the search should return all the stations available in a JSON format

  Scenario: service is down
    Given the service is not up and running
    And I am on a RESTFull client screen
    When I call the searchAll API
    Then the response code should be 503

  Scenario: api doesn't exist
    Given the service is up and running
    And I am on a RESTFull client screen
    When I call the searchAllThatDoesNotExist API
    Then the response code should be 404

  Scenario: check health of the service
    Given the service is up and running
    And I am on a RESTFull client screen
    When I call the health API
    Then the response code should be 200
    And the response body should contain a JSON message telling that the service is 'UP'
