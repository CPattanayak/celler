

SECTION_SEPARATOR_CHAR = '-'
DEFAULT_SECTION_SEPARATOR = SECTION_SEPARATOR_CHAR * 10
FEATURE_SECTION_SEPARATOR = SECTION_SEPARATOR_CHAR * 10
SCENARIO_SECTION_SEPARATOR = SECTION_SEPARATOR_CHAR * 15
STEP_SECTION_SEPARATOR = SECTION_SEPARATOR_CHAR * 20

def before_step(context, step):

    print("{separator} Start Feature: [{}] Scenario: [{}] Step: [{}] {separator}\r\n".format(context.feature.name, context.scenario.name, step.name, separator=STEP_SECTION_SEPARATOR))


def after_step(context, step):

    print("{separator} End Feature: [{}] Scenario:[{}] Step: [{}]{separator}\r\n".format(context.feature.name, context.scenario.name, step.name, separator=STEP_SECTION_SEPARATOR))


def before_scenario(context, scenario):

    print("{separator} Start Feature: [{}] Scenario: [{}] {separator}\r\n".format(context.feature.name, context.scenario.name, separator=SCENARIO_SECTION_SEPARATOR))


def after_scenario(context, scenario):

    print("{separator} EndFeature: [{}] Scenario: [{}] {separator}\r\n".format(context.feature.name, context.scenario.name, separator=SCENARIO_SECTION_SEPARATOR))


def before_feature(context, feature):

    print("{separator} Start Feature: [{}] {separator}\r\n".format(context.feature.name, separator=FEATURE_SECTION_SEPARATOR))


def after_feature(context, feature):

    print("{separator} End Feature: [{}] {separator}\r\n".format(context.feature.name, separator=FEATURE_SECTION_SEPARATOR))


def before_tag(context, tag):

    pass


def after_tag(context, tag):

    pass


def before_all(context):
    pass




#===================- test code ===============

