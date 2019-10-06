### Change points and levels 

```
/ftpoints <pool> <amount> [player]
```

Add / remove points from the given point pool.

Params:

* **pool:** the pool id in the form (domain):(path)
* **amount:** the number of points, value is a double
* **player:** (optional) if omitted, defaults to the command sender

```
/ftpoints <pool> <amount>L [player]
```

Add / remove levels from the given point pool.

Params:

* **pool:** the pool id in the form (domain):(path)
* **amount:** the number of levels, value is an integer
* **player:** (optional) if omitted, defaults to the command sender

### Show points and levels

```
/ftshow <pool>|all [player]
```

Display the point total, points for next level, and current level for the given pool or `all`.

Params:

* **pool:** the pool id in the form (domain):(path) or `all`
* **player:** (optional) if omitted, defaults to the command sender