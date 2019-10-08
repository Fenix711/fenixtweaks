The `ms_damage_dealt` pool type awards points to players when they deal damage and the point-per-damage award can be defined per held item. 

### Data

The data for this pool type looks like this:

```ts
"data": {
  "points": {
    String: double,
    ...
  },
  "default": double
}
```

The `String` key in the `points` object is an item string that defines a held item to match in the form `<domain>:<path>:<meta>` where `<meta>` *should* be a wildcard: `*`.

The `double` defines the number of points to be awarded for harvesting the block.

If the player deals damage with an item that is not specified in the list, the points awarded defaults to `1` and can be overridden by supplying a value for `default`.  

!!! warning
    If your item takes damage, don't forget to set the meta to `*`.

### Data Example

In this example, dealing damage with a `mmorpg:sword/sword0` awards `3` points per point of damage dealt. Dealing damage with any other item awards a default of `2` points per damage dealt.

```js
"data": {
  "points": {
    "mmorpg:sword/sword0": 3
  },
  "default": 2
}
```

### Full Example

This is what a `ms_damage_dealt` point pool might look like:

```js
{
  "type": "ms_damage_dealt",
  "name": "Damage Dealt",
  "data": {
    "points": {
      "mmorpg:sword/sword0": 3
    },
    "default": 2
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