# Mythic Mobs Addon
Adds support for [TradingCards](https://github.com/TreasureIslandMC/XenTradingCards).

```yaml
MythicMobs-Enabled: false
# Chance chance/rarity based on the level of the mob?
Per-Level-Chances: true
# With this you're able to set the individual rarity chances of every mythic mob level.
# For example, you could make it so level 100 boss-type mobs always drop shiny legendaries.
# You can add as many levels as you want, if a mob is higher than the highest level here,
# its chances will be based on the highest level in this config. E.G. if you only have
# levels 0-5 here and you kill a level 10 mob, it will use the level 5 chances.
Levels:
  0:
    Shiny-Version-Chance: 1
    # 'Drop-Chance' is the chance a card will drop AT ALL. This should be chance out of 100 (Drop-Chance: 10 = 10% chance)
    # For rarities: 100,000 = 100% chance, 10,000 = 10% chance, 1000 = 1% chance, 100 = 0.1% chance, 10 = 0.01% chance, 1 = 0.001% chance
    Drop-Chance: 10
    Rarities:
      Common: 100000
      Uncommon: 20000
      Rare: 1000
      Very Rare: 10
      Legendary: 1
  1:
    Shiny-Version-Chance: 1
    Drop-Chance: 20
    Rarities:
      Common: 100000
      Uncommon: 20000
      Rare: 1000
      Very Rare: 10
      Legendary: 1
  2:
    Shiny-Version-Chance: 1
    Drop-Chance: 30
    Rarities:
      Common: 100000
      Uncommon: 20000
      Rare: 1000
      Very Rare: 10
      Legendary: 1
  3:
    Shiny-Version-Chance: 1
    Drop-Chance: 40
    Rarities:
      Common: 100000
      Uncommon: 20000
      Rare: 1000
      Very Rare: 10
      Legendary: 1
  4:
    Shiny-Version-Chance: 1
    Drop-Chance: 50
    Rarities:
      Common: 100000
      Uncommon: 20000
      Rare: 1000
      Very Rare: 10
      Legendary: 1
  5:
    Shiny-Version-Chance: 1
    Drop-Chance: 75
    Rarities:
      Common: 100000
      Uncommon: 20000
      Rare: 1000
      Very Rare: 10
      Legendary: 1
```
