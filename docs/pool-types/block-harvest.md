The `block_harvest` pool type awards points to players when the successfully harvest a block and allows defining different point values per block. 

### Data

The data for this pool type looks like this:

```ts
"data": {
  "points": {
    String: double,
    ...
  }
}
```

The `String` key in the `points` object is a block string that defines an in-world block state to match in the form `<domain>:<path>:<meta>` where `<meta>` can be a wildcard: `*`.

The `double` defines the number of points to be awarded for harvesting the block.

!!! warning
    Block strings describe an in-world block state, not an item stack, and can deviate from what you would expect.

    For example, the block strings for logs and furnaces have a different meta value depending on which direction they're facing and leaves have a different meta value depending on their decay state. 

### Data Example

In this example, mining `minecraft:stone` awards `1` point and `minecraft:iron_ore` awards `5` points.

```js
"data": {
  "points": {
    "minecraft:stone": 1,
    "minecraft:iron_ore": 5
  }
}
```

### Full Example

This is what a `block_harvest` point pool might look like:

```js
{
  "type": "block_harvest",
  "name": "Mining",
  "data": {
    "points": {
      "minecraft:stone": 1,
      "minecraft:iron_ore": 5
    }
  },
  "growth": {
    "slope": 20,
    "exponent": 2,
    "intercept": 100
  },
  "goals": [
    {
      "level": 1,
      "command": "/xp 40 @p"
    }
  ]
}
```