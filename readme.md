# Fusion modeling at different iterations

(v1) Modifying Eshan's (@eshanking) HAL model to see what fusion at it's most simple does to solid tumor evolution patterns.

## current list of model assumptions (at its most simple, no fusion)

(1) Cells have a moore neighborhood in which to divide/fuse
(2) At each time step, cells can divide, mutate and become resistant (only if parental type to start), or do nothing. these params are specific to the individual cell types, not the model more generally.
(3) cells can only divide into empty neighboring grid spaces


