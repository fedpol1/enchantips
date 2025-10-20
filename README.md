# Enchantips
Mod that enhances tooltips related to enchanting or enchantments.

## Dependencies
+ Minecraft 1.21.9/10
+ Fabric Loader 0.17.2 or greater
+ Fabric API 0.134.0 or greater

**For Configuration:**
+ Mod Menu 16.0.0 or greater
+ YetAnotherConfigLib 3.8.0 or greater (optional - not required if compact configuration is enabled)

## Features

**Repair Cost:** any item that was worked in an anvil has an added penalty for future operations, this can be made visible.

**Enchantability:** each tool has an inherent value that determines how well it responds to getting enchanted; this can be made visible.

**Enchantment Anvil Cost:** This is a per-enchantment value that indicates how much it costs to add via an anvil; this can be made visible.

**Enchantment Targets:** The set of items that the enchantment can go on, either through an enchanting table or through an anvil. This can be made visible as a series of icons next to the enchantment name. By default, only items which are applicable to vanilla survival are listed, but more may be added via resource packs.

**Enchantment Tags:** The set of tags an enchantment is a member of. This can be made visible as a series of icons next to the enchantment name. By default, only tags that are applicable to vanilla survival are listed (curse, tradeable, etc.) but more may be added via resource packs.

**Enchanting Power:** when enchanting an item, the game modifies the given level offer (e.g. level 30 enchantment) and uses that modified value to determine which enchantments you get. Enchantips gives a range of possible values that this value could be and displays it.

**Anvil Optimizer:** Sometimes, swapping the two input items in an anvil reduces the cost of the operation without influencing the output enchantments. Enchantips adds an indicator that displays when an operation could be optimized like this and also a button that swaps the items for you with one click. _Note that using the button could flag anticheats on servers._

**Enchantment Sorting:** Enchantips sorts enchantments in item tooltips.

**Item Highlights:** Enchantips highlights slots with enchanted items in them.

**Enchantment Info Screen:** Press a configurable keybind to view every enchantment and some of its internal values.

## Item Highlights

Enchantips gives the option to highlight enchanted items based on the enchantments they have. Highlighting a slot with multiple enchantments is supported. 

By default, every normal (non-treasure non-curse) enchantment has the same settings. You will need to configure colors manually to leverage this feature fully.

You can also configure the order in which enchantments are highlighted. As an example, you can set the mod to prioritize mending so mending will always be the leftmost highlight in the slot. This also influences enchantment tooltip sorting, so mending would also be the topmost enchantment in the item's tooltip.

## Configuration

Nearly every feature is configurable, however not all features are enabled by default. Enchantments in item tooltips are always sorted, there is no way to disable this.

For per-enchantment configuration, you will need to enter a world to initialize the configuration. This is because enchantments are now defined per-world. On later game sessions, before entering a world, any previously saved per-enchantment configurations will have an _Enchantips: Unknown_ title; this is also due to enchantments being defined per-world. There is no guarantee of enchantment names staying consistent since the server's/world's enchantment definition (which includes the name) may change any time.
