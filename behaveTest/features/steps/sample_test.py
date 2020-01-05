from behave import given, then, when
@given('sample paload "{test_case_scenario}"')
def sample_given(context,test_case_scenario):
    print('Running sample_given')
@when('calling service')
def sample_when(context):
    print('calling sample when')


@then('giving response')
def sample_then(context):
    print('calling sample_then')
