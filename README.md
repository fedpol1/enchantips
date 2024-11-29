# Enchantips
Mod that enhances tooltips related to enchanting or enchantments.

## Dependencies
+ Minecraft 1.21.2/3
+ Fabric Loader 0.16.7 or greater
+ Fabric API 0.106.1 or greater

**For Configuration:**
+ Mod Menu 12.0.0 or greater
+ YetAnotherConfigLib 3.6.1 or greater

## Features
+ ability to sort enchantments in the tooltip
+ ability to change enchantment colors per-enchantment
  + unbreakable gets a special color
  + color gradient between min levelled enchantments and max levelled enchantments
  + can opt for a swatch-style color display
+ enchantment anvil cost display
  + this value is a factor in how expensive it is to combine an enchantment in an anvil
+ enchantment compatible items display
+ item enchantability display
+ item anvil penalty display
+ enchantment predictions when enchanting via enchantment table
+ modified enchantment level display when enchanting via enchantment table
  + modified enchantment level is an internal value used to determine which enchantments are possible to get
+ ability to highlight items based on which enchantments they have
  + supports items with multiple enchantments; highlights will have n stripes where n is the number of enchantments, capped to a configurable value
+ enchantment info screen which shows, per enchantment:
  + id
  + name
  + maximum level
  + minimum and maximum required enchantment power
  + anvil cost
  + weight
  + primary and secondary items
  + tags
