from behave import given, then, when
@given('sample paload')
def sample_given(context):
    print('Running sample_given')
@when('calling service')
def sample_when(context):
    print('calling sample when')


@then('giving response')
def sample_then(context):
    print('calling sample_then')
