/**
 * Instructions:
 * - "debug" is used to (in)activate debug messages from a layer (value-reasoning, goal-reasoning, planning or acting)
 * - "vl::tests" is used to (in)activate the sequence of tests to demonstrate fonctionality of value-reasoning layer
 * - "planner::debug" is used to (in)activate debug messages of the planner (which can be quite verbose if active)
 */

/*
 * IMPORTANT:
 * - This is the only file that can be edit in this package and sub-packages
 * - Do not change the includes, only change the boolean of the beliefs below
 */

vl::debug(true).
gl::debug(true).
pl::debug(true).
al::debug(true).
planner::debug(false).

vl::tests(false).
demo::demo(false).

//{ include("tests/demo.asl") }
//{ include("tests/value-reasoning_tests.asl") }

{ include("layers/value-reasoning.asl") }
{ include("layers/goal-reasoning.asl") }
{ include("layers/planning.asl") }
{ include("layers/acting.asl") }
