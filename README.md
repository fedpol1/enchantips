# Enchantips
Mod that enhances tooltips related to enchanting or enchantments.

## Dependencies
+ Minecraft 1.19.4
+ Fabric Loader 0.14.17 or greater
+ Fabric API 0.77.0 or greater

**For Configuration:**
+ Mod Menu 6.10.0-rc4 or greater
+ YetAnotherConfigLib 2.1.1 or greater

## Features
+ ability to sort enchantments in the tooltip
+ ability to change enchantment colors per-enchantment
  + unbreakable gets a special color
  + color gradient between min levelled enchantments and max levelled enchantments
+ enchantment rarity display
  + this value is a factor in how expensive it is to combine an enchantment in an anvil and how often it appears in the enchantment table
+ item enchantability display
+ item anvil penalty display
+ enchantment predictions when enchanting via enchantment table
+ modified enchantment level display when enchanting via enchantment table
  + modified enchantment level is an internal value used to determine which enchantments are possible to get
+ ability to highlight items based on which enchantments they have
  + supports items with multiple enchantments; highlights will have n stripes where n is the number of enchantments, capped to a configurable value
