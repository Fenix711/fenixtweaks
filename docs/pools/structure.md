All point pool definitions share the syntax below.

```ts
{
  "type": String,
  "name": String,
  "data": Object,
  "growth": {
    "slope": double,
    "exponent": double,
    "intercept": double
  },
  "goals": [
    {
      "level": int,
      "command": String
    }
  ]
}
```

### Type

**Required**

The `type` parameter is a pre-defined string that identifies the type of point pool.

### Name

**Required**

The `name` parameter is used as a display name for the point pool in chat messages.

### Data

**Required**

The `data` parameter is going to be different depending on the `type` of the point pool. This is where `type` specific data is passed to the pool during creation. See a specific type for it's data syntax.

### Growth

This object defines parameters for the function used to determine how many points are required for each level. 

We use the formula `y = m * x^p + b` to define a point pool's level growth.

```
points = slope * level ^ exponent + intercept
```

The growth object is **required**, however all of the following parameters are **optional**.

* **slope:** the larger this number is, the steeper the line is
* **exponent:** this value lets you curve the line
* **intercept:** this value adjusts the required points by a fixed amount

Supplying an empty object, `"growth": {}`, will default to a `1:1`, linear progression where:

```
y = 1 * x^1 + 0
```

To get a feel for how each value affects the leveling point requirements, you can fiddle with the values here: [https://www.desmos.com/calculator/ynttr2s1kk](https://www.desmos.com/calculator/ynttr2s1kk). The x axis is the level and the y axis is the number of points required for that level.

### Goals

Goals are objects which define commands to be run when a player achieves a given level.

You can put as many goals as you want in this list and can define multiple goals with the same level.

All parameters below are required for each goal object.

* **level:** achieving this level will trigger this goal
* **command:** the command to run when triggered, include the preceding `/`