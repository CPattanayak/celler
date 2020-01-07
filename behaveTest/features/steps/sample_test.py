from behave import given, then, when
import behave_webdriver


@given('sample paload "{test_case_scenario}"')
def sample_given(context,test_case_scenario):
    print('Running sample_given')
@when('calling service')
def sample_when(context):
    print('calling sample when')


@then('giving response')
def sample_then(context):
    print('calling sample_then')

@when(u'I open google.com')
def step_impl(context):
   """

   :type context: object
   """
   # context.vdisplay = Xvfb()
   # context.vdisplay.start()
   print("> Starting the browser")
   context.driver = behave_webdriver.Chrome.headless()
   context.driver.get("http://celler:8082/swagger/index.html")

@then(u'the title should contain "{title}"')
def step_then(context,title):
    browser_title = context.driver.title
    assert 'Swagger UI' in browser_title, 'Found "%s" instead ' % browser_title
    context.driver.close()
    context.driver.quit()
    # context.vdisplay.stop()
