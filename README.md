# Cost-Register-Automata-library
a java library for Cost Register Automata

program structure: 

Main

CRA

ACRA



Configuration: <qi, registersState> // holds the current state and the current regs values

Rule: <ri,regsList,change> // struct for: “ri = rj+rk+...+rl+d” 

DeltaImage: <qj,updateRules for(𝛿(qi,𝜎)))> //the delta func. type is: DeltaImage[][] for QxRules

Pair: <Key, Value> // a simple implementation of Pair due to techincal problems

Parser: // a wrapping class for parsing additive rules methods, ease creating ACRAs

UpdateRuleList: Rule[] // list of all update rules, the domain of the second argument in the cartesian product of the delta function

